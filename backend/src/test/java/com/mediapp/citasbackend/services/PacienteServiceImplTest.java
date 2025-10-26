package com.mediapp.citasbackend.services;

import com.mediapp.citasbackend.entities.Paciente;
import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.exceptions.InvalidDataException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.exceptions.ResourceNotFoundException;
import com.mediapp.citasbackend.repositories.PacienteRepository;
import com.mediapp.citasbackend.services.implementation.PacienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para PacienteServiceImpl")
class PacienteServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    private Paciente paciente;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Configurar usuario
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setEmail("paciente@test.com");
        usuario.setNombre("María");
        usuario.setApellido("García");
        usuario.setTipoUsuario(Usuario.TipoUsuario.PACIENTE);
        usuario.setEstado(Usuario.Estado.ACTIVO);

        // Configurar paciente
        paciente = new Paciente();
        paciente.setIdPaciente(1);
        paciente.setUsuario(usuario);
        paciente.setNumeroIdentificacion("12345678");
        paciente.setTipoSangre("O+");
        paciente.setAlergias("Ninguna");
        paciente.setEnfermedadesCronicas("Ninguna");
        paciente.setMedicamentosActuales("Ninguno");
        paciente.setContactoEmergencia("Juan García");
        paciente.setTelefonoEmergencia("+57 300 1234567");
    }

    @Test
    @DisplayName("Debe guardar un paciente exitosamente")
    void testGuardarPaciente_Exitoso() {
        // Arrange
        when(pacienteRepository.existsByUsuario_IdUsuario(anyInt())).thenReturn(false);
        when(pacienteRepository.existsByNumeroIdentificacion(anyString())).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        // Act
        Paciente pacienteGuardado = pacienteService.guardarPaciente(paciente);

        // Assert
        assertNotNull(pacienteGuardado);
        assertEquals(paciente.getIdPaciente(), pacienteGuardado.getIdPaciente());
        assertEquals(paciente.getNumeroIdentificacion(), pacienteGuardado.getNumeroIdentificacion());
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar paciente con usuario ya asociado")
    void testGuardarPaciente_UsuarioYaAsociado() {
        // Arrange
        when(pacienteRepository.existsByUsuario_IdUsuario(anyInt())).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, 
                () -> pacienteService.guardarPaciente(paciente));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar paciente con identificación duplicada")
    void testGuardarPaciente_IdentificacionDuplicada() {
        // Arrange
        when(pacienteRepository.existsByUsuario_IdUsuario(anyInt())).thenReturn(false);
        when(pacienteRepository.existsByNumeroIdentificacion(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, 
                () -> pacienteService.guardarPaciente(paciente));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar paciente con datos nulos")
    void testGuardarPaciente_DatosNulos() {
        // Arrange
        Paciente pacienteNulo = new Paciente();

        // Act & Assert
        assertThrows(InvalidDataException.class, 
                () -> pacienteService.guardarPaciente(pacienteNulo));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Debe actualizar un paciente exitosamente")
    void testActualizarPaciente_Exitoso() {
        // Arrange
        Paciente pacienteActualizado = new Paciente();
        pacienteActualizado.setUsuario(usuario);
        pacienteActualizado.setNumeroIdentificacion("87654321");
        pacienteActualizado.setTipoSangre("A+");
        pacienteActualizado.setAlergias("Penicilina");
        pacienteActualizado.setEnfermedadesCronicas("Diabetes");
        pacienteActualizado.setMedicamentosActuales("Metformina");
        pacienteActualizado.setContactoEmergencia("Pedro García");
        pacienteActualizado.setTelefonoEmergencia("+57 310 7654321");

        when(pacienteRepository.findById(anyInt())).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(pacienteActualizado);

        // Act
        Paciente resultado = pacienteService.actualizarPaciente(1, pacienteActualizado);

        // Assert
        assertNotNull(resultado);
        verify(pacienteRepository, times(1)).findById(1);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar paciente inexistente")
    void testActualizarPaciente_NoExiste() {
        // Arrange
        when(pacienteRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> pacienteService.actualizarPaciente(999, paciente));
        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Debe eliminar un paciente exitosamente")
    void testEliminarPaciente_Exitoso() {
        // Arrange
        when(pacienteRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(pacienteRepository).deleteById(anyInt());

        // Act
        pacienteService.eliminarPaciente(1);

        // Assert
        verify(pacienteRepository, times(1)).existsById(1);
        verify(pacienteRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar paciente inexistente")
    void testEliminarPaciente_NoExiste() {
        // Arrange
        when(pacienteRepository.existsById(anyInt())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> pacienteService.eliminarPaciente(999));
        verify(pacienteRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Debe obtener un paciente por ID")
    void testObtenerPacientePorId() {
        // Arrange
        when(pacienteRepository.findById(anyInt())).thenReturn(Optional.of(paciente));

        // Act
        Optional<Paciente> resultado = pacienteService.obtenerPacientePorId(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(paciente.getIdPaciente(), resultado.get().getIdPaciente());
        verify(pacienteRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe obtener todos los pacientes")
    void testObtenerTodosLosPacientes() {
        // Arrange
        List<Paciente> pacientes = Arrays.asList(paciente, new Paciente());
        when(pacienteRepository.findAll()).thenReturn(pacientes);

        // Act
        List<Paciente> resultado = pacienteService.obtenerTodosLosPacientes();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(pacienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener paciente por número de identificación")
    void testObtenerPacientePorNumeroIdentificacion() {
        // Arrange
        when(pacienteRepository.findByNumeroIdentificacion(anyString()))
                .thenReturn(Optional.of(paciente));

        // Act
        Optional<Paciente> resultado = pacienteService
                .obtenerPacientePorNumeroIdentificacion("12345678");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(paciente.getNumeroIdentificacion(), 
                resultado.get().getNumeroIdentificacion());
        verify(pacienteRepository, times(1)).findByNumeroIdentificacion("12345678");
    }

    @Test
    @DisplayName("Debe obtener paciente por usuario ID")
    void testObtenerPacientePorUsuarioId() {
        // Arrange
        when(pacienteRepository.findByUsuario_IdUsuario(anyInt()))
                .thenReturn(Optional.of(paciente));

        // Act
        Optional<Paciente> resultado = pacienteService.obtenerPacientePorUsuarioId(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(paciente.getUsuario().getIdUsuario(), 
                resultado.get().getUsuario().getIdUsuario());
        verify(pacienteRepository, times(1)).findByUsuario_IdUsuario(1);
    }

    @Test
    @DisplayName("Debe buscar pacientes por nombre")
    void testBuscarPacientesPorNombre() {
        // Arrange
        List<Paciente> pacientes = Arrays.asList(paciente);
        when(pacienteRepository.buscarPacientesPorNombre(anyString())).thenReturn(pacientes);

        // Act
        List<Paciente> resultado = pacienteService.buscarPacientesPorNombre("María");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pacienteRepository, times(1)).buscarPacientesPorNombre("María");
    }

    @Test
    @DisplayName("Debe obtener pacientes por tipo de sangre")
    void testObtenerPacientesPorTipoSangre() {
        // Arrange
        List<Paciente> pacientes = Arrays.asList(paciente);
        when(pacienteRepository.findByTipoSangre(anyString())).thenReturn(pacientes);

        // Act
        List<Paciente> resultado = pacienteService.obtenerPacientesPorTipoSangre("O+");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pacienteRepository, times(1)).findByTipoSangre("O+");
    }
}
