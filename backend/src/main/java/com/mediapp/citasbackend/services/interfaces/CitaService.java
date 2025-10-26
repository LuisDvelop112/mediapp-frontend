package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.Cita;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Paciente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface CitaService {

    // Operaciones CRUD básicas
    Cita guardarCita(Cita cita);
    
    Cita actualizarCita(Integer id, Cita cita);
    
    void eliminarCita(Integer id);
    
    Optional<Cita> obtenerCitaPorId(Integer id);
    
    List<Cita> obtenerTodasLasCitas();

    // Búsquedas por paciente
    List<Cita> obtenerCitasPorPaciente(Paciente paciente);
    
    List<Cita> obtenerCitasPorPacienteId(Integer idPaciente);
    
    List<Cita> obtenerCitasPorPacienteYEstado(Integer idPaciente, Cita.Estado estado);
    
    List<Cita> obtenerCitasProgramadasPorPaciente(Integer idPaciente);

    // Búsquedas por médico
    List<Cita> obtenerCitasPorMedico(Medico medico);
    
    List<Cita> obtenerCitasPorMedicoId(Integer idMedico);
    
    List<Cita> obtenerCitasPorMedicoYEstado(Integer idMedico, Cita.Estado estado);
    
    List<Cita> obtenerCitasProgramadasPorMedico(Integer idMedico);

    // Búsquedas por estado
    List<Cita> obtenerCitasPorEstado(Cita.Estado estado);

    // Búsquedas por fecha
    List<Cita> obtenerCitasPorFecha(LocalDate fecha);
    
    List<Cita> obtenerCitasPorMedicoYFecha(Integer idMedico, LocalDate fecha);
    
    List<Cita> obtenerCitasPorPacienteYFecha(Integer idPaciente, LocalDate fecha);

    // Búsquedas por rango de fechas
    List<Cita> obtenerCitasEnRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
    
    List<Cita> obtenerCitasPorMedicoEnRangoFechas(Integer idMedico, LocalDate fechaInicio, LocalDate fechaFin);
    
    List<Cita> obtenerCitasPorPacienteEnRangoFechas(Integer idPaciente, LocalDate fechaInicio, LocalDate fechaFin);

    // Verificación de disponibilidad
    Optional<Cita> verificarDisponibilidadMedicoFechaHora(Integer idMedico, LocalDate fecha, LocalTime hora);
    
    boolean medicoDisponibleEnFechaHora(Integer idMedico, LocalDate fecha, LocalTime hora);

    // Búsquedas por tipo de cita
    List<Cita> obtenerCitasPorTipo(Cita.TipoCita tipoCita);
    
    List<Cita> obtenerCitasTelemedicinaProgramadasPorMedico(Integer idMedico);

    // Próximas citas
    List<Cita> obtenerProximasCitasPorPaciente(Integer idPaciente);
    
    List<Cita> obtenerProximasCitasPorMedico(Integer idMedico);

    // Historial de citas
    List<Cita> obtenerHistorialCitasPorPaciente(Integer idPaciente);
    
    List<Cita> obtenerHistorialCitasPorMedico(Integer idMedico);

    // Conteo de citas
    Long contarCitasPorEstado(Cita.Estado estado);
    
    Long contarCitasPorMedicoYEstado(Integer idMedico, Cita.Estado estado);
    
    Long contarCitasPorPacienteYEstado(Integer idPaciente, Cita.Estado estado);

    // Citas del día
    List<Cita> obtenerCitasDelDiaPorMedico(Integer idMedico, LocalDate fecha);

    // Citas canceladas
    List<Cita> obtenerCitasCanceladasEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Verificación de existencia
    boolean existeCitaEntrePacienteMedicoYFecha(Integer idPaciente, Integer idMedico, LocalDate fechaCita);

    // Última cita
    Optional<Cita> obtenerUltimaCitaEntrePacienteYMedico(Integer idPaciente, Integer idMedico);

    // Operaciones de cambio de estado
    Cita completarCita(Integer id);
    
    Cita cancelarCita(Integer id);
    
    Cita marcarComoNoAsistio(Integer id);

    // Validaciones
    void validarCita(Cita cita);
    
    void validarFechaCita(LocalDate fechaCita, LocalTime horaCita);
}
