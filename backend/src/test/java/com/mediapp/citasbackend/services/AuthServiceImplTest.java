package com.mediapp.citasbackend.services;

import com.mediapp.citasbackend.dtos.AuthResponseDTO;
import com.mediapp.citasbackend.dtos.LoginRequestDTO;
import com.mediapp.citasbackend.dtos.RegisterRequestDTO;
import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.exceptions.InvalidCredentialsException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.repositories.UsuarioRepository;
import com.mediapp.citasbackend.security.JwtService;
import com.mediapp.citasbackend.services.implementation.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequestDTO registerRequest;
    private LoginRequestDTO loginRequest;
    private Usuario usuario;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        registerRequest = new RegisterRequestDTO();
        registerRequest.setNombre("Juan");
        registerRequest.setApellido("Pérez");
        registerRequest.setEmail("juan.perez@example.com");
        registerRequest.setContraseña("password123");
        registerRequest.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        registerRequest.setGenero(Usuario.Genero.MASCULINO);
        registerRequest.setTelefono("3001234567");
        registerRequest.setDireccion("Calle 123");
        registerRequest.setCiudad("Armenia");
        registerRequest.setPais("Colombia");
        registerRequest.setTipoUsuario(Usuario.TipoUsuario.PACIENTE);

        loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("juan.perez@example.com");
        loginRequest.setContraseña("password123");

        usuario = Usuario.builder()
                .idUsuario(1)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan.perez@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .genero(Usuario.Genero.MASCULINO)
                .telefono("3001234567")
                .direccion("Calle 123")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.ACTIVO)
                .build();

        userDetails = new User(
                usuario.getEmail(),
                usuario.getContraseña(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_PACIENTE"))
        );
    }

    @Test
    @DisplayName("Registro exitoso de nuevo usuario")
    void testRegister_Success() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("refreshToken");

        // Act
        AuthResponseDTO response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("accessToken", response.getToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(1, response.getIdUsuario());
        assertEquals("juan.perez@example.com", response.getEmail());
        assertEquals("Juan", response.getNombre());
        assertEquals("Pérez", response.getApellido());
        assertEquals("PACIENTE", response.getTipoUsuario());

        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
        verify(jwtService, times(1)).generateRefreshToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("Registro falla cuando el email ya existe")
    void testRegister_EmailAlreadyExists() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // Act & Assert
        ResourceAlreadyExistsException exception = assertThrows(
                ResourceAlreadyExistsException.class,
                () -> authService.register(registerRequest)
        );

        assertTrue(exception.getMessage().contains("email ya está registrado"));
        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Login exitoso con credenciales válidas")
    void testLogin_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("refreshToken");

        // Act
        AuthResponseDTO response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("accessToken", response.getToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(1, response.getIdUsuario());
        assertEquals("juan.perez@example.com", response.getEmail());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository, times(1)).findByEmail(anyString());
        verify(jwtService, times(1)).generateToken(any(UserDetails.class));
        verify(jwtService, times(1)).generateRefreshToken(any(UserDetails.class));
    }

    @Test
    @DisplayName("Login falla con credenciales inválidas")
    void testLogin_InvalidCredentials() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new InvalidCredentialsException("Credenciales inválidas"));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("Credenciales inválidas", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Login falla con cuenta inactiva")
    void testLogin_InactiveAccount() {
        // Arrange
        usuario.setEstado(Usuario.Estado.INACTIVO);
        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getPrincipal()).thenReturn(userDetails);
        lenient().when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("La cuenta está inactiva", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Login falla con cuenta bloqueada")
    void testLogin_BlockedAccount() {
        // Arrange
        usuario.setEstado(Usuario.Estado.BLOQUEADO);
        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getPrincipal()).thenReturn(userDetails);
        lenient().when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(loginRequest)
        );

        assertEquals("La cuenta está bloqueada", exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("RefreshToken genera nuevo access token")
    void testRefreshToken_Success() {
        // Arrange
        String refreshToken = "validRefreshToken";
        String email = "juan.perez@example.com";
        
        when(jwtService.extractUsername(refreshToken)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(refreshToken, userDetails)).thenReturn(true);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(userDetails)).thenReturn("newAccessToken");

        // Act
        AuthResponseDTO response = authService.refreshToken(refreshToken);

        // Assert
        assertNotNull(response);
        assertEquals("newAccessToken", response.getToken());
        assertEquals(refreshToken, response.getRefreshToken());
        assertEquals(email, response.getEmail());

        verify(jwtService, times(1)).extractUsername(refreshToken);
        verify(jwtService, times(1)).isTokenValid(refreshToken, userDetails);
        verify(jwtService, times(1)).generateToken(userDetails);
    }

    @Test
    @DisplayName("RefreshToken falla con token inválido")
    void testRefreshToken_InvalidToken() {
        // Arrange
        String refreshToken = "invalidRefreshToken";
        String email = "juan.perez@example.com";
        
        when(jwtService.extractUsername(refreshToken)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(refreshToken, userDetails)).thenReturn(false);

        // Act & Assert
        InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.refreshToken(refreshToken)
        );

        assertTrue(exception.getMessage().contains("Refresh token inválido"));
        verify(jwtService, times(1)).isTokenValid(refreshToken, userDetails);
        verify(jwtService, never()).generateToken(any());
    }
}
