package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.Cita;
import com.mediapp.citasbackend.entities.HistorialMedico;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Paciente;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HistorialMedicoService {

    // Operaciones CRUD básicas
    HistorialMedico guardarHistorialMedico(HistorialMedico historialMedico);
    
    HistorialMedico actualizarHistorialMedico(Integer id, HistorialMedico historialMedico);
    
    void eliminarHistorialMedico(Integer id);
    
    Optional<HistorialMedico> obtenerHistorialMedicoPorId(Integer id);
    
    List<HistorialMedico> obtenerTodosLosHistorialesMedicos();

    // Búsqueda por cita
    Optional<HistorialMedico> obtenerHistorialMedicoPorCita(Cita cita);
    
    Optional<HistorialMedico> obtenerHistorialMedicoPorCitaId(Integer idCita);
    
    boolean existeHistorialMedicoPorCitaId(Integer idCita);

    // Búsquedas por paciente
    List<HistorialMedico> obtenerHistorialMedicoPorPaciente(Paciente paciente);
    
    List<HistorialMedico> obtenerHistorialMedicoPorPacienteId(Integer idPaciente);
    
    List<HistorialMedico> obtenerHistorialMedicoPorPacienteOrdenado(Integer idPaciente);

    // Búsquedas por médico
    List<HistorialMedico> obtenerHistorialMedicoPorMedico(Medico medico);
    
    List<HistorialMedico> obtenerHistorialMedicoPorMedicoId(Integer idMedico);
    
    List<HistorialMedico> obtenerHistorialMedicoPorMedicoOrdenado(Integer idMedico);

    // Búsqueda entre paciente y médico
    List<HistorialMedico> obtenerHistorialMedicoEntrePacienteYMedico(Integer idPaciente, Integer idMedico);

    // Búsquedas por diagnóstico
    List<HistorialMedico> buscarHistorialMedicoPorDiagnostico(String diagnostico);
    
    List<HistorialMedico> buscarHistorialMedicoPacientePorDiagnostico(Integer idPaciente, String diagnostico);

    // Búsquedas por síntomas
    List<HistorialMedico> buscarHistorialMedicoPorSintomas(String sintoma);

    // Búsquedas por tratamiento
    List<HistorialMedico> buscarHistorialMedicoPorTratamiento(String tratamiento);

    // Búsquedas por medicamento
    List<HistorialMedico> buscarHistorialMedicoPorMedicamento(String medicamento);
    
    List<HistorialMedico> buscarHistorialMedicoPacientePorMedicamento(Integer idPaciente, String medicamento);

    // Seguimientos
    List<HistorialMedico> obtenerHistorialMedicoConSeguimiento();
    
    List<HistorialMedico> obtenerHistorialMedicoConSeguimientoPorPaciente(Integer idPaciente);
    
    List<HistorialMedico> obtenerHistorialMedicoConSeguimientoPendiente();
    
    List<HistorialMedico> obtenerHistorialMedicoConSeguimientoPendientePorPaciente(Integer idPaciente);
    
    List<HistorialMedico> obtenerHistorialMedicoConSeguimientoVencido();

    // Búsquedas por rango de fechas
    List<HistorialMedico> obtenerHistorialMedicoPorPacienteEnRangoFechas(
            Integer idPaciente, 
            LocalDate fechaInicio, 
            LocalDate fechaFin
    );
    
    List<HistorialMedico> obtenerHistorialMedicoPorMedicoEnRangoFechas(
            Integer idMedico, 
            LocalDate fechaInicio, 
            LocalDate fechaFin
    );

    // Conteo de registros
    Long contarHistorialMedicoPorPaciente(Integer idPaciente);
    
    Long contarHistorialMedicoPorMedico(Integer idMedico);

    // Último historial
    Optional<HistorialMedico> obtenerUltimoHistorialMedicoPorPaciente(Integer idPaciente);

    // Historial con observaciones
    List<HistorialMedico> obtenerHistorialMedicoConObservaciones();

    // Historial reciente
    List<HistorialMedico> obtenerHistorialMedicoRecientePorPaciente(Integer idPaciente, int limite);

    // Validaciones
    void validarHistorialMedico(HistorialMedico historialMedico);
}
