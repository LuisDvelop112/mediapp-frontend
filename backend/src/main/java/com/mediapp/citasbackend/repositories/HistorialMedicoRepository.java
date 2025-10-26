package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Cita;
import com.mediapp.citasbackend.entities.HistorialMedico;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer> {

    // Buscar historial médico por cita
    Optional<HistorialMedico> findByCita(Cita cita);

    // Buscar historial médico por ID de cita
    Optional<HistorialMedico> findByCita_IdCita(Integer idCita);

    // Verificar si existe historial para una cita
    boolean existsByCita_IdCita(Integer idCita);

    // Buscar historial médico de un paciente
    List<HistorialMedico> findByPaciente(Paciente paciente);

    // Buscar historial médico de un paciente por ID
    List<HistorialMedico> findByPaciente_IdPaciente(Integer idPaciente);

    // Buscar historial médico de un paciente ordenado por fecha de cita descendente
    @Query("SELECT h FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente " +
           "ORDER BY h.cita.fechaCita DESC, h.cita.horaCita DESC")
    List<HistorialMedico> findHistorialByPacienteOrdenado(@Param("idPaciente") Integer idPaciente);

    // Buscar historial médico creado por un médico
    List<HistorialMedico> findByMedico(Medico medico);

    // Buscar historial médico creado por un médico por ID
    List<HistorialMedico> findByMedico_IdMedico(Integer idMedico);

    // Buscar historial médico de un médico ordenado por fecha
    @Query("SELECT h FROM HistorialMedico h WHERE h.medico.idMedico = :idMedico " +
           "ORDER BY h.cita.fechaCita DESC, h.cita.horaCita DESC")
    List<HistorialMedico> findHistorialByMedicoOrdenado(@Param("idMedico") Integer idMedico);

    // Buscar historial médico entre un paciente y un médico específico
    @Query("SELECT h FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente " +
           "AND h.medico.idMedico = :idMedico " +
           "ORDER BY h.cita.fechaCita DESC, h.cita.horaCita DESC")
    List<HistorialMedico> findHistorialByPacienteAndMedico(
            @Param("idPaciente") Integer idPaciente,
            @Param("idMedico") Integer idMedico
    );

    // Buscar historial con diagnóstico específico (búsqueda parcial)
    @Query("SELECT h FROM HistorialMedico h WHERE LOWER(h.diagnostico) LIKE LOWER(CONCAT('%', :diagnostico, '%'))")
    List<HistorialMedico> buscarPorDiagnostico(@Param("diagnostico") String diagnostico);

    // Buscar historial de un paciente con diagnóstico específico
    @Query("SELECT h FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente " +
           "AND LOWER(h.diagnostico) LIKE LOWER(CONCAT('%', :diagnostico, '%')) " +
           "ORDER BY h.cita.fechaCita DESC")
    List<HistorialMedico> buscarHistorialPacientePorDiagnostico(
            @Param("idPaciente") Integer idPaciente,
            @Param("diagnostico") String diagnostico
    );

    // Buscar historial con síntomas específicos
    @Query("SELECT h FROM HistorialMedico h WHERE LOWER(h.sintomasReportados) LIKE LOWER(CONCAT('%', :sintoma, '%'))")
    List<HistorialMedico> buscarPorSintomas(@Param("sintoma") String sintoma);

    // Buscar historial con tratamiento específico
    @Query("SELECT h FROM HistorialMedico h WHERE LOWER(h.tratamientoRecomendado) LIKE LOWER(CONCAT('%', :tratamiento, '%'))")
    List<HistorialMedico> buscarPorTratamiento(@Param("tratamiento") String tratamiento);

    // Buscar historial con medicamento específico
    @Query("SELECT h FROM HistorialMedico h WHERE LOWER(h.medicamentosPrescritos) LIKE LOWER(CONCAT('%', :medicamento, '%'))")
    List<HistorialMedico> buscarPorMedicamento(@Param("medicamento") String medicamento);

    // Buscar historial de un paciente con medicamento específico
    @Query("SELECT h FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente " +
           "AND LOWER(h.medicamentosPrescritos) LIKE LOWER(CONCAT('%', :medicamento, '%')) " +
           "ORDER BY h.cita.fechaCita DESC")
    List<HistorialMedico> buscarHistorialPacientePorMedicamento(
            @Param("idPaciente") Integer idPaciente,
            @Param("medicamento") String medicamento
    );

    // Buscar historial con seguimiento programado
    @Query("SELECT h FROM HistorialMedico h WHERE h.fechaProximoSeguimiento IS NOT NULL " +
           "ORDER BY h.fechaProximoSeguimiento ASC")
    List<HistorialMedico> findHistorialConSeguimiento();

    // Buscar historial de un paciente con seguimiento programado
    @Query("SELECT h FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente " +
           "AND h.fechaProximoSeguimiento IS NOT NULL " +
           "ORDER BY h.fechaProximoSeguimiento ASC")
    List<HistorialMedico> findHistorialConSeguimientoByPaciente(@Param("idPaciente") Integer idPaciente);

    // Buscar historial con seguimientos pendientes (fecha futura)
    @Query("SELECT h FROM HistorialMedico h WHERE h.fechaProximoSeguimiento >= :fechaActual " +
           "ORDER BY h.fechaProximoSeguimiento ASC")
    List<HistorialMedico> findHistorialConSeguimientoPendiente(@Param("fechaActual") LocalDate fechaActual);

    // Buscar historial de un paciente con seguimientos pendientes
    @Query("SELECT h FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente " +
           "AND h.fechaProximoSeguimiento >= :fechaActual " +
           "ORDER BY h.fechaProximoSeguimiento ASC")
    List<HistorialMedico> findHistorialConSeguimientoPendienteByPaciente(
            @Param("idPaciente") Integer idPaciente,
            @Param("fechaActual") LocalDate fechaActual
    );

    // Buscar historial con seguimientos vencidos
    @Query("SELECT h FROM HistorialMedico h WHERE h.fechaProximoSeguimiento < :fechaActual " +
           "ORDER BY h.fechaProximoSeguimiento DESC")
    List<HistorialMedico> findHistorialConSeguimientoVencido(@Param("fechaActual") LocalDate fechaActual);

    // Buscar historial de un paciente en un rango de fechas de cita
    @Query("SELECT h FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente " +
           "AND h.cita.fechaCita BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY h.cita.fechaCita DESC, h.cita.horaCita DESC")
    List<HistorialMedico> findHistorialByPacienteEnRangoFechas(
            @Param("idPaciente") Integer idPaciente,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar historial de un médico en un rango de fechas
    @Query("SELECT h FROM HistorialMedico h WHERE h.medico.idMedico = :idMedico " +
           "AND h.cita.fechaCita BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY h.cita.fechaCita DESC, h.cita.horaCita DESC")
    List<HistorialMedico> findHistorialByMedicoEnRangoFechas(
            @Param("idMedico") Integer idMedico,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Contar registros de historial de un paciente
    @Query("SELECT COUNT(h) FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente")
    Long contarHistorialByPaciente(@Param("idPaciente") Integer idPaciente);

    // Contar registros de historial de un médico
    @Query("SELECT COUNT(h) FROM HistorialMedico h WHERE h.medico.idMedico = :idMedico")
    Long contarHistorialByMedico(@Param("idMedico") Integer idMedico);

    // Buscar último historial de un paciente
    @Query("SELECT h FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente " +
           "ORDER BY h.cita.fechaCita DESC, h.cita.horaCita DESC")
    List<HistorialMedico> findUltimoHistorialByPaciente(@Param("idPaciente") Integer idPaciente);

    // Buscar historial con observaciones
    @Query("SELECT h FROM HistorialMedico h WHERE h.observaciones IS NOT NULL AND h.observaciones != '' " +
           "ORDER BY h.cita.fechaCita DESC")
    List<HistorialMedico> findHistorialConObservaciones();

    // Buscar historial reciente de un paciente (últimos N registros)
    @Query("SELECT h FROM HistorialMedico h WHERE h.paciente.idPaciente = :idPaciente " +
           "ORDER BY h.cita.fechaCita DESC, h.cita.horaCita DESC")
    List<HistorialMedico> findHistorialRecienteByPaciente(@Param("idPaciente") Integer idPaciente);
}
