package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "prescripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una prescripción médica")
public class Prescripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prescripcion")
    @Schema(description = "ID único de la prescripción (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idPrescripcion;

    @ManyToOne
    @JoinColumn(name = "id_historial", nullable = false)
    @Schema(description = "Historial médico al que pertenece esta prescripción", required = true)
    private HistorialMedico historialMedico;

    @Column(name = "nombre_medicamento", nullable = false, length = 100)
    @Schema(description = "Nombre del medicamento prescrito", example = "Paracetamol", required = true)
    private String nombreMedicamento;

    @Column(name = "dosis", length = 50)
    @Schema(description = "Dosis del medicamento (opcional)", example = "500mg", required = false)
    private String dosis;

    @Column(name = "frecuencia", length = 50)
    @Schema(description = "Frecuencia de administración (opcional)", example = "Cada 8 horas", required = false)
    private String frecuencia;

    @Column(name = "duracion_dias")
    @Schema(description = "Duración del tratamiento en días (opcional)", example = "7", required = false)
    private Integer duracionDias;

    @Column(name = "instrucciones", columnDefinition = "TEXT")
    @Schema(description = "Instrucciones adicionales para el medicamento (opcional)", example = "Tomar después de las comidas", required = false)
    private String instrucciones;

    @Column(name = "fecha_prescripcion", nullable = false)
    @Schema(description = "Fecha de emisión de la prescripción (por defecto: fecha actual)", accessMode = Schema.AccessMode.READ_ONLY, example = "2025-10-24")
    private LocalDate fechaPrescripcion;

    @PrePersist
    protected void onCreate() {
        if (fechaPrescripcion == null) {
            fechaPrescripcion = LocalDate.now();
        }
    }
}
