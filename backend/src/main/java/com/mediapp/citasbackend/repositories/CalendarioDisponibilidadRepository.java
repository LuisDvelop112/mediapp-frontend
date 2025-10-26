package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.CalendarioDisponibilidad;
import com.mediapp.citasbackend.entities.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarioDisponibilidadRepository extends JpaRepository<CalendarioDisponibilidad, Integer> {

    // Buscar todas las disponibilidades de un médico
    List<CalendarioDisponibilidad> findByMedico(Medico medico);

    // Buscar disponibilidades de un médico por ID
    List<CalendarioDisponibilidad> findByMedico_IdMedico(Integer idMedico);

    // Buscar disponibilidades activas de un médico
    @Query("SELECT cd FROM CalendarioDisponibilidad cd WHERE cd.medico.idMedico = :idMedico AND cd.estado = 'ACTIVO'")
    List<CalendarioDisponibilidad> findDisponibilidadesActivasByMedico(@Param("idMedico") Integer idMedico);

    // Buscar disponibilidades de un médico en un día específico
    @Query("SELECT cd FROM CalendarioDisponibilidad cd WHERE cd.medico.idMedico = :idMedico " +
           "AND cd.diaSemana = :dia AND cd.estado = 'ACTIVO'")
    List<CalendarioDisponibilidad> findDisponibilidadesByMedicoAndDia(
            @Param("idMedico") Integer idMedico,
            @Param("dia") CalendarioDisponibilidad.DiaSemana dia
    );

    // Buscar disponibilidades por día de la semana
    List<CalendarioDisponibilidad> findByDiaSemana(CalendarioDisponibilidad.DiaSemana diaSemana);

    // Buscar disponibilidades por estado
    List<CalendarioDisponibilidad> findByEstado(CalendarioDisponibilidad.Estado estado);

    // Verificar si existe disponibilidad para un médico en un día y horario específico
    @Query("SELECT cd FROM CalendarioDisponibilidad cd WHERE cd.medico.idMedico = :idMedico " +
           "AND cd.diaSemana = :dia " +
           "AND cd.horaInicio <= :horaFin " +
           "AND cd.horaFin >= :horaInicio")
    List<CalendarioDisponibilidad> findDisponibilidadesConflicto(
            @Param("idMedico") Integer idMedico,
            @Param("dia") CalendarioDisponibilidad.DiaSemana dia,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin
    );

    // Buscar disponibilidad específica de un médico en un día
    Optional<CalendarioDisponibilidad> findByMedico_IdMedicoAndDiaSemana(
            Integer idMedico,
            CalendarioDisponibilidad.DiaSemana diaSemana
    );

    // Contar disponibilidades activas de un médico
    @Query("SELECT COUNT(cd) FROM CalendarioDisponibilidad cd WHERE cd.medico.idMedico = :idMedico AND cd.estado = 'ACTIVO'")
    Long contarDisponibilidadesActivasByMedico(@Param("idMedico") Integer idMedico);

    // Buscar médicos disponibles en un día específico
    @Query("SELECT DISTINCT cd.medico FROM CalendarioDisponibilidad cd " +
           "WHERE cd.diaSemana = :dia AND cd.estado = 'ACTIVO'")
    List<Medico> findMedicosDisponiblesEnDia(@Param("dia") CalendarioDisponibilidad.DiaSemana dia);

    // Buscar médicos disponibles en un rango de horas específico
    @Query("SELECT DISTINCT cd.medico FROM CalendarioDisponibilidad cd " +
           "WHERE cd.diaSemana = :dia " +
           "AND cd.estado = 'ACTIVO' " +
           "AND cd.horaInicio <= :hora " +
           "AND cd.horaFin >= :hora")
    List<Medico> findMedicosDisponiblesEnDiaYHora(
            @Param("dia") CalendarioDisponibilidad.DiaSemana dia,
            @Param("hora") LocalTime hora
    );

    // Buscar todas las disponibilidades de un médico ordenadas por día
    @Query("SELECT cd FROM CalendarioDisponibilidad cd WHERE cd.medico.idMedico = :idMedico " +
           "ORDER BY cd.diaSemana, cd.horaInicio")
    List<CalendarioDisponibilidad> findByMedicoOrdenadas(@Param("idMedico") Integer idMedico);

    // Eliminar todas las disponibilidades de un médico
    void deleteByMedico_IdMedico(Integer idMedico);

    // Verificar si un médico tiene disponibilidad configurada
    boolean existsByMedico_IdMedico(Integer idMedico);

    // Buscar disponibilidades por médico y estado
    List<CalendarioDisponibilidad> findByMedico_IdMedicoAndEstado(
            Integer idMedico,
            CalendarioDisponibilidad.Estado estado
    );

    // Obtener horarios con intervalo de cita específico
    List<CalendarioDisponibilidad> findByIntervaloCitaMinutos(Integer intervalo);
}
