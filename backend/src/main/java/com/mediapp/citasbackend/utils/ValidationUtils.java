package com.mediapp.citasbackend.utils;

import java.util.regex.Pattern;

/**
 * Clase de utilidades para validaciones comunes
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10}$"
    );

    private static final Pattern CEDULA_PATTERN = Pattern.compile(
        "^[0-9]{6,10}$"
    );

    /**
     * Valida si un email tiene un formato válido
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida si un teléfono tiene un formato válido (10 dígitos)
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Valida si una cédula tiene un formato válido (6 a 10 dígitos)
     */
    public static boolean isValidCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        return CEDULA_PATTERN.matcher(cedula).matches();
    }

    /**
     * Valida si un string no es null ni está vacío
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Valida si un string es null o está vacío
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Valida si un string tiene una longitud mínima
     */
    public static boolean hasMinLength(String value, int minLength) {
        return value != null && value.length() >= minLength;
    }

    /**
     * Valida si un string tiene una longitud máxima
     */
    public static boolean hasMaxLength(String value, int maxLength) {
        return value != null && value.length() <= maxLength;
    }

    /**
     * Valida si un string tiene una longitud dentro de un rango
     */
    public static boolean hasLengthInRange(String value, int minLength, int maxLength) {
        return value != null && value.length() >= minLength && value.length() <= maxLength;
    }

    /**
     * Valida si un número está dentro de un rango
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Valida si un número está dentro de un rango
     */
    public static boolean isInRange(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * Valida si un número es positivo
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }

    /**
     * Valida si un número es positivo
     */
    public static boolean isPositive(double value) {
        return value > 0;
    }

    /**
     * Valida si un número no es negativo
     */
    public static boolean isNonNegative(int value) {
        return value >= 0;
    }

    /**
     * Valida si un número no es negativo
     */
    public static boolean isNonNegative(double value) {
        return value >= 0;
    }

    /**
     * Valida si una contraseña es segura (mínimo 8 caracteres, al menos una mayúscula, 
     * una minúscula y un número)
     */
    public static boolean isStrongPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit;
    }

    /**
     * Normaliza un string eliminando espacios extras
     */
    public static String normalizeString(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().replaceAll("\\s+", " ");
    }

    /**
     * Capitaliza la primera letra de cada palabra
     */
    public static String capitalizeWords(String value) {
        if (value == null || value.trim().isEmpty()) {
            return value;
        }

        String[] words = value.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            if (!words[i].isEmpty()) {
                result.append(Character.toUpperCase(words[i].charAt(0)));
                if (words[i].length() > 1) {
                    result.append(words[i].substring(1));
                }
            }
        }

        return result.toString();
    }
}
