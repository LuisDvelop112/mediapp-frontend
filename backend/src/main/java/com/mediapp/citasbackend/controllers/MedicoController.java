package com.mediapp.citasbackend.controllers;

import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.services.interfaces.MedicoService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
@Tag(name = "Médicos", description = "Gestión de médicos - registro, consulta y actualización de información profesional")
@SecurityRequirement(name = "bearerAuth")
public class MedicoController {

    private final MedicoService medicoService;

    @PostMapping // http://56.125.172.86:8080/api/medicos
    @Operation(summary = "Crear nuevo médico", description = "Registra un nuevo médico en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Médico creado exitosamente",
            content = @Content(schema = @Schema(implementation = Medico.class))),
        @ApiResponse(responseCode = "400", description = "Datos de médico inválidos")
    })
    public ResponseEntity<Medico> crearMedico(
        @Parameter(description = "Datos del médico a crear", required = true)
        @RequestBody Medico medico) {
        try {
            Medico nuevoMedico = medicoService.guardarMedico(medico);
            return new ResponseEntity<>(nuevoMedico, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping // http://56.125.172.86:8080/api/medicos
    @Operation(summary = "Obtener todos los médicos", description = "Retorna listado completo de médicos del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de médicos obtenida exitosamente")
    public ResponseEntity<List<Medico>> obtenerTodosLosMedicos() {
        List<Medico> medicos = medicoService.obtenerTodosLosMedicos();
        return ResponseEntity.ok(medicos);
    }

    @GetMapping("/{id}") // http://56.125.172.86:8080/api/medicos/{id}
    @Operation(summary = "Obtener médico por ID", description = "Retorna los detalles de un médico específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Médico encontrado"),
        @ApiResponse(responseCode = "404", description = "Médico no encontrado")
    })
    public ResponseEntity<Medico> obtenerMedicoPorId(
        @Parameter(description = "ID del médico", required = true, example = "1")
        @PathVariable Integer id) {
        Optional<Medico> medico = medicoService.obtenerMedicoPorId(id);
        return medico.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}") // http://56.125.172.86:8080/api/medicos/{id}
    @Operation(summary = "Actualizar médico", description = "Actualiza la información de un médico existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Médico actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Médico no encontrado")
    })
    public ResponseEntity<Medico> actualizarMedico(
            @Parameter(description = "ID del médico", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del médico", required = true)
            @RequestBody Medico medico) {
        try {
            Medico medicoActualizado = medicoService.actualizarMedico(id, medico);
            return ResponseEntity.ok(medicoActualizado);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}") // http://56.125.172.86:8080/api/medicos/{id}
    @Operation(summary = "Eliminar médico", description = "Elimina un médico del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Médico eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Médico no encontrado")
    })
    public ResponseEntity<Void> eliminarMedico(
        @Parameter(description = "ID del médico", required = true, example = "1")
        @PathVariable Integer id) {
        try {
            medicoService.eliminarMedico(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/usuario/{idUsuario}") // http://56.125.172.86:8080/api/medicos/usuario/{idUsuario}
    @Operation(summary = "Obtener médico por ID de usuario", 
               description = "Retorna el perfil de médico asociado a un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Médico encontrado"),
        @ApiResponse(responseCode = "404", description = "Médico no encontrado para este usuario")
    })
    public ResponseEntity<Medico> obtenerMedicoPorUsuario(
        @Parameter(description = "ID del usuario", required = true, example = "1")
        @PathVariable Integer idUsuario) {
        Optional<Medico> medico = medicoService.obtenerMedicoPorUsuarioId(idUsuario);
        return medico.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/usuario/{idUsuario}/existe") // http://56.125.172.86:8080/api/medicos/usuario/{idUsuario}/existe
    @Operation(summary = "Verificar si existe médico por usuario", 
               description = "Verifica si existe un perfil de médico asociado al usuario")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    public ResponseEntity<Boolean> existeMedicoPorUsuario(
        @Parameter(description = "ID del usuario", required = true, example = "1")
        @PathVariable Integer idUsuario) {
        boolean existe = medicoService.existeMedicoPorUsuarioId(idUsuario);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/licencia/{numeroLicencia}") // http://56.125.172.86:8080/api/medicos/licencia/{numeroLicencia}
    @Operation(summary = "Obtener médico por número de licencia", 
               description = "Busca un médico por su número de licencia profesional")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Médico encontrado"),
        @ApiResponse(responseCode = "404", description = "Médico no encontrado con esta licencia")
    })
    public ResponseEntity<Medico> obtenerMedicoPorNumeroLicencia(
        @Parameter(description = "Número de licencia profesional", required = true, example = "LIC123456")
        @PathVariable String numeroLicencia) {
        Optional<Medico> medico = medicoService.obtenerMedicoPorNumeroLicencia(numeroLicencia);
        return medico.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/licencia/{numeroLicencia}/existe") // http://56.125.172.86:8080/api/medicos/licencia/{numeroLicencia}/existe
    @Operation(summary = "Verificar existencia de licencia", 
               description = "Verifica si un número de licencia profesional ya está registrado")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    public ResponseEntity<Boolean> existeNumeroLicencia(
        @Parameter(description = "Número de licencia a verificar", required = true, example = "LIC123456")
        @PathVariable String numeroLicencia) {
        boolean existe = medicoService.existeNumeroLicencia(numeroLicencia);
        return ResponseEntity.ok(existe);
    }

    @GetMapping("/especialidad/{idEspecialidad}") // http://56.125.172.86:8080/api/medicos/especialidad/{idEspecialidad}
    @Operation(summary = "Obtener médicos por especialidad", 
               description = "Retorna todos los médicos de una especialidad específica")
    @ApiResponse(responseCode = "200", description = "Lista de médicos de la especialidad")
    public ResponseEntity<List<Medico>> obtenerMedicosPorEspecialidad(
        @Parameter(description = "ID de la especialidad", required = true, example = "1")
        @PathVariable Integer idEspecialidad) {
        List<Medico> medicos = medicoService.obtenerMedicosPorEspecialidadId(idEspecialidad);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos verificados por especialidad
     */
    @GetMapping("/especialidad/{idEspecialidad}/verificados") // http://56.125.172.86:8080/api/medicos/especialidad/{idEspecialidad}/verificados
    public ResponseEntity<List<Medico>> obtenerMedicosVerificadosPorEspecialidad(
            @PathVariable Integer idEspecialidad) {
        List<Medico> medicos = medicoService.obtenerMedicosVerificadosPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos por estado de verificación
     */
    @GetMapping("/estado-verificacion/{estadoVerificacion}") // http://56.125.172.86:8080/api/medicos/estado-verificacion/{estadoVerificacion}
    public ResponseEntity<List<Medico>> obtenerMedicosPorEstadoVerificacion(
            @PathVariable Medico.EstadoVerificacion estadoVerificacion) {
        List<Medico> medicos = medicoService.obtenerMedicosPorEstadoVerificacion(estadoVerificacion);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos verificados
     */
    @GetMapping("/verificados") // http://56.125.172.86:8080/api/medicos/verificados
    public ResponseEntity<List<Medico>> obtenerMedicosVerificados() {
        List<Medico> medicos = medicoService.obtenerMedicosVerificados();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos pendientes de verificación
     */
    @GetMapping("/pendientes-verificacion") // http://56.125.172.86:8080/api/medicos/pendientes-verificacion
    public ResponseEntity<List<Medico>> obtenerMedicosPendientesVerificacion() {
        List<Medico> medicos = medicoService.obtenerMedicosPendientesVerificacion();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos activos y verificados
     */
    @GetMapping("/activos-verificados") // http://56.125.172.86:8080/api/medicos/activos-verificados
    public ResponseEntity<List<Medico>> obtenerMedicosActivosVerificados() {
        List<Medico> medicos = medicoService.obtenerMedicosActivosVerificados();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos por hospital
     */
    @GetMapping("/hospital/{hospitalAfiliado}") // http://56.125.172.86:8080/api/medicos/hospital/{hospitalAfiliado}
    public ResponseEntity<List<Medico>> obtenerMedicosPorHospital(@PathVariable String hospitalAfiliado) {
        List<Medico> medicos = medicoService.obtenerMedicosPorHospital(hospitalAfiliado);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos con experiencia mínima
     */
    @GetMapping("/experiencia-minima/{aniosMinimos}") // http://56.125.172.86:8080/api/medicos/experiencia-minima/{aniosMinimos}
    public ResponseEntity<List<Medico>> obtenerMedicosConExperienciaMinima(@PathVariable Integer aniosMinimos) {
        List<Medico> medicos = medicoService.obtenerMedicosConExperienciaMinima(aniosMinimos);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos por rango de experiencia
     */
    @GetMapping("/experiencia-rango") // http://56.125.172.86:8080/api/medicos/experiencia-rango
    public ResponseEntity<List<Medico>> obtenerMedicosPorRangoExperiencia(
            @RequestParam Integer minAnos,
            @RequestParam Integer maxAnos) {
        List<Medico> medicos = medicoService.obtenerMedicosPorRangoExperiencia(minAnos, maxAnos);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos con calificación mínima
     */
    @GetMapping("/calificacion-minima/{calificacionMinima}") // http://56.125.172.86:8080/api/medicos/calificacion-minima/{calificacionMinima}
    public ResponseEntity<List<Medico>> obtenerMedicosConCalificacionMinima(
            @PathVariable BigDecimal calificacionMinima) {
        List<Medico> medicos = medicoService.obtenerMedicosConCalificacionMinima(calificacionMinima);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos mejor calificados
     */
    @GetMapping("/mejor-calificados") // http://56.125.172.86:8080/api/medicos/mejor-calificados
    public ResponseEntity<List<Medico>> obtenerMedicosMejorCalificados() {
        List<Medico> medicos = medicoService.obtenerMedicosMejorCalificados();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos mejor calificados por especialidad
     */
    @GetMapping("/especialidad/{idEspecialidad}/mejor-calificados") // http://56.125.172.86:8080/api/medicos/especialidad/{idEspecialidad}/mejor-calificados
    public ResponseEntity<List<Medico>> obtenerMedicosMejorCalificadosPorEspecialidad(
            @PathVariable Integer idEspecialidad) {
        List<Medico> medicos = medicoService.obtenerMedicosMejorCalificadosPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos sin calificación
     */
    @GetMapping("/sin-calificacion") // http://56.125.172.86:8080/api/medicos/sin-calificacion
    public ResponseEntity<List<Medico>> obtenerMedicosSinCalificacion() {
        List<Medico> medicos = medicoService.obtenerMedicosSinCalificacion();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Buscar médicos por nombre
     */
    @GetMapping("/buscar") // http://56.125.172.86:8080/api/medicos/buscar
    public ResponseEntity<List<Medico>> buscarMedicosPorNombre(@RequestParam String nombre) {
        List<Medico> medicos = medicoService.buscarMedicosPorNombre(nombre);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Buscar médicos activos por nombre
     */
    @GetMapping("/buscar/activos") // http://56.125.172.86:8080/api/medicos/buscar/activos
    public ResponseEntity<List<Medico>> buscarMedicosActivosPorNombre(@RequestParam String nombre) {
        List<Medico> medicos = medicoService.buscarMedicosActivosPorNombre(nombre);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos por ciudad
     */
    @GetMapping("/ciudad/{ciudad}") // http://56.125.172.86:8080/api/medicos/ciudad/{ciudad}
    public ResponseEntity<List<Medico>> obtenerMedicosPorCiudad(@PathVariable String ciudad) {
        List<Medico> medicos = medicoService.obtenerMedicosPorCiudad(ciudad);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos por ciudad y especialidad
     */
    @GetMapping("/ciudad/{ciudad}/especialidad/{idEspecialidad}") // http://56.125.172.86:8080/api/medicos/ciudad/{ciudad}/especialidad/{idEspecialidad}
    public ResponseEntity<List<Medico>> obtenerMedicosPorCiudadYEspecialidad(
            @PathVariable String ciudad,
            @PathVariable Integer idEspecialidad) {
        List<Medico> medicos = medicoService.obtenerMedicosPorCiudadYEspecialidad(ciudad, idEspecialidad);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Contar médicos por especialidad
     */
    @GetMapping("/especialidad/{idEspecialidad}/contar") // http://56.125.172.86:8080/api/medicos/especialidad/{idEspecialidad}/contar
    public ResponseEntity<Long> contarMedicosPorEspecialidad(@PathVariable Integer idEspecialidad) {
        Long count = medicoService.contarMedicosPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(count);
    }

    /**
     * Contar médicos verificados
     */
    @GetMapping("/contar/verificados") // http://56.125.172.86:8080/api/medicos/contar/verificados
    public ResponseEntity<Long> contarMedicosVerificados() {
        Long count = medicoService.contarMedicosVerificados();
        return ResponseEntity.ok(count);
    }

    /**
     * Contar médicos pendientes
     */
    @GetMapping("/contar/pendientes") // http://56.125.172.86:8080/api/medicos/contar/pendientes
    public ResponseEntity<Long> contarMedicosPendientes() {
        Long count = medicoService.contarMedicosPendientes();
        return ResponseEntity.ok(count);
    }

    /**
     * Obtener médicos recientes
     */
    @GetMapping("/recientes") // http://56.125.172.86:8080/api/medicos/recientes
    public ResponseEntity<List<Medico>> obtenerMedicosRecientes() {
        List<Medico> medicos = medicoService.obtenerMedicosRecientes();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médico por email
     */
    @GetMapping("/email/{email}") // http://56.125.172.86:8080/api/medicos/email/{email}
    public ResponseEntity<Medico> obtenerMedicoPorEmail(@PathVariable String email) {
        Optional<Medico> medico = medicoService.obtenerMedicoPorEmail(email);
        return medico.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Obtener médicos con biografía
     */
    @GetMapping("/con-bio") // http://56.125.172.86:8080/api/medicos/con-bio
    public ResponseEntity<List<Medico>> obtenerMedicosConBio() {
        List<Medico> medicos = medicoService.obtenerMedicosConBio();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos disponibles (con calendario)
     */
    @GetMapping("/disponibles") // http://56.125.172.86:8080/api/medicos/disponibles
    public ResponseEntity<List<Medico>> obtenerMedicosDisponibles() {
        List<Medico> medicos = medicoService.obtenerMedicosDisponibles();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener médicos disponibles por especialidad
     */
    @GetMapping("/disponibles/especialidad/{idEspecialidad}") // http://56.125.172.86:8080/api/medicos/disponibles/especialidad/{idEspecialidad}
    public ResponseEntity<List<Medico>> obtenerMedicosDisponiblesPorEspecialidad(
            @PathVariable Integer idEspecialidad) {
        List<Medico> medicos = medicoService.obtenerMedicosDisponiblesPorEspecialidad(idEspecialidad);
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener todos los médicos ordenados por calificación
     */
    @GetMapping("/ordenados/calificacion") // http://56.125.172.86:8080/api/medicos/ordenados/calificacion
    public ResponseEntity<List<Medico>> obtenerTodosLosMedicosOrdenadosPorCalificacion() {
        List<Medico> medicos = medicoService.obtenerTodosLosMedicosOrdenadosPorCalificacion();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Verificar médico
     */
    @PatchMapping("/{id}/verificar") // http://56.125.172.86:8080/api/medicos/{id}/verificar
    public ResponseEntity<Medico> verificarMedico(@PathVariable Integer id) {
        try {
            Medico medico = medicoService.verificarMedico(id);
            return ResponseEntity.ok(medico);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Marcar médico como pendiente
     */
    @PatchMapping("/{id}/pendiente") // http://56.125.172.86:8080/api/medicos/{id}/pendiente
    public ResponseEntity<Medico> marcarComoPendiente(@PathVariable Integer id) {
        try {
            Medico medico = medicoService.marcarComoPendiente(id);
            return ResponseEntity.ok(medico);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualizar calificación promedio del médico
     */
    @PatchMapping("/{id}/calificacion") // http://56.125.172.86:8080/api/medicos/{id}/calificacion
    public ResponseEntity<Medico> actualizarCalificacionPromedio(
            @PathVariable Integer id,
            @RequestBody Map<String, BigDecimal> request) {
        try {
            BigDecimal calificacion = request.get("calificacion");
            if (calificacion == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Medico medico = medicoService.actualizarCalificacionPromedio(id, calificacion);
            return ResponseEntity.ok(medico);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Verificar si la licencia es única (excluyendo un ID específico)
     */
    @GetMapping("/licencia-unica") // http://56.125.172.86:8080/api/medicos/licencia-unica
    public ResponseEntity<Boolean> licenciaEsUnica(
            @RequestParam String numeroLicencia,
            @RequestParam(required = false) Integer idExcluir) {
        boolean esUnica = medicoService.licenciaEsUnica(numeroLicencia, idExcluir);
        return ResponseEntity.ok(esUnica);
    }
}
