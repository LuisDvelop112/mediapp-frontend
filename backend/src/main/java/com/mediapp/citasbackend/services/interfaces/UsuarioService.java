package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    // Operaciones CRUD básicas
    Usuario guardarUsuario(Usuario usuario);
    
    Usuario actualizarUsuario(Integer id, Usuario usuario);
    
    void eliminarUsuario(Integer id);
    
    Optional<Usuario> obtenerUsuarioPorId(Integer id);
    
    List<Usuario> obtenerTodosLosUsuarios();

    // Búsqueda por email
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
    
    boolean existeEmail(String email);

    // Búsqueda por tipo de usuario
    List<Usuario> obtenerUsuariosPorTipo(Usuario.TipoUsuario tipoUsuario);

    // Búsqueda por estado
    List<Usuario> obtenerUsuariosPorEstado(Usuario.Estado estado);

    // Búsqueda por tipo y estado
    List<Usuario> obtenerUsuariosPorTipoYEstado(Usuario.TipoUsuario tipoUsuario, Usuario.Estado estado);

    // Usuarios activos por tipo
    List<Usuario> obtenerUsuariosActivosPorTipo(Usuario.TipoUsuario tipoUsuario);

    // Búsqueda por nombre o apellido
    List<Usuario> buscarUsuariosPorNombreOApellido(String busqueda);

    // Búsqueda por ubicación
    List<Usuario> obtenerUsuariosPorCiudad(String ciudad);
    
    List<Usuario> obtenerUsuariosPorPais(String pais);

    // Conteo de usuarios
    Long contarUsuariosPorTipo(Usuario.TipoUsuario tipoUsuario);
    
    Long contarUsuariosActivos();

    // Búsquedas específicas
    List<Usuario> obtenerMedicosActivos();
    
    List<Usuario> obtenerPacientesActivos();

    // Búsqueda por teléfono
    Optional<Usuario> obtenerUsuarioPorTelefono(String telefono);
    
    boolean existeTelefono(String telefono);

    // Operaciones de estado
    Usuario activarUsuario(Integer id);
    
    Usuario desactivarUsuario(Integer id);
    
    Usuario bloquearUsuario(Integer id);

    // Validaciones
    void validarUsuario(Usuario usuario);
}
