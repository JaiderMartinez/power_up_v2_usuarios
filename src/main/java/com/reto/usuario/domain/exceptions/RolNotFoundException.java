package com.reto.usuario.domain.exceptions;

public class RolNotFoundException extends RuntimeException {

    public RolNotFoundException(String message) {
        super(message);
    }
}
