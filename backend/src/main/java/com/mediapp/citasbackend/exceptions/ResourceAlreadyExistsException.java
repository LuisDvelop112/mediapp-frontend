package com.mediapp.citasbackend.exceptions;

/**
 * Excepción lanzada cuando ya existe un recurso con los mismos datos únicos
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
    
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s ya existe con %s: '%s'", resourceName, fieldName, fieldValue));
    }
}
