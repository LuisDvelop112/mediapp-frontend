package com.mediapp.citasbackend.exceptions;

/**
 * Excepción lanzada cuando las credenciales de autenticación son inválidas
 */
public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
    
    public InvalidCredentialsException() {
        super("Email o contraseña incorrectos");
    }
}
