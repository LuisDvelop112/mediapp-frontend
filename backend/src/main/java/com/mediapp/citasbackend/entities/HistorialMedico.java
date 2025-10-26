package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "historial_medico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa el historial médico de una cita")
public class HistorialMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    @Schema(description = "ID único del historial médico (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idHistorial;

    @OneToOne
    @JoinColumn(name = "id_cita", nullable = false, unique = true)
    @Schema(description = "Cita médica asociada a este historial", required = true)
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    @Schema(description = "Paciente del historial médico", required = true)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    @Schema(description = "Médico que atendió la cita", required = true)
    private Medico medico;

    @Column(name = "diagnostico", columnDefinition = "TEXT")
    @Schema(description = "Diagnóstico médico realizado (opcional)", example = "Gripe común", required = false)
    private String diagnostico;

    @Column(name = "sintomas_reportados", columnDefinition = "TEXT")
    @Schema(description = "Síntomas reportados por el paciente (opcional)", example = "Fiebre, dolor de garganta, tos", required = false)
    private String sintomasReportados;

    @Column(name = "tratamiento_recomendado", columnDefinition = "TEXT")
    @Schema(description = "Tratamiento recomendado por el médico (opcional)", example = "Reposo, hidratación abundante", required = false)
    private String tratamientoRecomendado;

    @Column(name = "medicamentos_prescritos", columnDefinition = "TEXT")
    @Schema(description = "Lista de medicamentos prescritos (opcional)", example = "Paracetamol 500mg, Ibuprofeno 400mg", required = false)
    private String medicamentosPrescritos;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    @Schema(description = "Observaciones adicionales del médico (opcional)", example = "Paciente en buen estado general", required = false)
    private String observaciones;

    @Column(name = "fecha_proximo_seguimiento")
    @Schema(description = "Fecha sugerida para próximo seguimiento (opcional)", example = "2025-11-30", required = false)
    private LocalDate fechaProximoSeguimiento;
}
