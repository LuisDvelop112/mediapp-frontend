package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Paciente;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@DisplayName("Tests para PacienteRepository")
class PacienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PacienteRepository pacienteRepository;

    private Paciente paciente;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Configurar usuario
        usuario = Usuario.builder()
                .email("paciente@test.com")
                .contraseña("password123")
                .nombre("María")
                .apellido("García")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .estado(Usuario.Estado.ACTIVO)
                .ciudad("Bogotá")
                .fechaRegistro(LocalDateTime.now())
                .build();
        usuario = entityManager.persist(usuario);

        // Configurar paciente
        paciente = new Paciente();
        paciente.setUsuario(usuario);
        paciente.setNumeroIdentificacion("12345678");
        paciente.setTipoSangre("O+");
        paciente.setAlergias("Ninguna");
        paciente.setEnfermedadesCronicas("Ninguna");
        paciente.setMedicamentosActuales("Ninguno");
        paciente.setContactoEmergencia("Juan García");
        paciente.setTelefonoEmergencia("+57 300 1234567");

        entityManager.flush();
    }

    @Test
    @DisplayName("Debe guardar un paciente correctamente")
    void testGuardarPaciente() {
        // Act
        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        entityManager.flush();

        // Assert
        assertNotNull(pacienteGuardado.getIdPaciente());
        assertEquals(paciente.getNumeroIdentificacion(), 
                pacienteGuardado.getNumeroIdentificacion());
        assertEquals(paciente.getTipoSangre(), pacienteGuardado.getTipoSangre());
    }

    @Test
    @DisplayName("Debe encontrar paciente por usuario")
    void testFindByUsuario() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findByUsuario(usuario);

        // Assert
        assertTrue(pacienteEncontrado.isPresent());
        assertEquals(usuario.getIdUsuario(), 
                pacienteEncontrado.get().getUsuario().getIdUsuario());
    }

    @Test
    @DisplayName("Debe encontrar paciente por ID de usuario")
    void testFindByUsuario_IdUsuario() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findByUsuario_IdUsuario(
                usuario.getIdUsuario());

        // Assert
        assertTrue(pacienteEncontrado.isPresent());
        assertEquals(paciente.getNumeroIdentificacion(), 
                pacienteEncontrado.get().getNumeroIdentificacion());
    }

    @Test
    @DisplayName("Debe verificar si existe paciente asociado a usuario")
    void testExistsByUsuario_IdUsuario() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        boolean existe = pacienteRepository.existsByUsuario_IdUsuario(usuario.getIdUsuario());

        // Assert
        assertTrue(existe);
    }

    @Test
    @DisplayName("Debe encontrar paciente por número de identificación")
    void testFindByNumeroIdentificacion() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findByNumeroIdentificacion("12345678");

        // Assert
        assertTrue(pacienteEncontrado.isPresent());
        assertEquals(paciente.getIdPaciente(), pacienteEncontrado.get().getIdPaciente());
    }

    @Test
    @DisplayName("Debe verificar si existe número de identificación")
    void testExistsByNumeroIdentificacion() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        boolean existe = pacienteRepository.existsByNumeroIdentificacion("12345678");

        // Assert
        assertTrue(existe);
    }

    @Test
    @DisplayName("Debe buscar pacientes por nombre")
    void testBuscarPacientesPorNombre() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        List<Paciente> pacientes = pacienteRepository.buscarPacientesPorNombre("María");

        // Assert
        assertNotNull(pacientes);
        assertFalse(pacientes.isEmpty());
    }

    @Test
    @DisplayName("Debe buscar pacientes por apellido")
    void testBuscarPacientesPorApellido() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        List<Paciente> pacientes = pacienteRepository.buscarPacientesPorNombre("García");

        // Assert
        assertNotNull(pacientes);
        assertFalse(pacientes.isEmpty());
    }

    @Test
    @DisplayName("Debe encontrar pacientes por tipo de sangre")
    void testFindByTipoSangre() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        List<Paciente> pacientes = pacienteRepository.findByTipoSangre("O+");

        // Assert
        assertNotNull(pacientes);
        assertFalse(pacientes.isEmpty());
        assertEquals(1, pacientes.size());
        assertEquals("O+", pacientes.get(0).getTipoSangre());
    }

    @Test
    @DisplayName("Debe encontrar pacientes por ciudad")
    void testFindPacientesByCiudad() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        List<Paciente> pacientes = pacienteRepository.findPacientesByCiudad("Bogotá");

        // Assert
        assertNotNull(pacientes);
        assertFalse(pacientes.isEmpty());
    }

    @Test
    @DisplayName("Debe contar pacientes activos")
    void testContarPacientesActivos() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        Long count = pacienteRepository.contarPacientesActivos();

        // Assert
        assertEquals(1L, count);
    }

    @Test
    @DisplayName("No debe encontrar paciente con email inexistente")
    void testFindByEmail_NoExiste() {
        // Arrange
        pacienteRepository.save(paciente);
        entityManager.flush();

        // Act
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findByEmail("noexiste@test.com");

        // Assert
        assertFalse(pacienteEncontrado.isPresent());
    }
}
