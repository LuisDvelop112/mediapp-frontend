package com.mediapp.citasbackend.services;

import com.mediapp.citasbackend.entities.*;
import com.mediapp.citasbackend.exceptions.BusinessRuleException;
import com.mediapp.citasbackend.exceptions.InvalidDataException;
import com.mediapp.citasbackend.exceptions.ResourceNotFoundException;
import com.mediapp.citasbackend.repositories.CitaRepository;
import com.mediapp.citasbackend.services.implementation.CitaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests para CitaServiceImpl")
class CitaServiceImplTest {

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private CitaServiceImpl citaService;

    private Cita cita;
    private Medico medico;
    private Paciente paciente;
    private Usuario usuarioMedico;
    private Usuario usuarioPaciente;
    private Especialidad especialidad;

    @BeforeEach
    void setUp() {
        // Configurar especialidad
        especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1);
        especialidad.setNombreEspecialidad("Cardiología");

        // Configurar usuario médico
        usuarioMedico = new Usuario();
        usuarioMedico.setIdUsuario(1);
        usuarioMedico.setEmail("medico@test.com");
        usuarioMedico.setNombre("Juan");
        usuarioMedico.setApellido("Pérez");
        usuarioMedico.setTipoUsuario(Usuario.TipoUsuario.MEDICO);

        // Configurar médico
        medico = new Medico();
        medico.setIdMedico(1);
        medico.setUsuario(usuarioMedico);
        medico.setNumeroLicencia("LIC123456");
        medico.setEspecialidad(especialidad);
        medico.setEstadoVerificacion(Medico.EstadoVerificacion.VERIFICADO);

        // Configurar usuario paciente
        usuarioPaciente = new Usuario();
        usuarioPaciente.setIdUsuario(2);
        usuarioPaciente.setEmail("paciente@test.com");
        usuarioPaciente.setNombre("María");
        usuarioPaciente.setApellido("García");
        usuarioPaciente.setTipoUsuario(Usuario.TipoUsuario.PACIENTE);

        // Configurar paciente
        paciente = new Paciente();
        paciente.setIdPaciente(1);
        paciente.setUsuario(usuarioPaciente);
        paciente.setNumeroIdentificacion("12345678");
        paciente.setTipoSangre("O+");

