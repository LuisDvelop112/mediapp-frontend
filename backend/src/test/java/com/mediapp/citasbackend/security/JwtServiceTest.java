package com.mediapp.citasbackend.security;

import com.mediapp.citasbackend.config.JwtConfigProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("JwtService Tests")
class JwtServiceTest {

    @Mock
    private JwtConfigProperties jwtConfig;

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Configurar mock de propiedades JWT con lenient
        lenient().when(jwtConfig.getSecret()).thenReturn("dGVzdFNlY3JldEtleUZvckpXVFRlc3RpbmdNZWRpQXBwMTIzNDU2Nzg5MA==");
        lenient().when(jwtConfig.getExpiration()).thenReturn(86400000L); // 24 horas
        lenient().when(jwtConfig.getRefreshExpiration()).thenReturn(604800000L); // 7 días

        userDetails = new User(
                "juan.perez@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_PACIENTE"))
        );
    }

    @Test
    @DisplayName("Generar token de acceso exitosamente")
    void testGenerateToken_Success() {
        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes separadas por puntos
    }

    @Test
    @DisplayName("Generar refresh token exitosamente")
    void testGenerateRefreshToken_Success() {
        // Act
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Assert
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
        assertTrue(refreshToken.split("\\.").length == 3);
    }

    @Test
    @DisplayName("Extraer username del token")
    void testExtractUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertNotNull(username);
        assertEquals("juan.perez@example.com", username);
    }

    @Test
    @DisplayName("Validar token válido")
    void testIsTokenValid_ValidToken() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Validar token con username diferente")
    void testIsTokenValid_DifferentUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        UserDetails differentUser = new User(
                "otro.usuario@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_PACIENTE"))
        );

        // Act
        boolean isValid = jwtService.isTokenValid(token, differentUser);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Validar token malformado")
    void testIsTokenValid_MalformedToken() {
        // Arrange
        String malformedToken = "this.is.not.a.valid.jwt.token";

        // Act & Assert
        assertThrows(MalformedJwtException.class, () -> {
            jwtService.extractUsername(malformedToken);
        });
    }

    @Test
    @DisplayName("Validar token expirado")
    void testIsTokenValid_ExpiredToken() {
        // Arrange
        lenient().when(jwtConfig.getExpiration()).thenReturn(-1000L); // Token expirado inmediatamente
        String expiredToken = jwtService.generateToken(userDetails);

        // Esperar un momento para que el token expire
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Act & Assert
        assertThrows(ExpiredJwtException.class, () -> {
            jwtService.extractUsername(expiredToken);
        });
    }

    @Test
    @DisplayName("Token de acceso y refresh token son diferentes")
    void testAccessAndRefreshTokensAreDifferent() {
        // Act
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Assert
        assertNotEquals(accessToken, refreshToken);
    }

    @Test
    @DisplayName("Extraer username de refresh token")
    void testExtractUsernameFromRefreshToken() {
        // Arrange
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Act
        String username = jwtService.extractUsername(refreshToken);

        // Assert
        assertNotNull(username);
        assertEquals("juan.perez@example.com", username);
    }

    @Test
    @DisplayName("Validar refresh token")
    void testIsRefreshTokenValid() {
        // Arrange
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(refreshToken, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Token contiene el username correcto")
    void testTokenContainsCorrectUsername() {
        // Arrange
        UserDetails user1 = new User("user1@example.com", "pass", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_PACIENTE")));
        UserDetails user2 = new User("user2@example.com", "pass", 
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEDICO")));

        // Act
        String token1 = jwtService.generateToken(user1);
        String token2 = jwtService.generateToken(user2);

        // Assert
        assertEquals("user1@example.com", jwtService.extractUsername(token1));
        assertEquals("user2@example.com", jwtService.extractUsername(token2));
        assertNotEquals(token1, token2);
    }
}
