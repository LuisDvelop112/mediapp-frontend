package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un médico en el sistema")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    @Schema(description = "ID único del médico (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idMedico;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    @Schema(description = "Usuario asociado al médico", required = true)
    private Usuario usuario;

    @Column(name = "numero_licencia", length = 50)
    @Schema(description = "Número de licencia médica (opcional)", example = "MP-123456", required = false)
    private String numeroLicencia;

    @ManyToOne
    @JoinColumn(name = "id_especialidad")
    @Schema(description = "Especialidad médica del doctor (opcional)", required = false)
    private Especialidad especialidad;

    @Column(name = "hospital_afiliado", length = 100)
    @Schema(description = "Hospital o institución de afiliación (opcional)", example = "Hospital San José", required = false)
    private String hospitalAfiliado;

    @Column(name = "experiencia_anos")
    @Schema(description = "Años de experiencia profesional (opcional)", example = "10", required = false)
    private Integer experienciaAnos;

    @Column(name = "resumen_bio", columnDefinition = "TEXT")
    @Schema(description = "Biografía o descripción profesional del médico (opcional)", example = "Especialista en cardiología con 10 años de experiencia...", required = false)
    private String resumenBio;

    @Column(name = "calificacion_promedio", precision = 2, scale = 1)
    @Schema(description = "Calificación promedio del médico (0.0 - 5.0, calculada automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "4.5")
    private BigDecimal calificacionPromedio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_verificacion", nullable = false, length = 20)
    @Schema(description = "Estado de verificación del médico (por defecto: PENDIENTE)", example = "PENDIENTE", required = false, allowableValues = {"VERIFICADO", "PENDIENTE"})
    private EstadoVerificacion estadoVerificacion;

    @Column(name = "fecha_verificacion")
    @Schema(description = "Fecha en que se verificó al médico (si aplica)", accessMode = Schema.AccessMode.READ_ONLY, example = "2025-10-20", required = false)
    private LocalDate fechaVerificacion;

    @PrePersist
    protected void onCreate() {
        if (estadoVerificacion == null) {
            estadoVerificacion = EstadoVerificacion.PENDIENTE;
        }
    }

    public enum EstadoVerificacion {
        VERIFICADO,
        PENDIENTE
    }
}
