package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.HistorialMedico;
import com.mediapp.citasbackend.entities.Prescripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrescripcionRepository extends JpaRepository<Prescripcion, Integer> {

    // Buscar prescripciones de un historial médico
    List<Prescripcion> findByHistorialMedico(HistorialMedico historialMedico);

    // Buscar prescripciones de historiales médico por ID
    List<Prescripcion> findByHistorialMedico_IdHistorial(Integer idHistorial);

    // Busca prescripciones de un paciente
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesByPaciente(@Param("idPaciente") Integer idPaciente);

    // Buscar prescripciones de un médico
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.medico.idMedico = :idMedico " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesByMedico(@Param("idMedico") Integer idMedico);

    // Buscar prescripciones por nombre de medicamento
    List<Prescripcion> findByNombreMedicamento(String nombreMedicamento);

    // Buscar prescripciones por medicamento (búsqueda parcial)
    @Query("SELECT p FROM Prescripcion p WHERE LOWER(p.nombreMedicamento) LIKE LOWER(CONCAT('%', :medicamento, '%')) " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> buscarPorMedicamento(@Param("medicamento") String medicamento);

    // Buscar prescripciones de un paciente por medicamento
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente " +
           "AND LOWER(p.nombreMedicamento) LIKE LOWER(CONCAT('%', :medicamento, '%')) " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> buscarPrescripcionesPacientePorMedicamento(
            @Param("idPaciente") Integer idPaciente,
            @Param("medicamento") String medicamento
    );

    // Buscar prescripciones por fecha
    List<Prescripcion> findByFechaPrescripcion(LocalDate fecha);

    // Buscar prescripciones en un rango de fechas
    @Query("SELECT p FROM Prescripcion p WHERE p.fechaPrescripcion BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesEnRangoFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar prescripciones de un paciente en un rango de fechas
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente " +
           "AND p.fechaPrescripcion BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesByPacienteEnRangoFechas(
            @Param("idPaciente") Integer idPaciente,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar prescripciones de un médico en un rango de fechas
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.medico.idMedico = :idMedico " +
           "AND p.fechaPrescripcion BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesByMedicoEnRangoFechas(
            @Param("idMedico") Integer idMedico,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar prescripciones recientes de un paciente
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente " +
           "AND p.fechaPrescripcion >= :fechaLimite " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesRecientesByPaciente(
            @Param("idPaciente") Integer idPaciente,
            @Param("fechaLimite") LocalDate fechaLimite
    );

    // Buscar prescripciones activas de un paciente (dentro de la duración)
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente " +
           "AND p.duracionDias IS NOT NULL " +
           "AND FUNCTION('DATE_ADD', p.fechaPrescripcion, p.duracionDias, 'DAY') >= :fechaActual " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesActivasByPaciente(
            @Param("idPaciente") Integer idPaciente,
            @Param("fechaActual") LocalDate fechaActual
    );

    // Buscar prescripciones por duración específica
    List<Prescripcion> findByDuracionDias(Integer duracionDias);

    // Buscar prescripciones con duración mayor a un valor
    @Query("SELECT p FROM Prescripcion p WHERE p.duracionDias >= :diasMinimos " +
           "ORDER BY p.duracionDias DESC")
    List<Prescripcion> findPrescripcionesConDuracionMinima(@Param("diasMinimos") Integer diasMinimos);

    // Buscar prescripciones por dosis
    List<Prescripcion> findByDosis(String dosis);

    // Buscar prescripciones por frecuencia
    List<Prescripcion> findByFrecuencia(String frecuencia);

    // Buscar prescripciones con instrucciones especiales
    @Query("SELECT p FROM Prescripcion p WHERE p.instrucciones IS NOT NULL AND p.instrucciones != '' " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesConInstrucciones();

    // Buscar prescripciones de un paciente con instrucciones
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente " +
           "AND p.instrucciones IS NOT NULL AND p.instrucciones != '' " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesConInstruccionesByPaciente(@Param("idPaciente") Integer idPaciente);

    // Contar prescripciones de un paciente
    @Query("SELECT COUNT(p) FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente")
    Long contarPrescripcionesByPaciente(@Param("idPaciente") Integer idPaciente);

    // Contar prescripciones de un médico
    @Query("SELECT COUNT(p) FROM Prescripcion p WHERE p.historialMedico.medico.idMedico = :idMedico")
    Long contarPrescripcionesByMedico(@Param("idMedico") Integer idMedico);

    // Contar prescripciones por medicamento
    @Query("SELECT COUNT(p) FROM Prescripcion p WHERE LOWER(p.nombreMedicamento) = LOWER(:medicamento)")
    Long contarPrescripcionesPorMedicamento(@Param("medicamento") String medicamento);

    // Buscar últimas prescripciones de un paciente
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findUltimasPrescripcionesByPaciente(@Param("idPaciente") Integer idPaciente);

    // Buscar medicamentos más prescritos (estadísticas)
    @Query("SELECT p.nombreMedicamento, COUNT(p) as total FROM Prescripcion p " +
           "GROUP BY p.nombreMedicamento " +
           "ORDER BY total DESC")
    List<Object[]> findMedicamentosMasPrescritos();

    // Buscar medicamentos más prescritos por un médico
    @Query("SELECT p.nombreMedicamento, COUNT(p) as total FROM Prescripcion p " +
           "WHERE p.historialMedico.medico.idMedico = :idMedico " +
           "GROUP BY p.nombreMedicamento " +
           "ORDER BY total DESC")
    List<Object[]> findMedicamentosMasPrescritosByMedico(@Param("idMedico") Integer idMedico);

    // Buscar prescripciones entre un paciente y un médico específico
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente " +
           "AND p.historialMedico.medico.idMedico = :idMedico " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesByPacienteAndMedico(
            @Param("idPaciente") Integer idPaciente,
            @Param("idMedico") Integer idMedico
    );

    // Buscar prescripciones de un paciente por cita específica
    @Query("SELECT p FROM Prescripcion p WHERE p.historialMedico.cita.idCita = :idCita")
    List<Prescripcion> findPrescripcionesByCita(@Param("idCita") Integer idCita);

    // Buscar prescripciones sin duración definida
    @Query("SELECT p FROM Prescripcion p WHERE p.duracionDias IS NULL")
    List<Prescripcion> findPrescripcionesSinDuracion();

    // Buscar prescripciones de tratamientos largos (más de 30 días)
    @Query("SELECT p FROM Prescripcion p WHERE p.duracionDias > 30 " +
           "ORDER BY p.duracionDias DESC")
    List<Prescripcion> findPrescripcionesTratamientosLargos();

    // Buscar prescripciones del día actual
    @Query("SELECT p FROM Prescripcion p WHERE p.fechaPrescripcion = CURRENT_DATE " +
           "ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findPrescripcionesDelDia();

    // Buscar todas las prescripciones ordenadas por fecha descendente
    @Query("SELECT p FROM Prescripcion p ORDER BY p.fechaPrescripcion DESC")
    List<Prescripcion> findAllOrdenadas();

    // Verificar si un paciente tiene prescripción de un medicamento específico
    @Query("SELECT COUNT(p) FROM Prescripcion p WHERE p.historialMedico.paciente.idPaciente = :idPaciente " +
           "AND LOWER(p.nombreMedicamento) = LOWER(:medicamento)")
    Long verificarPrescripcionMedicamento(@Param("idPaciente") Integer idPaciente, @Param("medicamento") String medicamento);
}
