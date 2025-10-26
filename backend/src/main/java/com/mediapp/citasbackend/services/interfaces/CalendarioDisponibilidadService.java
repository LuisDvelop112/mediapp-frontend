package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.CalendarioDisponibilidad;
import com.mediapp.citasbackend.entities.Medico;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CalendarioDisponibilidadService {

    // Operaciones CRUD básicas
    CalendarioDisponibilidad guardarDisponibilidad(CalendarioDisponibilidad disponibilidad);
    
    CalendarioDisponibilidad actualizarDisponibilidad(Integer id, CalendarioDisponibilidad disponibilidad);
    
    void eliminarDisponibilidad(Integer id);
    
    Optional<CalendarioDisponibilidad> obtenerDisponibilidadPorId(Integer id);
    
    List<CalendarioDisponibilidad> obtenerTodasLasDisponibilidades();

    // Búsquedas por médico
    List<CalendarioDisponibilidad> obtenerDisponibilidadesPorMedico(Medico medico);
    
    List<CalendarioDisponibilidad> obtenerDisponibilidadesPorMedicoId(Integer idMedico);
    
    List<CalendarioDisponibilidad> obtenerDisponibilidadesActivasPorMedico(Integer idMedico);

    // Búsquedas por día
    List<CalendarioDisponibilidad> obtenerDisponibilidadesPorMedicoYDia(
            Integer idMedico, 
            CalendarioDisponibilidad.DiaSemana dia
    );
    
    List<CalendarioDisponibilidad> obtenerDisponibilidadesPorDia(CalendarioDisponibilidad.DiaSemana diaSemana);

    // Búsquedas por estado
    List<CalendarioDisponibilidad> obtenerDisponibilidadesPorEstado(CalendarioDisponibilidad.Estado estado);
    
    List<CalendarioDisponibilidad> obtenerDisponibilidadesPorMedicoYEstado(
            Integer idMedico, 
            CalendarioDisponibilidad.Estado estado
    );

    // Verificación de conflictos
    List<CalendarioDisponibilidad> verificarConflictosDeHorario(
            Integer idMedico,
            CalendarioDisponibilidad.DiaSemana dia,
            LocalTime horaInicio,
            LocalTime horaFin
    );
    
    boolean tieneConflictoDeHorario(
            Integer idMedico,
            CalendarioDisponibilidad.DiaSemana dia,
            LocalTime horaInicio,
            LocalTime horaFin
    );

    // Búsqueda específica
    Optional<CalendarioDisponibilidad> obtenerDisponibilidadPorMedicoYDia(
            Integer idMedico,
            CalendarioDisponibilidad.DiaSemana diaSemana
    );

    // Conteo
    Long contarDisponibilidadesActivasPorMedico(Integer idMedico);

    // Búsqueda de médicos disponibles
    List<Medico> obtenerMedicosDisponiblesEnDia(CalendarioDisponibilidad.DiaSemana dia);
    
    List<Medico> obtenerMedicosDisponiblesEnDiaYHora(
            CalendarioDisponibilidad.DiaSemana dia,
            LocalTime hora
    );

    // Ordenamiento
    List<CalendarioDisponibilidad> obtenerDisponibilidadesOrdenadasPorMedico(Integer idMedico);

    // Operaciones masivas
    void eliminarTodasLasDisponibilidadesPorMedico(Integer idMedico);
    
    boolean medicoTieneDisponibilidadConfigurada(Integer idMedico);

    // Búsqueda por intervalo
    List<CalendarioDisponibilidad> obtenerDisponibilidadesPorIntervalo(Integer intervalo);

    // Operaciones de estado
    CalendarioDisponibilidad activarDisponibilidad(Integer id);
    
    CalendarioDisponibilidad desactivarDisponibilidad(Integer id);

    // Validaciones
    void validarDisponibilidad(CalendarioDisponibilidad disponibilidad);
    
    void validarHorario(LocalTime horaInicio, LocalTime horaFin);
}
