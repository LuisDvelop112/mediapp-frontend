package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.HistorialMedico;
import com.mediapp.citasbackend.entities.Prescripcion;
import com.mediapp.citasbackend.repositories.PrescripcionRepository;
import com.mediapp.citasbackend.services.interfaces.PrescripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescripcionServiceImpl implements PrescripcionService {

    private final PrescripcionRepository prescripcionRepository;

    @Override
    public Prescripcion guardarPrescripcion(Prescripcion prescripcion) {
        validarPrescripcion(prescripcion);
        
        // Si no se especifica fecha de prescripción, se usará la actual (PrePersist)
        if (prescripcion.getFechaPrescripcion() == null) {
            prescripcion.setFechaPrescripcion(LocalDate.now());
        }
        
        return prescripcionRepository.save(prescripcion);
    }

    @Override
    public Prescripcion actualizarPrescripcion(Integer id, Prescripcion prescripcion) {
        Prescripcion prescripcionExistente = prescripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prescripción no encontrada con ID: " + id));

        validarPrescripcion(prescripcion);

        // Actualizar campos
        prescripcionExistente.setHistorialMedico(prescripcion.getHistorialMedico());
        prescripcionExistente.setNombreMedicamento(prescripcion.getNombreMedicamento());
        prescripcionExistente.setDosis(prescripcion.getDosis());
        prescripcionExistente.setFrecuencia(prescripcion.getFrecuencia());
        prescripcionExistente.setDuracionDias(prescripcion.getDuracionDias());
        prescripcionExistente.setInstrucciones(prescripcion.getInstrucciones());
        prescripcionExistente.setFechaPrescripcion(prescripcion.getFechaPrescripcion());

        return prescripcionRepository.save(prescripcionExistente);
    }

    @Override
    public void eliminarPrescripcion(Integer id) {
        if (!prescripcionRepository.existsById(id)) {
            throw new IllegalArgumentException("Prescripción no encontrada con ID: " + id);
        }
        prescripcionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Prescripcion> obtenerPrescripcionPorId(Integer id) {
        return prescripcionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerTodasLasPrescripciones() {
        return prescripcionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerTodasLasPrescripcionesOrdenadas() {
        return prescripcionRepository.findAllOrdenadas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorHistorial(HistorialMedico historialMedico) {
        if (historialMedico == null) {
            throw new IllegalArgumentException("El historial médico no puede ser nulo");
        }
        return prescripcionRepository.findByHistorialMedico(historialMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorHistorialId(Integer idHistorial) {
        if (idHistorial == null) {
            throw new IllegalArgumentException("El ID del historial no puede ser nulo");
        }
        return prescripcionRepository.findByHistorialMedico_IdHistorial(idHistorial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return prescripcionRepository.findPrescripcionesByPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerUltimasPrescripcionesPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return prescripcionRepository.findUltimasPrescripcionesByPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesRecientesPorPaciente(Integer idPaciente, Integer dias) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (dias == null || dias < 0) {
            throw new IllegalArgumentException("El número de días debe ser un valor positivo");
        }
        
        LocalDate fechaLimite = LocalDate.now().minusDays(dias);
        return prescripcionRepository.findPrescripcionesRecientesByPaciente(idPaciente, fechaLimite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesActivasPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return prescripcionRepository.findPrescripcionesActivasByPaciente(idPaciente, LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return prescripcionRepository.findPrescripcionesByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorPacienteYMedico(Integer idPaciente, Integer idMedico) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return prescripcionRepository.findPrescripcionesByPacienteAndMedico(idPaciente, idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorCita(Integer idCita) {
        if (idCita == null) {
            throw new IllegalArgumentException("El ID de la cita no puede ser nulo");
        }
        return prescripcionRepository.findPrescripcionesByCita(idCita);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorNombreMedicamento(String nombreMedicamento) {
        if (nombreMedicamento == null || nombreMedicamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del medicamento no puede estar vacío");
        }
        return prescripcionRepository.findByNombreMedicamento(nombreMedicamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> buscarPrescripcionesPorMedicamento(String medicamento) {
        if (medicamento == null || medicamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        return prescripcionRepository.buscarPorMedicamento(medicamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> buscarPrescripcionesPacientePorMedicamento(Integer idPaciente, String medicamento) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (medicamento == null || medicamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        return prescripcionRepository.buscarPrescripcionesPacientePorMedicamento(idPaciente, medicamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        return prescripcionRepository.findByFechaPrescripcion(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesDelDia() {
        return prescripcionRepository.findPrescripcionesDelDia();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesEnRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return prescripcionRepository.findPrescripcionesEnRangoFechas(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorPacienteEnRangoFechas(
            Integer idPaciente, LocalDate fechaInicio, LocalDate fechaFin) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return prescripcionRepository.findPrescripcionesByPacienteEnRangoFechas(idPaciente, fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorMedicoEnRangoFechas(
            Integer idMedico, LocalDate fechaInicio, LocalDate fechaFin) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return prescripcionRepository.findPrescripcionesByMedicoEnRangoFechas(idMedico, fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorDuracion(Integer duracionDias) {
        if (duracionDias == null || duracionDias < 0) {
            throw new IllegalArgumentException("La duración en días debe ser un valor no negativo");
        }
        return prescripcionRepository.findByDuracionDias(duracionDias);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesConDuracionMinima(Integer diasMinimos) {
        if (diasMinimos == null || diasMinimos < 0) {
            throw new IllegalArgumentException("Los días mínimos deben ser un valor no negativo");
        }
        return prescripcionRepository.findPrescripcionesConDuracionMinima(diasMinimos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesSinDuracion() {
        return prescripcionRepository.findPrescripcionesSinDuracion();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesTratamientosLargos() {
        return prescripcionRepository.findPrescripcionesTratamientosLargos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorDosis(String dosis) {
        if (dosis == null || dosis.trim().isEmpty()) {
            throw new IllegalArgumentException("La dosis no puede estar vacía");
        }
        return prescripcionRepository.findByDosis(dosis);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesPorFrecuencia(String frecuencia) {
        if (frecuencia == null || frecuencia.trim().isEmpty()) {
            throw new IllegalArgumentException("La frecuencia no puede estar vacía");
        }
        return prescripcionRepository.findByFrecuencia(frecuencia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesConInstrucciones() {
        return prescripcionRepository.findPrescripcionesConInstrucciones();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescripcion> obtenerPrescripcionesConInstruccionesPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return prescripcionRepository.findPrescripcionesConInstruccionesByPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPrescripcionesPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return prescripcionRepository.contarPrescripcionesByPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPrescripcionesPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return prescripcionRepository.contarPrescripcionesByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPrescripcionesPorMedicamento(String medicamento) {
        if (medicamento == null || medicamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del medicamento no puede estar vacío");
        }
        return prescripcionRepository.contarPrescripcionesPorMedicamento(medicamento);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPrescripciones() {
        return prescripcionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> obtenerMedicamentosMasPrescritos() {
        List<Object[]> resultados = prescripcionRepository.findMedicamentosMasPrescritos();
        
        Map<String, Long> medicamentosMap = new LinkedHashMap<>();
        for (Object[] resultado : resultados) {
            String medicamento = (String) resultado[0];
            Long total = ((Number) resultado[1]).longValue();
            medicamentosMap.put(medicamento, total);
        }
        
        return medicamentosMap;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> obtenerMedicamentosMasPrescritosPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        
        List<Object[]> resultados = prescripcionRepository.findMedicamentosMasPrescritosByMedico(idMedico);
        
        Map<String, Long> medicamentosMap = new LinkedHashMap<>();
        for (Object[] resultado : resultados) {
            String medicamento = (String) resultado[0];
            Long total = ((Number) resultado[1]).longValue();
            medicamentosMap.put(medicamento, total);
        }
        
        return medicamentosMap;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean pacienteTienePrescripcionDeMedicamento(Integer idPaciente, String medicamento) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (medicamento == null || medicamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del medicamento no puede estar vacío");
        }
        
        Long count = prescripcionRepository.verificarPrescripcionMedicamento(idPaciente, medicamento);
        return count > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean prescripcionEstaActiva(Integer idPrescripcion) {
        if (idPrescripcion == null) {
            throw new IllegalArgumentException("El ID de la prescripción no puede ser nulo");
        }
        
        Optional<Prescripcion> prescripcionOpt = prescripcionRepository.findById(idPrescripcion);
        if (prescripcionOpt.isEmpty()) {
            return false;
        }
        
        Prescripcion prescripcion = prescripcionOpt.get();
        
        // Si no tiene duración definida, no podemos determinar si está activa
        if (prescripcion.getDuracionDias() == null) {
            return false;
        }
        
        LocalDate fechaFin = calcularFechaFinTratamiento(prescripcion);
        return !LocalDate.now().isAfter(fechaFin);
    }

    @Override
    public void validarPrescripcion(Prescripcion prescripcion) {
        if (prescripcion == null) {
            throw new IllegalArgumentException("La prescripción no puede ser nula");
        }

        if (prescripcion.getHistorialMedico() == null) {
            throw new IllegalArgumentException("El historial médico es obligatorio");
        }

        if (prescripcion.getNombreMedicamento() == null || prescripcion.getNombreMedicamento().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del medicamento es obligatorio");
        }

        if (prescripcion.getNombreMedicamento().length() > 100) {
            throw new IllegalArgumentException("El nombre del medicamento no puede exceder 100 caracteres");
        }

        // Validar dosis (si se proporciona)
        if (prescripcion.getDosis() != null && prescripcion.getDosis().length() > 50) {
            throw new IllegalArgumentException("La dosis no puede exceder 50 caracteres");
        }

        // Validar frecuencia (si se proporciona)
        if (prescripcion.getFrecuencia() != null && prescripcion.getFrecuencia().length() > 50) {
            throw new IllegalArgumentException("La frecuencia no puede exceder 50 caracteres");
        }

        // Validar duración (si se proporciona)
        if (prescripcion.getDuracionDias() != null) {
            if (prescripcion.getDuracionDias() < 0) {
                throw new IllegalArgumentException("La duración en días no puede ser negativa");
            }
            if (prescripcion.getDuracionDias() > 3650) { // Máximo 10 años
                throw new IllegalArgumentException("La duración en días no puede exceder 3650 días (10 años)");
            }
        }

        // Validar instrucciones (si se proporciona)
        if (prescripcion.getInstrucciones() != null && prescripcion.getInstrucciones().length() > 10000) {
            throw new IllegalArgumentException("Las instrucciones no pueden exceder 10000 caracteres");
        }

        // Validar fecha de prescripción
        if (prescripcion.getFechaPrescripcion() == null) {
            prescripcion.setFechaPrescripcion(LocalDate.now());
        }

        // La fecha de prescripción no puede ser futura
        if (prescripcion.getFechaPrescripcion().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de prescripción no puede ser una fecha futura");
        }

        // La fecha de prescripción no puede ser muy antigua (más de 10 años)
        if (prescripcion.getFechaPrescripcion().isBefore(LocalDate.now().minusYears(10))) {
            throw new IllegalArgumentException("La fecha de prescripción no puede ser anterior a 10 años");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public LocalDate calcularFechaFinTratamiento(Prescripcion prescripcion) {
        if (prescripcion == null) {
            throw new IllegalArgumentException("La prescripción no puede ser nula");
        }
        
        if (prescripcion.getFechaPrescripcion() == null) {
            throw new IllegalArgumentException("La prescripción no tiene fecha de prescripción");
        }
        
        if (prescripcion.getDuracionDias() == null) {
            throw new IllegalArgumentException("La prescripción no tiene duración definida");
        }
        
        return prescripcion.getFechaPrescripcion().plusDays(prescripcion.getDuracionDias());
    }
}
