package com.mediapp.citasbackend.controllers;

import com.mediapp.citasbackend.entities.Notificacion;
import com.mediapp.citasbackend.services.interfaces.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Gestión de notificaciones - alertas y mensajes del sistema")
@SecurityRequirement(name = "bearerAuth")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping // http://56.125.172.86:8080/api/notificaciones
    @Operation(summary = "Crear nueva notificación", 
               description = "Crea una nueva notificación para un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente",
            content = @Content(schema = @Schema(implementation = Notificacion.class))),
        @ApiResponse(responseCode = "400", description = "Datos de notificación inválidos")
    })
    public ResponseEntity<Notificacion> crearNotificacion(
        @Parameter(description = "Datos de la notificación a crear", required = true)
        @RequestBody Notificacion notificacion) {
        try {
            Notificacion nuevaNotificacion = notificacionService.guardarNotificacion(notificacion);
            return new ResponseEntity<>(nuevaNotificacion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping // http://56.125.172.86:8080/api/notificaciones
    @Operation(summary = "Obtener todas las notificaciones", 
               description = "Retorna listado completo de notificaciones del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones obtenida exitosamente")
    public ResponseEntity<List<Notificacion>> obtenerTodasLasNotificaciones() {
        List<Notificacion> notificaciones = notificacionService.obtenerTodasLasNotificaciones();
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/{id}") // http://56.125.172.86:8080/api/notificaciones/{id}
    @Operation(summary = "Obtener notificación por ID", 
               description = "Retorna los detalles de una notificación específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<Notificacion> obtenerNotificacionPorId(
        @Parameter(description = "ID de la notificación", required = true, example = "1")
        @PathVariable Integer id) {
        Optional<Notificacion> notificacion = notificacionService.obtenerNotificacionPorId(id);
        return notificacion.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}") // http://56.125.172.86:8080/api/notificaciones/{id}
    @Operation(summary = "Actualizar notificación", 
               description = "Actualiza una notificación existente (ej: marcar como leída)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<Notificacion> actualizarNotificacion(
            @Parameter(description = "ID de la notificación", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados de la notificación", required = true)
            @RequestBody Notificacion notificacion) {
        try {
            Notificacion notificacionActualizada = notificacionService.actualizarNotificacion(id, notificacion);
            return ResponseEntity.ok(notificacionActualizada);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Eliminar una notificación
     */
    @DeleteMapping("/{id}") // http://56.125.172.86:8080/api/notificaciones/{id}
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Integer id) {
        try {
            notificacionService.eliminarNotificacion(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtener notificaciones por usuario
     */
    @GetMapping("/usuario/{idUsuario}") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorUsuario(@PathVariable Integer idUsuario) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorUsuarioId(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones por usuario ordenadas
     */
    @GetMapping("/usuario/{idUsuario}/ordenadas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/ordenadas
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorUsuarioOrdenadas(
            @PathVariable Integer idUsuario) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorUsuarioOrdenadas(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones no leídas por usuario
     */
    @GetMapping("/usuario/{idUsuario}/no-leidas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/no-leidas
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesNoLeidasPorUsuario(
            @PathVariable Integer idUsuario) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesNoLeidasPorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones leídas por usuario
     */
    @GetMapping("/usuario/{idUsuario}/leidas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/leidas
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesLeidasPorUsuario(
            @PathVariable Integer idUsuario) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesLeidasPorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones por estado de lectura
     */
    @GetMapping("/estado-lectura/{leida}") // http://56.125.172.86:8080/api/notificaciones/estado-lectura/{leida}
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorEstadoLectura(
            @PathVariable Boolean leida) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorEstadoLectura(leida);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones por usuario y estado de lectura
     */
    @GetMapping("/usuario/{idUsuario}/estado-lectura/{leida}") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/estado-lectura/{leida}
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorUsuarioYEstadoLectura(
            @PathVariable Integer idUsuario,
            @PathVariable Boolean leida) {
        List<Notificacion> notificaciones = 
                notificacionService.obtenerNotificacionesPorUsuarioYEstadoLectura(idUsuario, leida);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones por tipo
     */
    @GetMapping("/tipo/{tipo}") // http://56.125.172.86:8080/api/notificaciones/tipo/{tipo}
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorTipo(
            @PathVariable Notificacion.TipoNotificacion tipo) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorTipo(tipo);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones por usuario y tipo
     */
    @GetMapping("/usuario/{idUsuario}/tipo/{tipo}") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/tipo/{tipo}
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorUsuarioYTipo(
            @PathVariable Integer idUsuario,
            @PathVariable Notificacion.TipoNotificacion tipo) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorUsuarioYTipo(idUsuario, tipo);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones no leídas por usuario y tipo
     */
    @GetMapping("/usuario/{idUsuario}/tipo/{tipo}/no-leidas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/tipo/{tipo}/no-leidas
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesNoLeidasPorUsuarioYTipo(
            @PathVariable Integer idUsuario,
            @PathVariable Notificacion.TipoNotificacion tipo) {
        List<Notificacion> notificaciones = 
                notificacionService.obtenerNotificacionesNoLeidasPorUsuarioYTipo(idUsuario, tipo);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Contar notificaciones no leídas por usuario
     */
    @GetMapping("/usuario/{idUsuario}/contar/no-leidas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/contar/no-leidas
    public ResponseEntity<Long> contarNotificacionesNoLeidasPorUsuario(@PathVariable Integer idUsuario) {
        Long count = notificacionService.contarNotificacionesNoLeidasPorUsuario(idUsuario);
        return ResponseEntity.ok(count);
    }

    /**
     * Contar notificaciones por usuario
     */
    @GetMapping("/usuario/{idUsuario}/contar") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/contar
    public ResponseEntity<Long> contarNotificacionesPorUsuario(@PathVariable Integer idUsuario) {
        Long count = notificacionService.contarNotificacionesPorUsuario(idUsuario);
        return ResponseEntity.ok(count);
    }

    /**
     * Obtener distribución de notificaciones por usuario
     */
    @GetMapping("/usuario/{idUsuario}/distribucion") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/distribucion
    public ResponseEntity<Map<Notificacion.TipoNotificacion, Long>> obtenerDistribucionNotificacionesPorUsuario(
            @PathVariable Integer idUsuario) {
        Map<Notificacion.TipoNotificacion, Long> distribucion = 
                notificacionService.obtenerDistribucionNotificacionesPorUsuario(idUsuario);
        return ResponseEntity.ok(distribucion);
    }

    /**
     * Obtener notificaciones en rango de fechas
     */
    @GetMapping("/rango-fechas") // http://56.125.172.86:8080/api/notificaciones/rango-fechas
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesEnRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Notificacion> notificaciones = 
                notificacionService.obtenerNotificacionesEnRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones por usuario en rango de fechas
     */
    @GetMapping("/usuario/{idUsuario}/rango-fechas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/rango-fechas
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesPorUsuarioEnRangoFechas(
            @PathVariable Integer idUsuario,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Notificacion> notificaciones = 
                notificacionService.obtenerNotificacionesPorUsuarioEnRangoFechas(idUsuario, fechaInicio, fechaFin);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones recientes por usuario
     */
    @GetMapping("/usuario/{idUsuario}/recientes") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/recientes
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesRecientesPorUsuario(
            @PathVariable Integer idUsuario,
            @RequestParam(defaultValue = "24") int horas) {
        List<Notificacion> notificaciones = 
                notificacionService.obtenerNotificacionesRecientesPorUsuario(idUsuario, horas);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones recientes no leídas por usuario
     */
    @GetMapping("/usuario/{idUsuario}/recientes/no-leidas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/recientes/no-leidas
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesRecientesNoLeidasPorUsuario(
            @PathVariable Integer idUsuario,
            @RequestParam(defaultValue = "24") int horas) {
        List<Notificacion> notificaciones = 
                notificacionService.obtenerNotificacionesRecientesNoLeidasPorUsuario(idUsuario, horas);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones del día por usuario
     */
    @GetMapping("/usuario/{idUsuario}/del-dia") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/del-dia
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesDelDiaPorUsuario(
            @PathVariable Integer idUsuario) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesDelDiaPorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener recordatorios pendientes
     */
    @GetMapping("/recordatorios-pendientes") // http://56.125.172.86:8080/api/notificaciones/recordatorios-pendientes
    public ResponseEntity<List<Notificacion>> obtenerRecordatoriosPendientes() {
        List<Notificacion> notificaciones = notificacionService.obtenerRecordatoriosPendientes();
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener recordatorios pendientes por usuario
     */
    @GetMapping("/usuario/{idUsuario}/recordatorios-pendientes") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/recordatorios-pendientes
    public ResponseEntity<List<Notificacion>> obtenerRecordatoriosPendientesPorUsuario(
            @PathVariable Integer idUsuario) {
        List<Notificacion> notificaciones = notificacionService.obtenerRecordatoriosPendientesPorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones con enlace
     */
    @GetMapping("/con-enlace") // http://56.125.172.86:8080/api/notificaciones/con-enlace
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesConEnlace() {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesConEnlace();
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener notificaciones con enlace por usuario
     */
    @GetMapping("/usuario/{idUsuario}/con-enlace") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/con-enlace
    public ResponseEntity<List<Notificacion>> obtenerNotificacionesConEnlacePorUsuario(
            @PathVariable Integer idUsuario) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesConEnlacePorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Obtener últimas notificaciones por usuario
     */
    @GetMapping("/usuario/{idUsuario}/ultimas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/ultimas
    public ResponseEntity<List<Notificacion>> obtenerUltimasNotificacionesPorUsuario(
            @PathVariable Integer idUsuario,
            @RequestParam(defaultValue = "10") int limite) {
        List<Notificacion> notificaciones = 
                notificacionService.obtenerUltimasNotificacionesPorUsuario(idUsuario, limite);
        return ResponseEntity.ok(notificaciones);
    }

    /**
     * Marcar notificación como leída
     */
    @PatchMapping("/{id}/marcar-leida") // http://56.125.172.86:8080/api/notificaciones/{id}/marcar-leida
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Integer id) {
        try {
            Notificacion notificacion = notificacionService.marcarComoLeida(id);
            return ResponseEntity.ok(notificacion);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Marcar todas las notificaciones como leídas por usuario
     */
    @PatchMapping("/usuario/{idUsuario}/marcar-todas-leidas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/marcar-todas-leidas
    public ResponseEntity<Void> marcarTodasComoLeidasPorUsuario(@PathVariable Integer idUsuario) {
        try {
            notificacionService.marcarTodasComoLeidasPorUsuario(idUsuario);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Marcar como leídas por usuario y tipo
     */
    @PatchMapping("/usuario/{idUsuario}/tipo/{tipo}/marcar-leidas") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/tipo/{tipo}/marcar-leidas
    public ResponseEntity<Void> marcarComoLeidasPorUsuarioYTipo(
            @PathVariable Integer idUsuario,
            @PathVariable Notificacion.TipoNotificacion tipo) {
        try {
            notificacionService.marcarComoLeidasPorUsuarioYTipo(idUsuario, tipo);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Eliminar notificaciones antiguas (días)
     */
    @DeleteMapping("/antiguas/{dias}") // http://56.125.172.86:8080/api/notificaciones/antiguas/{dias}
    public ResponseEntity<Void> eliminarNotificacionesAntiguas(@PathVariable int dias) {
        try {
            notificacionService.eliminarNotificacionesAntiguas(dias);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Eliminar notificaciones leídas antiguas por usuario
     */
    @DeleteMapping("/usuario/{idUsuario}/leidas-antiguas/{dias}") // http://56.125.172.86:8080/api/notificaciones/usuario/{idUsuario}/leidas-antiguas/{dias}
    public ResponseEntity<Void> eliminarNotificacionesLeidasAntiguasPorUsuario(
            @PathVariable Integer idUsuario,
            @PathVariable int dias) {
        try {
            notificacionService.eliminarNotificacionesLeidasAntiguasPorUsuario(idUsuario, dias);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
