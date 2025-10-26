package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.Especialidad;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.exceptions.InvalidDataException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.exceptions.ResourceNotFoundException;
import com.mediapp.citasbackend.repositories.MedicoRepository;
import com.mediapp.citasbackend.services.interfaces.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;

    @Override
    public Medico guardarMedico(Medico medico) {
        validarMedico(medico);
        
        // Verificar que el usuario no esté ya asociado a otro médico
        if (medicoRepository.existsByUsuario_IdUsuario(medico.getUsuario().getIdUsuario())) {
            throw new ResourceAlreadyExistsException("El usuario ya está asociado a un perfil de médico");
        }
        
        // Verificar que el número de licencia sea único (si se proporciona)
        if (medico.getNumeroLicencia() != null && !medico.getNumeroLicencia().isEmpty()) {
            if (medicoRepository.existsByNumeroLicencia(medico.getNumeroLicencia())) {
                throw new ResourceAlreadyExistsException("Médico", "número de licencia", medico.getNumeroLicencia());
            }
        }
        
        return medicoRepository.save(medico);
    }

    @Override
    public Medico actualizarMedico(Integer id, Medico medico) {
        Medico medicoExistente = medicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico", "ID", id));

        validarMedico(medico);

        // Verificar que el número de licencia sea único (excluyendo el médico actual)
        if (medico.getNumeroLicencia() != null && !medico.getNumeroLicencia().isEmpty()) {
            if (!medico.getNumeroLicencia().equals(medicoExistente.getNumeroLicencia())) {
                if (!licenciaEsUnica(medico.getNumeroLicencia(), id)) {
                    throw new ResourceAlreadyExistsException("Médico", "número de licencia", medico.getNumeroLicencia());
                }
            }
        }

        // Actualizar campos
        medicoExistente.setUsuario(medico.getUsuario());
        medicoExistente.setNumeroLicencia(medico.getNumeroLicencia());
        medicoExistente.setEspecialidad(medico.getEspecialidad());
        medicoExistente.setHospitalAfiliado(medico.getHospitalAfiliado());
        medicoExistente.setExperienciaAnos(medico.getExperienciaAnos());
        medicoExistente.setResumenBio(medico.getResumenBio());
        medicoExistente.setCalificacionPromedio(medico.getCalificacionPromedio());
        medicoExistente.setEstadoVerificacion(medico.getEstadoVerificacion());
        medicoExistente.setFechaVerificacion(medico.getFechaVerificacion());

        return medicoRepository.save(medicoExistente);
    }

    @Override
    public void eliminarMedico(Integer id) {
        if (!medicoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Médico", "ID", id);
        }
        medicoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medico> obtenerMedicoPorId(Integer id) {
        return medicoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerTodosLosMedicos() {
        return medicoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medico> obtenerMedicoPorUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new InvalidDataException("El usuario no puede ser nulo");
        }
        return medicoRepository.findByUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medico> obtenerMedicoPorUsuarioId(Integer idUsuario) {
        if (idUsuario == null) {
            throw new InvalidDataException("El ID del usuario no puede ser nulo");
        }
        return medicoRepository.findByUsuario_IdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeMedicoPorUsuarioId(Integer idUsuario) {
        if (idUsuario == null) {
            return false;
        }
        return medicoRepository.existsByUsuario_IdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medico> obtenerMedicoPorNumeroLicencia(String numeroLicencia) {
        if (numeroLicencia == null || numeroLicencia.trim().isEmpty()) {
            throw new InvalidDataException("El número de licencia no puede estar vacío");
        }
        return medicoRepository.findByNumeroLicencia(numeroLicencia);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNumeroLicencia(String numeroLicencia) {
        if (numeroLicencia == null || numeroLicencia.trim().isEmpty()) {
            return false;
        }
        return medicoRepository.existsByNumeroLicencia(numeroLicencia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPorEspecialidad(Especialidad especialidad) {
        if (especialidad == null) {
            throw new InvalidDataException("La especialidad no puede ser nula");
        }
        return medicoRepository.findByEspecialidad(especialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPorEspecialidadId(Integer idEspecialidad) {
        if (idEspecialidad == null) {
            throw new InvalidDataException("El ID de la especialidad no puede ser nulo");
        }
        return medicoRepository.findByEspecialidad_IdEspecialidad(idEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosVerificadosPorEspecialidad(Integer idEspecialidad) {
        if (idEspecialidad == null) {
            throw new InvalidDataException("El ID de la especialidad no puede ser nulo");
        }
        return medicoRepository.findMedicosVerificadosByEspecialidad(idEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPorEstadoVerificacion(Medico.EstadoVerificacion estadoVerificacion) {
        if (estadoVerificacion == null) {
            throw new InvalidDataException("El estado de verificación no puede ser nulo");
        }
        return medicoRepository.findByEstadoVerificacion(estadoVerificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosVerificados() {
        return medicoRepository.findMedicosVerificados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPendientesVerificacion() {
        return medicoRepository.findMedicosPendientesVerificacion();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosActivosVerificados() {
        return medicoRepository.findMedicosActivosVerificados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPorHospital(String hospitalAfiliado) {
        if (hospitalAfiliado == null || hospitalAfiliado.trim().isEmpty()) {
            throw new InvalidDataException("El nombre del hospital no puede estar vacío");
        }
        return medicoRepository.findByHospitalAfiliado(hospitalAfiliado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosConExperienciaMinima(Integer aniosMinimos) {
        if (aniosMinimos == null || aniosMinimos < 0) {
            throw new InvalidDataException("Los años mínimos deben ser mayores o iguales a cero");
        }
        return medicoRepository.findMedicosConExperienciaMinima(aniosMinimos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPorRangoExperiencia(Integer minAnos, Integer maxAnos) {
        if (minAnos == null || maxAnos == null) {
            throw new InvalidDataException("Los años mínimos y máximos no pueden ser nulos");
        }
        if (minAnos < 0 || maxAnos < 0) {
            throw new InvalidDataException("Los años no pueden ser negativos");
        }
        if (minAnos > maxAnos) {
            throw new InvalidDataException("Los años mínimos no pueden ser mayores que los máximos");
        }
        return medicoRepository.findMedicosPorRangoExperiencia(minAnos, maxAnos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosConCalificacionMinima(BigDecimal calificacionMinima) {
        if (calificacionMinima == null) {
            throw new InvalidDataException("La calificación mínima no puede ser nula");
        }
        if (calificacionMinima.compareTo(BigDecimal.ZERO) < 0 || 
            calificacionMinima.compareTo(new BigDecimal("5.0")) > 0) {
            throw new InvalidDataException("La calificación debe estar entre 0 y 5");
        }
        return medicoRepository.findMedicosConCalificacionMinima(calificacionMinima);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosMejorCalificados() {
        return medicoRepository.findMedicosMejorCalificados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosMejorCalificadosPorEspecialidad(Integer idEspecialidad) {
        if (idEspecialidad == null) {
            throw new InvalidDataException("El ID de la especialidad no puede ser nulo");
        }
        return medicoRepository.findMedicosMejorCalificadosByEspecialidad(idEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosSinCalificacion() {
        return medicoRepository.findMedicosSinCalificacion();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> buscarMedicosPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return medicoRepository.buscarMedicosPorNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> buscarMedicosActivosPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return medicoRepository.buscarMedicosActivosPorNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPorCiudad(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new InvalidDataException("La ciudad no puede estar vacía");
        }
        return medicoRepository.findMedicosByCiudad(ciudad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosPorCiudadYEspecialidad(String ciudad, Integer idEspecialidad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new InvalidDataException("La ciudad no puede estar vacía");
        }
        if (idEspecialidad == null) {
            throw new InvalidDataException("El ID de la especialidad no puede ser nulo");
        }
        return medicoRepository.findMedicosByCiudadAndEspecialidad(ciudad, idEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarMedicosPorEspecialidad(Integer idEspecialidad) {
        if (idEspecialidad == null) {
            throw new InvalidDataException("El ID de la especialidad no puede ser nulo");
        }
        return medicoRepository.contarMedicosByEspecialidad(idEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarMedicosVerificados() {
        return medicoRepository.contarMedicosVerificados();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarMedicosPendientes() {
        return medicoRepository.contarMedicosPendientes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosRecientes() {
        return medicoRepository.findMedicosRecientes();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Medico> obtenerMedicoPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidDataException("El email no puede estar vacío");
        }
        return medicoRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosConBio() {
        return medicoRepository.findMedicosConBio();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosDisponibles() {
        return medicoRepository.findMedicosDisponibles();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerMedicosDisponiblesPorEspecialidad(Integer idEspecialidad) {
        if (idEspecialidad == null) {
            throw new InvalidDataException("El ID de la especialidad no puede ser nulo");
        }
        return medicoRepository.findMedicosDisponiblesByEspecialidad(idEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Medico> obtenerTodosLosMedicosOrdenadosPorCalificacion() {
        return medicoRepository.findAllOrdenadosPorCalificacion();
    }

    @Override
    public Medico verificarMedico(Integer id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con ID: " + id));
        
        medico.setEstadoVerificacion(Medico.EstadoVerificacion.VERIFICADO);
        medico.setFechaVerificacion(LocalDate.now());
        
        return medicoRepository.save(medico);
    }

    @Override
    public Medico marcarComoPendiente(Integer id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con ID: " + id));
        
        medico.setEstadoVerificacion(Medico.EstadoVerificacion.PENDIENTE);
        medico.setFechaVerificacion(null);
        
        return medicoRepository.save(medico);
    }

    @Override
    public Medico actualizarCalificacionPromedio(Integer id, BigDecimal calificacion) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con ID: " + id));
        
        if (calificacion == null) {
            throw new InvalidDataException("La calificación no puede ser nula");
        }
        
        if (calificacion.compareTo(BigDecimal.ZERO) < 0 || 
            calificacion.compareTo(new BigDecimal("5.0")) > 0) {
            throw new InvalidDataException("La calificación debe estar entre 0 y 5");
        }
        
        medico.setCalificacionPromedio(calificacion);
        
        return medicoRepository.save(medico);
    }

    @Override
    public void validarMedico(Medico medico) {
        if (medico == null) {
            throw new InvalidDataException("El médico no puede ser nulo");
        }

        if (medico.getUsuario() == null) {
            throw new InvalidDataException("El usuario es obligatorio");
        }

        // Validar número de licencia (si se proporciona)
        if (medico.getNumeroLicencia() != null && !medico.getNumeroLicencia().trim().isEmpty()) {
            if (medico.getNumeroLicencia().length() > 50) {
                throw new InvalidDataException("El número de licencia no puede exceder 50 caracteres");
            }
        }

        // Validar experiencia en años (si se proporciona)
        if (medico.getExperienciaAnos() != null) {
            if (medico.getExperienciaAnos() < 0) {
                throw new InvalidDataException("Los años de experiencia no pueden ser negativos");
            }
            if (medico.getExperienciaAnos() > 70) {
                throw new InvalidDataException("Los años de experiencia no pueden exceder 70 años");
            }
        }

        // Validar calificación promedio (si se proporciona)
        if (medico.getCalificacionPromedio() != null) {
            if (medico.getCalificacionPromedio().compareTo(BigDecimal.ZERO) < 0 || 
                medico.getCalificacionPromedio().compareTo(new BigDecimal("5.0")) > 0) {
                throw new InvalidDataException("La calificación promedio debe estar entre 0 y 5");
            }
        }

        // Validar longitud del resumen bio (si se proporciona)
        if (medico.getResumenBio() != null && medico.getResumenBio().length() > 5000) {
            throw new InvalidDataException("El resumen biográfico no puede exceder 5000 caracteres");
        }

        // Validar longitud del hospital afiliado (si se proporciona)
        if (medico.getHospitalAfiliado() != null && medico.getHospitalAfiliado().length() > 100) {
            throw new InvalidDataException("El nombre del hospital no puede exceder 100 caracteres");
        }

        // Validar fecha de verificación (si está verificado)
        if (medico.getEstadoVerificacion() == Medico.EstadoVerificacion.VERIFICADO) {
            if (medico.getFechaVerificacion() != null && 
                medico.getFechaVerificacion().isAfter(LocalDate.now())) {
                throw new InvalidDataException("La fecha de verificación no puede ser futura");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean licenciaEsUnica(String numeroLicencia, Integer idExcluir) {
        if (numeroLicencia == null || numeroLicencia.trim().isEmpty()) {
            return true; // Licencia vacía no se valida como única
        }
        
        if (idExcluir == null) {
            return !medicoRepository.existsByNumeroLicencia(numeroLicencia);
        }
        
        Long count = medicoRepository.contarPorLicenciaExcluyendoId(numeroLicencia, idExcluir);
        return count == 0;
    }
}
