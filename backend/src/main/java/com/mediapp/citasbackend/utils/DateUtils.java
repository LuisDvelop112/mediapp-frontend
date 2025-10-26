package com.mediapp.citasbackend.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Clase de utilidades para manejo de fechas y horas
 */
public class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Verifica si una fecha es futura
     */
    public static boolean isFutureDate(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    /**
     * Verifica si una fecha y hora es futura
     */
    public static boolean isFutureDateTime(LocalDateTime dateTime) {
        return dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Verifica si una fecha es pasada
     */
    public static boolean isPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    /**
     * Verifica si una fecha y hora es pasada
     */
    public static boolean isPastDateTime(LocalDateTime dateTime) {
        return dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Verifica si una fecha es hoy
     */
    public static boolean isToday(LocalDate date) {
        return date.isEqual(LocalDate.now());
    }

    /**
     * Calcula la diferencia en días entre dos fechas
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * Calcula la diferencia en horas entre dos fechas y horas
     */
    public static long hoursBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.HOURS.between(start, end);
    }

    /**
     * Calcula la diferencia en minutos entre dos fechas y horas
     */
    public static long minutesBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.MINUTES.between(start, end);
    }

    /**
     * Formatea una fecha en formato dd/MM/yyyy
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * Formatea una hora en formato HH:mm
     */
    public static String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    /**
     * Formatea una fecha y hora en formato dd/MM/yyyy HH:mm
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * Verifica si dos rangos de tiempo se solapan
     */
    public static boolean timeRangesOverlap(LocalDateTime start1, LocalDateTime end1, 
                                           LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /**
     * Verifica si una fecha está dentro de un rango
     */
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Verifica si una fecha y hora está dentro de un rango
     */
    public static boolean isDateTimeInRange(LocalDateTime dateTime, 
                                           LocalDateTime startDateTime, 
                                           LocalDateTime endDateTime) {
        return !dateTime.isBefore(startDateTime) && !dateTime.isAfter(endDateTime);
    }

    /**
     * Obtiene el inicio del día para una fecha dada
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    /**
     * Obtiene el final del día para una fecha dada
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        return date.atTime(23, 59, 59);
    }

    /**
     * Verifica si la hora de inicio es anterior a la hora de fin
     */
    public static boolean isValidTimeRange(LocalTime start, LocalTime end) {
        return start.isBefore(end);
    }

    /**
     * Verifica si la fecha y hora de inicio es anterior a la fecha y hora de fin
     */
    public static boolean isValidDateTimeRange(LocalDateTime start, LocalDateTime end) {
        return start.isBefore(end);
    }

    /**
     * Agrega días laborables a una fecha (excluyendo fines de semana)
     */
    public static LocalDate addBusinessDays(LocalDate date, int days) {
        LocalDate result = date;
        int addedDays = 0;
        
        while (addedDays < days) {
            result = result.plusDays(1);
            if (result.getDayOfWeek().getValue() < 6) { // Lunes a Viernes
                addedDays++;
            }
        }
        
        return result;
    }

    /**
     * Verifica si una fecha es un día laborable (Lunes a Viernes)
     */
    public static boolean isBusinessDay(LocalDate date) {
        return date.getDayOfWeek().getValue() < 6;
    }
}
