package com.mediapp.citasbackend.controllers;

import com.mediapp.citasbackend.entities.CalendarioDisponibilidad;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.services.interfaces.CalendarioDisponibilidadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendario-disponibilidad")
@RequiredArgsConstructor
@Tag(name = "Calendario de Disponibilidad", description = "Gestión de horarios y disponibilidad de médicos para citas")
@SecurityRequirement(name = "bearerAuth")
public class CalendarioDisponibilidadController {

    private final CalendarioDisponibilidadService calendarioService;

    @PostMapping // http://56.125.172.86:8080/api/calendario-disponibilidad
    @Operation(summary = "Crear nueva disponibilidad", 
               description = "Registra un nuevo horario de disponibilidad para un médico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Disponibilidad creada exitosamente",
            content = @Content(schema = @Schema(implementation = CalendarioDisponibilidad.class))),
        @ApiResponse(responseCode = "400", description = "Datos de disponibilidad inválidos")
    })
    public ResponseEntity<CalendarioDisponibilidad> crearDisponibilidad(
            @Parameter(description = "Datos de la disponibilidad a crear", required = true)
            @RequestBody CalendarioDisponibilidad disponibilidad) {
        try {
            CalendarioDisponibilidad nuevaDisponibilidad = calendarioService.guardarDisponibilidad(disponibilidad);
            return new ResponseEntity<>(nuevaDisponibilidad, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping // http://56.125.172.86:8080/api/calendario-disponibilidad
    @Operation(summary = "Obtener todas las disponibilidades", 
               description = "Retorna listado completo de horarios de disponibilidad")
    @ApiResponse(responseCode = "200", description = "Lista de disponibilidades obtenida exitosamente")
    public ResponseEntity<List<CalendarioDisponibilidad>> obtenerTodasLasDisponibilidades() {
        List<CalendarioDisponibilidad> disponibilidades = calendarioService.obtenerTodasLasDisponibilidades();
        return ResponseEntity.ok(disponibilidades);
    }

    @GetMapping("/{id}") // http://56.125.172.86:8080/api/calendario-disponibilidad/{id}
    @Operation(summary = "Obtener disponibilidad por ID", 
               description = "Retorna los detalles de un horario de disponibilidad específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disponibilidad encontrada"),
        @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    public ResponseEntity<CalendarioDisponibilidad> obtenerDisponibilidadPorId(
        @Parameter(description = "ID de la disponibilidad", required = true, example = "1")
        @PathVariable Integer id) {
        Optional<CalendarioDisponibilidad> disponibilidad = calendarioService.obtenerDisponibilidadPorId(id);
        return disponibilidad.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}") // http://56.125.172.86:8080/api/calendario-disponibilidad/{id}
    @Operation(summary = "Actualizar disponibilidad", 
               description = "Actualiza la información de un horario de disponibilidad existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disponibilidad actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Disponibilidad no encontrada")
    })
    public ResponseEntity<CalendarioDisponibilidad> actualizarDisponibilidad(
            @Parameter(description = "ID de la disponibilidad", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados de la disponibilidad", required = true)
            @RequestBody CalendarioDisponibilidad disponibilidad) {
        try {
            CalendarioDisponibilidad disponibilidadActualizada = 
                    calendarioService.actualizarDisponibilidad(id, disponibilidad);
            return ResponseEntity.ok(disponibilidadActualizada);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Eliminar una disponibilidad
     */
    @DeleteMapping("/{id}") // http://56.125.172.86:8080/api/calendario-disponibilidad/{id}
    public ResponseEntity<Void> eliminarDisponibilidad(@PathVariable Integer id) {
        try {
            calendarioService.eliminarDisponibilidad(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtener disponibilidades por médico
     */
    @GetMapping("/medico/{idMedico}") // http://56.125.172.86:8080/api/calendario-disponibilidad/medico/{idMedico}
    public ResponseEntity<List<CalendarioDisponibilidad>> obtenerDisponibilidadesPorMedico(
            @PathVariable Integer idMedico) {
        List<CalendarioDisponibilidad> disponibilidades = 
                calendarioService.obtenerDisponibilidadesPorMedicoId(idMedico);
        return ResponseEntity.ok(disponibilidades);
    }

    /**
     * Obtener disponibilidades activas por médico
     */
    @GetMapping("/medico/{idMedico}/activas") // http://56.125.172.86:8080/api/calendario-disponibilidad/medico/{idMedico}/activas
    public ResponseEntity<List<CalendarioDisponibilidad>> obtenerDisponibilidadesActivasPorMedico(
            @PathVariable Integer idMedico) {
        List<CalendarioDisponibilidad> disponibilidades = 
                calendarioService.obtenerDisponibilidadesActivasPorMedico(idMedico);
        return ResponseEntity.ok(disponibilidades);
    }

    /**
     * Obtener disponibilidades por médico y día
     */
    @GetMapping("/medico/{idMedico}/dia/{dia}") // http://56.125.172.86:8080/api/calendario-disponibilidad/medico/{idMedico}/dia/{dia}
    public ResponseEntity<List<CalendarioDisponibilidad>> obtenerDisponibilidadesPorMedicoYDia(
            @PathVariable Integer idMedico,
            @PathVariable CalendarioDisponibilidad.DiaSemana dia) {
        List<CalendarioDisponibilidad> disponibilidades = 
                calendarioService.obtenerDisponibilidadesPorMedicoYDia(idMedico, dia);
        return ResponseEntity.ok(disponibilidades);
    }

    /**
     * Obtener disponibilidad específica por médico y día
     */
    @GetMapping("/medico/{idMedico}/dia-especifico/{dia}") // http://56.125.172.86:8080/api/calendario-disponibilidad/medico/{idMedico}/dia-especifico/{dia}
    public ResponseEntity<CalendarioDisponibilidad> obtenerDisponibilidadPorMedicoYDia(
            @PathVariable Integer idMedico,
            @PathVariable CalendarioDisponibilidad.DiaSemana dia) {
        Optional<CalendarioDisponibilidad> disponibilidad = 
                calendarioService.obtenerDisponibilidadPorMedicoYDia(idMedico, dia);
        return disponibilidad.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtener disponibilidades por día de la semana
     */
    @GetMapping("/dia/{dia}") // http://56.125.172.86:8080/api/calendario-disponibilidad/dia/{dia}
    public ResponseEntity<List<CalendarioDisponibilidad>> obtenerDisponibilidadesPorDia(
            @PathVariable CalendarioDisponibilidad.DiaSemana dia) {
        List<CalendarioDisponibilidad> disponibilidades = 
                calendarioService.obtenerDisponibilidadesPorDia(dia);
        return ResponseEntity.ok(disponibilidades);
    }

    /**
     * Obtener disponibilidades por estado
     */
    @GetMapping("/estado/{estado}") // http://56.125.172.86:8080/api/calendario-disponibilidad/estado/{estado}
    public ResponseEntity<List<CalendarioDisponibilidad>> obtenerDisponibilidadesPorEstado(
            @PathVariable CalendarioDisponibilidad.Estado estado) {
        List<CalendarioDisponibilidad> disponibilidades = 
                calendarioService.obtenerDisponibilidadesPorEstado(estado);
        return ResponseEntity.ok(disponibilidades);
    }

    /**
     * Obtener disponibilidades por médico y estado
     */
    @GetMapping("/medico/{idMedico}/estado/{estado}") // http://56.125.172.86:8080/api/calendario-disponibilidad/medico/{idMedico}/estado/{estado}
    public ResponseEntity<List<CalendarioDisponibilidad>> obtenerDisponibilidadesPorMedicoYEstado(
            @PathVariable Integer idMedico,
            @PathVariable CalendarioDisponibilidad.Estado estado) {
        List<CalendarioDisponibilidad> disponibilidades = 
                calendarioService.obtenerDisponibilidadesPorMedicoYEstado(idMedico, estado);
        return ResponseEntity.ok(disponibilidades);
    }

    /**
     * Verificar conflictos de horario
     */
    @GetMapping("/verificar-conflictos") // http://56.125.172.86:8080/api/calendario-disponibilidad/verificar-conflictos?idMedico={idMedico}&dia={dia}&horaInicio={horaInicio}&horaFin={horaFin}
    public ResponseEntity<List<CalendarioDisponibilidad>> verificarConflictosDeHorario(
            @RequestParam Integer idMedico,
            @RequestParam CalendarioDisponibilidad.DiaSemana dia,
            @RequestParam String horaInicio,
            @RequestParam String horaFin) {
        try {
            LocalTime inicio = LocalTime.parse(horaInicio);
            LocalTime fin = LocalTime.parse(horaFin);
            List<CalendarioDisponibilidad> conflictos = 
                    calendarioService.verificarConflictosDeHorario(idMedico, dia, inicio, fin);
            return ResponseEntity.ok(conflictos);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Verificar si tiene conflicto de horario
     */
    @GetMapping("/tiene-conflicto") // http://56.125.172.86:8080/api/calendario-disponibilidad/tiene-conflicto?idMedico={idMedico}&dia={dia}&horaInicio={horaInicio}&horaFin={horaFin}
    public ResponseEntity<Boolean> tieneConflictoDeHorario(
            @RequestParam Integer idMedico,
            @RequestParam CalendarioDisponibilidad.DiaSemana dia,
            @RequestParam String horaInicio,
            @RequestParam String horaFin) {
        try {
            LocalTime inicio = LocalTime.parse(horaInicio);
            LocalTime fin = LocalTime.parse(horaFin);
            boolean tieneConflicto = 
                    calendarioService.tieneConflictoDeHorario(idMedico, dia, inicio, fin);
            return ResponseEntity.ok(tieneConflicto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Contar disponibilidades activas por médico
     */
    @GetMapping("/medico/{idMedico}/contar-activas") // http://56.125.172.86:8080/api/calendario-disponibilidad/medico/{idMedico}/contar-activas
    public ResponseEntity<Long> contarDisponibilidadesActivasPorMedico(@PathVariable Integer idMedico) {
        Long count = calendarioService.contarDisponibilidadesActivasPorMedico(idMedico);
        return ResponseEntity.ok(count);
    }

    /**
     * Obtener médicos disponibles en un día
     */
    @GetMapping("/medicos-disponibles/dia/{dia}") // http://56.125.172.86:8080/api/calendario-disponibilidad/medicos-disponibles/dia/{dia}
    public ResponseEntity<List<Medico>> obtenerMedicosDisponiblesEnDia(
            @PathVariable CalendarioDisponibilidad.DiaSemana dia) {
        List<Medico> medicos = calendarioService.obtenerMedicosDisponiblesEnDia(dia);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos disponibles en un día y hora específica
     */
    @GetMapping("/medicos-disponibles/dia/{dia}/hora") // http://56.125.172.86:8080/api/calendario-disponibilidad/medicos-disponibles/dia/{dia}/hora?hora={hora}
    public ResponseEntity<List<Medico>> obtenerMedicosDisponiblesEnDiaYHora(
            @PathVariable CalendarioDisponibilidad.DiaSemana dia,
            @RequestParam String hora) {
        try {
            LocalTime horaCita = LocalTime.parse(hora);
            List<Medico> medicos = 
                    calendarioService.obtenerMedicosDisponiblesEnDiaYHora(dia, horaCita);
            return ResponseEntity.ok(medicos);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Obtener disponibilidades ordenadas por médico
     */
    @GetMapping("/medico/{idMedico}/ordenadas") // http://56.125.172.86:8080/api/calendario-disponibilidad/medico/{idMedico}/ordenadas
    public ResponseEntity<List<CalendarioDisponibilidad>> obtenerDisponibilidadesOrdenadasPorMedico(
            @PathVariable Integer idMedico) {
        List<CalendarioDisponibilidad> disponibilidades = 
                calendarioService.obtenerDisponibilidadesOrdenadasPorMedico(idMedico);
        return ResponseEntity.ok(disponibilidades);
    }

    /**
     * Eliminar todas las disponibilidades de un médico
     */
    @DeleteMapping("/medico/{idMedico}") // http://56.125.172.86:8080/api/calendario-disponibilidad/medico/{idMedico}
    public ResponseEntity<Void> eliminarTodasLasDisponibilidadesPorMedico(@PathVariable Integer idMedico) {
        try {
            calendarioService.eliminarTodasLasDisponibilidadesPorMedico(idMedico);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Verificar si un médico tiene disponibilidad configurada
     */
    @GetMapping("/medico/{idMedico}/tiene-disponibilidad") // http://56.125.172.86:8080/api/calendario-disponibilidad/medico/{idMedico}/tiene-disponibilidad
    public ResponseEntity<Boolean> medicoTieneDisponibilidadConfigurada(@PathVariable Integer idMedico) {
        boolean tieneDisponibilidad = calendarioService.medicoTieneDisponibilidadConfigurada(idMedico);
        return ResponseEntity.ok(tieneDisponibilidad);
    }

    /**
     * Obtener disponibilidades por intervalo de cita
     */
    @GetMapping("/intervalo/{intervalo}") // http://56.125.172.86:8080/api/calendario-disponibilidad/intervalo/{intervalo}
    public ResponseEntity<List<CalendarioDisponibilidad>> obtenerDisponibilidadesPorIntervalo(
            @PathVariable Integer intervalo) {
        List<CalendarioDisponibilidad> disponibilidades = 
                calendarioService.obtenerDisponibilidadesPorIntervalo(intervalo);
        return ResponseEntity.ok(disponibilidades);
    }

    /**
     * Activar disponibilidad
     */
    @PatchMapping("/{id}/activar") // http://56.125.172.86:8080/api/calendario-disponibilidad/{id}/activar
    public ResponseEntity<CalendarioDisponibilidad> activarDisponibilidad(@PathVariable Integer id) {
        try {
            CalendarioDisponibilidad disponibilidad = calendarioService.activarDisponibilidad(id);
            return ResponseEntity.ok(disponibilidad);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Desactivar disponibilidad
     */
    @PatchMapping("/{id}/desactivar") // http://56.125.172.86:8080/api/calendario-disponibilidad/{id}/desactivar
    public ResponseEntity<CalendarioDisponibilidad> desactivarDisponibilidad(@PathVariable Integer id) {
        try {
            CalendarioDisponibilidad disponibilidad = calendarioService.desactivarDisponibilidad(id);
            return ResponseEntity.ok(disponibilidad);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
