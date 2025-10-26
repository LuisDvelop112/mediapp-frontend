package com.mediapp.citasbackend.controllers;

import com.mediapp.citasbackend.dtos.AuthResponseDTO;
import com.mediapp.citasbackend.dtos.LoginRequestDTO;
import com.mediapp.citasbackend.dtos.RegisterRequestDTO;
import com.mediapp.citasbackend.services.interfaces.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro de usuarios en el sistema MediApp")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register") // http://56.125.172.86:8080/api/auth/register
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea un nuevo usuario en el sistema. Puede ser PACIENTE, MEDICO o ADMIN. Retorna tokens JWT de acceso y refresh."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de registro inválidos o email ya registrado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login") // http://56.125.172.86:8080/api/auth/login
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario con email y contraseña. Retorna tokens JWT de acceso y refresh para autenticación."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autenticación exitosa",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh") // http://56.125.172.86:8080/api/auth/refresh
    @Operation(
        summary = "Refrescar token de acceso",
        description = "Genera un nuevo token de acceso utilizando el refresh token. El refresh token debe enviarse en el header Authorization con el formato 'Bearer {token}'.",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token refrescado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponseDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Refresh token inválido o expirado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<AuthResponseDTO> refreshToken(
        @Parameter(
            description = "Refresh token en formato Bearer",
            required = true,
            example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        @RequestHeader("Authorization") String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String refreshToken = authHeader.substring(7);
        AuthResponseDTO response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}
