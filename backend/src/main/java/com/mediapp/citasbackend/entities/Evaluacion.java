package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa la evaluación de un paciente a un médico")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    @Schema(description = "ID único de la evaluación (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idEvaluacion;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    @Schema(description = "Paciente que realiza la evaluación", required = true)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    @Schema(description = "Médico evaluado", required = true)
    private Medico medico;

    @Column(name = "calificacion", nullable = false)
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Schema(description = "Calificación del médico (escala de 1 a 5)", example = "5", required = true, minimum = "1", maximum = "5")
    private Integer calificacion;

    @Column(name = "comentario", columnDefinition = "TEXT")
    @Schema(description = "Comentario o reseña del paciente (opcional)", example = "Excelente atención, muy profesional", required = false)
    private String comentario;

    @Column(name = "fecha_evaluacion", nullable = false)
    @Schema(description = "Fecha y hora de la evaluación (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "2025-10-24T14:30:00")
    private LocalDateTime fechaEvaluacion;

    @Column(name = "respuesta_medico", columnDefinition = "TEXT")
    @Schema(description = "Respuesta del médico al comentario (opcional)", example = "Gracias por su confianza", required = false)
    private String respuestaMedico;

    @PrePersist
    protected void onCreate() {
        if (fechaEvaluacion == null) {
            fechaEvaluacion = LocalDateTime.now();
        }
    }
}
