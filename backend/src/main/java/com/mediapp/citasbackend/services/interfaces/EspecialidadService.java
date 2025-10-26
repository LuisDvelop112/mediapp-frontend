package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.Especialidad;

import java.util.List;
import java.util.Optional;

public interface EspecialidadService {

    // Operaciones CRUD básicas
    Especialidad guardarEspecialidad(Especialidad especialidad);
    
    Especialidad actualizarEspecialidad(Integer id, Especialidad especialidad);
    
    void eliminarEspecialidad(Integer id);
    
    Optional<Especialidad> obtenerEspecialidadPorId(Integer id);
    
    List<Especialidad> obtenerTodasLasEspecialidades();

    // Búsqueda por nombre
    Optional<Especialidad> obtenerEspecialidadPorNombre(String nombreEspecialidad);
    
    boolean existeEspecialidadPorNombre(String nombreEspecialidad);

    // Búsquedas por estado
    List<Especialidad> obtenerEspecialidadesPorEstado(Especialidad.Estado estado);
    
    List<Especialidad> obtenerEspecialidadesActivas();

    // Búsquedas parciales
    List<Especialidad> buscarEspecialidadesPorNombre(String nombre);
    
    List<Especialidad> buscarEspecialidadesActivasPorNombre(String nombre);
    
    List<Especialidad> buscarEspecialidadesPorDescripcion(String texto);

    // Conteo de especialidades
    Long contarEspecialidadesPorEstado(Especialidad.Estado estado);
    
    Long contarEspecialidadesActivas();

    // Especialidades ordenadas
    List<Especialidad> obtenerEspecialidadesOrdenadas();

    // Especialidades con médicos
    List<Especialidad> obtenerEspecialidadesConMedicos();

    // Especialidades recientes
    List<Especialidad> obtenerEspecialidadesRecientes();

    // Operaciones de estado
    Especialidad activarEspecialidad(Integer id);
    
    Especialidad desactivarEspecialidad(Integer id);

    // Validaciones
    void validarEspecialidad(Especialidad especialidad);
    
    boolean nombreEsUnico(String nombre, Integer idExcluir);
}
