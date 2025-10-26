package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Evaluacion;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Integer> {

    // Buscar evaluaciones de un paciente
    List<Evaluacion> findByPaciente(Paciente paciente);

    // Buscar evaluaciones de un paciente por ID
    List<Evaluacion> findByPaciente_IdPaciente(Integer idPaciente);

    // Buscar evaluaciones de un médico
    List<Evaluacion> findByMedico(Medico medico);

    // Buscar evaluaciones de un médico por ID
    List<Evaluacion> findByMedico_IdMedico(Integer idMedico);

    // Buscar evaluaciones de un médico ordenadas por fecha descendente
    @Query("SELECT e FROM Evaluacion e WHERE e.medico.idMedico = :idMedico " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesByMedicoOrdenadas(@Param("idMedico") Integer idMedico);

    // Buscar evaluaciones de un paciente ordenadas por fecha descendente
    @Query("SELECT e FROM Evaluacion e WHERE e.paciente.idPaciente = :idPaciente " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesByPacienteOrdenadas(@Param("idPaciente") Integer idPaciente);

    // Buscar evaluaciones por calificación específica
    List<Evaluacion> findByCalificacion(Integer calificacion);

    // Buscar evaluaciones de un médico por calificación
    @Query("SELECT e FROM Evaluacion e WHERE e.medico.idMedico = :idMedico AND e.calificacion = :calificacion")
    List<Evaluacion> findEvaluacionesByMedicoAndCalificacion(
            @Param("idMedico") Integer idMedico,
            @Param("calificacion") Integer calificacion
    );

    // Buscar evaluaciones con calificación mayor o igual a un valor
    @Query("SELECT e FROM Evaluacion e WHERE e.calificacion >= :minCalificacion")
    List<Evaluacion> findEvaluacionesConCalificacionMinima(@Param("minCalificacion") Integer minCalificacion);

    // Buscar evaluaciones de un médico con calificación mayor o igual a un valor
    @Query("SELECT e FROM Evaluacion e WHERE e.medico.idMedico = :idMedico " +
           "AND e.calificacion >= :minCalificacion " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesByMedicoConCalificacionMinima(
            @Param("idMedico") Integer idMedico,
            @Param("minCalificacion") Integer minCalificacion
    );

    // Calcular promedio de calificaciones de un médico
    @Query("SELECT AVG(e.calificacion) FROM Evaluacion e WHERE e.medico.idMedico = :idMedico")
    Double calcularPromedioCalificacionesByMedico(@Param("idMedico") Integer idMedico);

    // Contar evaluaciones de un médico
    @Query("SELECT COUNT(e) FROM Evaluacion e WHERE e.medico.idMedico = :idMedico")
    Long contarEvaluacionesByMedico(@Param("idMedico") Integer idMedico);

    // Contar evaluaciones de un paciente
    @Query("SELECT COUNT(e) FROM Evaluacion e WHERE e.paciente.idPaciente = :idPaciente")
    Long contarEvaluacionesByPaciente(@Param("idPaciente") Integer idPaciente);

    // Buscar evaluaciones en un rango de fechas
    @Query("SELECT e FROM Evaluacion e WHERE e.fechaEvaluacion BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesEnRangoFechas(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    // Buscar evaluaciones de un médico en un rango de fechas
    @Query("SELECT e FROM Evaluacion e WHERE e.medico.idMedico = :idMedico " +
           "AND e.fechaEvaluacion BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesByMedicoEnRangoFechas(
            @Param("idMedico") Integer idMedico,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    // Buscar evaluaciones con respuesta del médico
    @Query("SELECT e FROM Evaluacion e WHERE e.respuestaMedico IS NOT NULL " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesConRespuesta();

    // Buscar evaluaciones sin respuesta del médico
    @Query("SELECT e FROM Evaluacion e WHERE e.respuestaMedico IS NULL " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesSinRespuesta();

    // Buscar evaluaciones de un médico sin respuesta
    @Query("SELECT e FROM Evaluacion e WHERE e.medico.idMedico = :idMedico " +
           "AND e.respuestaMedico IS NULL " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesSinRespuestaByMedico(@Param("idMedico") Integer idMedico);

    // Buscar evaluaciones con comentario
    @Query("SELECT e FROM Evaluacion e WHERE e.comentario IS NOT NULL AND e.comentario != '' " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesConComentario();

    // Buscar últimas N evaluaciones de un médico
    @Query("SELECT e FROM Evaluacion e WHERE e.medico.idMedico = :idMedico " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findUltimasEvaluacionesByMedico(@Param("idMedico") Integer idMedico);

    // Buscar evaluaciones recientes (últimos N días)
    @Query("SELECT e FROM Evaluacion e WHERE e.fechaEvaluacion >= :fechaLimite " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesRecientes(@Param("fechaLimite") LocalDateTime fechaLimite);

    // Contar evaluaciones por calificación de un médico
    @Query("SELECT e.calificacion, COUNT(e) FROM Evaluacion e " +
           "WHERE e.medico.idMedico = :idMedico " +
           "GROUP BY e.calificacion " +
           "ORDER BY e.calificacion DESC")
    List<Object[]> contarEvaluacionesPorCalificacionByMedico(@Param("idMedico") Integer idMedico);

    // Buscar mejores evaluaciones (calificación 5)
    @Query("SELECT e FROM Evaluacion e WHERE e.calificacion = 5 " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findMejoresEvaluaciones();

    // Buscar mejores evaluaciones de un médico
    @Query("SELECT e FROM Evaluacion e WHERE e.medico.idMedico = :idMedico AND e.calificacion = 5 " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findMejoresEvaluacionesByMedico(@Param("idMedico") Integer idMedico);

    // Verificar si existe evaluación entre paciente y médico
    boolean existsByPaciente_IdPacienteAndMedico_IdMedico(Integer idPaciente, Integer idMedico);

    // Buscar evaluación específica entre paciente y médico
    @Query("SELECT e FROM Evaluacion e WHERE e.paciente.idPaciente = :idPaciente " +
           "AND e.medico.idMedico = :idMedico " +
           "ORDER BY e.fechaEvaluacion DESC")
    List<Evaluacion> findEvaluacionesByPacienteAndMedico(
            @Param("idPaciente") Integer idPaciente,
            @Param("idMedico") Integer idMedico
    );
}
