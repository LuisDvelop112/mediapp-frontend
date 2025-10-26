package com.mediapp.citasbackend.services;

import com.mediapp.citasbackend.entities.Especialidad;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.exceptions.InvalidDataException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.exceptions.ResourceNotFoundException;
import com.mediapp.citasbackend.repositories.MedicoRepository;
import com.mediapp.citasbackend.services.implementation.MedicoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para MedicoServiceImpl")
class MedicoServiceImplTest {

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private MedicoServiceImpl medicoService;

    private Medico medico;
    private Usuario usuario;
    private Especialidad especialidad;

    @BeforeEach
    void setUp() {
        // Configurar especialidad
        especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1);
        especialidad.setNombreEspecialidad("Cardiología");

        // Configurar usuario
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setEmail("medico@test.com");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setTipoUsuario(Usuario.TipoUsuario.MEDICO);

        // Configurar médico
        medico = new Medico();
        medico.setIdMedico(1);
        medico.setUsuario(usuario);
        medico.setNumeroLicencia("LIC123456");
        medico.setEspecialidad(especialidad);
        medico.setHospitalAfiliado("Hospital Central");
        medico.setExperienciaAnos(10);
        medico.setResumenBio("Médico especialista en cardiología");
        medico.setCalificacionPromedio(new BigDecimal("4.5"));
        medico.setEstadoVerificacion(Medico.EstadoVerificacion.VERIFICADO);
        medico.setFechaVerificacion(LocalDate.now());
    }

    @Test
    @DisplayName("Debe guardar un médico exitosamente")
    void testGuardarMedico_Exitoso() {
        // Arrange
        when(medicoRepository.existsByUsuario_IdUsuario(anyInt())).thenReturn(false);
        when(medicoRepository.existsByNumeroLicencia(anyString())).thenReturn(false);
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        // Act
        Medico medicoGuardado = medicoService.guardarMedico(medico);

        // Assert
        assertNotNull(medicoGuardado);
        assertEquals(medico.getIdMedico(), medicoGuardado.getIdMedico());
        assertEquals(medico.getNumeroLicencia(), medicoGuardado.getNumeroLicencia());
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar médico con usuario ya asociado")
    void testGuardarMedico_UsuarioYaAsociado() {
        // Arrange
        when(medicoRepository.existsByUsuario_IdUsuario(anyInt())).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, 
                () -> medicoService.guardarMedico(medico));
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar médico con licencia duplicada")
    void testGuardarMedico_LicenciaDuplicada() {
        // Arrange
        when(medicoRepository.existsByUsuario_IdUsuario(anyInt())).thenReturn(false);
        when(medicoRepository.existsByNumeroLicencia(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, 
                () -> medicoService.guardarMedico(medico));
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar médico con datos nulos")
    void testGuardarMedico_DatosNulos() {
        // Arrange
        Medico medicoNulo = new Medico();

        // Act & Assert
        assertThrows(InvalidDataException.class, 
                () -> medicoService.guardarMedico(medicoNulo));
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    @DisplayName("Debe actualizar un médico exitosamente")
    void testActualizarMedico_Exitoso() {
        // Arrange
        Medico medicoActualizado = new Medico();
        medicoActualizado.setUsuario(usuario);
        medicoActualizado.setNumeroLicencia("LIC789012");
        medicoActualizado.setEspecialidad(especialidad);
        medicoActualizado.setHospitalAfiliado("Hospital Actualizado");
        medicoActualizado.setExperienciaAnos(15);
        medicoActualizado.setResumenBio("Bio actualizada");
        medicoActualizado.setCalificacionPromedio(new BigDecimal("4.8"));
        medicoActualizado.setEstadoVerificacion(Medico.EstadoVerificacion.VERIFICADO);

        when(medicoRepository.findById(anyInt())).thenReturn(Optional.of(medico));
        when(medicoRepository.save(any(Medico.class))).thenReturn(medicoActualizado);

        // Act
        Medico resultado = medicoService.actualizarMedico(1, medicoActualizado);

        // Assert
        assertNotNull(resultado);
        verify(medicoRepository, times(1)).findById(1);
        verify(medicoRepository, times(1)).save(any(Medico.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar médico inexistente")
    void testActualizarMedico_NoExiste() {
        // Arrange
        when(medicoRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> medicoService.actualizarMedico(999, medico));
        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    @DisplayName("Debe eliminar un médico exitosamente")
    void testEliminarMedico_Exitoso() {
        // Arrange
        when(medicoRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(medicoRepository).deleteById(anyInt());

        // Act
        medicoService.eliminarMedico(1);

        // Assert
        verify(medicoRepository, times(1)).existsById(1);
        verify(medicoRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar médico inexistente")
    void testEliminarMedico_NoExiste() {
        // Arrange
        when(medicoRepository.existsById(anyInt())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> medicoService.eliminarMedico(999));
        verify(medicoRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Debe obtener un médico por ID")
    void testObtenerMedicoPorId() {
        // Arrange
        when(medicoRepository.findById(anyInt())).thenReturn(Optional.of(medico));

        // Act
        Optional<Medico> resultado = medicoService.obtenerMedicoPorId(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(medico.getIdMedico(), resultado.get().getIdMedico());
        verify(medicoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe obtener todos los médicos")
    void testObtenerTodosLosMedicos() {
        // Arrange
        List<Medico> medicos = Arrays.asList(medico, new Medico());
        when(medicoRepository.findAll()).thenReturn(medicos);

        // Act
        List<Medico> resultado = medicoService.obtenerTodosLosMedicos();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(medicoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener médicos por especialidad")
    void testObtenerMedicosPorEspecialidad() {
        // Arrange
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoRepository.findByEspecialidad_IdEspecialidad(anyInt())).thenReturn(medicos);

        // Act
        List<Medico> resultado = medicoService.obtenerMedicosPorEspecialidadId(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(medicoRepository, times(1)).findByEspecialidad_IdEspecialidad(1);
    }

    @Test
    @DisplayName("Debe obtener médicos verificados")
    void testObtenerMedicosVerificados() {
        // Arrange
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoRepository.findMedicosVerificados())
                .thenReturn(medicos);

        // Act
        List<Medico> resultado = medicoService.obtenerMedicosVerificados();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(medicoRepository, times(1))
                .findMedicosVerificados();
    }

    @Test
    @DisplayName("Debe obtener médico por número de licencia")
    void testObtenerMedicoPorNumeroLicencia() {
        // Arrange
        when(medicoRepository.findByNumeroLicencia(anyString())).thenReturn(Optional.of(medico));

        // Act
        Optional<Medico> resultado = medicoService.obtenerMedicoPorNumeroLicencia("LIC123456");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(medico.getNumeroLicencia(), resultado.get().getNumeroLicencia());
        verify(medicoRepository, times(1)).findByNumeroLicencia("LIC123456");
    }

    @Test
    @DisplayName("Debe obtener médico por usuario ID")
    void testObtenerMedicoPorUsuarioId() {
        // Arrange
        when(medicoRepository.findByUsuario_IdUsuario(anyInt())).thenReturn(Optional.of(medico));

        // Act
        Optional<Medico> resultado = medicoService.obtenerMedicoPorUsuarioId(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(medico.getUsuario().getIdUsuario(), resultado.get().getUsuario().getIdUsuario());
        verify(medicoRepository, times(1)).findByUsuario_IdUsuario(1);
    }

    @Test
    @DisplayName("Debe buscar médicos por nombre")
    void testBuscarMedicosPorNombre() {
        // Arrange
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoRepository.buscarMedicosPorNombre(anyString())).thenReturn(medicos);

        // Act
        List<Medico> resultado = medicoService.buscarMedicosPorNombre("Juan");

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(medicoRepository, times(1)).buscarMedicosPorNombre("Juan");
    }
}
