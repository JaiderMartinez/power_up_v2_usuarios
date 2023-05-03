package com.reto.usuario.infrastructure.drivenadapter.exceptions;

public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(String message) {
        super(message);
    }
}
