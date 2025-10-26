package com.mediapp.citasbackend.security;

import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getContraseña())
                .authorities(getAuthorities(usuario))
                .accountExpired(false)
                .accountLocked(usuario.getEstado() == Usuario.Estado.BLOQUEADO)
                .credentialsExpired(false)
                .disabled(usuario.getEstado() == Usuario.Estado.INACTIVO)
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
        // Convertir el TipoUsuario a rol de Spring Security con prefijo "ROLE_"
        String role = "ROLE_" + usuario.getTipoUsuario().name();
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
