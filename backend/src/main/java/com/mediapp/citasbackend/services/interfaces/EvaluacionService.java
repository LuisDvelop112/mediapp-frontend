package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.Evaluacion;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Paciente;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EvaluacionService {

    // Operaciones CRUD básicas
    Evaluacion guardarEvaluacion(Evaluacion evaluacion);
    
    Evaluacion actualizarEvaluacion(Integer id, Evaluacion evaluacion);
    
    void eliminarEvaluacion(Integer id);
    
    Optional<Evaluacion> obtenerEvaluacionPorId(Integer id);
    
    List<Evaluacion> obtenerTodasLasEvaluaciones();

    // Búsquedas por paciente
    List<Evaluacion> obtenerEvaluacionesPorPaciente(Paciente paciente);
    
    List<Evaluacion> obtenerEvaluacionesPorPacienteId(Integer idPaciente);
    
    List<Evaluacion> obtenerEvaluacionesPorPacienteOrdenadas(Integer idPaciente);

    // Búsquedas por médico
    List<Evaluacion> obtenerEvaluacionesPorMedico(Medico medico);
    
    List<Evaluacion> obtenerEvaluacionesPorMedicoId(Integer idMedico);
    
    List<Evaluacion> obtenerEvaluacionesPorMedicoOrdenadas(Integer idMedico);

    // Búsquedas por calificación
    List<Evaluacion> obtenerEvaluacionesPorCalificacion(Integer calificacion);
    
    List<Evaluacion> obtenerEvaluacionesPorMedicoYCalificacion(Integer idMedico, Integer calificacion);
    
    List<Evaluacion> obtenerEvaluacionesConCalificacionMinima(Integer minCalificacion);
    
    List<Evaluacion> obtenerEvaluacionesPorMedicoConCalificacionMinima(Integer idMedico, Integer minCalificacion);

    // Cálculos estadísticos
    Double calcularPromedioCalificacionesPorMedico(Integer idMedico);
    
    Long contarEvaluacionesPorMedico(Integer idMedico);
    
    Long contarEvaluacionesPorPaciente(Integer idPaciente);

    // Búsquedas por rango de fechas
    List<Evaluacion> obtenerEvaluacionesEnRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Evaluacion> obtenerEvaluacionesPorMedicoEnRangoFechas(
            Integer idMedico, 
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin
    );

    // Búsquedas por respuesta del médico
    List<Evaluacion> obtenerEvaluacionesConRespuesta();
    
    List<Evaluacion> obtenerEvaluacionesSinRespuesta();
    
    List<Evaluacion> obtenerEvaluacionesSinRespuestaPorMedico(Integer idMedico);

    // Búsquedas por comentario
    List<Evaluacion> obtenerEvaluacionesConComentario();

    // Evaluaciones recientes
    List<Evaluacion> obtenerUltimasEvaluacionesPorMedico(Integer idMedico);
    
    List<Evaluacion> obtenerEvaluacionesRecientes(int dias);

    // Distribución de calificaciones
    Map<Integer, Long> obtenerDistribucionCalificacionesPorMedico(Integer idMedico);

    // Mejores evaluaciones
    List<Evaluacion> obtenerMejoresEvaluaciones();
    
    List<Evaluacion> obtenerMejoresEvaluacionesPorMedico(Integer idMedico);

    // Verificación de existencia
    boolean existeEvaluacionEntrePacienteYMedico(Integer idPaciente, Integer idMedico);
    
    List<Evaluacion> obtenerEvaluacionesEntrePacienteYMedico(Integer idPaciente, Integer idMedico);

    // Respuesta del médico
    Evaluacion agregarRespuestaMedico(Integer id, String respuesta);

    // Validaciones
    void validarEvaluacion(Evaluacion evaluacion);
    
    void validarCalificacion(Integer calificacion);
}
