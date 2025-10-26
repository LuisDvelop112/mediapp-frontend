package com.mediapp.citasbackend.dtos;

import com.mediapp.citasbackend.entities.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para actualizar datos de un usuario existente (todos los campos son opcionales)")
public class UsuarioUpdateDTO {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nombre del usuario (opcional)", example = "Carlos", required = false)
    private String nombre;

    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    @Schema(description = "Apellido del usuario (opcional)", example = "Pérez", required = false)
    private String apellido;

    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede exceder 100 caracteres")
    @Schema(description = "Correo electrónico del usuario (opcional)", example = "carlos.perez@gmail.com", required = false)
    private String email;

    @Schema(description = "Fecha de nacimiento (opcional)", example = "1985-12-10", required = false)
    private LocalDate fechaNacimiento;

    @Schema(description = "Género (MASCULINO o FEMENINO, opcional)", example = "MASCULINO", required = false, allowableValues = {"MASCULINO", "FEMENINO"})
    private Usuario.Genero genero;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Schema(description = "Número de teléfono (opcional)", example = "3201234567", required = false)
    private String telefono;

    @Size(max = 150, message = "La dirección no puede exceder 150 caracteres")
    @Schema(description = "Dirección de residencia (opcional)", example = "Avenida 5 #12-34", required = false)
    private String direccion;

    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    @Schema(description = "Ciudad de residencia (opcional)", example = "Medellín", required = false)
    private String ciudad;

    @Size(max = 100, message = "El país no puede exceder 100 caracteres")
    @Schema(description = "País de residencia (opcional)", example = "Colombia", required = false)
    private String pais;

    @Size(max = 255, message = "La URL de la foto de perfil no puede exceder 255 caracteres")
    @Schema(description = "URL de la foto de perfil (opcional)", example = "https://example.com/avatar.jpg", required = false)
    private String fotoPerfil;

    @Schema(description = "Estado del usuario: ACTIVO, INACTIVO o BLOQUEADO (opcional, solo admin)", example = "ACTIVO", required = false, allowableValues = {"ACTIVO", "INACTIVO", "BLOQUEADO"})
    private Usuario.Estado estado;
}
