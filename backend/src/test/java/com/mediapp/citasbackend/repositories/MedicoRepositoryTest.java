package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Especialidad;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@DisplayName("Tests para MedicoRepository")
class MedicoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MedicoRepository medicoRepository;

    private Medico medico;
    private Usuario usuario;
    private Especialidad especialidad;

    @BeforeEach
    void setUp() {
        // Configurar especialidad
        especialidad = new Especialidad();
        especialidad.setNombreEspecialidad("Cardiología");
        especialidad.setDescripcion("Especialidad en enfermedades del corazón");
        especialidad = entityManager.persist(especialidad);

        // Configurar usuario
        usuario = Usuario.builder()
                .email("medico@test.com")
                .contraseña("password123")
                .nombre("Juan")
                .apellido("Pérez")
                .tipoUsuario(Usuario.TipoUsuario.MEDICO)
                .estado(Usuario.Estado.ACTIVO)
                .ciudad("Bogotá")
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuario = entityManager.persist(usuario);

        // Configurar médico
        medico = new Medico();
        medico.setUsuario(usuario);
        medico.setNumeroLicencia("LIC123456");
        medico.setEspecialidad(especialidad);
        medico.setHospitalAfiliado("Hospital Central");
        medico.setExperienciaAnos(10);
        medico.setResumenBio("Médico especialista");
        medico.setCalificacionPromedio(new BigDecimal("4.5"));
        medico.setEstadoVerificacion(Medico.EstadoVerificacion.VERIFICADO);
        medico.setFechaVerificacion(LocalDate.now());

        entityManager.flush();
    }

    @Test
    @DisplayName("Debe guardar un médico correctamente")
    void testGuardarMedico() {
        // Act
        Medico medicoGuardado = medicoRepository.save(medico);
        entityManager.flush();

        // Assert
        assertNotNull(medicoGuardado.getIdMedico());
        assertEquals(medico.getNumeroLicencia(), medicoGuardado.getNumeroLicencia());
        assertEquals(medico.getEspecialidad().getIdEspecialidad(), 
                medicoGuardado.getEspecialidad().getIdEspecialidad());
    }

    @Test
    @DisplayName("Debe encontrar médico por usuario")
    void testFindByUsuario() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        Optional<Medico> medicoEncontrado = medicoRepository.findByUsuario(usuario);

        // Assert
        assertTrue(medicoEncontrado.isPresent());
        assertEquals(usuario.getIdUsuario(), medicoEncontrado.get().getUsuario().getIdUsuario());
    }

    @Test
    @DisplayName("Debe encontrar médico por ID de usuario")
    void testFindByUsuario_IdUsuario() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        Optional<Medico> medicoEncontrado = medicoRepository.findByUsuario_IdUsuario(
                usuario.getIdUsuario());

        // Assert
        assertTrue(medicoEncontrado.isPresent());
        assertEquals(medico.getNumeroLicencia(), medicoEncontrado.get().getNumeroLicencia());
    }

    @Test
    @DisplayName("Debe verificar si existe médico asociado a usuario")
    void testExistsByUsuario_IdUsuario() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        boolean existe = medicoRepository.existsByUsuario_IdUsuario(usuario.getIdUsuario());

        // Assert
        assertTrue(existe);
    }

    @Test
    @DisplayName("Debe encontrar médico por número de licencia")
    void testFindByNumeroLicencia() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        Optional<Medico> medicoEncontrado = medicoRepository.findByNumeroLicencia("LIC123456");

        // Assert
        assertTrue(medicoEncontrado.isPresent());
        assertEquals(medico.getIdMedico(), medicoEncontrado.get().getIdMedico());
    }

    @Test
    @DisplayName("Debe verificar si existe número de licencia")
    void testExistsByNumeroLicencia() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        boolean existe = medicoRepository.existsByNumeroLicencia("LIC123456");

        // Assert
        assertTrue(existe);
    }

    @Test
    @DisplayName("Debe encontrar médicos por especialidad")
    void testFindByEspecialidad() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        List<Medico> medicos = medicoRepository.findByEspecialidad(especialidad);

        // Assert
        assertNotNull(medicos);
        assertFalse(medicos.isEmpty());
        assertEquals(1, medicos.size());
    }

    @Test
    @DisplayName("Debe encontrar médicos por ID de especialidad")
    void testFindByEspecialidad_IdEspecialidad() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        List<Medico> medicos = medicoRepository.findByEspecialidad_IdEspecialidad(
                especialidad.getIdEspecialidad());

        // Assert
        assertNotNull(medicos);
        assertFalse(medicos.isEmpty());
    }

    @Test
    @DisplayName("Debe encontrar médicos por estado de verificación")
    void testFindByEstadoVerificacion() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        List<Medico> medicos = medicoRepository.findByEstadoVerificacion(
                Medico.EstadoVerificacion.VERIFICADO);

        // Assert
        assertNotNull(medicos);
        assertFalse(medicos.isEmpty());
        assertTrue(medicos.stream().allMatch(m -> 
                m.getEstadoVerificacion() == Medico.EstadoVerificacion.VERIFICADO));
    }

    @Test
    @DisplayName("Debe encontrar médicos verificados")
    void testFindMedicosVerificados() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        List<Medico> medicos = medicoRepository.findMedicosVerificados();

        // Assert
        assertNotNull(medicos);
        assertFalse(medicos.isEmpty());
    }

    @Test
    @DisplayName("Debe encontrar médicos por hospital afiliado")
    void testFindByHospitalAfiliado() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        List<Medico> medicos = medicoRepository.findByHospitalAfiliado("Hospital Central");

        // Assert
        assertNotNull(medicos);
        assertFalse(medicos.isEmpty());
        assertEquals(1, medicos.size());
    }

    @Test
    @DisplayName("Debe buscar médicos por nombre")
    void testBuscarMedicosPorNombre() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        List<Medico> medicos = medicoRepository.buscarMedicosPorNombre("Juan");

        // Assert
        assertNotNull(medicos);
        assertFalse(medicos.isEmpty());
    }

    @Test
    @DisplayName("Debe encontrar médicos por ciudad")
    void testFindMedicosByCiudad() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        List<Medico> medicos = medicoRepository.findMedicosByCiudad("Bogotá");

        // Assert
        assertNotNull(medicos);
        assertFalse(medicos.isEmpty());
    }

    @Test
    @DisplayName("Debe contar médicos por especialidad")
    void testContarMedicosByEspecialidad() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        Long count = medicoRepository.contarMedicosByEspecialidad(
                especialidad.getIdEspecialidad());

        // Assert
        assertEquals(1L, count);
    }

    @Test
    @DisplayName("Debe contar médicos verificados")
    void testContarMedicosVerificados() {
        // Arrange
        medicoRepository.save(medico);
        entityManager.flush();

        // Act
        Long count = medicoRepository.contarMedicosVerificados();

        // Assert
        assertEquals(1L, count);
    }
}
