package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@DisplayName("Tests para CitaRepository")
class CitaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CitaRepository citaRepository;

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
        especialidad.setNombreEspecialidad("Cardiología");
        especialidad.setDescripcion("Especialidad en enfermedades del corazón");
        especialidad = entityManager.persist(especialidad);

        // Configurar usuario médico
        usuarioMedico = Usuario.builder()
                .email("medico@test.com")
                .contraseña("password123")
                .nombre("Juan")
                .apellido("Pérez")
                .tipoUsuario(Usuario.TipoUsuario.MEDICO)
                .estado(Usuario.Estado.ACTIVO)
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuarioMedico = entityManager.persist(usuarioMedico);

        // Configurar médico
        medico = new Medico();
        medico.setUsuario(usuarioMedico);
        medico.setNumeroLicencia("LIC123456");
        medico.setEspecialidad(especialidad);
        medico.setEstadoVerificacion(Medico.EstadoVerificacion.VERIFICADO);
        medico.setFechaVerificacion(LocalDate.now());
        medico = entityManager.persist(medico);

        // Configurar usuario paciente
        usuarioPaciente = Usuario.builder()
                .email("paciente@test.com")
                .contraseña("password456")
                .nombre("María")
                .apellido("García")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .estado(Usuario.Estado.ACTIVO)
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuarioPaciente = entityManager.persist(usuarioPaciente);

        // Configurar paciente
        paciente = new Paciente();
        paciente.setUsuario(usuarioPaciente);
        paciente.setNumeroIdentificacion("12345678");
        paciente.setTipoSangre("O+");
        paciente = entityManager.persist(paciente);

        // Configurar cita
        cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaCita(LocalDate.now().plusDays(2));
        cita.setHoraCita(LocalTime.of(10, 0));
        cita.setTipoCita(Cita.TipoCita.PRESENCIAL);
        cita.setMotivoConsulta("Consulta de control");
        cita.setEstado(Cita.Estado.PROGRAMADA);

        entityManager.flush();
    }

    @Test
    @DisplayName("Debe guardar una cita correctamente")
    void testGuardarCita() {
        // Act
        Cita citaGuardada = citaRepository.save(cita);
        entityManager.flush();

        // Assert
        assertNotNull(citaGuardada.getIdCita());
        assertEquals(cita.getMotivoConsulta(), citaGuardada.getMotivoConsulta());
        assertEquals(cita.getEstado(), citaGuardada.getEstado());
    }

    @Test
    @DisplayName("Debe encontrar citas por paciente")
    void testFindByPaciente() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findByPaciente(paciente);

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertEquals(1, citas.size());
        assertEquals(paciente.getIdPaciente(), citas.get(0).getPaciente().getIdPaciente());
    }

    @Test
    @DisplayName("Debe encontrar citas por ID de paciente")
    void testFindByPaciente_IdPaciente() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findByPaciente_IdPaciente(paciente.getIdPaciente());

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertEquals(1, citas.size());
    }

    @Test
    @DisplayName("Debe encontrar citas por médico")
    void testFindByMedico() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findByMedico(medico);

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertEquals(1, citas.size());
        assertEquals(medico.getIdMedico(), citas.get(0).getMedico().getIdMedico());
    }

    @Test
    @DisplayName("Debe encontrar citas por ID de médico")
    void testFindByMedico_IdMedico() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findByMedico_IdMedico(medico.getIdMedico());

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertEquals(1, citas.size());
    }

    @Test
    @DisplayName("Debe encontrar citas por estado")
    void testFindByEstado() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findByEstado(Cita.Estado.PROGRAMADA);

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertTrue(citas.stream().allMatch(c -> c.getEstado() == Cita.Estado.PROGRAMADA));
    }

    @Test
    @DisplayName("Debe encontrar citas por fecha")
    void testFindByFechaCita() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findByFechaCita(cita.getFechaCita());

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertEquals(1, citas.size());
        assertEquals(cita.getFechaCita(), citas.get(0).getFechaCita());
    }

    @Test
    @DisplayName("Debe encontrar citas por paciente y estado")
    void testFindByPaciente_IdPacienteAndEstado() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findByPaciente_IdPacienteAndEstado(
                paciente.getIdPaciente(), Cita.Estado.PROGRAMADA);

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertEquals(1, citas.size());
        assertEquals(Cita.Estado.PROGRAMADA, citas.get(0).getEstado());
    }

    @Test
    @DisplayName("Debe encontrar citas por médico y estado")
    void testFindByMedico_IdMedicoAndEstado() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findByMedico_IdMedicoAndEstado(
                medico.getIdMedico(), Cita.Estado.PROGRAMADA);

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertEquals(1, citas.size());
        assertEquals(Cita.Estado.PROGRAMADA, citas.get(0).getEstado());
    }

    @Test
    @DisplayName("Debe verificar disponibilidad de médico")
    void testFindCitaByMedicoFechaHora() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        Optional<Cita> citaEncontrada = citaRepository.findCitaByMedicoFechaHora(
                medico.getIdMedico(), cita.getFechaCita(), cita.getHoraCita());

        // Assert
        assertTrue(citaEncontrada.isPresent());
        assertEquals(cita.getMedico().getIdMedico(), citaEncontrada.get().getMedico().getIdMedico());
    }

    @Test
    @DisplayName("Debe retornar vacío cuando no hay conflicto de horario")
    void testFindCitaByMedicoFechaHora_NoConflicto() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        Optional<Cita> citaEncontrada = citaRepository.findCitaByMedicoFechaHora(
                medico.getIdMedico(), cita.getFechaCita(), LocalTime.of(14, 0));

        // Assert
        assertFalse(citaEncontrada.isPresent());
    }

    @Test
    @DisplayName("Debe encontrar citas programadas por paciente")
    void testFindCitasProgramadasByPaciente() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findCitasProgramadasByPaciente(
                paciente.getIdPaciente());

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertTrue(citas.stream().allMatch(c -> c.getEstado() == Cita.Estado.PROGRAMADA));
    }

    @Test
    @DisplayName("Debe encontrar citas programadas por médico")
    void testFindCitasProgramadasByMedico() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findCitasProgramadasByMedico(
                medico.getIdMedico());

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertTrue(citas.stream().allMatch(c -> c.getEstado() == Cita.Estado.PROGRAMADA));
    }

    @Test
    @DisplayName("Debe encontrar citas por médico y fecha")
    void testFindCitasByMedicoAndFecha() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findCitasByMedicoAndFecha(
                medico.getIdMedico(), cita.getFechaCita());

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertEquals(1, citas.size());
    }

    @Test
    @DisplayName("Debe encontrar citas por paciente y fecha")
    void testFindCitasByPacienteAndFecha() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        // Act
        List<Cita> citas = citaRepository.findCitasByPacienteAndFecha(
                paciente.getIdPaciente(), cita.getFechaCita());

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertEquals(1, citas.size());
    }

    @Test
    @DisplayName("Debe encontrar citas en rango de fechas")
    void testFindCitasEnRangoFechas() {
        // Arrange
        citaRepository.save(cita);
        entityManager.flush();

        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaFin = LocalDate.now().plusDays(7);

        // Act
        List<Cita> citas = citaRepository.findCitasEnRangoFechas(fechaInicio, fechaFin);

        // Assert
        assertNotNull(citas);
        assertFalse(citas.isEmpty());
        assertTrue(citas.stream().allMatch(c -> 
            !c.getFechaCita().isBefore(fechaInicio) && !c.getFechaCita().isAfter(fechaFin)));
    }
}
