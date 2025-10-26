package com.mediapp.citasbackend.exceptions;

/**
 * Excepción lanzada cuando una operación de negocio no es válida
 */
public class BusinessRuleException extends RuntimeException {
    
    public BusinessRuleException(String message) {
        super(message);
    }
    
    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
