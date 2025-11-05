package com.avaliacao.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.Data;
import lombok.NoArgsConstructor;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        
        Class<?> requiredType = ex.getRequiredType();
        String typeName = requiredType != null ? requiredType.getSimpleName() : "tipo válido";
        String message = String.format("Parâmetro '%s' deve ser do tipo %s", 
            ex.getName(), typeName);
        
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(message));
    }

    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex) {
        
        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("não encontrado")) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException ex) {
        return ResponseEntity.notFound().build();
    }

   
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("Erro interno do servidor"));
    }

    @Data
    @NoArgsConstructor
    public static class ErrorResponse {
        private String erro;
        private long timestamp = System.currentTimeMillis();

        public ErrorResponse(String erro) {
            this.erro = erro;
            this.timestamp = System.currentTimeMillis();
        }
    }


}