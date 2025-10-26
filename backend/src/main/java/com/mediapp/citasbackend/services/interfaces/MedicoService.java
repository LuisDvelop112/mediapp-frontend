package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.Especialidad;
import com.mediapp.citasbackend.entities.Medico;
import com.mediapp.citasbackend.entities.Usuario;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MedicoService {

    // Operaciones CRUD básicas
    Medico guardarMedico(Medico medico);
    
    Medico actualizarMedico(Integer id, Medico medico);
    
    void eliminarMedico(Integer id);
    
    Optional<Medico> obtenerMedicoPorId(Integer id);
    
    List<Medico> obtenerTodosLosMedicos();

    // Búsqueda por usuario
    Optional<Medico> obtenerMedicoPorUsuario(Usuario usuario);
    
    Optional<Medico> obtenerMedicoPorUsuarioId(Integer idUsuario);
    
    boolean existeMedicoPorUsuarioId(Integer idUsuario);

    // Búsqueda por licencia
    Optional<Medico> obtenerMedicoPorNumeroLicencia(String numeroLicencia);
    
    boolean existeNumeroLicencia(String numeroLicencia);

    // Búsquedas por especialidad
    List<Medico> obtenerMedicosPorEspecialidad(Especialidad especialidad);
    
    List<Medico> obtenerMedicosPorEspecialidadId(Integer idEspecialidad);
    
    List<Medico> obtenerMedicosVerificadosPorEspecialidad(Integer idEspecialidad);

    // Búsquedas por estado de verificación
    List<Medico> obtenerMedicosPorEstadoVerificacion(Medico.EstadoVerificacion estadoVerificacion);
    
    List<Medico> obtenerMedicosVerificados();
    
    List<Medico> obtenerMedicosPendientesVerificacion();
    
    List<Medico> obtenerMedicosActivosVerificados();

    // Búsquedas por hospital
    List<Medico> obtenerMedicosPorHospital(String hospitalAfiliado);

    // Búsquedas por experiencia
    List<Medico> obtenerMedicosConExperienciaMinima(Integer aniosMinimos);
    
    List<Medico> obtenerMedicosPorRangoExperiencia(Integer minAnos, Integer maxAnos);

    // Búsquedas por calificación
    List<Medico> obtenerMedicosConCalificacionMinima(BigDecimal calificacionMinima);
    
    List<Medico> obtenerMedicosMejorCalificados();
    
    List<Medico> obtenerMedicosMejorCalificadosPorEspecialidad(Integer idEspecialidad);
    
    List<Medico> obtenerMedicosSinCalificacion();

    // Búsquedas por nombre
    List<Medico> buscarMedicosPorNombre(String nombre);
    
    List<Medico> buscarMedicosActivosPorNombre(String nombre);

    // Búsquedas por ubicación
    List<Medico> obtenerMedicosPorCiudad(String ciudad);
    
    List<Medico> obtenerMedicosPorCiudadYEspecialidad(String ciudad, Integer idEspecialidad);

    // Conteo de médicos
    Long contarMedicosPorEspecialidad(Integer idEspecialidad);
    
    Long contarMedicosVerificados();
    
    Long contarMedicosPendientes();

    // Médicos recientes
    List<Medico> obtenerMedicosRecientes();

    // Búsqueda por email
    Optional<Medico> obtenerMedicoPorEmail(String email);

    // Médicos con bio
    List<Medico> obtenerMedicosConBio();

    // Médicos disponibles (con calendario)
    List<Medico> obtenerMedicosDisponibles();
    
    List<Medico> obtenerMedicosDisponiblesPorEspecialidad(Integer idEspecialidad);

    // Ordenamiento
    List<Medico> obtenerTodosLosMedicosOrdenadosPorCalificacion();

    // Operaciones de verificación
    Medico verificarMedico(Integer id);
    
    Medico marcarComoPendiente(Integer id);

    // Actualizar calificación
    Medico actualizarCalificacionPromedio(Integer id, BigDecimal calificacion);

    // Validaciones
    void validarMedico(Medico medico);
    
    boolean licenciaEsUnica(String numeroLicencia, Integer idExcluir);
}
