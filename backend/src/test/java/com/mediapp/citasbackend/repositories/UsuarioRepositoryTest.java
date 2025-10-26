package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("UsuarioRepository Tests")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
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
    @DisplayName("Guardar y buscar usuario por email")
    void testSaveAndFindByEmail() {
        // Arrange & Act
        Usuario savedUsuario = entityManager.persistAndFlush(usuario);

        // Assert
        Optional<Usuario> foundUsuario = usuarioRepository.findByEmail(usuario.getEmail());
        assertTrue(foundUsuario.isPresent());
        assertEquals(savedUsuario.getEmail(), foundUsuario.get().getEmail());
        assertEquals(savedUsuario.getNombre(), foundUsuario.get().getNombre());
    }

    @Test
    @DisplayName("Verificar existencia de email")
    void testExistsByEmail() {
        // Arrange
        entityManager.persistAndFlush(usuario);

        // Act
        boolean exists = usuarioRepository.existsByEmail(usuario.getEmail());
        boolean notExists = usuarioRepository.existsByEmail("noexiste@example.com");

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("Buscar usuarios por tipo")
    void testFindByTipoUsuario() {
        // Arrange
        entityManager.persistAndFlush(usuario);

        Usuario medico = Usuario.builder()
                .nombre("María")
                .apellido("García")
                .email("maria.garcia@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1985, 3, 15))
                .genero(Usuario.Genero.FEMENINO)
                .telefono("3009876543")
                .direccion("Carrera 45")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.MEDICO)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.ACTIVO)
                .build();
        entityManager.persistAndFlush(medico);

        // Act
        List<Usuario> pacientes = usuarioRepository.findByTipoUsuario(Usuario.TipoUsuario.PACIENTE);
        List<Usuario> medicos = usuarioRepository.findByTipoUsuario(Usuario.TipoUsuario.MEDICO);

        // Assert
        assertEquals(1, pacientes.size());
        assertEquals(1, medicos.size());
        assertEquals(Usuario.TipoUsuario.PACIENTE, pacientes.get(0).getTipoUsuario());
        assertEquals(Usuario.TipoUsuario.MEDICO, medicos.get(0).getTipoUsuario());
    }

    @Test
    @DisplayName("Buscar usuarios por estado")
    void testFindByEstado() {
        // Arrange
        entityManager.persistAndFlush(usuario);

        Usuario usuarioInactivo = Usuario.builder()
                .nombre("Carlos")
                .apellido("López")
                .email("carlos.lopez@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1988, 7, 20))
                .genero(Usuario.Genero.MASCULINO)
                .telefono("3005555555")
                .direccion("Avenida 10")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.INACTIVO)
                .build();
        entityManager.persistAndFlush(usuarioInactivo);

        // Act
        List<Usuario> activos = usuarioRepository.findByEstado(Usuario.Estado.ACTIVO);
        List<Usuario> inactivos = usuarioRepository.findByEstado(Usuario.Estado.INACTIVO);

        // Assert
        assertEquals(1, activos.size());
        assertEquals(1, inactivos.size());
        assertEquals(Usuario.Estado.ACTIVO, activos.get(0).getEstado());
        assertEquals(Usuario.Estado.INACTIVO, inactivos.get(0).getEstado());
    }

    @Test
    @DisplayName("Buscar usuarios por tipo y estado")
    void testFindByTipoUsuarioAndEstado() {
        // Arrange
        entityManager.persistAndFlush(usuario);

        Usuario pacienteInactivo = Usuario.builder()
                .nombre("Ana")
                .apellido("Martínez")
                .email("ana.martinez@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1995, 11, 5))
                .genero(Usuario.Genero.FEMENINO)
                .telefono("3007777777")
                .direccion("Calle 50")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.INACTIVO)
                .build();
        entityManager.persistAndFlush(pacienteInactivo);

        // Act
        List<Usuario> pacientesActivos = usuarioRepository.findByTipoUsuarioAndEstado(
                Usuario.TipoUsuario.PACIENTE, Usuario.Estado.ACTIVO);
        List<Usuario> pacientesInactivos = usuarioRepository.findByTipoUsuarioAndEstado(
                Usuario.TipoUsuario.PACIENTE, Usuario.Estado.INACTIVO);

        // Assert
        assertEquals(1, pacientesActivos.size());
        assertEquals(1, pacientesInactivos.size());
    }

    @Test
    @DisplayName("Buscar usuarios activos por tipo con Query")
    void testFindUsuariosActivosByTipo() {
        // Arrange
        entityManager.persistAndFlush(usuario);

        Usuario pacienteInactivo = Usuario.builder()
                .nombre("Pedro")
                .apellido("Gómez")
                .email("pedro.gomez@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1992, 4, 10))
                .genero(Usuario.Genero.MASCULINO)
                .telefono("3008888888")
                .direccion("Carrera 20")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.INACTIVO)
                .build();
        entityManager.persistAndFlush(pacienteInactivo);

        // Act
        List<Usuario> pacientesActivos = usuarioRepository.findUsuariosActivosByTipo(Usuario.TipoUsuario.PACIENTE);

        // Assert
        assertEquals(1, pacientesActivos.size());
        assertEquals(Usuario.Estado.ACTIVO, pacientesActivos.get(0).getEstado());
    }

    @Test
    @DisplayName("Buscar por nombre o apellido")
    void testBuscarPorNombreOApellido() {
        // Arrange
        entityManager.persistAndFlush(usuario);

        Usuario usuario2 = Usuario.builder()
                .nombre("María")
                .apellido("Pérez")
                .email("maria.perez@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1993, 6, 25))
                .genero(Usuario.Genero.FEMENINO)
                .telefono("3009999999")
                .direccion("Calle 30")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.ACTIVO)
                .build();
        entityManager.persistAndFlush(usuario2);

        // Act
        List<Usuario> resultadoJuan = usuarioRepository.buscarPorNombreOApellido("Juan");
        List<Usuario> resultadoPerez = usuarioRepository.buscarPorNombreOApellido("Pérez");
        List<Usuario> resultadoMaria = usuarioRepository.buscarPorNombreOApellido("María");

        // Assert
        assertEquals(1, resultadoJuan.size());
        assertEquals("Juan", resultadoJuan.get(0).getNombre());

        assertEquals(2, resultadoPerez.size()); // Both have Pérez as apellido

        assertEquals(1, resultadoMaria.size());
        assertEquals("María", resultadoMaria.get(0).getNombre());
    }

    @Test
    @DisplayName("Buscar usuarios por ciudad")
    void testFindByCiudad() {
        // Arrange
        entityManager.persistAndFlush(usuario);

        Usuario usuarioBogota = Usuario.builder()
                .nombre("Laura")
                .apellido("Ramírez")
                .email("laura.ramirez@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1991, 9, 30))
                .genero(Usuario.Genero.FEMENINO)
                .telefono("3001111111")
                .direccion("Calle 100")
                .ciudad("Bogotá")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.ACTIVO)
                .build();
        entityManager.persistAndFlush(usuarioBogota);

        // Act
        List<Usuario> usuariosArmenia = usuarioRepository.findByCiudad("Armenia");
        List<Usuario> usuariosBogota = usuarioRepository.findByCiudad("Bogotá");

        // Assert
        assertEquals(1, usuariosArmenia.size());
        assertEquals("Armenia", usuariosArmenia.get(0).getCiudad());

        assertEquals(1, usuariosBogota.size());
        assertEquals("Bogotá", usuariosBogota.get(0).getCiudad());
    }

    @Test
    @DisplayName("Contar usuarios por tipo")
    void testContarUsuariosPorTipo() {
        // Arrange
        entityManager.persistAndFlush(usuario);

        Usuario medico = Usuario.builder()
                .nombre("Dr. Carlos")
                .apellido("Díaz")
                .email("carlos.diaz@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1980, 2, 14))
                .genero(Usuario.Genero.MASCULINO)
                .telefono("3002222222")
                .direccion("Calle 70")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.MEDICO)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.ACTIVO)
                .build();
        entityManager.persistAndFlush(medico);

        // Act
        Long countPacientes = usuarioRepository.contarUsuariosPorTipo(Usuario.TipoUsuario.PACIENTE);
        Long countMedicos = usuarioRepository.contarUsuariosPorTipo(Usuario.TipoUsuario.MEDICO);

        // Assert
        assertEquals(1, countPacientes);
        assertEquals(1, countMedicos);
    }

    @Test
    @DisplayName("Contar usuarios activos")
    void testContarUsuariosActivos() {
        // Arrange
        entityManager.persistAndFlush(usuario);

        Usuario usuarioInactivo = Usuario.builder()
                .nombre("Inactivo")
                .apellido("Usuario")
                .email("inactivo@example.com")
                .contraseña("encodedPassword")
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .genero(Usuario.Genero.MASCULINO)
                .telefono("3003333333")
                .direccion("Calle 1")
                .ciudad("Armenia")
                .pais("Colombia")
                .tipoUsuario(Usuario.TipoUsuario.PACIENTE)
                .fechaRegistro(LocalDateTime.now())
                .estado(Usuario.Estado.INACTIVO)
                .build();
        entityManager.persistAndFlush(usuarioInactivo);

        // Act
        Long countActivos = usuarioRepository.contarUsuariosActivos();

        // Assert
        assertEquals(1, countActivos);
    }
}
