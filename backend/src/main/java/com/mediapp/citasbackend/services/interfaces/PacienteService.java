package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.Paciente;
import com.mediapp.citasbackend.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface PacienteService {

    // Operaciones CRUD básicas
    Paciente guardarPaciente(Paciente paciente);
    
    Paciente actualizarPaciente(Integer id, Paciente paciente);
    
    void eliminarPaciente(Integer id);
    
    Optional<Paciente> obtenerPacientePorId(Integer id);
    
    List<Paciente> obtenerTodosLosPacientes();

    // Búsqueda por usuario
    Optional<Paciente> obtenerPacientePorUsuario(Usuario usuario);
    
    Optional<Paciente> obtenerPacientePorUsuarioId(Integer idUsuario);
    
    boolean existePacientePorUsuarioId(Integer idUsuario);

    // Búsqueda por identificación
    Optional<Paciente> obtenerPacientePorNumeroIdentificacion(String numeroIdentificacion);
    
    boolean existeNumeroIdentificacion(String numeroIdentificacion);

    // Búsquedas por tipo de sangre
    List<Paciente> obtenerPacientesPorTipoSangre(String tipoSangre);
    
    List<Paciente> obtenerPacientesSinTipoSangre();

    // Pacientes activos
    List<Paciente> obtenerPacientesActivos();

    // Búsquedas por nombre
    List<Paciente> buscarPacientesPorNombre(String nombre);
    
    List<Paciente> buscarPacientesActivosPorNombre(String nombre);

    // Búsqueda por email
    Optional<Paciente> obtenerPacientePorEmail(String email);

    // Búsquedas por ubicación
    List<Paciente> obtenerPacientesPorCiudad(String ciudad);
    
    List<Paciente> obtenerPacientesPorPais(String pais);

    // Búsquedas por condiciones médicas
    List<Paciente> obtenerPacientesConAlergias();
    
    List<Paciente> obtenerPacientesConEnfermedadesCronicas();
    
    List<Paciente> obtenerPacientesConMedicamentosActuales();

    // Búsquedas específicas por condición
    List<Paciente> buscarPacientesPorAlergia(String alergia);
    
    List<Paciente> buscarPacientesPorEnfermedad(String enfermedad);
    
    List<Paciente> buscarPacientesPorMedicamento(String medicamento);

    // Búsquedas por contacto de emergencia
    List<Paciente> obtenerPacientesConContactoEmergencia();
    
    List<Paciente> obtenerPacientesSinContactoEmergencia();

    // Perfil del paciente
    List<Paciente> obtenerPacientesConPerfilCompleto();
    
    List<Paciente> obtenerPacientesConPerfilIncompleto();

    // Conteo de pacientes
    Long contarPacientesActivos();
    
    Long contarPacientes();
    
    Long contarPacientesPorTipoSangre(String tipoSangre);

    // Búsqueda por género
    List<Paciente> obtenerPacientesPorGenero(Usuario.Genero genero);

    // Pacientes recientes
    List<Paciente> obtenerPacientesRecientes();

    // Ordenamiento
    List<Paciente> obtenerTodosLosPacientesOrdenadosPorNombre();

    // Historial médico
    List<Paciente> obtenerPacientesConHistorialMedico();
    
    List<Paciente> obtenerPacientesSinHistorialMedico();

    // Citas programadas
    List<Paciente> obtenerPacientesConCitasProgramadas();

    // Validaciones
    void validarPaciente(Paciente paciente);
    
    boolean identificacionEsUnica(String numeroIdentificacion, Integer idExcluir);
}
