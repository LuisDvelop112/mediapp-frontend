package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.Cita;
import com.mediapp.citasbackend.entities.HistorialMedico;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Paciente;
import com.mediapp.citasbackend.repositories.HistorialMedicoRepository;
import com.mediapp.citasbackend.services.interfaces.HistorialMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HistorialMedicoServiceImpl implements HistorialMedicoService {

    private final HistorialMedicoRepository historialMedicoRepository;

    @Override
    public HistorialMedico guardarHistorialMedico(HistorialMedico historialMedico) {
        validarHistorialMedico(historialMedico);
        
        // Verificar que no exista ya un historial para esta cita
        if (historialMedicoRepository.existsByCita_IdCita(historialMedico.getCita().getIdCita())) {
            throw new IllegalArgumentException(
                "Ya existe un historial médico para la cita con ID: " + 
                historialMedico.getCita().getIdCita()
            );
        }
        
        return historialMedicoRepository.save(historialMedico);
    }

    @Override
    public HistorialMedico actualizarHistorialMedico(Integer id, HistorialMedico historialMedico) {
        HistorialMedico historialExistente = historialMedicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Historial médico no encontrado con ID: " + id));

        validarHistorialMedico(historialMedico);

        // Verificar que no se esté intentando cambiar la cita a una que ya tiene historial
        if (!historialExistente.getCita().getIdCita().equals(historialMedico.getCita().getIdCita())) {
            if (historialMedicoRepository.existsByCita_IdCita(historialMedico.getCita().getIdCita())) {
                throw new IllegalArgumentException(
                    "Ya existe un historial médico para la cita con ID: " + 
                    historialMedico.getCita().getIdCita()
                );
            }
        }

        // Actualizar campos
        historialExistente.setCita(historialMedico.getCita());
        historialExistente.setPaciente(historialMedico.getPaciente());
        historialExistente.setMedico(historialMedico.getMedico());
        historialExistente.setDiagnostico(historialMedico.getDiagnostico());
        historialExistente.setSintomasReportados(historialMedico.getSintomasReportados());
        historialExistente.setTratamientoRecomendado(historialMedico.getTratamientoRecomendado());
        historialExistente.setMedicamentosPrescritos(historialMedico.getMedicamentosPrescritos());
        historialExistente.setObservaciones(historialMedico.getObservaciones());
        historialExistente.setFechaProximoSeguimiento(historialMedico.getFechaProximoSeguimiento());

        return historialMedicoRepository.save(historialExistente);
    }

    @Override
    public void eliminarHistorialMedico(Integer id) {
        if (!historialMedicoRepository.existsById(id)) {
            throw new IllegalArgumentException("Historial médico no encontrado con ID: " + id);
        }
        historialMedicoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistorialMedico> obtenerHistorialMedicoPorId(Integer id) {
        return historialMedicoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerTodosLosHistorialesMedicos() {
        return historialMedicoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistorialMedico> obtenerHistorialMedicoPorCita(Cita cita) {
        if (cita == null) {
            throw new IllegalArgumentException("La cita no puede ser nula");
        }
        return historialMedicoRepository.findByCita(cita);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistorialMedico> obtenerHistorialMedicoPorCitaId(Integer idCita) {
        if (idCita == null) {
            throw new IllegalArgumentException("El ID de la cita no puede ser nulo");
        }
        return historialMedicoRepository.findByCita_IdCita(idCita);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeHistorialMedicoPorCitaId(Integer idCita) {
        if (idCita == null) {
            throw new IllegalArgumentException("El ID de la cita no puede ser nulo");
        }
        return historialMedicoRepository.existsByCita_IdCita(idCita);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoPorPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("El paciente no puede ser nulo");
        }
        return historialMedicoRepository.findByPaciente(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoPorPacienteId(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return historialMedicoRepository.findByPaciente_IdPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoPorPacienteOrdenado(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return historialMedicoRepository.findHistorialByPacienteOrdenado(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoPorMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("El médico no puede ser nulo");
        }
        return historialMedicoRepository.findByMedico(medico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoPorMedicoId(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return historialMedicoRepository.findByMedico_IdMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoPorMedicoOrdenado(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return historialMedicoRepository.findHistorialByMedicoOrdenado(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoEntrePacienteYMedico(Integer idPaciente, Integer idMedico) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return historialMedicoRepository.findHistorialByPacienteAndMedico(idPaciente, idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> buscarHistorialMedicoPorDiagnostico(String diagnostico) {
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        return historialMedicoRepository.buscarPorDiagnostico(diagnostico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> buscarHistorialMedicoPacientePorDiagnostico(Integer idPaciente, String diagnostico) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        return historialMedicoRepository.buscarHistorialPacientePorDiagnostico(idPaciente, diagnostico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> buscarHistorialMedicoPorSintomas(String sintoma) {
        if (sintoma == null || sintoma.trim().isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        return historialMedicoRepository.buscarPorSintomas(sintoma);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> buscarHistorialMedicoPorTratamiento(String tratamiento) {
        if (tratamiento == null || tratamiento.trim().isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        return historialMedicoRepository.buscarPorTratamiento(tratamiento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> buscarHistorialMedicoPorMedicamento(String medicamento) {
        if (medicamento == null || medicamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        return historialMedicoRepository.buscarPorMedicamento(medicamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> buscarHistorialMedicoPacientePorMedicamento(Integer idPaciente, String medicamento) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (medicamento == null || medicamento.trim().isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }
        return historialMedicoRepository.buscarHistorialPacientePorMedicamento(idPaciente, medicamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoConSeguimiento() {
        return historialMedicoRepository.findHistorialConSeguimiento();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoConSeguimientoPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return historialMedicoRepository.findHistorialConSeguimientoByPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoConSeguimientoPendiente() {
        return historialMedicoRepository.findHistorialConSeguimientoPendiente(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoConSeguimientoPendientePorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return historialMedicoRepository.findHistorialConSeguimientoPendienteByPaciente(
                idPaciente, 
                LocalDate.now()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoConSeguimientoVencido() {
        return historialMedicoRepository.findHistorialConSeguimientoVencido(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoPorPacienteEnRangoFechas(
            Integer idPaciente, 
            LocalDate fechaInicio, 
            LocalDate fechaFin) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return historialMedicoRepository.findHistorialByPacienteEnRangoFechas(
                idPaciente, 
                fechaInicio, 
                fechaFin
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoPorMedicoEnRangoFechas(
            Integer idMedico, 
            LocalDate fechaInicio, 
            LocalDate fechaFin) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return historialMedicoRepository.findHistorialByMedicoEnRangoFechas(
                idMedico, 
                fechaInicio, 
                fechaFin
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarHistorialMedicoPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        return historialMedicoRepository.contarHistorialByPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarHistorialMedicoPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return historialMedicoRepository.contarHistorialByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistorialMedico> obtenerUltimoHistorialMedicoPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        List<HistorialMedico> historiales = historialMedicoRepository.findUltimoHistorialByPaciente(idPaciente);
        return historiales.isEmpty() ? Optional.empty() : Optional.of(historiales.get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoConObservaciones() {
        return historialMedicoRepository.findHistorialConObservaciones();
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMedico> obtenerHistorialMedicoRecientePorPaciente(Integer idPaciente, int limite) {
        if (idPaciente == null) {
            throw new IllegalArgumentException("El ID del paciente no puede ser nulo");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor a cero");
        }
        
        List<HistorialMedico> historiales = historialMedicoRepository.findHistorialRecienteByPaciente(idPaciente);
        
        // Limitar los resultados
        if (historiales.size() > limite) {
            return historiales.subList(0, limite);
        }
        
        return historiales;
    }

    @Override
    public void validarHistorialMedico(HistorialMedico historialMedico) {
        if (historialMedico == null) {
            throw new IllegalArgumentException("El historial médico no puede ser nulo");
        }

        if (historialMedico.getCita() == null) {
            throw new IllegalArgumentException("La cita es obligatoria");
        }

        if (historialMedico.getPaciente() == null) {
            throw new IllegalArgumentException("El paciente es obligatorio");
        }

        if (historialMedico.getMedico() == null) {
            throw new IllegalArgumentException("El médico es obligatorio");
        }

        // Validar que la fecha de próximo seguimiento sea futura (si se especifica)
        if (historialMedico.getFechaProximoSeguimiento() != null) {
            if (historialMedico.getFechaProximoSeguimiento().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha de próximo seguimiento debe ser una fecha futura");
            }
        }

        // Validar longitud de los campos de texto (opcional, para evitar textos excesivamente largos)
        if (historialMedico.getDiagnostico() != null && historialMedico.getDiagnostico().length() > 10000) {
            throw new IllegalArgumentException("El diagnóstico no puede exceder 10000 caracteres");
        }

        if (historialMedico.getSintomasReportados() != null && historialMedico.getSintomasReportados().length() > 10000) {
            throw new IllegalArgumentException("Los síntomas reportados no pueden exceder 10000 caracteres");
        }

        if (historialMedico.getTratamientoRecomendado() != null && historialMedico.getTratamientoRecomendado().length() > 10000) {
            throw new IllegalArgumentException("El tratamiento recomendado no puede exceder 10000 caracteres");
        }

        if (historialMedico.getMedicamentosPrescritos() != null && historialMedico.getMedicamentosPrescritos().length() > 10000) {
            throw new IllegalArgumentException("Los medicamentos prescritos no pueden exceder 10000 caracteres");
        }

        if (historialMedico.getObservaciones() != null && historialMedico.getObservaciones().length() > 10000) {
            throw new IllegalArgumentException("Las observaciones no pueden exceder 10000 caracteres");
        }
    }
}
