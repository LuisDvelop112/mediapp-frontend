package com.mediapp.citasbackend.dtos;

import com.mediapp.citasbackend.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para registrar un nuevo usuario en el sistema")
public class RegisterRequestDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nombre del usuario", example = "Sebastian", required = true)
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    @Schema(description = "Apellido del usuario", example = "Agudelo", required = true)
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Schema(description = "Correo electrónico del usuario (debe ser único)", example = "sebastian@gmail.com", required = true)
    private String email;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 255, message = "La contraseña debe tener entre 6 y 255 caracteres")
    @Schema(description = "Contraseña del usuario (mínimo 6 caracteres)", example = "sebas031800", required = true, minLength = 6)
    private String contraseña;
    
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    @Schema(description = "Fecha de nacimiento del usuario (opcional)", example = "1995-05-15", required = false)
    private LocalDate fechaNacimiento;
    
    @Schema(description = "Género del usuario (MASCULINO o FEMENINO, opcional)", example = "MASCULINO", required = false, allowableValues = {"MASCULINO", "FEMENINO"})
    private Usuario.Genero genero;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Schema(description = "Número de teléfono del usuario (opcional)", example = "3001234567", required = false)
    private String telefono;
    
    @Size(max = 150, message = "La dirección no puede exceder 150 caracteres")
    @Schema(description = "Dirección de residencia del usuario (opcional)", example = "Calle 123 #45-67", required = false)
    private String direccion;
    
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Schema(description = "Ciudad de residencia del usuario (opcional)", example = "Armenia", required = false)
    private String ciudad;
    
    @Size(max = 100, message = "El país no puede exceder 100 caracteres")
    @Schema(description = "País de residencia del usuario (opcional)", example = "Colombia", required = false)
    private String pais;
    
    @NotNull(message = "El tipo de usuario es obligatorio")
    @Schema(description = "Tipo de usuario: PACIENTE, MEDICO o ADMIN", example = "PACIENTE", required = true, allowableValues = {"PACIENTE", "MEDICO", "ADMIN"})
    private Usuario.TipoUsuario tipoUsuario;
}
