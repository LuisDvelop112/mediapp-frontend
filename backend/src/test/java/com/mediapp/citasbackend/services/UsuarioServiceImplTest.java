package com.mediapp.citasbackend.services;

import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.exceptions.InvalidDataException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.exceptions.ResourceNotFoundException;
import com.mediapp.citasbackend.repositories.UsuarioRepository;
import com.mediapp.citasbackend.services.implementation.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService Tests")
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .idUsuario(1)
                .nombre("Juan")
                .apellido("Pérez")
                .email("juan.perez@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .genero(Usuario.Genero.MASCULINO)
                .telefono("3001234567")
                .direccion("Calle 123")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.ACTIVO)
                .build();
    }

    @Test
    @DisplayName("Guardar usuario exitosamente")
    void testGuardarUsuario_Success() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByTelefono(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        Usuario result = usuarioService.guardarUsuario(usuario);

        // Assert
        assertNotNull(result);
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals(usuario.getNombre(), result.getNombre());
        
        verify(usuarioRepository, times(1)).existsByEmail(anyString());
        verify(usuarioRepository, times(1)).existsByTelefono(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Guardar usuario falla cuando email ya existe")
    void testGuardarUsuario_EmailExists() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, 
                () -> usuarioService.guardarUsuario(usuario));
        
        verify(usuarioRepository, times(1)).existsByEmail(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Guardar usuario falla cuando teléfono ya existe")
    void testGuardarUsuario_TelefonoExists() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByTelefono(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, 
                () -> usuarioService.guardarUsuario(usuario));
        
        verify(usuarioRepository, times(1)).existsByEmail(anyString());
        verify(usuarioRepository, times(1)).existsByTelefono(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Guardar usuario falla con datos inválidos - nombre vacío")
    void testGuardarUsuario_InvalidData_EmptyNombre() {
        // Arrange
        usuario.setNombre("");

        // Act & Assert
        assertThrows(InvalidDataException.class, 
                () -> usuarioService.guardarUsuario(usuario));
        
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Guardar usuario falla con datos inválidos - email inválido")
    void testGuardarUsuario_InvalidData_InvalidEmail() {
        // Arrange
        usuario.setEmail("emailinvalido");

        // Act & Assert
        assertThrows(InvalidDataException.class, 
                () -> usuarioService.guardarUsuario(usuario));
        
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Actualizar usuario exitosamente")
    void testActualizarUsuario_Success() {
        // Arrange
        Usuario usuarioActualizado = Usuario.builder()
                .nombre("Juan Carlos")
                .apellido("Pérez García")
                .email("juan.perez@example.com")
                .contraseña("newPassword")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .genero(Usuario.Genero.MASCULINO)
                .telefono("3001234567")
                .direccion("Calle 456")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .estado(Usuario.Estado.ACTIVO)
                .build();

        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act
        Usuario result = usuarioService.actualizarUsuario(1, usuarioActualizado);

        // Assert
        assertNotNull(result);
        assertEquals("Juan Carlos", result.getNombre());
        assertEquals("Calle 456", result.getDireccion());
        
        verify(usuarioRepository, times(1)).findById(anyInt());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Actualizar usuario falla cuando no existe")
    void testActualizarUsuario_NotFound() {
        // Arrange
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> usuarioService.actualizarUsuario(1, usuario));
        
        verify(usuarioRepository, times(1)).findById(anyInt());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Buscar usuario por ID exitosamente")
    void testObtenerUsuarioPorId_Success() {
        // Arrange
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> result = usuarioService.obtenerUsuarioPorId(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(usuario.getIdUsuario(), result.get().getIdUsuario());
        assertEquals(usuario.getEmail(), result.get().getEmail());
        
        verify(usuarioRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Buscar usuario por ID no encontrado")
    void testObtenerUsuarioPorId_NotFound() {
        // Arrange
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> result = usuarioService.obtenerUsuarioPorId(1);

        // Assert
        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Buscar usuario por email exitosamente")
    void testObtenerUsuarioPorEmail_Success() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> result = usuarioService.obtenerUsuarioPorEmail("juan.perez@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(usuario.getEmail(), result.get().getEmail());
        
        verify(usuarioRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Buscar usuario por email no encontrado")
    void testObtenerUsuarioPorEmail_NotFound() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> result = usuarioService.obtenerUsuarioPorEmail("noexiste@example.com");

        // Assert
        assertFalse(result.isPresent());
        verify(usuarioRepository, times(1)).findByEmail(anyString());
    }

    @Test
    @DisplayName("Listar todos los usuarios")
    void testObtenerTodosLosUsuarios() {
        // Arrange
        Usuario usuario2 = Usuario.builder()
                .idUsuario(2)
                .nombre("María")
                .apellido("García")
                .email("maria.garcia@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1992, 5, 15))
                .genero(Usuario.Genero.FEMENINO)
                .telefono("3009876543")
                .direccion("Carrera 45")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.MEDICO)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.ACTIVO)
                .build();

        List<Usuario> usuarios = Arrays.asList(usuario, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> result = usuarioService.obtenerTodosLosUsuarios();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        assertEquals("María", result.get(1).getNombre());
        
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Eliminar usuario exitosamente")
    void testEliminarUsuario_Success() {
        // Arrange
        when(usuarioRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(anyInt());

        // Act
        assertDoesNotThrow(() -> usuarioService.eliminarUsuario(1));

        // Assert
        verify(usuarioRepository, times(1)).existsById(anyInt());
        verify(usuarioRepository, times(1)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Eliminar usuario falla cuando no existe")
    void testEliminarUsuario_NotFound() {
        // Arrange
        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> usuarioService.eliminarUsuario(1));
        
        verify(usuarioRepository, times(1)).existsById(anyInt());
        verify(usuarioRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Buscar usuarios por tipo")
    void testObtenerUsuariosPorTipo() {
        // Arrange
        List<Usuario> pacientes = Arrays.asList(usuario);
        when(usuarioRepository.findByTipoUsuario(any())).thenReturn(pacientes);

        // Act
        List<Usuario> result = usuarioService.obtenerUsuariosPorTipo(Usuario.TipoUsuario.PACIENTE);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Usuario.TipoUsuario.PACIENTE, result.get(0).getTipoUsuario());
        
        verify(usuarioRepository, times(1)).findByTipoUsuario(any());
    }

    @Test
    @DisplayName("Buscar usuarios por estado")
    void testObtenerUsuariosPorEstado() {
        // Arrange
        List<Usuario> usuariosActivos = Arrays.asList(usuario);
        when(usuarioRepository.findByEstado(any())).thenReturn(usuariosActivos);

        // Act
        List<Usuario> result = usuarioService.obtenerUsuariosPorEstado(Usuario.Estado.ACTIVO);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Usuario.Estado.ACTIVO, result.get(0).getEstado());
        
        verify(usuarioRepository, times(1)).findByEstado(any());
    }

    @Test
    @DisplayName("Verificar si existe email")
    void testExisteEmail() {
        // Arrange
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act
        boolean result = usuarioService.existeEmail("juan.perez@example.com");

        // Assert
        assertTrue(result);
        verify(usuarioRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    @DisplayName("Buscar usuarios por nombre o apellido")
    void testBuscarUsuariosPorNombreOApellido() {
        // Arrange
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.buscarPorNombreOApellido(anyString())).thenReturn(usuarios);

        // Act
        List<Usuario> result = usuarioService.buscarUsuariosPorNombreOApellido("Juan");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(usuarioRepository, times(1)).buscarPorNombreOApellido(anyString());
    }

    @Test
    @DisplayName("Contar usuarios por tipo")
    void testContarUsuariosPorTipo() {
        // Arrange
        when(usuarioRepository.contarUsuariosPorTipo(any())).thenReturn(5L);

        // Act
        Long result = usuarioService.contarUsuariosPorTipo(Usuario.TipoUsuario.PACIENTE);

        // Assert
        assertNotNull(result);
        assertEquals(5L, result);
        verify(usuarioRepository, times(1)).contarUsuariosPorTipo(any());
    }
}
