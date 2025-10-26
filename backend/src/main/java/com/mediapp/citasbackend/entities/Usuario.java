package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un usuario del sistema")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    @Schema(description = "ID único del usuario (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idUsuario;

    @Column(name = "nombre", nullable = false, length = 100)
    @Schema(description = "Nombre del usuario", example = "Juan", required = true)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    @Schema(description = "Apellido del usuario", example = "Pérez", required = true)
    private String apellido;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Schema(description = "Correo electrónico único del usuario", example = "juan.perez@gmail.com", required = true)
    private String email;

    @Column(name = "contraseña", nullable = false, length = 255)
    @Schema(description = "Contraseña encriptada del usuario", accessMode = Schema.AccessMode.WRITE_ONLY, required = true)
    private String contraseña;

    @Column(name = "fecha_nacimiento")
    @Schema(description = "Fecha de nacimiento del usuario (opcional)", example = "1990-05-15", required = false)
    private LocalDate fechaNacimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", length = 20)
    @Schema(description = "Género del usuario (opcional)", example = "MASCULINO", required = false, allowableValues = {"MASCULINO", "FEMENINO"})
    private Genero genero;

    @Column(name = "telefono", length = 20)
    @Schema(description = "Número de teléfono (opcional)", example = "3001234567", required = false)
    private String telefono;

    @Column(name = "direccion", length = 150)
    @Schema(description = "Dirección de residencia (opcional)", example = "Calle 123 #45-67", required = false)
    private String direccion;

    @Column(name = "ciudad", length = 100)
    @Schema(description = "Ciudad de residencia (opcional)", example = "Bogotá", required = false)
    private String ciudad;

    @Column(name = "pais", length = 100)
    @Schema(description = "País de residencia (opcional)", example = "Colombia", required = false)
    private String pais;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false, length = 20)
    @Schema(description = "Tipo de usuario en el sistema", example = "PACIENTE", required = true, allowableValues = {"PACIENTE", "MEDICO", "ADMIN"})
    private TipoUsuario tipoUsuario;

    @Column(name = "foto_perfil", length = 255)
    @Schema(description = "URL de la foto de perfil (opcional)", example = "https://example.com/foto.jpg", required = false)
    private String fotoPerfil;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    @Schema(description = "Fecha y hora de registro del usuario (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "2025-10-24T10:30:00")
    private LocalDateTime fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Schema(description = "Estado de la cuenta del usuario (por defecto: ACTIVO)", example = "ACTIVO", required = false, allowableValues = {"ACTIVO", "INACTIVO", "BLOQUEADO"})
    private Estado estado;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (estado == null) {
            estado = Estado.ACTIVO;
        }
    }

    public enum Genero {
        MASCULINO,
        FEMENINO
    }

    public enum TipoUsuario {
        PACIENTE,
        MEDICO,
        ADMIN
    }

    public enum Estado {
        ACTIVO,
        INACTIVO,
        BLOQUEADO
    }
}
