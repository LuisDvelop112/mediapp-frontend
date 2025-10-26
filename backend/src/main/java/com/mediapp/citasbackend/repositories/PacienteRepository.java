package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Paciente;
import com.mediapp.citasbackend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    // Buscar paciente por usuario
    Optional<Paciente> findByUsuario(Usuario usuario);

    // Buscar paciente por ID de usuario
    Optional<Paciente> findByUsuario_IdUsuario(Integer idUsuario);

    // Verificar si existe un paciente asociado a un usuario
    boolean existsByUsuario_IdUsuario(Integer idUsuario);

    // Buscar paciente por número de identificación
    Optional<Paciente> findByNumeroIdentificacion(String numeroIdentificacion);

    // Verificar si existe un número de identificación
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);

    // Buscar pacientes por tipo de sangre
    List<Paciente> findByTipoSangre(String tipoSangre);

    // Buscar pacientes activos (usuarios activos)
    @Query("SELECT p FROM Paciente p WHERE p.usuario.estado = 'ACTIVO'")
    List<Paciente> findPacientesActivos();

    // Buscar pacientes por nombre (búsqueda en usuario)
    @Query("SELECT p FROM Paciente p WHERE " +
           "LOWER(p.usuario.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "OR LOWER(p.usuario.apellido) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Paciente> buscarPacientesPorNombre(@Param("nombre") String nombre);

    // Buscar pacientes activos por nombre
    @Query("SELECT p FROM Paciente p WHERE " +
           "(LOWER(p.usuario.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "OR LOWER(p.usuario.apellido) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND p.usuario.estado = 'ACTIVO'")
    List<Paciente> buscarPacientesActivosPorNombre(@Param("nombre") String nombre);

    // Buscar pacientes por email (desde usuario)
    @Query("SELECT p FROM Paciente p WHERE p.usuario.email = :email")
    Optional<Paciente> findByEmail(@Param("email") String email);

    // Buscar pacientes por ciudad (desde usuario)
    @Query("SELECT p FROM Paciente p WHERE p.usuario.ciudad = :ciudad " +
           "AND p.usuario.estado = 'ACTIVO'")
    List<Paciente> findPacientesByCiudad(@Param("ciudad") String ciudad);

    // Buscar pacientes con alergias registradas
    @Query("SELECT p FROM Paciente p WHERE p.alergias IS NOT NULL AND p.alergias != ''")
    List<Paciente> findPacientesConAlergias();

    // Buscar pacientes con enfermedades crónicas
    @Query("SELECT p FROM Paciente p WHERE p.enfermedadesCronicas IS NOT NULL AND p.enfermedadesCronicas != ''")
    List<Paciente> findPacientesConEnfermedadesCronicas();

    // Buscar pacientes con medicamentos actuales
    @Query("SELECT p FROM Paciente p WHERE p.medicamentosActuales IS NOT NULL AND p.medicamentosActuales != ''")
    List<Paciente> findPacientesConMedicamentosActuales();

    // Buscar pacientes por alergia específica (búsqueda parcial)
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.alergias) LIKE LOWER(CONCAT('%', :alergia, '%'))")
    List<Paciente> buscarPacientesPorAlergia(@Param("alergia") String alergia);

    // Buscar pacientes por enfermedad crónica específica
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.enfermedadesCronicas) LIKE LOWER(CONCAT('%', :enfermedad, '%'))")
    List<Paciente> buscarPacientesPorEnfermedad(@Param("enfermedad") String enfermedad);

    // Buscar pacientes por medicamento actual
    @Query("SELECT p FROM Paciente p WHERE LOWER(p.medicamentosActuales) LIKE LOWER(CONCAT('%', :medicamento, '%'))")
    List<Paciente> buscarPacientesPorMedicamento(@Param("medicamento") String medicamento);

    // Buscar pacientes con contacto de emergencia registrado
    @Query("SELECT p FROM Paciente p WHERE p.contactoEmergencia IS NOT NULL AND p.contactoEmergencia != ''")
    List<Paciente> findPacientesConContactoEmergencia();

    // Buscar pacientes sin contacto de emergencia
    @Query("SELECT p FROM Paciente p WHERE p.contactoEmergencia IS NULL OR p.contactoEmergencia = ''")
    List<Paciente> findPacientesSinContactoEmergencia();

    // Buscar pacientes sin tipo de sangre registrado
    @Query("SELECT p FROM Paciente p WHERE p.tipoSangre IS NULL OR p.tipoSangre = ''")
    List<Paciente> findPacientesSinTipoSangre();

    // Buscar pacientes con perfil completo (todos los campos importantes llenos)
    @Query("SELECT p FROM Paciente p WHERE " +
           "p.numeroIdentificacion IS NOT NULL AND p.numeroIdentificacion != '' " +
           "AND p.tipoSangre IS NOT NULL AND p.tipoSangre != '' " +
           "AND p.contactoEmergencia IS NOT NULL AND p.contactoEmergencia != '' " +
           "AND p.telefonoEmergencia IS NOT NULL AND p.telefonoEmergencia != ''")
    List<Paciente> findPacientesConPerfilCompleto();

    // Buscar pacientes con perfil incompleto
    @Query("SELECT p FROM Paciente p WHERE " +
           "p.numeroIdentificacion IS NULL OR p.numeroIdentificacion = '' " +
           "OR p.tipoSangre IS NULL OR p.tipoSangre = '' " +
           "OR p.contactoEmergencia IS NULL OR p.contactoEmergencia = '' " +
           "OR p.telefonoEmergencia IS NULL OR p.telefonoEmergencia = ''")
    List<Paciente> findPacientesConPerfilIncompleto();

    // Contar pacientes activos
    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.usuario.estado = 'ACTIVO'")
    Long contarPacientesActivos();

    // Contar total de pacientes
    @Query("SELECT COUNT(p) FROM Paciente p")
    Long contarPacientes();

    // Contar pacientes por tipo de sangre
    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.tipoSangre = :tipoSangre")
    Long contarPacientesPorTipoSangre(@Param("tipoSangre") String tipoSangre);

    // Buscar pacientes por género (desde usuario)
    @Query("SELECT p FROM Paciente p WHERE p.usuario.genero = :genero")
    List<Paciente> findPacientesByGenero(@Param("genero") Usuario.Genero genero);

    // Buscar pacientes nuevos (recientemente registrados)
    @Query("SELECT p FROM Paciente p WHERE p.usuario.estado = 'ACTIVO' " +
           "ORDER BY p.usuario.fechaRegistro DESC")
    List<Paciente> findPacientesRecientes();

    // Buscar todos los pacientes ordenados por nombre
    @Query("SELECT p FROM Paciente p ORDER BY p.usuario.nombre, p.usuario.apellido")
    List<Paciente> findAllOrdenadosPorNombre();

    // Verificar si el número de identificación ya existe (excluyendo un ID específico)
    @Query("SELECT COUNT(p) FROM Paciente p WHERE p.numeroIdentificacion = :numeroIdentificacion " +
           "AND p.idPaciente != :idExcluir")
    Long contarPorIdentificacionExcluyendoId(
            @Param("numeroIdentificacion") String numeroIdentificacion,
            @Param("idExcluir") Integer idExcluir
    );

    // Buscar pacientes por país (desde usuario)
    @Query("SELECT p FROM Paciente p WHERE p.usuario.pais = :pais")
    List<Paciente> findPacientesByPais(@Param("pais") String pais);

    // Buscar pacientes que han tenido citas (tienen historial médico)
    @Query("SELECT DISTINCT p FROM Paciente p JOIN HistorialMedico h ON h.paciente = p")
    List<Paciente> findPacientesConHistorialMedico();

    // Buscar pacientes sin historial médico
    @Query("SELECT p FROM Paciente p WHERE NOT EXISTS " +
           "(SELECT h FROM HistorialMedico h WHERE h.paciente = p)")
    List<Paciente> findPacientesSinHistorialMedico();

    // Buscar pacientes con citas programadas
    @Query("SELECT DISTINCT p FROM Paciente p JOIN Cita c ON c.paciente = p " +
           "WHERE c.estado = 'PROGRAMADA'")
    List<Paciente> findPacientesConCitasProgramadas();
}
