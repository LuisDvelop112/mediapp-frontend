package com.mediapp.citasbackend.controllers;

import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.services.interfaces.UsuarioService;
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
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema - administración de cuentas y perfiles")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping // http://56.125.172.86:8080/api/usuarios
    @Operation(summary = "Crear nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(schema = @Schema(implementation = Usuario.class))),
        @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos")
    })
    public ResponseEntity<Usuario> crearUsuario(
        @Parameter(description = "Datos del usuario a crear", required = true)
        @RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping // http://56.125.172.86:8080/api/usuarios
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna listado completo de usuarios del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    public ResponseEntity<List<Usuario>> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}") // http://56.125.172.86:8080/api/usuarios/{id}
    @Operation(summary = "Obtener usuario por ID", description = "Retorna los detalles de un usuario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> obtenerUsuarioPorId(
        @Parameter(description = "ID del usuario", required = true, example = "1")
        @PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}") // http://56.125.172.86:8080/api/usuarios/{id}
    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> actualizarUsuario(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos actualizados del usuario", required = true)
            @RequestBody Usuario usuario) {
        try {
            Usuario usuarioActualizado = usuarioService.actualizarUsuario(id, usuario);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}") // http://56.125.172.86:8080/api/usuarios/{id}
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> eliminarUsuario(
        @Parameter(description = "ID del usuario", required = true, example = "1")
        @PathVariable Integer id) {
        try {
            usuarioService.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/{email}") // http://56.125.172.86:8080/api/usuarios/email/{email}
    @Operation(summary = "Obtener usuario por email", description = "Busca un usuario por su dirección de correo electrónico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> obtenerUsuarioPorEmail(
        @Parameter(description = "Email del usuario", required = true, example = "usuario@ejemplo.com")
        @PathVariable String email) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorEmail(email);
        return usuario.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email/existe/{email}") // http://56.125.172.86:8080/api/usuarios/email/existe/{email}
    @Operation(summary = "Verificar existencia de email", description = "Verifica si un email ya está registrado en el sistema")
    @ApiResponse(responseCode = "200", description = "Resultado de la verificación")
    public ResponseEntity<Boolean> existeEmail(
        @Parameter(description = "Email a verificar", required = true, example = "usuario@ejemplo.com")
        @PathVariable String email) {
        boolean existe = usuarioService.existeEmail(email);
        return ResponseEntity.ok(existe);
    }

    /**
     * Obtener usuario por teléfono
     */
    @GetMapping("/telefono/{telefono}") // http://56.125.172.86:8080/api/usuarios/telefono/{telefono}
    public ResponseEntity<Usuario> obtenerUsuarioPorTelefono(@PathVariable String telefono) {
        Optional<Usuario> usuario = usuarioService.obtenerUsuarioPorTelefono(telefono);
        return usuario.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Verificar si existe un teléfono
     */
    @GetMapping("/telefono/existe/{telefono}") // http://56.125.172.86:8080/api/usuarios/telefono/existe/{telefono}
    public ResponseEntity<Boolean> existeTelefono(@PathVariable String telefono) {
        boolean existe = usuarioService.existeTelefono(telefono);
        return ResponseEntity.ok(existe);
    }

    /**
     * Obtener usuarios por tipo
     */
    @GetMapping("/tipo/{tipoUsuario}") // http://56.125.172.86:8080/api/usuarios/tipo/{tipoUsuario}
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorTipo(
            @PathVariable Usuario.TipoUsuario tipoUsuario) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorTipo(tipoUsuario);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtener usuarios por estado
     */
    @GetMapping("/estado/{estado}") // http://56.125.172.86:8080/api/usuarios/estado/{estado}
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorEstado(
            @PathVariable Usuario.Estado estado) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorEstado(estado);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtener usuarios por tipo y estado
     */
    @GetMapping("/tipo/{tipoUsuario}/estado/{estado}") // http://56.125.172.86:8080/api/usuarios/tipo/{tipoUsuario}/estado/{estado}
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorTipoYEstado(
            @PathVariable Usuario.TipoUsuario tipoUsuario,
            @PathVariable Usuario.Estado estado) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorTipoYEstado(tipoUsuario, estado);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtener usuarios activos por tipo
     */
    @GetMapping("/activos/tipo/{tipoUsuario}") // http://56.125.172.86:8080/api/usuarios/activos/tipo/{tipoUsuario}
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivosPorTipo(
            @PathVariable Usuario.TipoUsuario tipoUsuario) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosActivosPorTipo(tipoUsuario);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtener médicos activos
     */
    @GetMapping("/medicos/activos") // http://56.125.172.86:8080/api/usuarios/medicos/activos
    public ResponseEntity<List<Usuario>> obtenerMedicosActivos() {
        List<Usuario> medicos = usuarioService.obtenerMedicosActivos();
        return ResponseEntity.ok(medicos);
    }

    /**
     * Obtener pacientes activos
     */
    @GetMapping("/pacientes/activos") // http://56.125.172.86:8080/api/usuarios/pacientes/activos
    public ResponseEntity<List<Usuario>> obtenerPacientesActivos() {
        List<Usuario> pacientes = usuarioService.obtenerPacientesActivos();
        return ResponseEntity.ok(pacientes);
    }

    /**
     * Buscar usuarios por nombre o apellido
     */
    @GetMapping("/buscar") // http://56.125.172.86:8080/api/usuarios/buscar?busqueda={texto}
    public ResponseEntity<List<Usuario>> buscarUsuariosPorNombreOApellido(
            @RequestParam String busqueda) {
        List<Usuario> usuarios = usuarioService.buscarUsuariosPorNombreOApellido(busqueda);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtener usuarios por ciudad
     */
    @GetMapping("/ciudad/{ciudad}") // http://56.125.172.86:8080/api/usuarios/ciudad/{ciudad}
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorCiudad(@PathVariable String ciudad) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorCiudad(ciudad);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtener usuarios por país
     */
    @GetMapping("/pais/{pais}") // http://56.125.172.86:8080/api/usuarios/pais/{pais}
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorPais(@PathVariable String pais) {
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPorPais(pais);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Contar usuarios por tipo
     */
    @GetMapping("/contar/tipo/{tipoUsuario}") // http://56.125.172.86:8080/api/usuarios/contar/tipo/{tipoUsuario}
    public ResponseEntity<Long> contarUsuariosPorTipo(
            @PathVariable Usuario.TipoUsuario tipoUsuario) {
        Long count = usuarioService.contarUsuariosPorTipo(tipoUsuario);
        return ResponseEntity.ok(count);
    }

    /**
     * Contar usuarios activos
     */
    @GetMapping("/contar/activos") // http://56.125.172.86:8080/api/usuarios/contar/activos
    public ResponseEntity<Long> contarUsuariosActivos() {
        Long count = usuarioService.contarUsuariosActivos();
        return ResponseEntity.ok(count);
    }

    /**
     * Activar usuario
     */
    @PatchMapping("/{id}/activar") // http://56.125.172.86:8080/api/usuarios/{id}/activar
    public ResponseEntity<Usuario> activarUsuario(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioService.activarUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Desactivar usuario
     */
    @PatchMapping("/{id}/desactivar") // http://56.125.172.86:8080/api/usuarios/{id}/desactivar
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioService.desactivarUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Bloquear usuario
     */
    @PatchMapping("/{id}/bloquear") // http://56.125.172.86:8080/api/usuarios/{id}/bloquear
    public ResponseEntity<Usuario> bloquearUsuario(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioService.bloquearUsuario(id);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
