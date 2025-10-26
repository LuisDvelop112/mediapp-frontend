package com.mediapp.citasbackend.controllers;

import com.mediapp.citasbackend.entities.Cita;
import com.mediapp.citasbackend.services.interfaces.CitaService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Tag(name = "Citas", description = "Gestión de citas médicas - crear, consultar, actualizar y cancelar citas")
@SecurityRequirement(name = "bearerAuth")
public class CitaController {

    private final CitaService citaService;

    @PostMapping // http://56.125.172.86:8080/api/citas
    @Operation(summary = "Crear nueva cita", description = "Crea una nueva cita médica en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cita creada exitosamente",
            content = @Content(schema = @Schema(implementation = Cita.class))),
        @ApiResponse(responseCode = "400", description = "Datos de cita inválidos")
    })
    public ResponseEntity<Cita> crearCita(
        @Parameter(description = "Datos de la cita a crear", required = true)
        @RequestBody Cita cita) {
        try {
            Cita nuevaCita = citaService.guardarCita(cita);
            return new ResponseEntity<>(nuevaCita, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping // http://56.125.172.86:8080/api/citas
    @Operation(summary = "Obtener todas las citas", description = "Retorna listado completo de citas médicas del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida exitosamente")
    public ResponseEntity<List<Cita>> obtenerTodasLasCitas() {
        List<Cita> citas = citaService.obtenerTodasLasCitas();
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}") // http://56.125.172.86:8080/api/citas/{id}
    @Operation(summary = "Obtener cita por ID", description = "Retorna los detalles de una cita específica por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita encontrada"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<Cita> obtenerCitaPorId(
        @Parameter(description = "ID de la cita", required = true, example = "1")
        @PathVariable Integer id) {
        Optional<Cita> cita = citaService.obtenerCitaPorId(id);
        return cita.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}") // http://56.125.172.86:8080/api/citas/{id}
    @Operation(summary = "Actualizar cita", description = "Actualiza la información de una cita existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<Cita> actualizarCita(
            @Parameter(description = "ID de la cita", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados de la cita", required = true)
            @RequestBody Cita cita) {
        try {
            Cita citaActualizada = citaService.actualizarCita(id, cita);
            return ResponseEntity.ok(citaActualizada);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}") // http://56.125.172.86:8080/api/citas/{id}
    @Operation(summary = "Eliminar cita", description = "Elimina una cita del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cita eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<Void> eliminarCita(
        @Parameter(description = "ID de la cita", required = true, example = "1")
        @PathVariable Integer id) {
        try {
            citaService.eliminarCita(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/paciente/{idPaciente}") // http://56.125.172.86:8080/api/citas/paciente/{idPaciente}
    @Operation(summary = "Obtener citas por paciente", description = "Retorna todas las citas de un paciente específico")
    @ApiResponse(responseCode = "200", description = "Lista de citas del paciente")
    public ResponseEntity<List<Cita>> obtenerCitasPorPaciente(
        @Parameter(description = "ID del paciente", required = true, example = "1")
        @PathVariable Integer idPaciente) {
        List<Cita> citas = citaService.obtenerCitasPorPacienteId(idPaciente);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/paciente/{idPaciente}/estado/{estado}") // http://56.125.172.86:8080/api/citas/paciente/{idPaciente}/estado/{estado}
    @Operation(summary = "Obtener citas por paciente y estado", 
               description = "Retorna las citas de un paciente filtradas por estado (PROGRAMADA, CONFIRMADA, EN_CURSO, COMPLETADA, CANCELADA)")
    @ApiResponse(responseCode = "200", description = "Lista de citas filtradas")
    public ResponseEntity<List<Cita>> obtenerCitasPorPacienteYEstado(
            @Parameter(description = "ID del paciente", required = true, example = "1")
            @PathVariable Integer idPaciente,
            @Parameter(description = "Estado de la cita", required = true, example = "PROGRAMADA")
            @PathVariable Cita.Estado estado) {
        List<Cita> citas = citaService.obtenerCitasPorPacienteYEstado(idPaciente, estado);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/paciente/{idPaciente}/programadas") // http://56.125.172.86:8080/api/citas/paciente/{idPaciente}/programadas
    @Operation(summary = "Obtener citas programadas por paciente", 
               description = "Retorna las citas programadas (futuras) de un paciente")
    @ApiResponse(responseCode = "200", description = "Lista de citas programadas")
    public ResponseEntity<List<Cita>> obtenerCitasProgramadasPorPaciente(
        @Parameter(description = "ID del paciente", required = true, example = "1")
        @PathVariable Integer idPaciente) {
        List<Cita> citas = citaService.obtenerCitasProgramadasPorPaciente(idPaciente);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/medico/{idMedico}") // http://56.125.172.86:8080/api/citas/medico/{idMedico}
    @Operation(summary = "Obtener citas por médico", description = "Retorna todas las citas de un médico específico")
    @ApiResponse(responseCode = "200", description = "Lista de citas del médico")
    public ResponseEntity<List<Cita>> obtenerCitasPorMedico(
        @Parameter(description = "ID del médico", required = true, example = "1")
        @PathVariable Integer idMedico) {
        List<Cita> citas = citaService.obtenerCitasPorMedicoId(idMedico);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas por médico y estado
     */
    @GetMapping("/medico/{idMedico}/estado/{estado}") // http://56.125.172.86:8080/api/citas/medico/{idMedico}/estado/{estado}
    public ResponseEntity<List<Cita>> obtenerCitasPorMedicoYEstado(
            @PathVariable Integer idMedico,
            @PathVariable Cita.Estado estado) {
        List<Cita> citas = citaService.obtenerCitasPorMedicoYEstado(idMedico, estado);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas programadas por médico
     */
    @GetMapping("/medico/{idMedico}/programadas") // http://56.125.172.86:8080/api/citas/medico/{idMedico}/programadas
    public ResponseEntity<List<Cita>> obtenerCitasProgramadasPorMedico(@PathVariable Integer idMedico) {
        List<Cita> citas = citaService.obtenerCitasProgramadasPorMedico(idMedico);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas por estado
     */
    @GetMapping("/estado/{estado}") // http://56.125.172.86:8080/api/citas/estado/{estado}
    public ResponseEntity<List<Cita>> obtenerCitasPorEstado(@PathVariable Cita.Estado estado) {
        List<Cita> citas = citaService.obtenerCitasPorEstado(estado);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas por fecha
     */
    @GetMapping("/fecha/{fecha}") // http://56.125.172.86:8080/api/citas/fecha/{fecha}
    public ResponseEntity<List<Cita>> obtenerCitasPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Cita> citas = citaService.obtenerCitasPorFecha(fecha);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas por médico y fecha
     */
    @GetMapping("/medico/{idMedico}/fecha/{fecha}") // http://56.125.172.86:8080/api/citas/medico/{idMedico}/fecha/{fecha}
    public ResponseEntity<List<Cita>> obtenerCitasPorMedicoYFecha(
            @PathVariable Integer idMedico,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Cita> citas = citaService.obtenerCitasPorMedicoYFecha(idMedico, fecha);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas por paciente y fecha
     */
    @GetMapping("/paciente/{idPaciente}/fecha/{fecha}") // http://56.125.172.86:8080/api/citas/paciente/{idPaciente}/fecha/{fecha}
    public ResponseEntity<List<Cita>> obtenerCitasPorPacienteYFecha(
            @PathVariable Integer idPaciente,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Cita> citas = citaService.obtenerCitasPorPacienteYFecha(idPaciente, fecha);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas en rango de fechas
     */
    @GetMapping("/rango-fechas") // http://56.125.172.86:8080/api/citas/rango-fechas
    public ResponseEntity<List<Cita>> obtenerCitasEnRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Cita> citas = citaService.obtenerCitasEnRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas por médico en rango de fechas
     */
    @GetMapping("/medico/{idMedico}/rango-fechas") // http://56.125.172.86:8080/api/citas/medico/{idMedico}/rango-fechas
    public ResponseEntity<List<Cita>> obtenerCitasPorMedicoEnRangoFechas(
            @PathVariable Integer idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Cita> citas = citaService.obtenerCitasPorMedicoEnRangoFechas(idMedico, fechaInicio, fechaFin);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas por paciente en rango de fechas
     */
    @GetMapping("/paciente/{idPaciente}/rango-fechas") // http://56.125.172.86:8080/api/citas/paciente/{idPaciente}/rango-fechas
    public ResponseEntity<List<Cita>> obtenerCitasPorPacienteEnRangoFechas(
            @PathVariable Integer idPaciente,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Cita> citas = citaService.obtenerCitasPorPacienteEnRangoFechas(idPaciente, fechaInicio, fechaFin);
        return ResponseEntity.ok(citas);
    }

    /**
     * Verificar disponibilidad de médico en fecha y hora
     */
    @GetMapping("/verificar-disponibilidad") // http://56.125.172.86:8080/api/citas/verificar-disponibilidad
    public ResponseEntity<Cita> verificarDisponibilidadMedicoFechaHora(
            @RequestParam Integer idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        Optional<Cita> cita = citaService.verificarDisponibilidadMedicoFechaHora(idMedico, fecha, hora);
        return cita.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Verificar si médico está disponible en fecha y hora
     */
    @GetMapping("/medico-disponible") // http://56.125.172.86:8080/api/citas/medico-disponible
    public ResponseEntity<Boolean> medicoDisponibleEnFechaHora(
            @RequestParam Integer idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {
        boolean disponible = citaService.medicoDisponibleEnFechaHora(idMedico, fecha, hora);
        return ResponseEntity.ok(disponible);
    }

    /**
     * Obtener citas por tipo
     */
    @GetMapping("/tipo/{tipoCita}") // http://56.125.172.86:8080/api/citas/tipo/{tipoCita}
    public ResponseEntity<List<Cita>> obtenerCitasPorTipo(@PathVariable Cita.TipoCita tipoCita) {
        List<Cita> citas = citaService.obtenerCitasPorTipo(tipoCita);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas de telemedicina programadas por médico
     */
    @GetMapping("/medico/{idMedico}/telemedicina-programadas") // http://56.125.172.86:8080/api/citas/medico/{idMedico}/telemedicina-programadas
    public ResponseEntity<List<Cita>> obtenerCitasTelemedicinaProgramadasPorMedico(
            @PathVariable Integer idMedico) {
        List<Cita> citas = citaService.obtenerCitasTelemedicinaProgramadasPorMedico(idMedico);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener próximas citas por paciente
     */
    @GetMapping("/paciente/{idPaciente}/proximas") // http://56.125.172.86:8080/api/citas/paciente/{idPaciente}/proximas
    public ResponseEntity<List<Cita>> obtenerProximasCitasPorPaciente(@PathVariable Integer idPaciente) {
        List<Cita> citas = citaService.obtenerProximasCitasPorPaciente(idPaciente);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener próximas citas por médico
     */
    @GetMapping("/medico/{idMedico}/proximas") // http://56.125.172.86:8080/api/citas/medico/{idMedico}/proximas
    public ResponseEntity<List<Cita>> obtenerProximasCitasPorMedico(@PathVariable Integer idMedico) {
        List<Cita> citas = citaService.obtenerProximasCitasPorMedico(idMedico);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener historial de citas por paciente
     */
    @GetMapping("/paciente/{idPaciente}/historial") // http://56.125.172.86:8080/api/citas/paciente/{idPaciente}/historial
    public ResponseEntity<List<Cita>> obtenerHistorialCitasPorPaciente(@PathVariable Integer idPaciente) {
        List<Cita> citas = citaService.obtenerHistorialCitasPorPaciente(idPaciente);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener historial de citas por médico
     */
    @GetMapping("/medico/{idMedico}/historial") // http://56.125.172.86:8080/api/citas/medico/{idMedico}/historial
    public ResponseEntity<List<Cita>> obtenerHistorialCitasPorMedico(@PathVariable Integer idMedico) {
        List<Cita> citas = citaService.obtenerHistorialCitasPorMedico(idMedico);
        return ResponseEntity.ok(citas);
    }

    /**
     * Contar citas por estado
     */
    @GetMapping("/contar/estado/{estado}") // http://56.125.172.86:8080/api/citas/contar/estado/{estado}
    public ResponseEntity<Long> contarCitasPorEstado(@PathVariable Cita.Estado estado) {
        Long count = citaService.contarCitasPorEstado(estado);
        return ResponseEntity.ok(count);
    }

    /**
     * Contar citas por médico y estado
     */
    @GetMapping("/contar/medico/{idMedico}/estado/{estado}") // http://56.125.172.86:8080/api/citas/contar/medico/{idMedico}/estado/{estado}
    public ResponseEntity<Long> contarCitasPorMedicoYEstado(
            @PathVariable Integer idMedico,
            @PathVariable Cita.Estado estado) {
        Long count = citaService.contarCitasPorMedicoYEstado(idMedico, estado);
        return ResponseEntity.ok(count);
    }

    /**
     * Contar citas por paciente y estado
     */
    @GetMapping("/contar/paciente/{idPaciente}/estado/{estado}") // http://56.125.172.86:8080/api/citas/contar/paciente/{idPaciente}/estado/{estado}
    public ResponseEntity<Long> contarCitasPorPacienteYEstado(
            @PathVariable Integer idPaciente,
            @PathVariable Cita.Estado estado) {
        Long count = citaService.contarCitasPorPacienteYEstado(idPaciente, estado);
        return ResponseEntity.ok(count);
    }

    /**
     * Obtener citas del día por médico
     */
    @GetMapping("/medico/{idMedico}/dia/{fecha}") // http://56.125.172.86:8080/api/citas/medico/{idMedico}/dia/{fecha}
    public ResponseEntity<List<Cita>> obtenerCitasDelDiaPorMedico(
            @PathVariable Integer idMedico,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Cita> citas = citaService.obtenerCitasDelDiaPorMedico(idMedico, fecha);
        return ResponseEntity.ok(citas);
    }

    /**
     * Obtener citas canceladas en rango
     */
    @GetMapping("/canceladas-rango") // http://56.125.172.86:8080/api/citas/canceladas-rango
    public ResponseEntity<List<Cita>> obtenerCitasCanceladasEnRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        List<Cita> citas = citaService.obtenerCitasCanceladasEnRango(fechaInicio, fechaFin);
        return ResponseEntity.ok(citas);
    }

    /**
     * Verificar si existe cita entre paciente, médico y fecha
     */
    @GetMapping("/existe") // http://56.125.172.86:8080/api/citas/existe
    public ResponseEntity<Boolean> existeCitaEntrePacienteMedicoYFecha(
            @RequestParam Integer idPaciente,
            @RequestParam Integer idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCita) {
        boolean existe = citaService.existeCitaEntrePacienteMedicoYFecha(idPaciente, idMedico, fechaCita);
        return ResponseEntity.ok(existe);
    }

    /**
     * Obtener última cita entre paciente y médico
     */
    @GetMapping("/ultima") // http://56.125.172.86:8080/api/citas/ultima
    public ResponseEntity<Cita> obtenerUltimaCitaEntrePacienteYMedico(
            @RequestParam Integer idPaciente,
            @RequestParam Integer idMedico) {
        Optional<Cita> cita = citaService.obtenerUltimaCitaEntrePacienteYMedico(idPaciente, idMedico);
        return cita.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Completar una cita
     */
    @PatchMapping("/{id}/completar") // http://56.125.172.86:8080/api/citas/{id}/completar
    public ResponseEntity<Cita> completarCita(@PathVariable Integer id) {
        try {
            Cita cita = citaService.completarCita(id);
            return ResponseEntity.ok(cita);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Cancelar una cita
     */
    @PatchMapping("/{id}/cancelar") // http://56.125.172.86:8080/api/citas/{id}/cancelar
    public ResponseEntity<Cita> cancelarCita(@PathVariable Integer id) {
        try {
            Cita cita = citaService.cancelarCita(id);
            return ResponseEntity.ok(cita);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Marcar como no asistió
     */
    @PatchMapping("/{id}/no-asistio") // http://56.125.172.86:8080/api/citas/{id}/no-asistio
    public ResponseEntity<Cita> marcarComoNoAsistio(@PathVariable Integer id) {
        try {
            Cita cita = citaService.marcarComoNoAsistio(id);
            return ResponseEntity.ok(cita);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
