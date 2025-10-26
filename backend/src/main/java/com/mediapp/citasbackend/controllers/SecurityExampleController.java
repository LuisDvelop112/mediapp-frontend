package com.mediapp.citasbackend.controllers;

import com.mediapp.citasbackend.dtos.UsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * EJEMPLO de cómo usar autenticación y autorización en controladores
 * 
 * Este es un controlador de ejemplo que muestra diferentes casos de uso
 * para proteger endpoints con JWT y roles.
 */
@RestController
@RequestMapping("/api/ejemplo")
@RequiredArgsConstructor
@Tag(name = "Ejemplo de Seguridad", description = "Endpoints de ejemplo con diferentes niveles de seguridad")
@SecurityRequirement(name = "Bearer Authentication")
public class SecurityExampleController {

    // ===========================================
    // EJEMPLO 1: Endpoint público (sin autenticación)
    // ===========================================
    
    @GetMapping("/publico")
    @Operation(summary = "Endpoint público", description = "No requiere autenticación")
    public ResponseEntity<String> endpointPublico() {
        return ResponseEntity.ok("Este endpoint es público y no requiere autenticación");
    }

    // ===========================================
    // EJEMPLO 2: Endpoint que requiere autenticación
    // ===========================================
    
    @GetMapping("/protegido")
    @Operation(summary = "Endpoint protegido", description = "Requiere estar autenticado")
    public ResponseEntity<String> endpointProtegido(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok("Hola " + email + ", estás autenticado!");
    }

    // ===========================================
    // EJEMPLO 3: Endpoint solo para PACIENTES
    // ===========================================
    
    @GetMapping("/paciente")
    @PreAuthorize("hasRole('PACIENTE')")
    @Operation(summary = "Solo para pacientes", description = "Solo usuarios con rol PACIENTE pueden acceder")
    public ResponseEntity<String> endpointPaciente(Authentication authentication) {
        return ResponseEntity.ok("Hola paciente " + authentication.getName());
    }

    // ===========================================
    // EJEMPLO 4: Endpoint solo para MEDICOS
    // ===========================================
    
    @GetMapping("/medico")
    @PreAuthorize("hasRole('MEDICO')")
    @Operation(summary = "Solo para médicos", description = "Solo usuarios con rol MEDICO pueden acceder")
    public ResponseEntity<String> endpointMedico(Authentication authentication) {
        return ResponseEntity.ok("Hola doctor/a " + authentication.getName());
    }

    // ===========================================
    // EJEMPLO 5: Endpoint solo para ADMIN
    // ===========================================
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Solo para administradores", description = "Solo usuarios con rol ADMIN pueden acceder")
    public ResponseEntity<String> endpointAdmin(Authentication authentication) {
        return ResponseEntity.ok("Hola admin " + authentication.getName());
    }

    // ===========================================
    // EJEMPLO 6: Endpoint para MEDICO o ADMIN
    // ===========================================
    
    @GetMapping("/medico-o-admin")
    @PreAuthorize("hasAnyRole('MEDICO', 'ADMIN')")
    @Operation(summary = "Para médicos o admins", description = "Usuarios con rol MEDICO o ADMIN")
    public ResponseEntity<String> endpointMedicoOAdmin(Authentication authentication) {
        return ResponseEntity.ok("Acceso permitido para médico o admin: " + authentication.getName());
    }

    // ===========================================
    // EJEMPLO 7: Obtener datos del usuario autenticado
    // ===========================================
    
    @GetMapping("/mi-perfil")
    @Operation(summary = "Obtener perfil", description = "Obtiene el perfil del usuario autenticado")
    public ResponseEntity<UsuarioDTO> obtenerMiPerfil(Authentication authentication) {
        String email = authentication.getName();
        
        // Aquí buscarías el usuario en la base de datos por su email
        // Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(...);
        
        // Por ahora retornamos un ejemplo
        UsuarioDTO perfil = new UsuarioDTO();
        perfil.setEmail(email);
        // ... llenar otros datos
        
        return ResponseEntity.ok(perfil);
    }

    // ===========================================
    // EJEMPLO 8: Validar que el usuario solo acceda a sus propios datos
    // ===========================================
    
    @GetMapping("/mis-citas/{idUsuario}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'MEDICO')")
    @Operation(summary = "Mis citas", description = "Obtiene las citas del usuario autenticado")
    public ResponseEntity<String> obtenerMisCitas(
            @PathVariable Integer idUsuario,
            Authentication authentication
    ) {
        // Obtener el usuario autenticado de la BD
        // Usuario usuarioAutenticado = usuarioRepository.findByEmail(authentication.getName()).orElseThrow(...);
        
        // Validar que el usuario solo acceda a sus propias citas
        // if (!usuarioAutenticado.getIdUsuario().equals(idUsuario)) {
        //     throw new AccessDeniedException("No puedes acceder a las citas de otro usuario");
        // }
        
        return ResponseEntity.ok("Citas del usuario " + idUsuario + " solicitadas por " + authentication.getName());
    }

    // ===========================================
    // ===========================================
    // EJEMPLO 9: ADMIN puede acceder a datos de cualquier usuario
    // ===========================================
    
    @GetMapping("/usuario/{idUsuario}/citas")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PACIENTE') and #idUsuario == authentication.principal.idUsuario)")
    @Operation(summary = "Citas de un usuario", description = "Admin puede ver cualquier cita, paciente solo las suyas")
    public ResponseEntity<String> obtenerCitasUsuario(
            @PathVariable Integer idUsuario,
            Authentication authentication
    ) {
        // El admin puede ver cualquier cita
        // El paciente solo puede ver sus propias citas (validado en @PreAuthorize)
        return ResponseEntity.ok("Citas del usuario " + idUsuario + " solicitadas por " + authentication.getName());
    }    // ===========================================
    // EJEMPLO 10: Verificar roles manualmente en el código
    // ===========================================
    
    @GetMapping("/verificar-rol")
    @Operation(summary = "Verificar rol", description = "Verifica el rol del usuario en el código")
    public ResponseEntity<String> verificarRol(Authentication authentication) {
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        
        boolean esMedico = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MEDICO"));
        
        boolean esPaciente = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PACIENTE"));
        
        String mensaje = String.format(
            "Usuario: %s | Admin: %s | Médico: %s | Paciente: %s",
            authentication.getName(), esAdmin, esMedico, esPaciente
        );
        
        return ResponseEntity.ok(mensaje);
    }
}
