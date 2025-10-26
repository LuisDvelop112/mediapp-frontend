package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "citas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una cita médica")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    @Schema(description = "ID único de la cita (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idCita;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    @Schema(description = "Paciente que solicita la cita", required = true)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    @Schema(description = "Médico que atenderá la cita", required = true)
    private Medico medico;

    @Column(name = "fecha_cita", nullable = false)
    @Schema(description = "Fecha de la cita", example = "2025-11-15", required = true)
    private LocalDate fechaCita;

    @Column(name = "hora_cita", nullable = false)
    @Schema(description = "Hora de la cita", example = "10:30:00", required = true)
    private LocalTime horaCita;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cita", nullable = false, length = 20)
    @Schema(description = "Tipo de cita: PRESENCIAL o TELEMEDICINA", example = "PRESENCIAL", required = true, allowableValues = {"PRESENCIAL", "TELEMEDICINA"})
    private TipoCita tipoCita;

    @Column(name = "motivo_consulta", length = 255)
    @Schema(description = "Motivo o razón de la consulta médica (opcional)", example = "Dolor de cabeza recurrente", required = false)
    private String motivoConsulta;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Schema(description = "Estado de la cita (por defecto: PROGRAMADA)", example = "PROGRAMADA", required = false, allowableValues = {"PROGRAMADA", "COMPLETADA", "CANCELADA", "NO_ASISTIO"})
    private Estado estado;

    @Column(name = "enlace_videollamada", length = 255)
    @Schema(description = "Enlace para videollamada (solo para telemedicina, opcional)", example = "https://meet.google.com/abc-defg-hij", required = false)
    private String enlaceVideollamada;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Schema(description = "Fecha y hora de creación de la cita (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "2025-10-24T10:30:00")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_cancelacion")
    @Schema(description = "Fecha y hora de cancelación de la cita (si aplica)", accessMode = Schema.AccessMode.READ_ONLY, example = "2025-10-23T15:00:00", required = false)
    private LocalDateTime fechaCancelacion;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (estado == null) {
            estado = Estado.PROGRAMADA;
        }
    }

    public enum TipoCita {
        PRESENCIAL,
        TELEMEDICINA
    }

    public enum Estado {
        PROGRAMADA,
        COMPLETADA,
        CANCELADA,
        NO_ASISTIO
    }
}
