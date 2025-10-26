package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.HistorialMedico;
import com.mediapp.citasbackend.entities.Prescripcion;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PrescripcionService {

    // Operaciones CRUD básicas
    Prescripcion guardarPrescripcion(Prescripcion prescripcion);
    
    Prescripcion actualizarPrescripcion(Integer id, Prescripcion prescripcion);
    
    void eliminarPrescripcion(Integer id);
    
    Optional<Prescripcion> obtenerPrescripcionPorId(Integer id);
    
    List<Prescripcion> obtenerTodasLasPrescripciones();
    
    List<Prescripcion> obtenerTodasLasPrescripcionesOrdenadas();

    // Búsqueda por historial médico
    List<Prescripcion> obtenerPrescripcionesPorHistorial(HistorialMedico historialMedico);
    
    List<Prescripcion> obtenerPrescripcionesPorHistorialId(Integer idHistorial);

    // Búsqueda por paciente
    List<Prescripcion> obtenerPrescripcionesPorPaciente(Integer idPaciente);
    
    List<Prescripcion> obtenerUltimasPrescripcionesPorPaciente(Integer idPaciente);
    
    List<Prescripcion> obtenerPrescripcionesRecientesPorPaciente(Integer idPaciente, Integer dias);
    
    List<Prescripcion> obtenerPrescripcionesActivasPorPaciente(Integer idPaciente);

    // Búsqueda por médico
    List<Prescripcion> obtenerPrescripcionesPorMedico(Integer idMedico);

    // Búsqueda por paciente y médico
    List<Prescripcion> obtenerPrescripcionesPorPacienteYMedico(Integer idPaciente, Integer idMedico);

    // Búsqueda por cita
    List<Prescripcion> obtenerPrescripcionesPorCita(Integer idCita);

    // Búsqueda por medicamento
    List<Prescripcion> obtenerPrescripcionesPorNombreMedicamento(String nombreMedicamento);
    
    List<Prescripcion> buscarPrescripcionesPorMedicamento(String medicamento);
    
    List<Prescripcion> buscarPrescripcionesPacientePorMedicamento(Integer idPaciente, String medicamento);

    // Búsqueda por fecha
    List<Prescripcion> obtenerPrescripcionesPorFecha(LocalDate fecha);
    
    List<Prescripcion> obtenerPrescripcionesDelDia();
    
    List<Prescripcion> obtenerPrescripcionesEnRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
    
    List<Prescripcion> obtenerPrescripcionesPorPacienteEnRangoFechas(Integer idPaciente, LocalDate fechaInicio, LocalDate fechaFin);
    
    List<Prescripcion> obtenerPrescripcionesPorMedicoEnRangoFechas(Integer idMedico, LocalDate fechaInicio, LocalDate fechaFin);

    // Búsqueda por características del tratamiento
    List<Prescripcion> obtenerPrescripcionesPorDuracion(Integer duracionDias);
    
    List<Prescripcion> obtenerPrescripcionesConDuracionMinima(Integer diasMinimos);
    
    List<Prescripcion> obtenerPrescripcionesSinDuracion();
    
    List<Prescripcion> obtenerPrescripcionesTratamientosLargos();
    
    List<Prescripcion> obtenerPrescripcionesPorDosis(String dosis);
    
    List<Prescripcion> obtenerPrescripcionesPorFrecuencia(String frecuencia);

    // Búsqueda por instrucciones
    List<Prescripcion> obtenerPrescripcionesConInstrucciones();
    
    List<Prescripcion> obtenerPrescripcionesConInstruccionesPorPaciente(Integer idPaciente);

    // Conteo de prescripciones
    Long contarPrescripcionesPorPaciente(Integer idPaciente);
    
    Long contarPrescripcionesPorMedico(Integer idMedico);
    
    Long contarPrescripcionesPorMedicamento(String medicamento);
    
    Long contarPrescripciones();

    // Estadísticas y análisis
    Map<String, Long> obtenerMedicamentosMasPrescritos();
    
    Map<String, Long> obtenerMedicamentosMasPrescritosPorMedico(Integer idMedico);

    // Verificaciones
    boolean pacienteTienePrescripcionDeMedicamento(Integer idPaciente, String medicamento);
    
    boolean prescripcionEstaActiva(Integer idPrescripcion);

    // Validaciones
    void validarPrescripcion(Prescripcion prescripcion);
    
    LocalDate calcularFechaFinTratamiento(Prescripcion prescripcion);
}
