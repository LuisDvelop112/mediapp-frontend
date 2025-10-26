package com.mediapp.citasbackend.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manejador global de excepciones para toda la aplicación
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones cuando no se encuentra un recurso
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Recurso No Encontrado",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones cuando un recurso ya existe
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Conflicto",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones de reglas de negocio
     */
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleException(
            BusinessRuleException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Error de Regla de Negocio",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de credenciales inválidas
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
            InvalidCredentialsException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Credenciales Inválidas",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Maneja excepciones de permisos insuficientes
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Acceso Denegado",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Maneja excepciones de datos inválidos
     */
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataException(
            InvalidDataException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Datos Inválidos",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de validación de @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {
        
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Error de Validación",
            "Los datos proporcionados no son válidos",
            request.getRequestURI(),
            errors
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja errores de tipo de argumento incorrecto
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(
            MethodArgumentTypeMismatchException ex, 
            HttpServletRequest request) {
        
        String message = String.format(
            "El parámetro '%s' tiene un valor inválido: '%s'",
            ex.getName(),
            ex.getValue()
        );
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Tipo de Dato Incorrecto",
            message,
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Argumento Inválido",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja todas las demás excepciones no controladas
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, 
            HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error Interno del Servidor",
            "Ha ocurrido un error inesperado. Por favor, contacte al administrador.",
            request.getRequestURI()
        );
        
        // Log del error para debugging (en producción)
        ex.printStackTrace();
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
