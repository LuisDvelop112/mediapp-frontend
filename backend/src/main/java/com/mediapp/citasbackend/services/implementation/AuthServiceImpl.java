package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.dtos.AuthResponseDTO;
import com.mediapp.citasbackend.dtos.LoginRequestDTO;
import com.mediapp.citasbackend.dtos.RegisterRequestDTO;
import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.exceptions.InvalidCredentialsException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.repositories.UsuarioRepository;
import com.mediapp.citasbackend.security.JwtService;
import com.mediapp.citasbackend.services.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("El email ya está registrado: " + request.getEmail());
        }

        // Crear nuevo usuario
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .contraseña(passwordEncoder.encode(request.getContraseña()))
                .fechaNacimiento(request.getFechaNacimiento())
                .genero(request.getGenero())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .ciudad(request.getCiudad())
                .pais(request.getPais())
                .tipoUsuario(request.getTipoUsuario())
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.ACTIVO)
                .build();

        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Generar tokens
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .refreshToken(refreshToken)
                .idUsuario(savedUsuario.getIdUsuario())
                .email(savedUsuario.getEmail())
                .nombre(savedUsuario.getNombre())
                .apellido(savedUsuario.getApellido())
                .tipoUsuario(savedUsuario.getTipoUsuario().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {
        try {
            // Autenticar usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getContraseña()
                    )
            );

            // Obtener usuario autenticado
            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

            // Verificar estado del usuario
            if (usuario.getEstado() == Usuario.Estado.INACTIVO) {
                throw new InvalidCredentialsException("La cuenta está inactiva");
            }
            if (usuario.getEstado() == Usuario.Estado.BLOQUEADO) {
                throw new InvalidCredentialsException("La cuenta está bloqueada");
            }

            // Generar tokens
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            return AuthResponseDTO.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .idUsuario(usuario.getIdUsuario())
                    .email(usuario.getEmail())
                    .nombre(usuario.getNombre())
                    .apellido(usuario.getApellido())
                    .tipoUsuario(usuario.getTipoUsuario().name())
                    .build();

        } catch (InvalidCredentialsException e) {
            // Re-lanzar excepciones de credenciales (incluyendo cuenta inactiva/bloqueada)
            throw e;
        } catch (Exception e) {
            // Capturar otras excepciones de autenticación
            throw new InvalidCredentialsException("Credenciales inválidas");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO refreshToken(String refreshToken) {
        // Extraer email del token
        String userEmail = jwtService.extractUsername(refreshToken);

        // Cargar usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        // Validar refresh token
        if (!jwtService.isTokenValid(refreshToken, userDetails)) {
            throw new InvalidCredentialsException("Refresh token inválido o expirado");
        }

        // Obtener información del usuario
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseThrow(() -> new InvalidCredentialsException("Usuario no encontrado"));

        // Generar nuevo token de acceso
        String newToken = jwtService.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(newToken)
                .refreshToken(refreshToken)
                .idUsuario(usuario.getIdUsuario())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .tipoUsuario(usuario.getTipoUsuario().name())
                .build();
    }
}
