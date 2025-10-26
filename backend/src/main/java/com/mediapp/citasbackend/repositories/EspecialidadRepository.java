package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

    // Buscar especialidad por nombre exacto
    Optional<Especialidad> findByNombreEspecialidad(String nombreEspecialidad);

    // Verificar si existe una especialidad por nombre
    boolean existsByNombreEspecialidad(String nombreEspecialidad);

    // Buscar especialidades por estado
    List<Especialidad> findByEstado(Especialidad.Estado estado);

    // Buscar todas las especialidades activas
    @Query("SELECT e FROM Especialidad e WHERE e.estado = 'ACTIVA' ORDER BY e.nombreEspecialidad")
    List<Especialidad> findEspecialidadesActivas();

    // Buscar especialidades por nombre (búsqueda parcial, case-insensitive)
    @Query("SELECT e FROM Especialidad e WHERE LOWER(e.nombreEspecialidad) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Especialidad> buscarPorNombre(@Param("nombre") String nombre);

    // Buscar especialidades activas por nombre parcial
    @Query("SELECT e FROM Especialidad e WHERE LOWER(e.nombreEspecialidad) LIKE LOWER(CONCAT('%', :nombre, '%')) " +
           "AND e.estado = 'ACTIVA' " +
           "ORDER BY e.nombreEspecialidad")
    List<Especialidad> buscarEspecialidadesActivasPorNombre(@Param("nombre") String nombre);

    // Buscar especialidades por descripción (búsqueda parcial)
    @Query("SELECT e FROM Especialidad e WHERE LOWER(e.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Especialidad> buscarPorDescripcion(@Param("texto") String texto);

    // Contar especialidades por estado
    @Query("SELECT COUNT(e) FROM Especialidad e WHERE e.estado = :estado")
    Long contarPorEstado(@Param("estado") Especialidad.Estado estado);

    // Contar especialidades activas
    @Query("SELECT COUNT(e) FROM Especialidad e WHERE e.estado = 'ACTIVA'")
    Long contarEspecialidadesActivas();

    // Obtener todas las especialidades ordenadas por nombre
    @Query("SELECT e FROM Especialidad e ORDER BY e.nombreEspecialidad")
    List<Especialidad> findAllOrdenadas();

    // Buscar especialidades con médicos asociados
    @Query("SELECT DISTINCT e FROM Especialidad e JOIN Medico m ON m.especialidad.idEspecialidad = e.idEspecialidad " +
           "WHERE e.estado = 'ACTIVA'")
    List<Especialidad> findEspecialidadesConMedicos();

    // Verificar si una especialidad tiene un nombre único (excluyendo su propio ID)
    @Query("SELECT COUNT(e) FROM Especialidad e WHERE LOWER(e.nombreEspecialidad) = LOWER(:nombre) " +
           "AND e.idEspecialidad != :idExcluir")
    Long contarPorNombreExcluyendoId(@Param("nombre") String nombre, @Param("idExcluir") Integer idExcluir);

    // Buscar especialidades creadas recientemente (últimas N especialidades)
    @Query("SELECT e FROM Especialidad e ORDER BY e.idEspecialidad DESC")
    List<Especialidad> findEspecialidadesRecientes();
}
