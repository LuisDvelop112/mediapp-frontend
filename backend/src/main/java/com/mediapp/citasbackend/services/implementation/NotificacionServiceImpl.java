package com.mediapp.citasbackend.services.implementation;

import com.mediapp.citasbackend.entities.Notificacion;
import com.mediapp.citasbackend.entities.Usuario;
import com.mediapp.citasbackend.repositories.NotificacionRepository;
import com.mediapp.citasbackend.services.interfaces.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository notificacionRepository;

    @Override
    public Notificacion guardarNotificacion(Notificacion notificacion) {
        validarNotificacion(notificacion);
        return notificacionRepository.save(notificacion);
    }

    @Override
    public Notificacion actualizarNotificacion(Integer id, Notificacion notificacion) {
        Notificacion notificacionExistente = notificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada con ID: " + id));

        validarNotificacion(notificacion);

        // Actualizar campos
        notificacionExistente.setUsuario(notificacion.getUsuario());
        notificacionExistente.setTipoNotificacion(notificacion.getTipoNotificacion());
        notificacionExistente.setTitulo(notificacion.getTitulo());
        notificacionExistente.setContenido(notificacion.getContenido());
        notificacionExistente.setLeida(notificacion.getLeida());
        notificacionExistente.setEnlaceRelacionado(notificacion.getEnlaceRelacionado());

        return notificacionRepository.save(notificacionExistente);
    }

    @Override
    public void eliminarNotificacion(Integer id) {
        if (!notificacionRepository.existsById(id)) {
            throw new IllegalArgumentException("Notificación no encontrada con ID: " + id);
        }
        notificacionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Notificacion> obtenerNotificacionPorId(Integer id) {
        return notificacionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerTodasLasNotificaciones() {
        return notificacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesPorUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        return notificacionRepository.findByUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesPorUsuarioId(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionRepository.findByUsuario_IdUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesPorUsuarioOrdenadas(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionRepository.findNotificacionesByUsuarioOrdenadas(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesNoLeidasPorUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionRepository.findNotificacionesNoLeidasByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesLeidasPorUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionRepository.findNotificacionesLeidasByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesPorEstadoLectura(Boolean leida) {
        if (leida == null) {
            throw new IllegalArgumentException("El estado de lectura no puede ser nulo");
        }
        return notificacionRepository.findByLeida(leida);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesPorUsuarioYEstadoLectura(Integer idUsuario, Boolean leida) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (leida == null) {
            throw new IllegalArgumentException("El estado de lectura no puede ser nulo");
        }
        return notificacionRepository.findByUsuario_IdUsuarioAndLeida(idUsuario, leida);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesPorTipo(Notificacion.TipoNotificacion tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de notificación no puede ser nulo");
        }
        return notificacionRepository.findByTipoNotificacion(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesPorUsuarioYTipo(
            Integer idUsuario, 
            Notificacion.TipoNotificacion tipo) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de notificación no puede ser nulo");
        }
        return notificacionRepository.findNotificacionesByUsuarioAndTipo(idUsuario, tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesNoLeidasPorUsuarioYTipo(
            Integer idUsuario, 
            Notificacion.TipoNotificacion tipo) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de notificación no puede ser nulo");
        }
        return notificacionRepository.findNotificacionesNoLeidasByUsuarioAndTipo(idUsuario, tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarNotificacionesNoLeidasPorUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionRepository.contarNotificacionesNoLeidasByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarNotificacionesPorUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionRepository.contarNotificacionesByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Notificacion.TipoNotificacion, Long> obtenerDistribucionNotificacionesPorUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        
        List<Object[]> resultados = notificacionRepository.contarNotificacionesPorTipoByUsuario(idUsuario);
        Map<Notificacion.TipoNotificacion, Long> distribucion = new HashMap<>();
        
        // Inicializar todos los tipos con 0
        for (Notificacion.TipoNotificacion tipo : Notificacion.TipoNotificacion.values()) {
            distribucion.put(tipo, 0L);
        }
        
        // Llenar con los datos reales
        for (Object[] resultado : resultados) {
            Notificacion.TipoNotificacion tipo = (Notificacion.TipoNotificacion) resultado[0];
            Long count = (Long) resultado[1];
            distribucion.put(tipo, count);
        }
        
        return distribucion;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesEnRangoFechas(
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return notificacionRepository.findNotificacionesEnRangoFechas(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesPorUsuarioEnRangoFechas(
            Integer idUsuario, 
            LocalDateTime fechaInicio, 
            LocalDateTime fechaFin) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior o igual a la fecha de fin");
        }
        return notificacionRepository.findNotificacionesByUsuarioEnRangoFechas(
                idUsuario, 
                fechaInicio, 
                fechaFin
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesRecientesPorUsuario(Integer idUsuario, int horas) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (horas <= 0) {
            throw new IllegalArgumentException("Las horas deben ser mayores a cero");
        }
        
        LocalDateTime fechaLimite = LocalDateTime.now().minusHours(horas);
        return notificacionRepository.findNotificacionesRecientesByUsuario(idUsuario, fechaLimite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesRecientesNoLeidasPorUsuario(Integer idUsuario, int horas) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (horas <= 0) {
            throw new IllegalArgumentException("Las horas deben ser mayores a cero");
        }
        
        LocalDateTime fechaLimite = LocalDateTime.now().minusHours(horas);
        return notificacionRepository.findNotificacionesRecientesNoLeidasByUsuario(idUsuario, fechaLimite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesDelDiaPorUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionRepository.findNotificacionesDelDiaByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerRecordatoriosPendientes() {
        return notificacionRepository.findRecordatoriosPendientes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerRecordatoriosPendientesPorUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionRepository.findRecordatoriosPendientesByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesConEnlace() {
        return notificacionRepository.findNotificacionesConEnlace();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerNotificacionesConEnlacePorUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        return notificacionRepository.findNotificacionesConEnlaceByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacion> obtenerUltimasNotificacionesPorUsuario(Integer idUsuario, int limite) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite debe ser mayor a cero");
        }
        
        List<Notificacion> notificaciones = notificacionRepository.findUltimasNotificacionesByUsuario(idUsuario);
        
        // Limitar los resultados
        if (notificaciones.size() > limite) {
            return notificaciones.subList(0, limite);
        }
        
        return notificaciones;
    }

    @Override
    public Notificacion marcarComoLeida(Integer id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificación no encontrada con ID: " + id));
        
        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }

    @Override
    public void marcarTodasComoLeidasPorUsuario(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        notificacionRepository.marcarTodasComoLeidasByUsuario(idUsuario);
    }

    @Override
    public void marcarComoLeidasPorUsuarioYTipo(Integer idUsuario, Notificacion.TipoNotificacion tipo) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de notificación no puede ser nulo");
        }
        notificacionRepository.marcarComoLeidasByUsuarioAndTipo(idUsuario, tipo);
    }

    @Override
    public void eliminarNotificacionesAntiguas(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días deben ser mayores a cero");
        }
        
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        notificacionRepository.eliminarNotificacionesAntiguas(fechaLimite);
    }

    @Override
    public void eliminarNotificacionesLeidasAntiguasPorUsuario(Integer idUsuario, int dias) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
        }
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días deben ser mayores a cero");
        }
        
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        notificacionRepository.eliminarNotificacionesLeidasAntiguasByUsuario(idUsuario, fechaLimite);
    }

    @Override
    public void validarNotificacion(Notificacion notificacion) {
        if (notificacion == null) {
            throw new IllegalArgumentException("La notificación no puede ser nula");
        }

        if (notificacion.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (notificacion.getTipoNotificacion() == null) {
            throw new IllegalArgumentException("El tipo de notificación es obligatorio");
        }

        if (notificacion.getTitulo() == null || notificacion.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }

        // Validar longitud del título
        if (notificacion.getTitulo().length() > 100) {
            throw new IllegalArgumentException("El título no puede exceder 100 caracteres");
        }

        // Validar longitud del contenido (si se proporciona)
        if (notificacion.getContenido() != null && notificacion.getContenido().length() > 5000) {
            throw new IllegalArgumentException("El contenido no puede exceder 5000 caracteres");
        }

        // Validar longitud del enlace (si se proporciona)
        if (notificacion.getEnlaceRelacionado() != null && 
            notificacion.getEnlaceRelacionado().length() > 255) {
            throw new IllegalArgumentException("El enlace relacionado no puede exceder 255 caracteres");
        }

        // Validar formato del enlace (básico)
        if (notificacion.getEnlaceRelacionado() != null && 
            !notificacion.getEnlaceRelacionado().trim().isEmpty()) {
            String enlace = notificacion.getEnlaceRelacionado().trim();
            if (!enlace.startsWith("http://") && !enlace.startsWith("https://") && 
                !enlace.startsWith("/")) {
                throw new IllegalArgumentException("El enlace relacionado debe ser una URL válida o una ruta");
            }
        }
    }
}