        // Configurar cita
        cita = new Cita();
        cita.setIdCita(1);
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaCita(LocalDate.now().plusDays(2));
        cita.setHoraCita(LocalTime.of(10, 0));
        cita.setTipoCita(Cita.TipoCita.PRESENCIAL);
        cita.setMotivoConsulta("Consulta de control");
        cita.setEstado(Cita.Estado.PROGRAMADA);
    }

    @Test
    @DisplayName("Debe guardar una cita exitosamente")
    void testGuardarCita_Exitoso() {
        // Arrange
        when(citaRepository.findCitaByMedicoFechaHora(
                anyInt(), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(Optional.empty());
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        // Act
        Cita citaGuardada = citaService.guardarCita(cita);

        // Assert
        assertNotNull(citaGuardada);
        assertEquals(cita.getIdCita(), citaGuardada.getIdCita());
        assertEquals(cita.getMotivoConsulta(), citaGuardada.getMotivoConsulta());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar cita con médico ocupado")
    void testGuardarCita_MedicoOcupado() {
        // Arrange
        Cita citaExistente = new Cita();
        citaExistente.setIdCita(2);
        citaExistente.setMedico(medico);
        citaExistente.setFechaCita(cita.getFechaCita());
        citaExistente.setHoraCita(cita.getHoraCita());
        citaExistente.setEstado(Cita.Estado.PROGRAMADA);

        when(citaRepository.findCitaByMedicoFechaHora(
                anyInt(), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(Optional.of(citaExistente));

        // Act & Assert
        assertThrows(BusinessRuleException.class, () -> citaService.guardarCita(cita));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar cita con datos nulos")
    void testGuardarCita_DatosNulos() {
        // Arrange
        Cita citaNula = new Cita();

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> citaService.guardarCita(citaNula));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al guardar cita con fecha pasada")
    void testGuardarCita_FechaPasada() {
        // Arrange
        cita.setFechaCita(LocalDate.now().minusDays(1));

        // Act & Assert
        assertThrows(InvalidDataException.class, () -> citaService.guardarCita(cita));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    @DisplayName("Debe actualizar una cita exitosamente")
    void testActualizarCita_Exitoso() {
        // Arrange
        Cita citaActualizada = new Cita();
        citaActualizada.setPaciente(paciente);
        citaActualizada.setMedico(medico);
        citaActualizada.setFechaCita(LocalDate.now().plusDays(3));
        citaActualizada.setHoraCita(LocalTime.of(11, 0));
        citaActualizada.setTipoCita(Cita.TipoCita.PRESENCIAL);
        citaActualizada.setMotivoConsulta("Consulta actualizada");
        citaActualizada.setEstado(Cita.Estado.PROGRAMADA);

        when(citaRepository.findById(anyInt())).thenReturn(Optional.of(cita));
        when(citaRepository.findCitaByMedicoFechaHora(
                anyInt(), any(LocalDate.class), any(LocalTime.class)))
                .thenReturn(Optional.empty());
        when(citaRepository.save(any(Cita.class))).thenReturn(citaActualizada);

        // Act
        Cita resultado = citaService.actualizarCita(1, citaActualizada);

        // Assert
        assertNotNull(resultado);
        verify(citaRepository, times(1)).findById(1);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar cita inexistente")
    void testActualizarCita_NoExiste() {
        // Arrange
        when(citaRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> citaService.actualizarCita(999, cita));
        verify(citaRepository, never()).save(any(Cita.class));
    }

    @Test
    @DisplayName("Debe eliminar una cita exitosamente")
    void testEliminarCita_Exitoso() {
        // Arrange
        when(citaRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(citaRepository).deleteById(anyInt());

        // Act
        citaService.eliminarCita(1);

        // Assert
        verify(citaRepository, times(1)).existsById(1);
        verify(citaRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar cita inexistente")
    void testEliminarCita_NoExiste() {
        // Arrange
        when(citaRepository.existsById(anyInt())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, 
                () -> citaService.eliminarCita(999));
        verify(citaRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Debe obtener una cita por ID")
    void testObtenerCitaPorId() {
        // Arrange
        when(citaRepository.findById(anyInt())).thenReturn(Optional.of(cita));

        // Act
        Optional<Cita> resultado = citaService.obtenerCitaPorId(1);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(cita.getIdCita(), resultado.get().getIdCita());
        verify(citaRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe obtener todas las citas")
    void testObtenerTodasLasCitas() {
        // Arrange
        List<Cita> citas = Arrays.asList(cita, new Cita());
        when(citaRepository.findAll()).thenReturn(citas);

        // Act
        List<Cita> resultado = citaService.obtenerTodasLasCitas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(citaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener citas por paciente ID")
    void testObtenerCitasPorPacienteId() {
        // Arrange
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByPaciente_IdPaciente(anyInt())).thenReturn(citas);

        // Act
        List<Cita> resultado = citaService.obtenerCitasPorPacienteId(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByPaciente_IdPaciente(1);
    }

    @Test
    @DisplayName("Debe obtener citas por médico ID")
    void testObtenerCitasPorMedicoId() {
        // Arrange
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByMedico_IdMedico(anyInt())).thenReturn(citas);

        // Act
        List<Cita> resultado = citaService.obtenerCitasPorMedicoId(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByMedico_IdMedico(1);
    }

    @Test
    @DisplayName("Debe obtener citas por estado")
    void testObtenerCitasPorEstado() {
        // Arrange
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByEstado(any(Cita.Estado.class))).thenReturn(citas);

        // Act
        List<Cita> resultado = citaService.obtenerCitasPorEstado(Cita.Estado.PROGRAMADA);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByEstado(Cita.Estado.PROGRAMADA);
    }

    @Test
    @DisplayName("Debe obtener citas por fecha")
    void testObtenerCitasPorFecha() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findByFechaCita(any(LocalDate.class))).thenReturn(citas);

        // Act
        List<Cita> resultado = citaService.obtenerCitasPorFecha(fecha);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1)).findByFechaCita(fecha);
    }

    @Test
    @DisplayName("Debe obtener citas programadas por paciente")
    void testObtenerCitasProgramadasPorPaciente() {
        // Arrange
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findCitasProgramadasByPaciente(anyInt()))
                .thenReturn(citas);

        // Act
        List<Cita> resultado = citaService.obtenerCitasProgramadasPorPaciente(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1))
                .findCitasProgramadasByPaciente(1);
    }

    @Test
    @DisplayName("Debe obtener citas programadas por médico")
    void testObtenerCitasProgramadasPorMedico() {
        // Arrange
        List<Cita> citas = Arrays.asList(cita);
        when(citaRepository.findCitasProgramadasByMedico(anyInt()))
                .thenReturn(citas);

        // Act
        List<Cita> resultado = citaService.obtenerCitasProgramadasPorMedico(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(citaRepository, times(1))
                .findCitasProgramadasByMedico(1);
    }
}
