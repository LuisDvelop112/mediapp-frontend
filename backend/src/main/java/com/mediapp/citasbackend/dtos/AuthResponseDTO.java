package com.mediapp.citasbackend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación con tokens JWT y datos del usuario")
public class AuthResponseDTO {
    
    @Schema(description = "Token de acceso JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "Token de actualización para renovar el token de acceso", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
    
    @Builder.Default
    @Schema(description = "Tipo de token (siempre 'Bearer')", example = "Bearer", defaultValue = "Bearer")
    private String tipo = "Bearer";
    
    @Schema(description = "ID del usuario autenticado", example = "1")
    private Integer idUsuario;
    
    @Schema(description = "Email del usuario autenticado", example = "sebastian@gmail.com")
    private String email;
    
    @Schema(description = "Nombre del usuario autenticado", example = "Sebastian")
    private String nombre;
    
    @Schema(description = "Apellido del usuario autenticado", example = "Agudelo")
    private String apellido;
    
    @Schema(description = "Tipo de usuario", example = "PACIENTE", allowableValues = {"PACIENTE", "MEDICO", "ADMIN"})
    private String tipoUsuario;
}
