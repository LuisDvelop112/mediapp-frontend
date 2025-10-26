package com.mediapp.citasbackend.dtos;

import com.mediapp.citasbackend.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO con la información completa de un usuario (sin contraseña)")
public class UsuarioDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Integer idUsuario;
    
    @Schema(description = "Nombre del usuario", example = "Laura")
    private String nombre;
    
    @Schema(description = "Apellido del usuario", example = "Gómez")
    private String apellido;
    
    @Schema(description = "Correo electrónico del usuario", example = "laura.gomez@gmail.com")
    private String email;
    
    @Schema(description = "Fecha de nacimiento", example = "1992-03-20")
    private LocalDate fechaNacimiento;
    
    @Schema(description = "Género del usuario", example = "FEMENINO", allowableValues = {"MASCULINO", "FEMENINO"})
    private Usuario.Genero genero;
    
    @Schema(description = "Número de teléfono", example = "3151234567")
    private String telefono;
    
    @Schema(description = "Dirección de residencia", example = "Carrera 15 #30-40")
    private String direccion;
    
    @Schema(description = "Ciudad de residencia", example = "Medellín")
    private String ciudad;
    
    @Schema(description = "País de residencia", example = "Colombia")
    private String pais;
    
    @Schema(description = "Tipo de usuario", example = "MEDICO", allowableValues = {"PACIENTE", "MEDICO", "ADMIN"})
    private Usuario.TipoUsuario tipoUsuario;
    
    @Schema(description = "URL de la foto de perfil", example = "https://example.com/avatar.jpg")
    private String fotoPerfil;
    
    @Schema(description = "Fecha y hora de registro", example = "2025-10-20T15:30:00")
    private LocalDateTime fechaRegistro;
    
    @Schema(description = "Estado de la cuenta", example = "ACTIVO", allowableValues = {"ACTIVO", "INACTIVO", "BLOQUEADO"})
    private Usuario.Estado estado;
}
