package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Cita;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    // Buscar citas de un paciente
    List<Cita> findByPaciente(Paciente paciente);

    // Buscar citas de un paciente por ID
    List<Cita> findByPaciente_IdPaciente(Integer idPaciente);

    // Buscar citas de un médico
    List<Cita> findByMedico(Medico medico);

    // Buscar citas de un médico por ID
    List<Cita> findByMedico_IdMedico(Integer idMedico);

    // Buscar citas por estado
    List<Cita> findByEstado(Cita.Estado estado);

    // Buscar citas de un paciente por estado
    List<Cita> findByPaciente_IdPacienteAndEstado(Integer idPaciente, Cita.Estado estado);

    // Buscar citas de un médico por estado
    List<Cita> findByMedico_IdMedicoAndEstado(Integer idMedico, Cita.Estado estado);

    // Buscar citas programadas de un paciente
    @Query("SELECT c FROM Cita c WHERE c.paciente.idPaciente = :idPaciente AND c.estado = 'PROGRAMADA' " +
           "ORDER BY c.fechaCita, c.horaCita")
    List<Cita> findCitasProgramadasByPaciente(@Param("idPaciente") Integer idPaciente);

    // Buscar citas programadas de un médico
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico AND c.estado = 'PROGRAMADA' " +
           "ORDER BY c.fechaCita, c.horaCita")
    List<Cita> findCitasProgramadasByMedico(@Param("idMedico") Integer idMedico);

    // Buscar citas por fecha
    List<Cita> findByFechaCita(LocalDate fecha);

    // Buscar citas de un médico en una fecha específica
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico AND c.fechaCita = :fecha " +
           "ORDER BY c.horaCita")
    List<Cita> findCitasByMedicoAndFecha(@Param("idMedico") Integer idMedico, @Param("fecha") LocalDate fecha);

    // Buscar citas de un paciente en una fecha específica
    @Query("SELECT c FROM Cita c WHERE c.paciente.idPaciente = :idPaciente AND c.fechaCita = :fecha " +
           "ORDER BY c.horaCita")
    List<Cita> findCitasByPacienteAndFecha(@Param("idPaciente") Integer idPaciente, @Param("fecha") LocalDate fecha);

    // Verificar disponibilidad de un médico en fecha y hora específica
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico " +
           "AND c.fechaCita = :fecha " +
           "AND c.horaCita = :hora " +
           "AND c.estado != 'CANCELADA'")
    Optional<Cita> findCitaByMedicoFechaHora(
            @Param("idMedico") Integer idMedico,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora
    );

    // Buscar citas en un rango de fechas
    @Query("SELECT c FROM Cita c WHERE c.fechaCita BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY c.fechaCita, c.horaCita")
    List<Cita> findCitasEnRangoFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar citas de un médico en un rango de fechas
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico " +
           "AND c.fechaCita BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY c.fechaCita, c.horaCita")
    List<Cita> findCitasByMedicoEnRangoFechas(
            @Param("idMedico") Integer idMedico,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar citas de un paciente en un rango de fechas
    @Query("SELECT c FROM Cita c WHERE c.paciente.idPaciente = :idPaciente " +
           "AND c.fechaCita BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY c.fechaCita, c.horaCita")
    List<Cita> findCitasByPacienteEnRangoFechas(
            @Param("idPaciente") Integer idPaciente,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    // Buscar citas por tipo de cita
    List<Cita> findByTipoCita(Cita.TipoCita tipoCita);

    // Buscar citas de telemedicina de un médico
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico " +
           "AND c.tipoCita = 'TELEMEDICINA' " +
           "AND c.estado = 'PROGRAMADA' " +
           "ORDER BY c.fechaCita, c.horaCita")
    List<Cita> findCitasTelemedicinaProgramadasByMedico(@Param("idMedico") Integer idMedico);

    // Buscar próximas citas de un paciente (a partir de hoy)
    @Query("SELECT c FROM Cita c WHERE c.paciente.idPaciente = :idPaciente " +
           "AND c.fechaCita >= :fechaActual " +
           "AND c.estado = 'PROGRAMADA' " +
           "ORDER BY c.fechaCita, c.horaCita")
    List<Cita> findProximasCitasByPaciente(
            @Param("idPaciente") Integer idPaciente,
            @Param("fechaActual") LocalDate fechaActual
    );

    // Buscar próximas citas de un médico (a partir de hoy)
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico " +
           "AND c.fechaCita >= :fechaActual " +
           "AND c.estado = 'PROGRAMADA' " +
           "ORDER BY c.fechaCita, c.horaCita")
    List<Cita> findProximasCitasByMedico(
            @Param("idMedico") Integer idMedico,
            @Param("fechaActual") LocalDate fechaActual
    );

    // Buscar historial de citas completadas de un paciente
    @Query("SELECT c FROM Cita c WHERE c.paciente.idPaciente = :idPaciente " +
           "AND c.estado = 'COMPLETADA' " +
           "ORDER BY c.fechaCita DESC, c.horaCita DESC")
    List<Cita> findHistorialCitasByPaciente(@Param("idPaciente") Integer idPaciente);

    // Buscar historial de citas completadas de un médico
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico " +
           "AND c.estado = 'COMPLETADA' " +
           "ORDER BY c.fechaCita DESC, c.horaCita DESC")
    List<Cita> findHistorialCitasByMedico(@Param("idMedico") Integer idMedico);

    // Contar citas por estado
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.estado = :estado")
    Long contarCitasPorEstado(@Param("estado") Cita.Estado estado);

    // Contar citas de un médico por estado
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.medico.idMedico = :idMedico AND c.estado = :estado")
    Long contarCitasByMedicoAndEstado(@Param("idMedico") Integer idMedico, @Param("estado") Cita.Estado estado);

    // Contar citas de un paciente por estado
    @Query("SELECT COUNT(c) FROM Cita c WHERE c.paciente.idPaciente = :idPaciente AND c.estado = :estado")
    Long contarCitasByPacienteAndEstado(@Param("idPaciente") Integer idPaciente, @Param("estado") Cita.Estado estado);

    // Buscar citas del día actual de un médico
    @Query("SELECT c FROM Cita c WHERE c.medico.idMedico = :idMedico " +
           "AND c.fechaCita = :fechaActual " +
           "AND c.estado = 'PROGRAMADA' " +
           "ORDER BY c.horaCita")
    List<Cita> findCitasDelDiaByMedico(
            @Param("idMedico") Integer idMedico,
            @Param("fechaActual") LocalDate fechaActual
    );

    // Buscar citas canceladas en un rango de fechas
    @Query("SELECT c FROM Cita c WHERE c.estado = 'CANCELADA' " +
           "AND c.fechaCancelacion BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY c.fechaCancelacion DESC")
    List<Cita> findCitasCanceladasEnRango(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    // Verificar si existe cita entre paciente y médico en una fecha
    boolean existsByPaciente_IdPacienteAndMedico_IdMedicoAndFechaCita(
            Integer idPaciente,
            Integer idMedico,
            LocalDate fechaCita
    );

    // Buscar última cita entre paciente y médico
    @Query("SELECT c FROM Cita c WHERE c.paciente.idPaciente = :idPaciente " +
           "AND c.medico.idMedico = :idMedico " +
           "ORDER BY c.fechaCita DESC, c.horaCita DESC")
    List<Cita> findUltimaCitaByPacienteAndMedico(
            @Param("idPaciente") Integer idPaciente,
            @Param("idMedico") Integer idMedico
    );
}
