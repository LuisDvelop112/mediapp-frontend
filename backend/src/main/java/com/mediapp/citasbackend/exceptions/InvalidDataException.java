package com.mediapp.citasbackend.exceptions;

/**
 * Excepción lanzada cuando los datos de entrada no son válidos
 */
public class InvalidDataException extends RuntimeException {
    
    public InvalidDataException(String message) {
        super(message);
    }
    
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
