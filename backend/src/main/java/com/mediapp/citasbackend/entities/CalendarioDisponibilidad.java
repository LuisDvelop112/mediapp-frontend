package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "calendario_disponibilidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa la disponibilidad horaria de un médico")
public class CalendarioDisponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disponibilidad")
    @Schema(description = "ID único del horario de disponibilidad (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idDisponibilidad;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    @Schema(description = "Médico al que pertenece este horario", required = true)
    private Medico medico;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false, length = 20)
    @Schema(description = "Día de la semana", example = "LUNES", required = true, allowableValues = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"})
    private DiaSemana diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    @Schema(description = "Hora de inicio de disponibilidad", example = "08:00:00", required = true)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    @Schema(description = "Hora de fin de disponibilidad", example = "12:00:00", required = true)
    private LocalTime horaFin;

    @Column(name = "intervalo_cita_minutos", nullable = false)
    @Schema(description = "Intervalo en minutos entre cada cita", example = "30", required = true)
    private Integer intervaloCitaMinutos;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Schema(description = "Estado del horario (por defecto: ACTIVO)", example = "ACTIVO", required = false, allowableValues = {"ACTIVO", "INACTIVO"})
    private Estado estado;

    @PrePersist
    protected void onCreate() {
        if (estado == null) {
            estado = Estado.ACTIVO;
        }
    }

    public enum DiaSemana {
        LUNES,
        MARTES,
        MIERCOLES,
        JUEVES,
        VIERNES,
        SABADO,
        DOMINGO
    }

    public enum Estado {
        ACTIVO,
        INACTIVO
    }
}
