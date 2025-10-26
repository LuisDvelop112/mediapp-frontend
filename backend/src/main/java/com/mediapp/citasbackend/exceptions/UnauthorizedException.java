package com.mediapp.citasbackend.exceptions;

/**
 * Excepción lanzada cuando un usuario no tiene permisos para realizar una acción
 */
public class UnauthorizedException extends RuntimeException {
    
    public UnauthorizedException(String message) {
        super(message);
    }
    
    public UnauthorizedException() {
        super("No tienes permisos para realizar esta acción");
    }
}
