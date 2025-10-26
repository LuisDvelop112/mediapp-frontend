package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.Cita;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Paciente;
import com.mediapp.citasbackend.exceptions.BusinessRuleException;
import com.mediapp.citasbackend.exceptions.InvalidDataException;
import com.mediapp.citasbackend.exceptions.ResourceNotFoundException;
import com.mediapp.citasbackend.repositories.CitaRepository;
import com.mediapp.citasbackend.services.interfaces.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;

    @Override
    public Cita guardarCita(Cita cita) {
        validarCita(cita);
        validarFechaCita(cita.getFechaCita(), cita.getHoraCita());
        
        // Verificar que el médico esté disponible en esa fecha y hora
        if (!medicoDisponibleEnFechaHora(
                cita.getMedico().getIdMedico(),
                cita.getFechaCita(),
                cita.getHoraCita()
        )) {
            throw new BusinessRuleException("El médico ya tiene una cita programada en esa fecha y hora");
        }
        
        return citaRepository.save(cita);
    }

    @Override
    public Cita actualizarCita(Integer id, Cita cita) {
        Cita citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", "ID", id));

        validarCita(cita);
        validarFechaCita(cita.getFechaCita(), cita.getHoraCita());

        // Verificar disponibilidad del médico si se cambió fecha/hora/médico
        boolean cambioHorario = !citaExistente.getFechaCita().equals(cita.getFechaCita()) ||
                               !citaExistente.getHoraCita().equals(cita.getHoraCita()) ||
                               !citaExistente.getMedico().getIdMedico().equals(cita.getMedico().getIdMedico());

        if (cambioHorario) {
            Optional<Cita> citaConflicto = verificarDisponibilidadMedicoFechaHora(
                    cita.getMedico().getIdMedico(),
                    cita.getFechaCita(),
                    cita.getHoraCita()
            );

            // Verificar que el conflicto no sea con la misma cita que estamos actualizando
            if (citaConflicto.isPresent() && !citaConflicto.get().getIdCita().equals(id)) {
                throw new BusinessRuleException("El médico ya tiene una cita programada en esa fecha y hora");
            }
        }

        // Actualizar campos
        citaExistente.setPaciente(cita.getPaciente());
        citaExistente.setMedico(cita.getMedico());
        citaExistente.setFechaCita(cita.getFechaCita());
        citaExistente.setHoraCita(cita.getHoraCita());
        citaExistente.setTipoCita(cita.getTipoCita());
        citaExistente.setMotivoConsulta(cita.getMotivoConsulta());
        citaExistente.setEstado(cita.getEstado());
        citaExistente.setEnlaceVideollamada(cita.getEnlaceVideollamada());

        return citaRepository.save(citaExistente);
    }

    @Override
    public void eliminarCita(Integer id) {
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita", "ID", id);
        }
        citaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cita> obtenerCitaPorId(Integer id) {
        return citaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerTodasLasCitas() {
        return citaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new InvalidDataException("El paciente no puede ser nulo");
        }
        return citaRepository.findByPaciente(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorPacienteId(Integer idPaciente) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        return citaRepository.findByPaciente_IdPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorPacienteYEstado(Integer idPaciente, Cita.Estado estado) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        if (estado == null) {
            throw new InvalidDataException("El estado no puede ser nulo");
        }
        return citaRepository.findByPaciente_IdPacienteAndEstado(idPaciente, estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasProgramadasPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        return citaRepository.findCitasProgramadasByPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorMedico(Medico medico) {
        if (medico == null) {
            throw new InvalidDataException("El médico no puede ser nulo");
        }
        return citaRepository.findByMedico(medico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorMedicoId(Integer idMedico) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        return citaRepository.findByMedico_IdMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorMedicoYEstado(Integer idMedico, Cita.Estado estado) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        if (estado == null) {
            throw new InvalidDataException("El estado no puede ser nulo");
        }
        return citaRepository.findByMedico_IdMedicoAndEstado(idMedico, estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasProgramadasPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        return citaRepository.findCitasProgramadasByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorEstado(Cita.Estado estado) {
        if (estado == null) {
            throw new InvalidDataException("El estado no puede ser nulo");
        }
        return citaRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new InvalidDataException("La fecha no puede ser nula");
        }
        return citaRepository.findByFechaCita(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorMedicoYFecha(Integer idMedico, LocalDate fecha) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        if (fecha == null) {
            throw new InvalidDataException("La fecha no puede ser nula");
        }
        return citaRepository.findCitasByMedicoAndFecha(idMedico, fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorPacienteYFecha(Integer idPaciente, LocalDate fecha) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        if (fecha == null) {
            throw new InvalidDataException("La fecha no puede ser nula");
        }
        return citaRepository.findCitasByPacienteAndFecha(idPaciente, fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasEnRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new InvalidDataException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return citaRepository.findCitasEnRangoFechas(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorMedicoEnRangoFechas(Integer idMedico, LocalDate fechaInicio, LocalDate fechaFin) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new InvalidDataException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return citaRepository.findCitasByMedicoEnRangoFechas(idMedico, fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorPacienteEnRangoFechas(Integer idPaciente, LocalDate fechaInicio, LocalDate fechaFin) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new InvalidDataException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return citaRepository.findCitasByPacienteEnRangoFechas(idPaciente, fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cita> verificarDisponibilidadMedicoFechaHora(Integer idMedico, LocalDate fecha, LocalTime hora) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        if (fecha == null) {
            throw new InvalidDataException("La fecha no puede ser nula");
        }
        if (hora == null) {
            throw new InvalidDataException("La hora no puede ser nula");
        }
        return citaRepository.findCitaByMedicoFechaHora(idMedico, fecha, hora);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean medicoDisponibleEnFechaHora(Integer idMedico, LocalDate fecha, LocalTime hora) {
        Optional<Cita> cita = verificarDisponibilidadMedicoFechaHora(idMedico, fecha, hora);
        return cita.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasPorTipo(Cita.TipoCita tipoCita) {
        if (tipoCita == null) {
            throw new InvalidDataException("El tipo de cita no puede ser nulo");
        }
        return citaRepository.findByTipoCita(tipoCita);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasTelemedicinaProgramadasPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        return citaRepository.findCitasTelemedicinaProgramadasByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerProximasCitasPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        return citaRepository.findProximasCitasByPaciente(idPaciente, LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerProximasCitasPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        return citaRepository.findProximasCitasByMedico(idMedico, LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerHistorialCitasPorPaciente(Integer idPaciente) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        return citaRepository.findHistorialCitasByPaciente(idPaciente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerHistorialCitasPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        return citaRepository.findHistorialCitasByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarCitasPorEstado(Cita.Estado estado) {
        if (estado == null) {
            throw new InvalidDataException("El estado no puede ser nulo");
        }
        return citaRepository.contarCitasPorEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarCitasPorMedicoYEstado(Integer idMedico, Cita.Estado estado) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        if (estado == null) {
            throw new InvalidDataException("El estado no puede ser nulo");
        }
        return citaRepository.contarCitasByMedicoAndEstado(idMedico, estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarCitasPorPacienteYEstado(Integer idPaciente, Cita.Estado estado) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        if (estado == null) {
            throw new InvalidDataException("El estado no puede ser nulo");
        }
        return citaRepository.contarCitasByPacienteAndEstado(idPaciente, estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasDelDiaPorMedico(Integer idMedico, LocalDate fecha) {
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        if (fecha == null) {
            throw new InvalidDataException("La fecha no puede ser nula");
        }
        return citaRepository.findCitasDelDiaByMedico(idMedico, fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cita> obtenerCitasCanceladasEnRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new InvalidDataException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new InvalidDataException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return citaRepository.findCitasCanceladasEnRango(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCitaEntrePacienteMedicoYFecha(Integer idPaciente, Integer idMedico, LocalDate fechaCita) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        if (fechaCita == null) {
            throw new InvalidDataException("La fecha de cita no puede ser nula");
        }
        return citaRepository.existsByPaciente_IdPacienteAndMedico_IdMedicoAndFechaCita(
                idPaciente, idMedico, fechaCita
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cita> obtenerUltimaCitaEntrePacienteYMedico(Integer idPaciente, Integer idMedico) {
        if (idPaciente == null) {
            throw new InvalidDataException("El ID del paciente no puede ser nulo");
        }
        if (idMedico == null) {
            throw new InvalidDataException("El ID del médico no puede ser nulo");
        }
        List<Cita> citas = citaRepository.findUltimaCitaByPacienteAndMedico(idPaciente, idMedico);
        return citas.isEmpty() ? Optional.empty() : Optional.of(citas.get(0));
    }

    @Override
    public Cita completarCita(Integer id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        
        if (cita.getEstado() == Cita.Estado.CANCELADA) {
            throw new InvalidDataException("No se puede completar una cita cancelada");
        }
        
        cita.setEstado(Cita.Estado.COMPLETADA);
        return citaRepository.save(cita);
    }

    @Override
    public Cita cancelarCita(Integer id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        
        if (cita.getEstado() == Cita.Estado.COMPLETADA) {
            throw new InvalidDataException("No se puede cancelar una cita completada");
        }
        
        cita.setEstado(Cita.Estado.CANCELADA);
        cita.setFechaCancelacion(LocalDateTime.now());
        return citaRepository.save(cita);
    }

    @Override
    public Cita marcarComoNoAsistio(Integer id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + id));
        
        if (cita.getEstado() == Cita.Estado.CANCELADA) {
            throw new InvalidDataException("No se puede marcar como no asistió una cita cancelada");
        }
        
        if (cita.getEstado() == Cita.Estado.COMPLETADA) {
            throw new InvalidDataException("No se puede marcar como no asistió una cita completada");
        }
        
        cita.setEstado(Cita.Estado.NO_ASISTIO);
        return citaRepository.save(cita);
    }

    @Override
    public void validarCita(Cita cita) {
        if (cita == null) {
            throw new InvalidDataException("La cita no puede ser nula");
        }

        if (cita.getPaciente() == null) {
            throw new InvalidDataException("El paciente es obligatorio");
        }

        if (cita.getMedico() == null) {
            throw new InvalidDataException("El médico es obligatorio");
        }

        if (cita.getFechaCita() == null) {
            throw new InvalidDataException("La fecha de cita es obligatoria");
        }

        if (cita.getHoraCita() == null) {
            throw new InvalidDataException("La hora de cita es obligatoria");
        }

        if (cita.getTipoCita() == null) {
            throw new InvalidDataException("El tipo de cita es obligatorio");
        }

        // Validar enlace de videollamada para telemedicina
        if (cita.getTipoCita() == Cita.TipoCita.TELEMEDICINA) {
            if (cita.getEnlaceVideollamada() == null || cita.getEnlaceVideollamada().trim().isEmpty()) {
                throw new InvalidDataException("El enlace de videollamada es obligatorio para citas de telemedicina");
            }
        }
    }

    @Override
    public void validarFechaCita(LocalDate fechaCita, LocalTime horaCita) {
        if (fechaCita == null || horaCita == null) {
            throw new InvalidDataException("La fecha y hora de cita no pueden ser nulas");
        }

        // No permitir citas en el pasado
        LocalDateTime fechaHoraCita = LocalDateTime.of(fechaCita, horaCita);
        if (fechaHoraCita.isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("No se pueden programar citas en el pasado");
        }

        // Validar que no sea en un horario muy lejano (máximo 6 meses adelante)
        LocalDateTime fechaMaxima = LocalDateTime.now().plusMonths(6);
        if (fechaHoraCita.isAfter(fechaMaxima)) {
            throw new InvalidDataException("No se pueden programar citas con más de 6 meses de anticipación");
        }
    }
}
