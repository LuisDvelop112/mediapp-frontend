package com.mediapp.citasbackend.controllers;

import com.mediapp.citasbackend.entities.Especialidad;
import com.mediapp.citasbackend.services.interfaces.EspecialidadService;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidades", description = "Gestión de especialidades médicas - catálogo de áreas de práctica médica")
@SecurityRequirement(name = "bearerAuth")
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @PostMapping // http://56.125.172.86:8080/api/especialidades
    @Operation(summary = "Crear nueva especialidad", description = "Registra una nueva especialidad médica en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Especialidad creada exitosamente",
            content = @Content(schema = @Schema(implementation = Especialidad.class))),
        @ApiResponse(responseCode = "400", description = "Datos de especialidad inválidos")
    })
    public ResponseEntity<Especialidad> crearEspecialidad(
        @Parameter(description = "Datos de la especialidad a crear", required = true)
        @RequestBody Especialidad especialidad) {
        try {
            Especialidad nuevaEspecialidad = especialidadService.guardarEspecialidad(especialidad);
            return new ResponseEntity<>(nuevaEspecialidad, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping // http://56.125.172.86:8080/api/especialidades
    @Operation(summary = "Obtener todas las especialidades", description = "Retorna catálogo completo de especialidades médicas")
    @ApiResponse(responseCode = "200", description = "Lista de especialidades obtenida exitosamente")
    public ResponseEntity<List<Especialidad>> obtenerTodasLasEspecialidades() {
        List<Especialidad> especialidades = especialidadService.obtenerTodasLasEspecialidades();
        return ResponseEntity.ok(especialidades);
    }

    @GetMapping("/{id}") // http://56.125.172.86:8080/api/especialidades/{id}
    @Operation(summary = "Obtener especialidad por ID", description = "Retorna los detalles de una especialidad específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad encontrada"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    public ResponseEntity<Especialidad> obtenerEspecialidadPorId(
        @Parameter(description = "ID de la especialidad", required = true, example = "1")
        @PathVariable Integer id) {
        Optional<Especialidad> especialidad = especialidadService.obtenerEspecialidadPorId(id);
        return especialidad.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}") // http://56.125.172.86:8080/api/especialidades/{id}
    @Operation(summary = "Actualizar especialidad", description = "Actualiza la información de una especialidad existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    public ResponseEntity<Especialidad> actualizarEspecialidad(
            @Parameter(description = "ID de la especialidad", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados de la especialidad", required = true)
            @RequestBody Especialidad especialidad) {
        try {
            Especialidad especialidadActualizada = especialidadService.actualizarEspecialidad(id, especialidad);
            return ResponseEntity.ok(especialidadActualizada);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}") // http://56.125.172.86:8080/api/especialidades/{id}
    @Operation(summary = "Eliminar especialidad", description = "Elimina una especialidad del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Especialidad eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    public ResponseEntity<Void> eliminarEspecialidad(
        @Parameter(description = "ID de la especialidad", required = true, example = "1")
        @PathVariable Integer id) {
        try {
            especialidadService.eliminarEspecialidad(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtener especialidad por nombre exacto
     */
    @GetMapping("/nombre/{nombreEspecialidad}") // http://56.125.172.86:8080/api/especialidades/nombre/{nombreEspecialidad}
    public ResponseEntity<Especialidad> obtenerEspecialidadPorNombre(
            @PathVariable String nombreEspecialidad) {
        Optional<Especialidad> especialidad = especialidadService.obtenerEspecialidadPorNombre(nombreEspecialidad);
        return especialidad.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Verificar si existe una especialidad por nombre
     */
    @GetMapping("/existe/nombre/{nombreEspecialidad}") // http://56.125.172.86:8080/api/especialidades/existe/nombre/{nombreEspecialidad}
    public ResponseEntity<Boolean> existeEspecialidadPorNombre(@PathVariable String nombreEspecialidad) {
        boolean existe = especialidadService.existeEspecialidadPorNombre(nombreEspecialidad);
        return ResponseEntity.ok(existe);
    }

    /**
     * Obtener especialidades por estado
     */
    @GetMapping("/estado/{estado}") // http://56.125.172.86:8080/api/especialidades/estado/{estado}
    public ResponseEntity<List<Especialidad>> obtenerEspecialidadesPorEstado(
            @PathVariable Especialidad.Estado estado) {
        List<Especialidad> especialidades = especialidadService.obtenerEspecialidadesPorEstado(estado);
        return ResponseEntity.ok(especialidades);
    }

    /**
     * Obtener especialidades activas
     */
    @GetMapping("/activas") // http://56.125.172.86:8080/api/especialidades/activas
    public ResponseEntity<List<Especialidad>> obtenerEspecialidadesActivas() {
        List<Especialidad> especialidades = especialidadService.obtenerEspecialidadesActivas();
        return ResponseEntity.ok(especialidades);
    }

    /**
     * Buscar especialidades por nombre (búsqueda parcial)
     */
    @GetMapping("/buscar") // http://56.125.172.86:8080/api/especialidades/buscar
    public ResponseEntity<List<Especialidad>> buscarEspecialidadesPorNombre(
            @RequestParam String nombre) {
        List<Especialidad> especialidades = especialidadService.buscarEspecialidadesPorNombre(nombre);
        return ResponseEntity.ok(especialidades);
    }

    /**
     * Buscar especialidades activas por nombre (búsqueda parcial)
     */
    @GetMapping("/buscar/activas") // http://56.125.172.86:8080/api/especialidades/buscar/activas
    public ResponseEntity<List<Especialidad>> buscarEspecialidadesActivasPorNombre(
            @RequestParam String nombre) {
        List<Especialidad> especialidades = especialidadService.buscarEspecialidadesActivasPorNombre(nombre);
        return ResponseEntity.ok(especialidades);
    }

    /**
     * Buscar especialidades por descripción
     */
    @GetMapping("/buscar/descripcion") // http://56.125.172.86:8080/api/especialidades/buscar/descripcion
    public ResponseEntity<List<Especialidad>> buscarEspecialidadesPorDescripcion(
            @RequestParam String texto) {
        List<Especialidad> especialidades = especialidadService.buscarEspecialidadesPorDescripcion(texto);
        return ResponseEntity.ok(especialidades);
    }

    /**
     * Contar especialidades por estado
     */
    @GetMapping("/contar/estado/{estado}") // http://56.125.172.86:8080/api/especialidades/contar/estado/{estado}
    public ResponseEntity<Long> contarEspecialidadesPorEstado(@PathVariable Especialidad.Estado estado) {
        Long count = especialidadService.contarEspecialidadesPorEstado(estado);
        return ResponseEntity.ok(count);
    }

    /**
     * Contar especialidades activas
     */
    @GetMapping("/contar/activas") // http://56.125.172.86:8080/api/especialidades/contar/activas
    public ResponseEntity<Long> contarEspecialidadesActivas() {
        Long count = especialidadService.contarEspecialidadesActivas();
        return ResponseEntity.ok(count);
    }

    /**
     * Obtener especialidades ordenadas alfabéticamente
     */
    @GetMapping("/ordenadas") // http://56.125.172.86:8080/api/especialidades/ordenadas
    public ResponseEntity<List<Especialidad>> obtenerEspecialidadesOrdenadas() {
        List<Especialidad> especialidades = especialidadService.obtenerEspecialidadesOrdenadas();
        return ResponseEntity.ok(especialidades);
    }

    /**
     * Obtener especialidades que tienen médicos asociados
     */
    @GetMapping("/con-medicos") // http://56.125.172.86:8080/api/especialidades/con-medicos
    public ResponseEntity<List<Especialidad>> obtenerEspecialidadesConMedicos() {
        List<Especialidad> especialidades = especialidadService.obtenerEspecialidadesConMedicos();
        return ResponseEntity.ok(especialidades);
    }

    /**
     * Obtener especialidades recientes
     */
    @GetMapping("/recientes") // http://56.125.172.86:8080/api/especialidades/recientes
    public ResponseEntity<List<Especialidad>> obtenerEspecialidadesRecientes() {
        List<Especialidad> especialidades = especialidadService.obtenerEspecialidadesRecientes();
        return ResponseEntity.ok(especialidades);
    }

    /**
     * Activar especialidad
     */
    @PatchMapping("/{id}/activar") // http://56.125.172.86:8080/api/especialidades/{id}/activar
    public ResponseEntity<Especialidad> activarEspecialidad(@PathVariable Integer id) {
        try {
            Especialidad especialidad = especialidadService.activarEspecialidad(id);
            return ResponseEntity.ok(especialidad);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Desactivar especialidad
     */
    @PatchMapping("/{id}/desactivar") // http://56.125.172.86:8080/api/especialidades/{id}/desactivar
    public ResponseEntity<Especialidad> desactivarEspecialidad(@PathVariable Integer id) {
        try {
            Especialidad especialidad = especialidadService.desactivarEspecialidad(id);
            return ResponseEntity.ok(especialidad);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Verificar si el nombre es único (excluyendo un ID específico)
     */
    @GetMapping("/nombre-unico") // http://56.125.172.86:8080/api/especialidades/nombre-unico
    public ResponseEntity<Boolean> nombreEsUnico(
            @RequestParam String nombre,
            @RequestParam(required = false) Integer idExcluir) {
        boolean esUnico = especialidadService.nombreEsUnico(nombre, idExcluir);
        return ResponseEntity.ok(esUnico);
    }
}
