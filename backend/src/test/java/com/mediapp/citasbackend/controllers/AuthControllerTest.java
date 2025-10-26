package com.mediapp.citasbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediapp.citasbackend.dtos.AuthResponseDTO;
import com.mediapp.citasbackend.dtos.LoginRequestDTO;
import com.mediapp.citasbackend.dtos.RegisterRequestDTO;
import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.exceptions.InvalidCredentialsException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.security.JwtAuthenticationFilter;
import com.mediapp.citasbackend.security.JwtService;
import com.mediapp.citasbackend.services.interfaces.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private RegisterRequestDTO registerRequest;
    private LoginRequestDTO loginRequest;
    private AuthResponseDTO authResponse;

    @BeforeEach
    void setUp() {
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

        authResponse = AuthResponseDTO.builder()
                .token("accessToken")
                .refreshToken("refreshToken")
                .idUsuario(1)
                .email("juan.perez@example.com")
                .nombre("Juan")
                .apellido("Pérez")
                .tipoUsuario("PACIENTE")
                .build();
    }

    @Test
    @DisplayName("POST /api/auth/register - Registro exitoso")
    void testRegister_Success() throws Exception {
        // Arrange
        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"))
                .andExpect(jsonPath("$.tipoUsuario").value("PACIENTE"));
    }

    @Test
    @DisplayName("POST /api/auth/register - Email ya existe")
    void testRegister_EmailAlreadyExists() throws Exception {
        // Arrange
        when(authService.register(any(RegisterRequestDTO.class)))
                .thenThrow(new ResourceAlreadyExistsException("El email ya está registrado"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("POST /api/auth/register - Datos inválidos")
    void testRegister_InvalidData() throws Exception {
        // Arrange
        registerRequest.setEmail("emailinvalido"); // Email sin formato válido

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/register - Campos requeridos faltantes")
    void testRegister_MissingRequiredFields() throws Exception {
        // Arrange
        registerRequest.setNombre(null);
        registerRequest.setEmail(null);

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login - Login exitoso")
    void testLogin_Success() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));
    }

    @Test
    @DisplayName("POST /api/auth/login - Credenciales inválidas")
    void testLogin_InvalidCredentials() throws Exception {
        // Arrange
        when(authService.login(any(LoginRequestDTO.class)))
                .thenThrow(new InvalidCredentialsException("Credenciales inválidas"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/login - Email no proporcionado")
    void testLogin_MissingEmail() throws Exception {
        // Arrange
        loginRequest.setEmail(null);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/login - Contraseña no proporcionada")
    void testLogin_MissingPassword() throws Exception {
        // Arrange
        loginRequest.setContraseña(null);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/auth/refresh - Refresh token exitoso")
    void testRefreshToken_Success() throws Exception {
        // Arrange
        when(authService.refreshToken(anyString())).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", "Bearer validRefreshToken"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    @DisplayName("POST /api/auth/refresh - Authorization header faltante")
    void testRefreshToken_MissingHeader() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/refresh - Authorization header sin Bearer")
    void testRefreshToken_InvalidHeaderFormat() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", "invalidRefreshToken"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/refresh - Token inválido")
    void testRefreshToken_InvalidToken() throws Exception {
        // Arrange
        when(authService.refreshToken(anyString()))
                .thenThrow(new InvalidCredentialsException("Refresh token inválido"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/refresh")
                        .header("Authorization", "Bearer invalidToken"))
                .andExpect(status().isUnauthorized());
    }
}
