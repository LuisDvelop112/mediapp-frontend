package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un paciente en el sistema")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    @Schema(description = "ID único del paciente (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idPaciente;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    @Schema(description = "Usuario asociado al paciente", required = true)
    private Usuario usuario;

    @Column(name = "numero_identificacion", length = 50)
    @Schema(description = "Número de documento de identificación (opcional)", example = "1234567890", required = false)
    private String numeroIdentificacion;

    @Column(name = "tipo_sangre", length = 5)
    @Schema(description = "Tipo de sangre del paciente (opcional)", example = "O+", required = false)
    private String tipoSangre;

    @Column(name = "alergias", columnDefinition = "TEXT")
    @Schema(description = "Alergias conocidas del paciente (opcional)", example = "Penicilina, Polen", required = false)
    private String alergias;

    @Column(name = "enfermedades_cronicas", columnDefinition = "TEXT")
    @Schema(description = "Enfermedades crónicas diagnosticadas (opcional)", example = "Diabetes tipo 2, Hipertensión", required = false)
    private String enfermedadesCronicas;

    @Column(name = "medicamentos_actuales", columnDefinition = "TEXT")
    @Schema(description = "Medicamentos que el paciente toma actualmente (opcional)", example = "Metformina 850mg, Losartán 50mg", required = false)
    private String medicamentosActuales;

    @Column(name = "contacto_emergencia", length = 100)
    @Schema(description = "Nombre del contacto de emergencia (opcional)", example = "Ana Rodriguez", required = false)
    private String contactoEmergencia;

    @Column(name = "telefono_emergencia", length = 20)
    @Schema(description = "Teléfono del contacto de emergencia (opcional)", example = "3201234567", required = false)
    private String telefonoEmergencia;
}
