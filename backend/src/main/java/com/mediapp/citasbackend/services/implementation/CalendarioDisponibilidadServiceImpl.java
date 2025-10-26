package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.CalendarioDisponibilidad;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.repositories.CalendarioDisponibilidadRepository;
import com.mediapp.citasbackend.services.interfaces.CalendarioDisponibilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarioDisponibilidadServiceImpl implements CalendarioDisponibilidadService {

    private final CalendarioDisponibilidadRepository calendarioRepository;

    @Override
    public CalendarioDisponibilidad guardarDisponibilidad(CalendarioDisponibilidad disponibilidad) {
        validarDisponibilidad(disponibilidad);
        validarHorario(disponibilidad.getHoraInicio(), disponibilidad.getHoraFin());
        
        // Verificar conflictos de horario
        if (tieneConflictoDeHorario(
                disponibilidad.getMedico().getIdMedico(),
                disponibilidad.getDiaSemana(),
                disponibilidad.getHoraInicio(),
                disponibilidad.getHoraFin()
        )) {
            throw new IllegalArgumentException(
                "Ya existe una disponibilidad para este médico en el día " + 
                disponibilidad.getDiaSemana() + " que se superpone con el horario especificado"
            );
        }
        
        return calendarioRepository.save(disponibilidad);
    }

    @Override
    public CalendarioDisponibilidad actualizarDisponibilidad(Integer id, CalendarioDisponibilidad disponibilidad) {
        CalendarioDisponibilidad disponibilidadExistente = calendarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada con ID: " + id));

        validarDisponibilidad(disponibilidad);
        validarHorario(disponibilidad.getHoraInicio(), disponibilidad.getHoraFin());

        // Verificar conflictos de horario excluyendo la disponibilidad actual
        List<CalendarioDisponibilidad> conflictos = verificarConflictosDeHorario(
                disponibilidad.getMedico().getIdMedico(),
                disponibilidad.getDiaSemana(),
                disponibilidad.getHoraInicio(),
                disponibilidad.getHoraFin()
        );

        // Filtrar el registro actual de los conflictos
        boolean tieneConflicto = conflictos.stream()
                .anyMatch(c -> !c.getIdDisponibilidad().equals(id));

        if (tieneConflicto) {
            throw new IllegalArgumentException(
                "Ya existe una disponibilidad para este médico en el día " + 
                disponibilidad.getDiaSemana() + " que se superpone con el horario especificado"
            );
        }

        // Actualizar campos
        disponibilidadExistente.setMedico(disponibilidad.getMedico());
        disponibilidadExistente.setDiaSemana(disponibilidad.getDiaSemana());
        disponibilidadExistente.setHoraInicio(disponibilidad.getHoraInicio());
        disponibilidadExistente.setHoraFin(disponibilidad.getHoraFin());
        disponibilidadExistente.setIntervaloCitaMinutos(disponibilidad.getIntervaloCitaMinutos());
        disponibilidadExistente.setEstado(disponibilidad.getEstado());

        return calendarioRepository.save(disponibilidadExistente);
    }

    @Override
    public void eliminarDisponibilidad(Integer id) {
        if (!calendarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Disponibilidad no encontrada con ID: " + id);
        }
        calendarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CalendarioDisponibilidad> obtenerDisponibilidadPorId(Integer id) {
        return calendarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerTodasLasDisponibilidades() {
        return calendarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerDisponibilidadesPorMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("El médico no puede ser nulo");
        }
        return calendarioRepository.findByMedico(medico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerDisponibilidadesPorMedicoId(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return calendarioRepository.findByMedico_IdMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerDisponibilidadesActivasPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return calendarioRepository.findDisponibilidadesActivasByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerDisponibilidadesPorMedicoYDia(
            Integer idMedico, 
            CalendarioDisponibilidad.DiaSemana dia) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        if (dia == null) {
            throw new IllegalArgumentException("El día de la semana no puede ser nulo");
        }
        return calendarioRepository.findDisponibilidadesByMedicoAndDia(idMedico, dia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerDisponibilidadesPorDia(CalendarioDisponibilidad.DiaSemana diaSemana) {
        if (diaSemana == null) {
            throw new IllegalArgumentException("El día de la semana no puede ser nulo");
        }
        return calendarioRepository.findByDiaSemana(diaSemana);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerDisponibilidadesPorEstado(CalendarioDisponibilidad.Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        return calendarioRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerDisponibilidadesPorMedicoYEstado(
            Integer idMedico, 
            CalendarioDisponibilidad.Estado estado) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }
        return calendarioRepository.findByMedico_IdMedicoAndEstado(idMedico, estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> verificarConflictosDeHorario(
            Integer idMedico,
            CalendarioDisponibilidad.DiaSemana dia,
            LocalTime horaInicio,
            LocalTime horaFin) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        if (dia == null) {
            throw new IllegalArgumentException("El día de la semana no puede ser nulo");
        }
        if (horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("Las horas de inicio y fin no pueden ser nulas");
        }
        return calendarioRepository.findDisponibilidadesConflicto(idMedico, dia, horaInicio, horaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneConflictoDeHorario(
            Integer idMedico,
            CalendarioDisponibilidad.DiaSemana dia,
            LocalTime horaInicio,
            LocalTime horaFin) {
        List<CalendarioDisponibilidad> conflictos = verificarConflictosDeHorario(
                idMedico, dia, horaInicio, horaFin
        );
        return !conflictos.isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CalendarioDisponibilidad> obtenerDisponibilidadPorMedicoYDia(
            Integer idMedico,
            CalendarioDisponibilidad.DiaSemana diaSemana) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        if (diaSemana == null) {
            throw new IllegalArgumentException("El día de la semana no puede ser nulo");
        }
        return calendarioRepository.findByMedico_IdMedicoAndDiaSemana(idMedico, diaSemana);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarDisponibilidadesActivasPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return calendarioRepository.contarDisponibilidadesActivasByMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosDisponiblesEnDia(CalendarioDisponibilidad.DiaSemana dia) {
        if (dia == null) {
            throw new IllegalArgumentException("El día de la semana no puede ser nulo");
        }
        return calendarioRepository.findMedicosDisponiblesEnDia(dia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosDisponiblesEnDiaYHora(
            CalendarioDisponibilidad.DiaSemana dia,
            LocalTime hora) {
        if (dia == null) {
            throw new IllegalArgumentException("El día de la semana no puede ser nulo");
        }
        if (hora == null) {
            throw new IllegalArgumentException("La hora no puede ser nula");
        }
        return calendarioRepository.findMedicosDisponiblesEnDiaYHora(dia, hora);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerDisponibilidadesOrdenadasPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return calendarioRepository.findByMedicoOrdenadas(idMedico);
    }

    @Override
    public void eliminarTodasLasDisponibilidadesPorMedico(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        calendarioRepository.deleteByMedico_IdMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean medicoTieneDisponibilidadConfigurada(Integer idMedico) {
        if (idMedico == null) {
            throw new IllegalArgumentException("El ID del médico no puede ser nulo");
        }
        return calendarioRepository.existsByMedico_IdMedico(idMedico);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarioDisponibilidad> obtenerDisponibilidadesPorIntervalo(Integer intervalo) {
        if (intervalo == null || intervalo <= 0) {
            throw new IllegalArgumentException("El intervalo debe ser mayor a cero");
        }
        return calendarioRepository.findByIntervaloCitaMinutos(intervalo);
    }

    @Override
    public CalendarioDisponibilidad activarDisponibilidad(Integer id) {
        CalendarioDisponibilidad disponibilidad = calendarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada con ID: " + id));
        disponibilidad.setEstado(CalendarioDisponibilidad.Estado.ACTIVO);
        return calendarioRepository.save(disponibilidad);
    }

    @Override
    public CalendarioDisponibilidad desactivarDisponibilidad(Integer id) {
        CalendarioDisponibilidad disponibilidad = calendarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Disponibilidad no encontrada con ID: " + id));
        disponibilidad.setEstado(CalendarioDisponibilidad.Estado.INACTIVO);
        return calendarioRepository.save(disponibilidad);
    }

    @Override
    public void validarDisponibilidad(CalendarioDisponibilidad disponibilidad) {
        if (disponibilidad == null) {
            throw new IllegalArgumentException("La disponibilidad no puede ser nula");
        }

        if (disponibilidad.getMedico() == null) {
            throw new IllegalArgumentException("El médico es obligatorio");
        }

        if (disponibilidad.getDiaSemana() == null) {
            throw new IllegalArgumentException("El día de la semana es obligatorio");
        }

        if (disponibilidad.getHoraInicio() == null) {
            throw new IllegalArgumentException("La hora de inicio es obligatoria");
        }

        if (disponibilidad.getHoraFin() == null) {
            throw new IllegalArgumentException("La hora de fin es obligatoria");
        }

        if (disponibilidad.getIntervaloCitaMinutos() == null || disponibilidad.getIntervaloCitaMinutos() <= 0) {
            throw new IllegalArgumentException("El intervalo de cita debe ser mayor a cero");
        }

        // Validar que el intervalo sea razonable (entre 5 y 120 minutos)
        if (disponibilidad.getIntervaloCitaMinutos() < 5 || disponibilidad.getIntervaloCitaMinutos() > 120) {
            throw new IllegalArgumentException("El intervalo de cita debe estar entre 5 y 120 minutos");
        }
    }

    @Override
    public void validarHorario(LocalTime horaInicio, LocalTime horaFin) {
        if (horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("Las horas de inicio y fin no pueden ser nulas");
        }

        if (horaInicio.isAfter(horaFin) || horaInicio.equals(horaFin)) {
            throw new IllegalArgumentException("La hora de inicio debe ser anterior a la hora de fin");
        }

        // Validar horarios razonables (entre 6:00 AM y 10:00 PM)
        LocalTime horaMinima = LocalTime.of(6, 0);
        LocalTime horaMaxima = LocalTime.of(22, 0);

        if (horaInicio.isBefore(horaMinima)) {
            throw new IllegalArgumentException("La hora de inicio no puede ser antes de las 6:00 AM");
        }

        if (horaFin.isAfter(horaMaxima)) {
            throw new IllegalArgumentException("La hora de fin no puede ser después de las 10:00 PM");
        }
    }
}
