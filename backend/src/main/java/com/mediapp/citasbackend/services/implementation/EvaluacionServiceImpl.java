package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.Evaluacion;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Paciente;
import com.mediapp.citasbackend.repositories.EvaluacionRepository;
import com.mediapp.citasbackend.services.interfaces.EvaluacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;

    @Override
    public Evaluacion guardarEvaluacion(Evaluacion evaluacion) {
        validarEvaluacion(evaluacion);
        validarCalificacion(evaluacion.getCalificacion());
        
        return evaluacionRepository.save(evaluacion);
    }

    @Override
    public Evaluacion actualizarEvaluacion(Integer id, Evaluacion evaluacion) {
        Evaluacion evaluacionExistente = evaluacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada con ID: " + id));

        validarEvaluacion(evaluacion);
        validarCalificacion(evaluacion.getCalificacion());

        // Actualizar campos
        evaluacionExistente.setPaciente(evaluacion.getPaciente());
        evaluacionExistente.setMedico(evaluacion.getMedico());
        evaluacionExistente.setCalificacion(evaluacion.getCalificacion());
        evaluacionExistente.setComentario(evaluacion.getComentario());
        evaluacionExistente.setRespuestaMedico(evaluacion.getRespuestaMedico());

        return evaluacionRepository.save(evaluacionExistente);
    }

    @Override
    public void eliminarEvaluacion(Integer id) {
        if (!evaluacionRepository.existsById(id)) {
            throw new IllegalArgumentException("Evaluación no encontrada con ID: " + id);
        }
        evaluacionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Evaluacion> obtenerEvaluacionPorId(Integer id) {
        return evaluacionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerTodasLasEvaluaciones() {
        return evaluacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo");
        }
        return evaluacionRepository.findByPaciente(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorPacienteId(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return evaluacionRepository.findByPaciente_IdPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorPacienteOrdenadas(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return evaluacionRepository.findEvaluacionesByPacienteOrdenadas(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("El médico no puede ser nulo");
        }
        return evaluacionRepository.findByMedico(medico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorMedicoId(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return evaluacionRepository.findByMedico_IdMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorMedicoOrdenadas(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return evaluacionRepository.findEvaluacionesByMedicoOrdenadas(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorCalificacion(Integer calificacion) {
        validarCalificacion(calificacion);
        return evaluacionRepository.findByCalificacion(calificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorMedicoYCalificacion(Integer idMedico, Integer calificacion) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        validarCalificacion(calificacion);
        return evaluacionRepository.findEvaluacionesByMedicoAndCalificacion(idMedico, calificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesConCalificacionMinima(Integer minCalificacion) {
        validarCalificacion(minCalificacion);
        return evaluacionRepository.findEvaluacionesConCalificacionMinima(minCalificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorMedicoConCalificacionMinima(Integer idMedico, Integer minCalificacion) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        validarCalificacion(minCalificacion);
        return evaluacionRepository.findEvaluacionesByMedicoConCalificacionMinima(idMedico, minCalificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularPromedioCalificacionesPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        Double promedio = evaluacionRepository.calcularPromedioCalificacionesByMedico(idMedico);
        return promedio != null ? promedio : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarEvaluacionesPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return evaluacionRepository.contarEvaluacionesByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarEvaluacionesPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return evaluacionRepository.contarEvaluacionesByPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesEnRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return evaluacionRepository.findEvaluacionesEnRangoFechas(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesPorMedicoEnRangoFechas(
            Integer idMedico, 
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return evaluacionRepository.findEvaluacionesByMedicoEnRangoFechas(idMedico, fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesConRespuesta() {
        return evaluacionRepository.findEvaluacionesConRespuesta();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesSinRespuesta() {
        return evaluacionRepository.findEvaluacionesSinRespuesta();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesSinRespuestaPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return evaluacionRepository.findEvaluacionesSinRespuestaByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesConComentario() {
        return evaluacionRepository.findEvaluacionesConComentario();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerUltimasEvaluacionesPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return evaluacionRepository.findUltimasEvaluacionesByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesRecientes(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("El número de días debe ser mayor a cero");
        }
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        return evaluacionRepository.findEvaluacionesRecientes(fechaLimite);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Integer, Long> obtenerDistribucionCalificacionesPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        
        List<Object[]> resultados = evaluacionRepository.contarEvaluacionesPorCalificacionByMedico(idMedico);
        Map<Integer, Long> distribucion = new HashMap<>();
        
        // Inicializar todas las calificaciones con 0
        for (int i = 1; i <= 5; i++) {
            distribucion.put(i, 0L);
        }
        
        // Llenar con los datos reales
        for (Object[] resultado : resultados) {
            Integer calificacion = (Integer) resultado[0];
            Long count = (Long) resultado[1];
            distribucion.put(calificacion, count);
        }
        
        return distribucion;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerMejoresEvaluaciones() {
        return evaluacionRepository.findMejoresEvaluaciones();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerMejoresEvaluacionesPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return evaluacionRepository.findMejoresEvaluacionesByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEvaluacionEntrePacienteYMedico(Integer idPaciente, Integer idMedico) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return evaluacionRepository.existsByPaciente_IdPacienteAndMedico_IdMedico(idPaciente, idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluacion> obtenerEvaluacionesEntrePacienteYMedico(Integer idPaciente, Integer idMedico) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return evaluacionRepository.findEvaluacionesByPacienteAndMedico(idPaciente, idMedico);
    }

    @Override
    public Evaluacion agregarRespuestaMedico(Integer id, String respuesta) {
        Evaluacion evaluacion = evaluacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación no encontrada con ID: " + id));
        
        if (respuesta == null || respuesta.trim().isEmpty()) {
            throw new IllegalArgumentException("La respuesta del médico no puede estar vacía");
        }
        
        evaluacion.setRespuestaMedico(respuesta);
        return evaluacionRepository.save(evaluacion);
    }

    @Override
    public void validarEvaluacion(Evaluacion evaluacion) {
        if (evaluacion == null) {
            throw new IllegalArgumentException("La evaluación no puede ser nula");
        }

        if (evaluacion.getPaciente() == null) {
            throw new IllegalArgumentException("El paciente es obligatorio");
        }

        if (evaluacion.getMedico() == null) {
            throw new IllegalArgumentException("El médico es obligatorio");
        }

        if (evaluacion.getCalificacion() == null) {
            throw new IllegalArgumentException("La calificación es obligatoria");
        }

        // Validar que el comentario no sea excesivamente largo (si existe)
        if (evaluacion.getComentario() != null && evaluacion.getComentario().length() > 5000) {
            throw new IllegalArgumentException("El comentario no puede exceder 5000 caracteres");
        }

        // Validar que la respuesta del médico no sea excesivamente larga (si existe)
        if (evaluacion.getRespuestaMedico() != null && evaluacion.getRespuestaMedico().length() > 5000) {
            throw new IllegalArgumentException("La respuesta del médico no puede exceder 5000 caracteres");
        }
    }

    @Override
    public void validarCalificacion(Integer calificacion) {
        if (calificacion == null) {
            throw new IllegalArgumentException("La calificación no puede ser nula");
        }

        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
    }
}
