package com.mediapp.citasbackend.services.interfaces;

import com.mediapp.citasbackend.entities.Notificacion;
import com.mediapp.citasbackend.entities.Usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NotificacionService {

    // Operaciones CRUD básicas
    Notificacion guardarNotificacion(Notificacion notificacion);
    
    Notificacion actualizarNotificacion(Integer id, Notificacion notificacion);
    
    void eliminarNotificacion(Integer id);
    
    Optional<Notificacion> obtenerNotificacionPorId(Integer id);
    
    List<Notificacion> obtenerTodasLasNotificaciones();

    // Búsquedas por usuario
    List<Notificacion> obtenerNotificacionesPorUsuario(Usuario usuario);
    
    List<Notificacion> obtenerNotificacionesPorUsuarioId(Integer idUsuario);
    
    List<Notificacion> obtenerNotificacionesPorUsuarioOrdenadas(Integer idUsuario);

    // Búsquedas por estado de lectura
    List<Notificacion> obtenerNotificacionesNoLeidasPorUsuario(Integer idUsuario);
    
    List<Notificacion> obtenerNotificacionesLeidasPorUsuario(Integer idUsuario);
    
    List<Notificacion> obtenerNotificacionesPorEstadoLectura(Boolean leida);
    
    List<Notificacion> obtenerNotificacionesPorUsuarioYEstadoLectura(Integer idUsuario, Boolean leida);

    // Búsquedas por tipo
    List<Notificacion> obtenerNotificacionesPorTipo(Notificacion.TipoNotificacion tipo);
    
    List<Notificacion> obtenerNotificacionesPorUsuarioYTipo(Integer idUsuario, Notificacion.TipoNotificacion tipo);
    
    List<Notificacion> obtenerNotificacionesNoLeidasPorUsuarioYTipo(
            Integer idUsuario, 
            Notificacion.TipoNotificacion tipo
    );

    // Conteo de notificaciones
    Long contarNotificacionesNoLeidasPorUsuario(Integer idUsuario);
    
    Long contarNotificacionesPorUsuario(Integer idUsuario);
    
    Map<Notificacion.TipoNotificacion, Long> obtenerDistribucionNotificacionesPorUsuario(Integer idUsuario);

    // Búsquedas por rango de fechas
    List<Notificacion> obtenerNotificacionesEnRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    List<Notificacion> obtenerNotificacionesPorUsuarioEnRangoFechas(
            Integer idUsuario, 
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin
    );

    // Notificaciones recientes
    List<Notificacion> obtenerNotificacionesRecientesPorUsuario(Integer idUsuario, int horas);
    
    List<Notificacion> obtenerNotificacionesRecientesNoLeidasPorUsuario(Integer idUsuario, int horas);

    // Notificaciones del día
    List<Notificacion> obtenerNotificacionesDelDiaPorUsuario(Integer idUsuario);

    // Recordatorios
    List<Notificacion> obtenerRecordatoriosPendientes();
    
    List<Notificacion> obtenerRecordatoriosPendientesPorUsuario(Integer idUsuario);

    // Notificaciones con enlace
    List<Notificacion> obtenerNotificacionesConEnlace();
    
    List<Notificacion> obtenerNotificacionesConEnlacePorUsuario(Integer idUsuario);

    // Últimas notificaciones
    List<Notificacion> obtenerUltimasNotificacionesPorUsuario(Integer idUsuario, int limite);

    // Marcar como leída
    Notificacion marcarComoLeida(Integer id);
    
    void marcarTodasComoLeidasPorUsuario(Integer idUsuario);
    
    void marcarComoLeidasPorUsuarioYTipo(Integer idUsuario, Notificacion.TipoNotificacion tipo);

    // Eliminar notificaciones antiguas
    void eliminarNotificacionesAntiguas(int dias);
    
    void eliminarNotificacionesLeidasAntiguasPorUsuario(Integer idUsuario, int dias);

    // Validaciones
    void validarNotificacion(Notificacion notificacion);
}
