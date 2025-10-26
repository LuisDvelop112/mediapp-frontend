package com.mediapp.citasbackend.repositories;

import com.mediapp.citasbackend.entities.Notificacion;
import com.mediapp.citasbackend.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    // Buscar notificaciones de un usuario
    List<Notificacion> findByUsuario(Usuario usuario);

    // Buscar notificaciones de un usuario por ID
    List<Notificacion> findByUsuario_IdUsuario(Integer idUsuario);

    // Buscar notificaciones de un usuario ordenadas por fecha descendente
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesByUsuarioOrdenadas(@Param("idUsuario") Integer idUsuario);

    // Buscar notificaciones no leídas de un usuario
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.leida = false " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesNoLeidasByUsuario(@Param("idUsuario") Integer idUsuario);

    // Buscar notificaciones leídas de un usuario
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.leida = true " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesLeidasByUsuario(@Param("idUsuario") Integer idUsuario);

    // Buscar notificaciones por tipo
    List<Notificacion> findByTipoNotificacion(Notificacion.TipoNotificacion tipoNotificacion);

    // Buscar notificaciones de un usuario por tipo
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.tipoNotificacion = :tipo " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesByUsuarioAndTipo(
            @Param("idUsuario") Integer idUsuario,
            @Param("tipo") Notificacion.TipoNotificacion tipo
    );

    // Buscar notificaciones no leídas de un usuario por tipo
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.tipoNotificacion = :tipo " +
           "AND n.leida = false " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesNoLeidasByUsuarioAndTipo(
            @Param("idUsuario") Integer idUsuario,
            @Param("tipo") Notificacion.TipoNotificacion tipo
    );

    // Contar notificaciones no leídas de un usuario
    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario AND n.leida = false")
    Long contarNotificacionesNoLeidasByUsuario(@Param("idUsuario") Integer idUsuario);

    // Contar notificaciones totales de un usuario
    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario")
    Long contarNotificacionesByUsuario(@Param("idUsuario") Integer idUsuario);

    // Buscar notificaciones en un rango de fechas
    @Query("SELECT n FROM Notificacion n WHERE n.fechaEnvio BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesEnRangoFechas(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    // Buscar notificaciones de un usuario en un rango de fechas
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.fechaEnvio BETWEEN :fechaInicio AND :fechaFin " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesByUsuarioEnRangoFechas(
            @Param("idUsuario") Integer idUsuario,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    // Buscar notificaciones recientes de un usuario (últimas N horas/días)
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.fechaEnvio >= :fechaLimite " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesRecientesByUsuario(
            @Param("idUsuario") Integer idUsuario,
            @Param("fechaLimite") LocalDateTime fechaLimite
    );

    // Buscar notificaciones recientes no leídas
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.leida = false " +
           "AND n.fechaEnvio >= :fechaLimite " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesRecientesNoLeidasByUsuario(
            @Param("idUsuario") Integer idUsuario,
            @Param("fechaLimite") LocalDateTime fechaLimite
    );

    // Marcar notificación como leída
    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.idNotificacion = :idNotificacion")
    void marcarComoLeida(@Param("idNotificacion") Integer idNotificacion);

    // Marcar todas las notificaciones de un usuario como leídas
    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.usuario.idUsuario = :idUsuario AND n.leida = false")
    void marcarTodasComoLeidasByUsuario(@Param("idUsuario") Integer idUsuario);

    // Marcar notificaciones de un tipo como leídas para un usuario
    @Modifying
    @Query("UPDATE Notificacion n SET n.leida = true WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.tipoNotificacion = :tipo AND n.leida = false")
    void marcarComoLeidasByUsuarioAndTipo(
            @Param("idUsuario") Integer idUsuario,
            @Param("tipo") Notificacion.TipoNotificacion tipo
    );

    // Eliminar notificaciones antiguas (antes de una fecha)
    @Modifying
    @Query("DELETE FROM Notificacion n WHERE n.fechaEnvio < :fechaLimite")
    void eliminarNotificacionesAntiguas(@Param("fechaLimite") LocalDateTime fechaLimite);

    // Eliminar notificaciones leídas antiguas de un usuario
    @Modifying
    @Query("DELETE FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.leida = true AND n.fechaEnvio < :fechaLimite")
    void eliminarNotificacionesLeidasAntiguasByUsuario(
            @Param("idUsuario") Integer idUsuario,
            @Param("fechaLimite") LocalDateTime fechaLimite
    );

    // Buscar notificaciones con enlace relacionado
    @Query("SELECT n FROM Notificacion n WHERE n.enlaceRelacionado IS NOT NULL " +
           "AND n.enlaceRelacionado != '' " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesConEnlace();

    // Buscar notificaciones de un usuario con enlace
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.enlaceRelacionado IS NOT NULL AND n.enlaceRelacionado != '' " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesConEnlaceByUsuario(@Param("idUsuario") Integer idUsuario);

    // Buscar últimas N notificaciones de un usuario
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findUltimasNotificacionesByUsuario(@Param("idUsuario") Integer idUsuario);

    // Buscar notificaciones por estado de lectura
    List<Notificacion> findByLeida(Boolean leida);

    // Buscar notificaciones de un usuario por estado de lectura
    List<Notificacion> findByUsuario_IdUsuarioAndLeida(Integer idUsuario, Boolean leida);

    // Buscar notificaciones del día actual de un usuario
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND CAST(n.fechaEnvio AS date) = CURRENT_DATE " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findNotificacionesDelDiaByUsuario(@Param("idUsuario") Integer idUsuario);

    // Buscar recordatorios pendientes (no leídos)
    @Query("SELECT n FROM Notificacion n WHERE n.tipoNotificacion = 'RECORDATORIO' " +
           "AND n.leida = false " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findRecordatoriosPendientes();

    // Buscar recordatorios pendientes de un usuario
    @Query("SELECT n FROM Notificacion n WHERE n.usuario.idUsuario = :idUsuario " +
           "AND n.tipoNotificacion = 'RECORDATORIO' " +
           "AND n.leida = false " +
           "ORDER BY n.fechaEnvio DESC")
    List<Notificacion> findRecordatoriosPendientesByUsuario(@Param("idUsuario") Integer idUsuario);

    // Contar notificaciones por tipo de un usuario
    @Query("SELECT n.tipoNotificacion, COUNT(n) FROM Notificacion n " +
           "WHERE n.usuario.idUsuario = :idUsuario " +
           "GROUP BY n.tipoNotificacion")
    List<Object[]> contarNotificacionesPorTipoByUsuario(@Param("idUsuario") Integer idUsuario);
}
