package com.mediapp.citasbackend.controllers;

import com.mediapp.citasbackend.entities.HistorialMedico;
import com.mediapp.citasbackend.services.interfaces.HistorialMedicoService;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/historial-medico")
@RequiredArgsConstructor
@Tag(name = "Historial Médico", description = "Gestión de historiales médicos - registros clínicos de pacientes")
@SecurityRequirement(name = "bearerAuth")
public class HistorialMedicoController {

    private final HistorialMedicoService historialMedicoService;

    @PostMapping // http://56.125.172.86:8080/api/historial-medico
    @Operation(summary = "Crear nuevo historial médico", 
               description = "Registra un nuevo historial médico para una cita")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Historial médico creado exitosamente",
            content = @Content(schema = @Schema(implementation = HistorialMedico.class))),
        @ApiResponse(responseCode = "400", description = "Datos de historial médico inválidos")
    })
    public ResponseEntity<HistorialMedico> crearHistorialMedico(
        @Parameter(description = "Datos del historial médico a crear", required = true)
        @RequestBody HistorialMedico historialMedico) {
        try {
            HistorialMedico nuevoHistorial = historialMedicoService.guardarHistorialMedico(historialMedico);
            return new ResponseEntity<>(nuevoHistorial, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping // http://56.125.172.86:8080/api/historial-medico
    @Operation(summary = "Obtener todos los historiales médicos", 
               description = "Retorna listado completo de historiales médicos")
    @ApiResponse(responseCode = "200", description = "Lista de historiales obtenida exitosamente")
    public ResponseEntity<List<HistorialMedico>> obtenerTodosLosHistorialesMedicos() {
        List<HistorialMedico> historiales = historialMedicoService.obtenerTodosLosHistorialesMedicos();
        return ResponseEntity.ok(historiales);
    }

    @GetMapping("/{id}") // http://56.125.172.86:8080/api/historial-medico/{id}
    @Operation(summary = "Obtener historial médico por ID", 
               description = "Retorna los detalles de un historial médico específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial médico encontrado"),
        @ApiResponse(responseCode = "404", description = "Historial médico no encontrado")
    })
    public ResponseEntity<HistorialMedico> obtenerHistorialMedicoPorId(
        @Parameter(description = "ID del historial médico", required = true, example = "1")
        @PathVariable Integer id) {
        Optional<HistorialMedico> historial = historialMedicoService.obtenerHistorialMedicoPorId(id);
        return historial.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}") // http://56.125.172.86:8080/api/historial-medico/{id}
    @Operation(summary = "Actualizar historial médico", 
               description = "Actualiza la información de un historial médico existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial médico actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Historial médico no encontrado")
    })
    public ResponseEntity<HistorialMedico> actualizarHistorialMedico(
            @Parameter(description = "ID del historial médico", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del historial médico", required = true)
            @RequestBody HistorialMedico historialMedico) {
        try {
            HistorialMedico historialActualizado = 
                    historialMedicoService.actualizarHistorialMedico(id, historialMedico);
            return ResponseEntity.ok(historialActualizado);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Eliminar un historial médico
     */
    @DeleteMapping("/{id}") // http://56.125.172.86:8080/api/historial-medico/{id}
    public ResponseEntity<Void> eliminarHistorialMedico(@PathVariable Integer id) {
        try {
            historialMedicoService.eliminarHistorialMedico(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtener historial médico por ID de cita
     */
    @GetMapping("/cita/{idCita}") // http://56.125.172.86:8080/api/historial-medico/cita/{idCita}
    public ResponseEntity<HistorialMedico> obtenerHistorialMedicoPorCita(@PathVariable Integer idCita) {
        Optional<HistorialMedico> historial = historialMedicoService.obtenerHistorialMedicoPorCitaId(idCita);
        return historial.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Verificar si existe historial médico por ID de cita
     */
    @GetMapping("/cita/{idCita}/existe") // http://56.125.172.86:8080/api/historial-medico/cita/{idCita}/existe
    public ResponseEntity<Boolean> existeHistorialMedicoPorCita(@PathVariable Integer idCita) {
        boolean existe = historialMedicoService.existeHistorialMedicoPorCitaId(idCita);
        return ResponseEntity.ok(existe);
    }

    /**
     * Obtener historial médico por paciente
     */
    @GetMapping("/paciente/{idPaciente}") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoPorPaciente(
            @PathVariable Integer idPaciente) {
        List<HistorialMedico> historiales = historialMedicoService.obtenerHistorialMedicoPorPacienteId(idPaciente);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico por paciente ordenado
     */
    @GetMapping("/paciente/{idPaciente}/ordenado") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}/ordenado
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoPorPacienteOrdenado(
            @PathVariable Integer idPaciente) {
        List<HistorialMedico> historiales = 
                historialMedicoService.obtenerHistorialMedicoPorPacienteOrdenado(idPaciente);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico por médico
     */
    @GetMapping("/medico/{idMedico}") // http://56.125.172.86:8080/api/historial-medico/medico/{idMedico}
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoPorMedico(
            @PathVariable Integer idMedico) {
        List<HistorialMedico> historiales = historialMedicoService.obtenerHistorialMedicoPorMedicoId(idMedico);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico por médico ordenado
     */
    @GetMapping("/medico/{idMedico}/ordenado") // http://56.125.172.86:8080/api/historial-medico/medico/{idMedico}/ordenado
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoPorMedicoOrdenado(
            @PathVariable Integer idMedico) {
        List<HistorialMedico> historiales = 
                historialMedicoService.obtenerHistorialMedicoPorMedicoOrdenado(idMedico);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico entre paciente y médico
     */
    @GetMapping("/entre-paciente-medico") // http://56.125.172.86:8080/api/historial-medico/entre-paciente-medico
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoEntrePacienteYMedico(
            @RequestParam Integer idPaciente,
            @RequestParam Integer idMedico) {
        List<HistorialMedico> historiales = 
                historialMedicoService.obtenerHistorialMedicoEntrePacienteYMedico(idPaciente, idMedico);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Buscar historial médico por diagnóstico
     */
    @GetMapping("/buscar/diagnostico") // http://56.125.172.86:8080/api/historial-medico/buscar/diagnostico
    public ResponseEntity<List<HistorialMedico>> buscarHistorialMedicoPorDiagnostico(
            @RequestParam String diagnostico) {
        List<HistorialMedico> historiales = 
                historialMedicoService.buscarHistorialMedicoPorDiagnostico(diagnostico);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Buscar historial médico del paciente por diagnóstico
     */
    @GetMapping("/paciente/{idPaciente}/buscar/diagnostico") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}/buscar/diagnostico
    public ResponseEntity<List<HistorialMedico>> buscarHistorialMedicoPacientePorDiagnostico(
            @PathVariable Integer idPaciente,
            @RequestParam String diagnostico) {
        List<HistorialMedico> historiales = 
                historialMedicoService.buscarHistorialMedicoPacientePorDiagnostico(idPaciente, diagnostico);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Buscar historial médico por síntomas
     */
    @GetMapping("/buscar/sintomas") // http://56.125.172.86:8080/api/historial-medico/buscar/sintomas
    public ResponseEntity<List<HistorialMedico>> buscarHistorialMedicoPorSintomas(
            @RequestParam String sintoma) {
        List<HistorialMedico> historiales = historialMedicoService.buscarHistorialMedicoPorSintomas(sintoma);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Buscar historial médico por tratamiento
     */
    @GetMapping("/buscar/tratamiento") // http://56.125.172.86:8080/api/historial-medico/buscar/tratamiento
    public ResponseEntity<List<HistorialMedico>> buscarHistorialMedicoPorTratamiento(
            @RequestParam String tratamiento) {
        List<HistorialMedico> historiales = 
                historialMedicoService.buscarHistorialMedicoPorTratamiento(tratamiento);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Buscar historial médico por medicamento
     */
    @GetMapping("/buscar/medicamento") // http://56.125.172.86:8080/api/historial-medico/buscar/medicamento
    public ResponseEntity<List<HistorialMedico>> buscarHistorialMedicoPorMedicamento(
            @RequestParam String medicamento) {
        List<HistorialMedico> historiales = 
                historialMedicoService.buscarHistorialMedicoPorMedicamento(medicamento);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Buscar historial médico del paciente por medicamento
     */
    @GetMapping("/paciente/{idPaciente}/buscar/medicamento") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}/buscar/medicamento
    public ResponseEntity<List<HistorialMedico>> buscarHistorialMedicoPacientePorMedicamento(
            @PathVariable Integer idPaciente,
            @RequestParam String medicamento) {
        List<HistorialMedico> historiales = 
                historialMedicoService.buscarHistorialMedicoPacientePorMedicamento(idPaciente, medicamento);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico con seguimiento
     */
    @GetMapping("/con-seguimiento") // http://56.125.172.86:8080/api/historial-medico/con-seguimiento
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoConSeguimiento() {
        List<HistorialMedico> historiales = historialMedicoService.obtenerHistorialMedicoConSeguimiento();
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico con seguimiento por paciente
     */
    @GetMapping("/paciente/{idPaciente}/con-seguimiento") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}/con-seguimiento
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoConSeguimientoPorPaciente(
            @PathVariable Integer idPaciente) {
        List<HistorialMedico> historiales = 
                historialMedicoService.obtenerHistorialMedicoConSeguimientoPorPaciente(idPaciente);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico con seguimiento pendiente
     */
    @GetMapping("/seguimiento-pendiente") // http://56.125.172.86:8080/api/historial-medico/seguimiento-pendiente
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoConSeguimientoPendiente() {
        List<HistorialMedico> historiales = historialMedicoService.obtenerHistorialMedicoConSeguimientoPendiente();
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico con seguimiento pendiente por paciente
     */
    @GetMapping("/paciente/{idPaciente}/seguimiento-pendiente") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}/seguimiento-pendiente
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoConSeguimientoPendientePorPaciente(
            @PathVariable Integer idPaciente) {
        List<HistorialMedico> historiales = 
                historialMedicoService.obtenerHistorialMedicoConSeguimientoPendientePorPaciente(idPaciente);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico con seguimiento vencido
     */
    @GetMapping("/seguimiento-vencido") // http://56.125.172.86:8080/api/historial-medico/seguimiento-vencido
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoConSeguimientoVencido() {
        List<HistorialMedico> historiales = historialMedicoService.obtenerHistorialMedicoConSeguimientoVencido();
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico por paciente en rango de fechas
     */
    @GetMapping("/paciente/{idPaciente}/rango-fechas") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}/rango-fechas
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoPorPacienteEnRangoFechas(
            @PathVariable Integer idPaciente,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<HistorialMedico> historiales = 
                historialMedicoService.obtenerHistorialMedicoPorPacienteEnRangoFechas(idPaciente, fechaInicio, fechaFin);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico por médico en rango de fechas
     */
    @GetMapping("/medico/{idMedico}/rango-fechas") // http://56.125.172.86:8080/api/historial-medico/medico/{idMedico}/rango-fechas
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoPorMedicoEnRangoFechas(
            @PathVariable Integer idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<HistorialMedico> historiales = 
                historialMedicoService.obtenerHistorialMedicoPorMedicoEnRangoFechas(idMedico, fechaInicio, fechaFin);
        return ResponseEntity.ok(historiales);
    }

    /**
     * Contar historial médico por paciente
     */
    @GetMapping("/paciente/{idPaciente}/contar") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}/contar
    public ResponseEntity<Long> contarHistorialMedicoPorPaciente(@PathVariable Integer idPaciente) {
        Long count = historialMedicoService.contarHistorialMedicoPorPaciente(idPaciente);
        return ResponseEntity.ok(count);
    }

    /**
     * Contar historial médico por médico
     */
    @GetMapping("/medico/{idMedico}/contar") // http://56.125.172.86:8080/api/historial-medico/medico/{idMedico}/contar
    public ResponseEntity<Long> contarHistorialMedicoPorMedico(@PathVariable Integer idMedico) {
        Long count = historialMedicoService.contarHistorialMedicoPorMedico(idMedico);
        return ResponseEntity.ok(count);
    }

    /**
     * Obtener último historial médico por paciente
     */
    @GetMapping("/paciente/{idPaciente}/ultimo") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}/ultimo
    public ResponseEntity<HistorialMedico> obtenerUltimoHistorialMedicoPorPaciente(
            @PathVariable Integer idPaciente) {
        Optional<HistorialMedico> historial = 
                historialMedicoService.obtenerUltimoHistorialMedicoPorPaciente(idPaciente);
        return historial.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtener historial médico con observaciones
     */
    @GetMapping("/con-observaciones") // http://56.125.172.86:8080/api/historial-medico/con-observaciones
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoConObservaciones() {
        List<HistorialMedico> historiales = historialMedicoService.obtenerHistorialMedicoConObservaciones();
        return ResponseEntity.ok(historiales);
    }

    /**
     * Obtener historial médico reciente por paciente
     */
    @GetMapping("/paciente/{idPaciente}/reciente") // http://56.125.172.86:8080/api/historial-medico/paciente/{idPaciente}/reciente
    public ResponseEntity<List<HistorialMedico>> obtenerHistorialMedicoRecientePorPaciente(
            @PathVariable Integer idPaciente,
            @RequestParam(defaultValue = "10") int limite) {
        List<HistorialMedico> historiales = 
                historialMedicoService.obtenerHistorialMedicoRecientePorPaciente(idPaciente, limite);
        return ResponseEntity.ok(historiales);
    }
}
