package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Especialidad;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {

    // Buscar médico por usuario
    Optional<Medico> findByUsuario(Usuario usuario);

    // Buscar médico por ID de usuario
    Optional<Medico> findByUsuario_IdUsuario(Integer idUsuario);

    // Verificar si existe un médico asociado a un usuario
    boolean existsByUsuario_IdUsuario(Integer idUsuario);

    // Buscar médico por número de licencia
    Optional<Medico> findByNumeroLicencia(String numeroLicencia);

    // Verificar si existe un número de licencia
    boolean existsByNumeroLicencia(String numeroLicencia);

    // Buscar médicos por especialidad
    List<Medico> findByEspecialidad(Especialidad especialidad);

    // Buscar médicos por ID de especialidad
    List<Medico> findByEspecialidad_IdEspecialidad(Integer idEspecialidad);

    // Buscar médicos por estado de verificación
    List<Medico> findByEstadoVerificacion(Medico.EstadoVerificacion estadoVerificacion);

    // Buscar médicos verificados
    @Query("SELECT m FROM Medico m WHERE m.estadoVerificacion = 'VERIFICADO' " +
           "ORDER BY m.calificacionPromedio DESC NULLS LAST")
    List<Medico> findMedicosVerificados();

    // Buscar médicos pendientes de verificación
    @Query("SELECT m FROM Medico m WHERE m.estadoVerificacion = 'PENDIENTE' " +
           "ORDER BY m.usuario.fechaRegistro ASC")
    List<Medico> findMedicosPendientesVerificacion();

    // Buscar médicos verificados por especialidad
    @Query("SELECT m FROM Medico m WHERE m.especialidad.idEspecialidad = :idEspecialidad " +
           "AND m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.usuario.estado = 'ACTIVO' " +
           "ORDER BY m.calificacionPromedio DESC NULLS LAST")
    List<Medico> findMedicosVerificadosByEspecialidad(@Param("idEspecialidad") Integer idEspecialidad);

    // Buscar médicos activos y verificados
    @Query("SELECT m FROM Medico m WHERE m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.usuario.estado = 'ACTIVO' " +
           "ORDER BY m.calificacionPromedio DESC NULLS LAST")
    List<Medico> findMedicosActivosVerificados();

    // Buscar médicos por hospital afiliado
    List<Medico> findByHospitalAfiliado(String hospitalAfiliado);

    // Buscar médicos con experiencia mínima
    @Query("SELECT m FROM Medico m WHERE m.experienciaAnos >= :aniosMinimos " +
           "AND m.estadoVerificacion = 'VERIFICADO' " +
           "ORDER BY m.experienciaAnos DESC")
    List<Medico> findMedicosConExperienciaMinima(@Param("aniosMinimos") Integer aniosMinimos);

    // Buscar médicos por calificación mínima
    @Query("SELECT m FROM Medico m WHERE m.calificacionPromedio >= :calificacionMinima " +
           "AND m.estadoVerificacion = 'VERIFICADO' " +
           "ORDER BY m.calificacionPromedio DESC")
    List<Medico> findMedicosConCalificacionMinima(@Param("calificacionMinima") BigDecimal calificacionMinima);

    // Buscar médicos mejor calificados (top N)
    @Query("SELECT m FROM Medico m WHERE m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.calificacionPromedio IS NOT NULL " +
           "ORDER BY m.calificacionPromedio DESC")
    List<Medico> findMedicosMejorCalificados();

    // Buscar médicos mejor calificados por especialidad
    @Query("SELECT m FROM Medico m WHERE m.especialidad.idEspecialidad = :idEspecialidad " +
           "AND m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.calificacionPromedio IS NOT NULL " +
           "ORDER BY m.calificacionPromedio DESC")
    List<Medico> findMedicosMejorCalificadosByEspecialidad(@Param("idEspecialidad") Integer idEspecialidad);

    // Buscar médicos por nombre (búsqueda en usuario)
    @Query("SELECT m FROM Medico m WHERE " +
           "LOWER(m.usuario.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "OR LOWER(m.usuario.apellido) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Medico> buscarMedicosPorNombre(@Param("nombre") String nombre);

    // Buscar médicos activos y verificados por nombre
    @Query("SELECT m FROM Medico m WHERE " +
           "(LOWER(m.usuario.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "OR LOWER(m.usuario.apellido) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
           "AND m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.usuario.estado = 'ACTIVO' " +
           "ORDER BY m.calificacionPromedio DESC NULLS LAST")
    List<Medico> buscarMedicosActivosPorNombre(@Param("nombre") String nombre);

    // Buscar médicos por ciudad (desde usuario)
    @Query("SELECT m FROM Medico m WHERE m.usuario.ciudad = :ciudad " +
           "AND m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.usuario.estado = 'ACTIVO'")
    List<Medico> findMedicosByCiudad(@Param("ciudad") String ciudad);

    // Buscar médicos por ciudad y especialidad
    @Query("SELECT m FROM Medico m WHERE m.usuario.ciudad = :ciudad " +
           "AND m.especialidad.idEspecialidad = :idEspecialidad " +
           "AND m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.usuario.estado = 'ACTIVO' " +
           "ORDER BY m.calificacionPromedio DESC NULLS LAST")
    List<Medico> findMedicosByCiudadAndEspecialidad(
            @Param("ciudad") String ciudad,
            @Param("idEspecialidad") Integer idEspecialidad
    );

    // Contar médicos por especialidad
    @Query("SELECT COUNT(m) FROM Medico m WHERE m.especialidad.idEspecialidad = :idEspecialidad " +
           "AND m.estadoVerificacion = 'VERIFICADO'")
    Long contarMedicosByEspecialidad(@Param("idEspecialidad") Integer idEspecialidad);

    // Contar médicos verificados
    @Query("SELECT COUNT(m) FROM Medico m WHERE m.estadoVerificacion = 'VERIFICADO'")
    Long contarMedicosVerificados();

    // Contar médicos pendientes
    @Query("SELECT COUNT(m) FROM Medico m WHERE m.estadoVerificacion = 'PENDIENTE'")
    Long contarMedicosPendientes();

    // Buscar médicos nuevos (recientemente registrados)
    @Query("SELECT m FROM Medico m WHERE m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.usuario.estado = 'ACTIVO' " +
           "ORDER BY m.fechaVerificacion DESC")
    List<Medico> findMedicosRecientes();

    // Buscar médicos por email (desde usuario)
    @Query("SELECT m FROM Medico m WHERE m.usuario.email = :email")
    Optional<Medico> findByEmail(@Param("email") String email);

    // Buscar médicos sin calificación
    @Query("SELECT m FROM Medico m WHERE m.calificacionPromedio IS NULL " +
           "AND m.estadoVerificacion = 'VERIFICADO'")
    List<Medico> findMedicosSinCalificacion();

    // Buscar médicos con bio
    @Query("SELECT m FROM Medico m WHERE m.resumenBio IS NOT NULL AND m.resumenBio != '' " +
           "AND m.estadoVerificacion = 'VERIFICADO'")
    List<Medico> findMedicosConBio();

    // Buscar médicos por rango de experiencia
    @Query("SELECT m FROM Medico m WHERE m.experienciaAnos BETWEEN :minAnos AND :maxAnos " +
           "AND m.estadoVerificacion = 'VERIFICADO' " +
           "ORDER BY m.experienciaAnos DESC")
    List<Medico> findMedicosPorRangoExperiencia(
            @Param("minAnos") Integer minAnos,
            @Param("maxAnos") Integer maxAnos
    );

    // Buscar médicos disponibles (que tienen calendario de disponibilidad configurado)
    @Query("SELECT DISTINCT m FROM Medico m " +
           "JOIN CalendarioDisponibilidad cd ON cd.medico = m " +
           "WHERE m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.usuario.estado = 'ACTIVO' " +
           "AND cd.estado = 'ACTIVO'")
    List<Medico> findMedicosDisponibles();

    // Buscar médicos disponibles por especialidad
    @Query("SELECT DISTINCT m FROM Medico m " +
           "JOIN CalendarioDisponibilidad cd ON cd.medico = m " +
           "WHERE m.especialidad.idEspecialidad = :idEspecialidad " +
           "AND m.estadoVerificacion = 'VERIFICADO' " +
           "AND m.usuario.estado = 'ACTIVO' " +
           "AND cd.estado = 'ACTIVO' " +
           "ORDER BY m.calificacionPromedio DESC NULLS LAST")
    List<Medico> findMedicosDisponiblesByEspecialidad(@Param("idEspecialidad") Integer idEspecialidad);

    // Buscar todos los médicos ordenados por calificación
    @Query("SELECT m FROM Medico m ORDER BY m.calificacionPromedio DESC NULLS LAST")
    List<Medico> findAllOrdenadosPorCalificacion();

    // Verificar si el número de licencia ya existe (excluyendo un ID específico)
    @Query("SELECT COUNT(m) FROM Medico m WHERE m.numeroLicencia = :numeroLicencia " +
           "AND m.idMedico != :idExcluir")
    Long contarPorLicenciaExcluyendoId(@Param("numeroLicencia") String numeroLicencia, @Param("idExcluir") Integer idExcluir);
}
