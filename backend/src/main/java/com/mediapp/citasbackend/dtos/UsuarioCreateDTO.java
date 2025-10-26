package com.mediapp.citasbackend.dtos;

import com.mediapp.citasbackend.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear un nuevo usuario (uso administrativo)")
public class UsuarioCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nombre del usuario", example = "Maria", required = true)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    @Schema(description = "Apellido del usuario", example = "Rodriguez", required = true)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Schema(description = "Correo electrónico único del usuario", example = "maria.rodriguez@gmail.com", required = true)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 255, message = "La contraseña debe tener entre 6 y 255 caracteres")
    @Schema(description = "Contraseña del usuario (mínimo 6 caracteres)", example = "password123", required = true, minLength = 6)
    private String contraseña;

    @Schema(description = "Fecha de nacimiento del usuario (opcional)", example = "1990-08-20", required = false)
    private LocalDate fechaNacimiento;

    @Schema(description = "Género del usuario (MASCULINO o FEMENINO, opcional)", example = "FEMENINO", required = false, allowableValues = {"MASCULINO", "FEMENINO"})
    private Usuario.Genero genero;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Schema(description = "Número de teléfono (opcional)", example = "3109876543", required = false)
    private String telefono;

    @Size(max = 150, message = "La dirección no puede exceder 150 caracteres")
    @Schema(description = "Dirección de residencia (opcional)", example = "Carrera 10 #20-30", required = false)
    private String direccion;

    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Schema(description = "Ciudad de residencia (opcional)", example = "Bogotá", required = false)
    private String ciudad;

    @Size(max = 100, message = "El país no puede exceder 100 caracteres")
    @Schema(description = "País de residencia (opcional)", example = "Colombia", required = false)
    private String pais;

    @NotNull(message = "El tipo de usuario es obligatorio")
    @Schema(description = "Tipo de usuario: PACIENTE, MEDICO o ADMIN", example = "MEDICO", required = true, allowableValues = {"PACIENTE", "MEDICO", "ADMIN"})
    private Usuario.TipoUsuario tipoUsuario;

    @Size(max = 255, message = "La URL de la foto de perfil no puede exceder 255 caracteres")
    @Schema(description = "URL de la foto de perfil (opcional)", example = "https://example.com/foto.jpg", required = false)
    private String fotoPerfil;
}
