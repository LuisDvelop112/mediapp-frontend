package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "especialidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una especialidad médica")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    @Schema(description = "ID único de la especialidad (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idEspecialidad;

    @Column(name = "nombre_especialidad", nullable = false, length = 100)
    @Schema(description = "Nombre de la especialidad médica", example = "Cardiología", required = true)
    private String nombreEspecialidad;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    @Schema(description = "Descripción detallada de la especialidad (opcional)", example = "Especialidad médica dedicada al estudio del corazón y sistema cardiovascular", required = false)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    @Schema(description = "Estado de la especialidad (por defecto: ACTIVA)", example = "ACTIVA", required = false, allowableValues = {"ACTIVA", "INACTIVA"})
    private Estado estado;

    @PrePersist
    protected void onCreate() {
        if (estado == null) {
            estado = Estado.ACTIVA;
        }
    }

    public enum Estado {
        ACTIVA,
        INACTIVA
    }
}
