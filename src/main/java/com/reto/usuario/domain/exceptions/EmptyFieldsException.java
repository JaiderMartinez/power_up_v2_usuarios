package com.reto.usuario.domain.exceptions;

public class EmptyFieldsException extends RuntimeException {
    
    public EmptyFieldsException(String message) {
        super(message);
    }
}
