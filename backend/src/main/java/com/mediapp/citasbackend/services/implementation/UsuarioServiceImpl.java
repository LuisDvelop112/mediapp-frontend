package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.exceptions.InvalidDataException;
import com.mediapp.citasbackend.exceptions.ResourceAlreadyExistsException;
import com.mediapp.citasbackend.exceptions.ResourceNotFoundException;
import com.mediapp.citasbackend.repositories.UsuarioRepository;
import com.mediapp.citasbackend.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        validarUsuario(usuario);
        
        // Validar email único
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ResourceAlreadyExistsException("Usuario", "email", usuario.getEmail());
        }
        
        // Validar teléfono único si se proporciona
        if (usuario.getTelefono() != null && !usuario.getTelefono().isEmpty()) {
            if (usuarioRepository.existsByTelefono(usuario.getTelefono())) {
                throw new ResourceAlreadyExistsException("Usuario", "teléfono", usuario.getTelefono());
            }
        }
        
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Integer id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));

        validarUsuario(usuario);

        // Validar email único (excepto el actual)
        if (!usuarioExistente.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(usuario.getEmail())) {
                throw new ResourceAlreadyExistsException("Usuario", "email", usuario.getEmail());
            }
        }

        // Validar teléfono único si se proporciona (excepto el actual)
        if (usuario.getTelefono() != null && !usuario.getTelefono().isEmpty()) {
            if (!usuario.getTelefono().equals(usuarioExistente.getTelefono())) {
                if (usuarioRepository.existsByTelefono(usuario.getTelefono())) {
                    throw new ResourceAlreadyExistsException("Usuario", "teléfono", usuario.getTelefono());
                }
            }
        }

        // Actualizar campos
        usuarioExistente.setNombre(usuario.getNombre());
        usuarioExistente.setApellido(usuario.getApellido());
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setFechaNacimiento(usuario.getFechaNacimiento());
        usuarioExistente.setGenero(usuario.getGenero());
        usuarioExistente.setTelefono(usuario.getTelefono());
        usuarioExistente.setDireccion(usuario.getDireccion());
        usuarioExistente.setCiudad(usuario.getCiudad());
        usuarioExistente.setPais(usuario.getPais());
        usuarioExistente.setTipoUsuario(usuario.getTipoUsuario());
        usuarioExistente.setFotoPerfil(usuario.getFotoPerfil());
        usuarioExistente.setEstado(usuario.getEstado());

        // Actualizar contraseña solo si se proporciona una nueva
        if (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty()) {
            usuarioExistente.setContraseña(usuario.getContraseña());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    public void eliminarUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario", "ID", id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorTipo(Usuario.TipoUsuario tipoUsuario) {
        return usuarioRepository.findByTipoUsuario(tipoUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorEstado(Usuario.Estado estado) {
        return usuarioRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorTipoYEstado(Usuario.TipoUsuario tipoUsuario, Usuario.Estado estado) {
        return usuarioRepository.findByTipoUsuarioAndEstado(tipoUsuario, estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivosPorTipo(Usuario.TipoUsuario tipoUsuario) {
        return usuarioRepository.findUsuariosActivosByTipo(tipoUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarUsuariosPorNombreOApellido(String busqueda) {
        if (busqueda == null || busqueda.trim().isEmpty()) {
            throw new InvalidDataException("El término de búsqueda no puede estar vacío");
        }
        return usuarioRepository.buscarPorNombreOApellido(busqueda);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorCiudad(String ciudad) {
        return usuarioRepository.findByCiudad(ciudad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorPais(String pais) {
        return usuarioRepository.findByPais(pais);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarUsuariosPorTipo(Usuario.TipoUsuario tipoUsuario) {
        return usuarioRepository.contarUsuariosPorTipo(tipoUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarUsuariosActivos() {
        return usuarioRepository.contarUsuariosActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerMedicosActivos() {
        return usuarioRepository.findMedicosActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerPacientesActivos() {
        return usuarioRepository.findPacientesActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorTelefono(String telefono) {
        return usuarioRepository.findByTelefono(telefono);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeTelefono(String telefono) {
        return usuarioRepository.existsByTelefono(telefono);
    }

    @Override
    public Usuario activarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));
        usuario.setEstado(Usuario.Estado.ACTIVO);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario desactivarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));
        usuario.setEstado(Usuario.Estado.INACTIVO);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario bloquearUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));
        usuario.setEstado(Usuario.Estado.BLOQUEADO);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new InvalidDataException("El usuario no puede ser nulo");
        }

        if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
            throw new InvalidDataException("El nombre es obligatorio");
        }

        if (usuario.getApellido() == null || usuario.getApellido().trim().isEmpty()) {
            throw new InvalidDataException("El apellido es obligatorio");
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new InvalidDataException("El email es obligatorio");
        }

        // Validación básica de formato de email
        if (!usuario.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidDataException("El formato del email no es válido");
        }

        if (usuario.getContraseña() == null || usuario.getContraseña().trim().isEmpty()) {
            throw new InvalidDataException("La contraseña es obligatoria");
        }

        if (usuario.getContraseña().length() < 8) {
            throw new InvalidDataException("La contraseña debe tener al menos 8 caracteres");
        }

        if (usuario.getTipoUsuario() == null) {
            throw new InvalidDataException("El tipo de usuario es obligatorio");
        }
    }
}
