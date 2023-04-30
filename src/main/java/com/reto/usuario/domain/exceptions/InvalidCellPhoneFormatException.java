package com.reto.usuario.domain.exceptions;

public class InvalidCellPhoneFormatException extends RuntimeException {
    public InvalidCellPhoneFormatException(String message) {
        super(message);
    }
}
