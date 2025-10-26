package com.mediapp.citasbackend.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una notificación del sistema")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    @Schema(description = "ID único de la notificación (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "1")
    private Integer idNotificacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    @Schema(description = "Usuario destinatario de la notificación", required = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion", nullable = false, length = 30)
    @Schema(description = "Tipo de notificación", example = "RECORDATORIO", required = true, allowableValues = {"CITA_CONFIRMADA", "RECORDATORIO", "RESULTADO", "MENSAJE"})
    private TipoNotificacion tipoNotificacion;

    @Column(name = "titulo", nullable = false, length = 100)
    @Schema(description = "Título de la notificación", example = "Recordatorio de cita", required = true)
    private String titulo;

    @Column(name = "contenido", columnDefinition = "TEXT")
    @Schema(description = "Contenido o mensaje de la notificación (opcional)", example = "Su cita es mañana a las 10:00 AM", required = false)
    private String contenido;

    @Column(name = "fecha_envio", nullable = false)
    @Schema(description = "Fecha y hora de envío de la notificación (generado automáticamente)", accessMode = Schema.AccessMode.READ_ONLY, example = "2025-10-24T10:30:00")
    private LocalDateTime fechaEnvio;

    @Column(name = "leida", nullable = false)
    @Schema(description = "Indica si la notificación ha sido leída (por defecto: false)", example = "false", required = false)
    private Boolean leida;

    @Column(name = "enlace_relacionado", length = 255)
    @Schema(description = "URL o enlace relacionado con la notificación (opcional)", example = "/citas/123", required = false)
    private String enlaceRelacionado;

    @PrePersist
    protected void onCreate() {
        if (fechaEnvio == null) {
            fechaEnvio = LocalDateTime.now();
        }
        if (leida == null) {
            leida = false;
        }
    }

    public enum TipoNotificacion {
        CITA_CONFIRMADA,
        RECORDATORIO,
        RESULTADO,
        MENSAJE
    }
}
