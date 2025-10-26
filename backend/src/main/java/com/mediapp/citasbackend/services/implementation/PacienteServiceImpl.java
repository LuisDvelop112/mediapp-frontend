package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.Paciente;
import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.exceptions.InvalidDataException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.exceptions.ResourceNotFoundException;
import com.mediapp.citasbackend.repositories.PacienteRepository;
import com.mediapp.citasbackend.services.interfaces.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    @Override
    public Paciente guardarPaciente(Paciente paciente) {
        validarPaciente(paciente);
        
        // Verificar que el usuario no esté ya asociado a otro paciente
        if (pacienteRepository.existsByUsuario_IdUsuario(paciente.getUsuario().getIdUsuario())) {
            throw new ResourceAlreadyExistsException("El usuario ya está asociado a un perfil de paciente");
        }
        
        // Verificar que el número de identificación sea único (si se proporciona)
        if (paciente.getNumeroIdentificacion() != null && !paciente.getNumeroIdentificacion().isEmpty()) {
            if (pacienteRepository.existsByNumeroIdentificacion(paciente.getNumeroIdentificacion())) {
                throw new ResourceAlreadyExistsException("Paciente", "número de identificación", paciente.getNumeroIdentificacion());
            }
        }
        
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente actualizarPaciente(Integer id, Paciente paciente) {
        Paciente pacienteExistente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente", "ID", id));

        validarPaciente(paciente);

        // Verificar que el número de identificación sea único (excluyendo el paciente actual)
        if (paciente.getNumeroIdentificacion() != null && !paciente.getNumeroIdentificacion().isEmpty()) {
            if (!paciente.getNumeroIdentificacion().equals(pacienteExistente.getNumeroIdentificacion())) {
                if (!identificacionEsUnica(paciente.getNumeroIdentificacion(), id)) {
                    throw new ResourceAlreadyExistsException("Paciente", "número de identificación", paciente.getNumeroIdentificacion());
                }
            }
        }

        // Actualizar campos
        pacienteExistente.setUsuario(paciente.getUsuario());
        pacienteExistente.setNumeroIdentificacion(paciente.getNumeroIdentificacion());
        pacienteExistente.setTipoSangre(paciente.getTipoSangre());
        pacienteExistente.setAlergias(paciente.getAlergias());
        pacienteExistente.setEnfermedadesCronicas(paciente.getEnfermedadesCronicas());
        pacienteExistente.setMedicamentosActuales(paciente.getMedicamentosActuales());
        pacienteExistente.setContactoEmergencia(paciente.getContactoEmergencia());
        pacienteExistente.setTelefonoEmergencia(paciente.getTelefonoEmergencia());

        return pacienteRepository.save(pacienteExistente);
    }

    @Override
    public void eliminarPaciente(Integer id) {
        if (!pacienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Paciente", "ID", id);
        }
        pacienteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> obtenerPacientePorId(Integer id) {
        return pacienteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerTodosLosPacientes() {
        return pacienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> obtenerPacientePorUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new InvalidDataException("El usuario no puede ser nulo");
        }
        return pacienteRepository.findByUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> obtenerPacientePorUsuarioId(Integer idUsuario) {
        if (idUsuario == null) {
            throw new InvalidDataException("El ID del usuario no puede ser nulo");
        }
        return pacienteRepository.findByUsuario_IdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePacientePorUsuarioId(Integer idUsuario) {
        if (idUsuario == null) {
            return false;
        }
        return pacienteRepository.existsByUsuario_IdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> obtenerPacientePorNumeroIdentificacion(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            throw new InvalidDataException("El número de identificación no puede estar vacío");
        }
        return pacienteRepository.findByNumeroIdentificacion(numeroIdentificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeNumeroIdentificacion(String numeroIdentificacion) {
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            return false;
        }
        return pacienteRepository.existsByNumeroIdentificacion(numeroIdentificacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesPorTipoSangre(String tipoSangre) {
        if (tipoSangre == null || tipoSangre.trim().isEmpty()) {
            throw new InvalidDataException("El tipo de sangre no puede estar vacío");
        }
        return pacienteRepository.findByTipoSangre(tipoSangre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesSinTipoSangre() {
        return pacienteRepository.findPacientesSinTipoSangre();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesActivos() {
        return pacienteRepository.findPacientesActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> buscarPacientesPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return pacienteRepository.buscarPacientesPorNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> buscarPacientesActivosPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return pacienteRepository.buscarPacientesActivosPorNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Paciente> obtenerPacientePorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidDataException("El email no puede estar vacío");
        }
        return pacienteRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesPorCiudad(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new InvalidDataException("La ciudad no puede estar vacía");
        }
        return pacienteRepository.findPacientesByCiudad(ciudad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesPorPais(String pais) {
        if (pais == null || pais.trim().isEmpty()) {
            throw new InvalidDataException("El país no puede estar vacío");
        }
        return pacienteRepository.findPacientesByPais(pais);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesConAlergias() {
        return pacienteRepository.findPacientesConAlergias();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesConEnfermedadesCronicas() {
        return pacienteRepository.findPacientesConEnfermedadesCronicas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesConMedicamentosActuales() {
        return pacienteRepository.findPacientesConMedicamentosActuales();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> buscarPacientesPorAlergia(String alergia) {
        if (alergia == null || alergia.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return pacienteRepository.buscarPacientesPorAlergia(alergia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> buscarPacientesPorEnfermedad(String enfermedad) {
        if (enfermedad == null || enfermedad.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return pacienteRepository.buscarPacientesPorEnfermedad(enfermedad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> buscarPacientesPorMedicamento(String medicamento) {
        if (medicamento == null || medicamento.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return pacienteRepository.buscarPacientesPorMedicamento(medicamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesConContactoEmergencia() {
        return pacienteRepository.findPacientesConContactoEmergencia();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesSinContactoEmergencia() {
        return pacienteRepository.findPacientesSinContactoEmergencia();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesConPerfilCompleto() {
        return pacienteRepository.findPacientesConPerfilCompleto();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesConPerfilIncompleto() {
        return pacienteRepository.findPacientesConPerfilIncompleto();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPacientesActivos() {
        return pacienteRepository.contarPacientesActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPacientes() {
        return pacienteRepository.contarPacientes();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarPacientesPorTipoSangre(String tipoSangre) {
        if (tipoSangre == null || tipoSangre.trim().isEmpty()) {
            throw new InvalidDataException("El tipo de sangre no puede estar vacío");
        }
        return pacienteRepository.contarPacientesPorTipoSangre(tipoSangre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesPorGenero(Usuario.Genero genero) {
        if (genero == null) {
            throw new InvalidDataException("El género no puede ser nulo");
        }
        return pacienteRepository.findPacientesByGenero(genero);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesRecientes() {
        return pacienteRepository.findPacientesRecientes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerTodosLosPacientesOrdenadosPorNombre() {
        return pacienteRepository.findAllOrdenadosPorNombre();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesConHistorialMedico() {
        return pacienteRepository.findPacientesConHistorialMedico();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesSinHistorialMedico() {
        return pacienteRepository.findPacientesSinHistorialMedico();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Paciente> obtenerPacientesConCitasProgramadas() {
        return pacienteRepository.findPacientesConCitasProgramadas();
    }

    @Override
    public void validarPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new InvalidDataException("El paciente no puede ser nulo");
        }

        if (paciente.getUsuario() == null) {
            throw new InvalidDataException("El usuario es obligatorio");
        }

        // Validar número de identificación (si se proporciona)
        if (paciente.getNumeroIdentificacion() != null && !paciente.getNumeroIdentificacion().trim().isEmpty()) {
            if (paciente.getNumeroIdentificacion().length() > 50) {
                throw new InvalidDataException("El número de identificación no puede exceder 50 caracteres");
            }
        }

        // Validar tipo de sangre (si se proporciona)
        if (paciente.getTipoSangre() != null && !paciente.getTipoSangre().trim().isEmpty()) {
            if (paciente.getTipoSangre().length() > 5) {
                throw new InvalidDataException("El tipo de sangre no puede exceder 5 caracteres");
            }
            
            // Validar formato de tipo de sangre (opcional pero recomendado)
            String tipoSangre = paciente.getTipoSangre().toUpperCase().trim();
            if (!tipoSangre.matches("^(A|B|AB|O)[+-]$")) {
                throw new InvalidDataException(
                    "El tipo de sangre debe tener un formato válido (A+, A-, B+, B-, AB+, AB-, O+, O-)"
                );
            }
        }

        // Validar longitud de alergias (si se proporciona)
        if (paciente.getAlergias() != null && paciente.getAlergias().length() > 5000) {
            throw new InvalidDataException("Las alergias no pueden exceder 5000 caracteres");
        }

        // Validar longitud de enfermedades crónicas (si se proporciona)
        if (paciente.getEnfermedadesCronicas() != null && paciente.getEnfermedadesCronicas().length() > 5000) {
            throw new InvalidDataException("Las enfermedades crónicas no pueden exceder 5000 caracteres");
        }

        // Validar longitud de medicamentos actuales (si se proporciona)
        if (paciente.getMedicamentosActuales() != null && paciente.getMedicamentosActuales().length() > 5000) {
            throw new InvalidDataException("Los medicamentos actuales no pueden exceder 5000 caracteres");
        }

        // Validar contacto de emergencia (si se proporciona)
        if (paciente.getContactoEmergencia() != null && !paciente.getContactoEmergencia().trim().isEmpty()) {
            if (paciente.getContactoEmergencia().length() > 100) {
                throw new InvalidDataException("El contacto de emergencia no puede exceder 100 caracteres");
            }
        }

        // Validar teléfono de emergencia (si se proporciona)
        if (paciente.getTelefonoEmergencia() != null && !paciente.getTelefonoEmergencia().trim().isEmpty()) {
            if (paciente.getTelefonoEmergencia().length() > 20) {
                throw new InvalidDataException("El teléfono de emergencia no puede exceder 20 caracteres");
            }
            
            // Validación básica de formato de teléfono (solo números, espacios, guiones, paréntesis, +)
            if (!paciente.getTelefonoEmergencia().matches("^[0-9\\s\\-\\(\\)\\+]+$")) {
                throw new InvalidDataException(
                    "El teléfono de emergencia solo puede contener números, espacios, guiones, paréntesis y el símbolo +"
                );
            }
        }

        // Validar que si hay teléfono de emergencia, también haya contacto de emergencia
        if (paciente.getTelefonoEmergencia() != null && !paciente.getTelefonoEmergencia().trim().isEmpty()) {
            if (paciente.getContactoEmergencia() == null || paciente.getContactoEmergencia().trim().isEmpty()) {
                throw new InvalidDataException(
                    "Si proporciona un teléfono de emergencia, debe proporcionar también un contacto de emergencia"
                );
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean identificacionEsUnica(String numeroIdentificacion, Integer idExcluir) {
        if (numeroIdentificacion == null || numeroIdentificacion.trim().isEmpty()) {
            return true; // Identificación vacía no se valida como única
        }
        
        if (idExcluir == null) {
            return !pacienteRepository.existsByNumeroIdentificacion(numeroIdentificacion);
        }
        
        Long count = pacienteRepository.contarPorIdentificacionExcluyendoId(numeroIdentificacion, idExcluir);
        return count == 0;
    }
}
