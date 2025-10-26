package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);

    // Verificar si existe un email
    boolean existsByEmail(String email);

    // Buscar usuarios por tipo
    List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    // Buscar usuarios por estado
    List<Usuario> findByEstado(Usuario.Estado estado);

    // Buscar usuarios por tipo y estado
    List<Usuario> findByTipoUsuarioAndEstado(Usuario.TipoUsuario tipoUsuario, Usuario.Estado estado);

    // Buscar usuarios activos por tipo
    @Query("SELECT u FROM Usuario u WHERE u.tipoUsuario = :tipo AND u.estado = 'ACTIVO'")
    List<Usuario> findUsuariosActivosByTipo(@Param("tipo") Usuario.TipoUsuario tipo);

    // Buscar por nombre o apellido (búsqueda parcial)
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
           "OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
    List<Usuario> buscarPorNombreOApellido(@Param("busqueda") String busqueda);

    // Buscar usuarios por ciudad
    List<Usuario> findByCiudad(String ciudad);

    // Buscar usuarios por país
    List<Usuario> findByPais(String pais);

    // Contar usuarios por tipo
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.tipoUsuario = :tipo")
    Long contarUsuariosPorTipo(@Param("tipo") Usuario.TipoUsuario tipo);

    // Contar usuarios activos
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.estado = 'ACTIVO'")
    Long contarUsuariosActivos();

    // Buscar usuarios médicos activos
    @Query("SELECT u FROM Usuario u WHERE u.tipoUsuario = 'MEDICO' AND u.estado = 'ACTIVO'")
    List<Usuario> findMedicosActivos();

    // Buscar usuarios pacientes activos
    @Query("SELECT u FROM Usuario u WHERE u.tipoUsuario = 'PACIENTE' AND u.estado = 'ACTIVO'")
    List<Usuario> findPacientesActivos();

    // Buscar por teléfono
    Optional<Usuario> findByTelefono(String telefono);

    // Verificar si existe un teléfono
    boolean existsByTelefono(String telefono);
}
