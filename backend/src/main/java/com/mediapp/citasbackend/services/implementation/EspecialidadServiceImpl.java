package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.Especialidad;
import com.mediapp.citasbackend.exceptions.InvalidDataException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.exceptions.ResourceNotFoundException;
import com.mediapp.citasbackend.repositories.EspecialidadRepository;
import com.mediapp.citasbackend.services.interfaces.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    @Override
    public Especialidad guardarEspecialidad(Especialidad especialidad) {
        validarEspecialidad(especialidad);
        
        // Verificar que el nombre sea único
        if (especialidadRepository.existsByNombreEspecialidad(especialidad.getNombreEspecialidad())) {
            throw new ResourceAlreadyExistsException("Especialidad", "nombre", especialidad.getNombreEspecialidad());
        }
        
        return especialidadRepository.save(especialidad);
    }

    @Override
    public Especialidad actualizarEspecialidad(Integer id, Especialidad especialidad) {
        Especialidad especialidadExistente = especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", "ID", id));

        validarEspecialidad(especialidad);

        // Verificar que el nombre sea único (excluyendo la especialidad actual)
        if (!especialidadExistente.getNombreEspecialidad().equals(especialidad.getNombreEspecialidad())) {
            if (!nombreEsUnico(especialidad.getNombreEspecialidad(), id)) {
                throw new ResourceAlreadyExistsException("Especialidad", "nombre", especialidad.getNombreEspecialidad());
            }
        }

        // Actualizar campos
        especialidadExistente.setNombreEspecialidad(especialidad.getNombreEspecialidad());
        especialidadExistente.setDescripcion(especialidad.getDescripcion());
        especialidadExistente.setEstado(especialidad.getEstado());

        return especialidadRepository.save(especialidadExistente);
    }

    @Override
    public void eliminarEspecialidad(Integer id) {
        if (!especialidadRepository.existsById(id)) {
            throw new ResourceNotFoundException("Especialidad", "ID", id);
        }
        especialidadRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Especialidad> obtenerEspecialidadPorId(Integer id) {
        return especialidadRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> obtenerTodasLasEspecialidades() {
        return especialidadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Especialidad> obtenerEspecialidadPorNombre(String nombreEspecialidad) {
        if (nombreEspecialidad == null || nombreEspecialidad.trim().isEmpty()) {
            throw new InvalidDataException("El nombre de la especialidad no puede estar vacío");
        }
        return especialidadRepository.findByNombreEspecialidad(nombreEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEspecialidadPorNombre(String nombreEspecialidad) {
        if (nombreEspecialidad == null || nombreEspecialidad.trim().isEmpty()) {
            return false;
        }
        return especialidadRepository.existsByNombreEspecialidad(nombreEspecialidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> obtenerEspecialidadesPorEstado(Especialidad.Estado estado) {
        if (estado == null) {
            throw new InvalidDataException("El estado no puede ser nulo");
        }
        return especialidadRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> obtenerEspecialidadesActivas() {
        return especialidadRepository.findEspecialidadesActivas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> buscarEspecialidadesPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return especialidadRepository.buscarPorNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> buscarEspecialidadesActivasPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return especialidadRepository.buscarEspecialidadesActivasPorNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> buscarEspecialidadesPorDescripcion(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return especialidadRepository.buscarPorDescripcion(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarEspecialidadesPorEstado(Especialidad.Estado estado) {
        if (estado == null) {
            throw new InvalidDataException("El estado no puede ser nulo");
        }
        return especialidadRepository.contarPorEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarEspecialidadesActivas() {
        return especialidadRepository.contarEspecialidadesActivas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> obtenerEspecialidadesOrdenadas() {
        return especialidadRepository.findAllOrdenadas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> obtenerEspecialidadesConMedicos() {
        return especialidadRepository.findEspecialidadesConMedicos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Especialidad> obtenerEspecialidadesRecientes() {
        return especialidadRepository.findEspecialidadesRecientes();
    }

    @Override
    public Especialidad activarEspecialidad(Integer id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", "ID", id));
        especialidad.setEstado(Especialidad.Estado.ACTIVA);
        return especialidadRepository.save(especialidad);
    }

    @Override
    public Especialidad desactivarEspecialidad(Integer id) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad", "ID", id));
        especialidad.setEstado(Especialidad.Estado.INACTIVA);
        return especialidadRepository.save(especialidad);
    }

    @Override
    public void validarEspecialidad(Especialidad especialidad) {
        if (especialidad == null) {
            throw new InvalidDataException("La especialidad no puede ser nula");
        }

        if (especialidad.getNombreEspecialidad() == null || 
            especialidad.getNombreEspecialidad().trim().isEmpty()) {
            throw new InvalidDataException("El nombre de la especialidad es obligatorio");
        }

        // Validar longitud del nombre
        if (especialidad.getNombreEspecialidad().length() > 100) {
            throw new InvalidDataException("El nombre de la especialidad no puede exceder 100 caracteres");
        }

        // Validar que el nombre no contenga solo espacios
        if (especialidad.getNombreEspecialidad().trim().isEmpty()) {
            throw new InvalidDataException("El nombre de la especialidad no puede contener solo espacios");
        }

        // Validar caracteres especiales excesivos
        if (especialidad.getNombreEspecialidad().matches(".*[<>{}\\[\\]|\\\\].*")) {
            throw new InvalidDataException("El nombre de la especialidad contiene caracteres no permitidos");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean nombreEsUnico(String nombre, Integer idExcluir) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        if (idExcluir == null) {
            return !especialidadRepository.existsByNombreEspecialidad(nombre);
        }
        
        Long count = especialidadRepository.contarPorNombreExcluyendoId(nombre, idExcluir);
        return count == 0;
    }
}
