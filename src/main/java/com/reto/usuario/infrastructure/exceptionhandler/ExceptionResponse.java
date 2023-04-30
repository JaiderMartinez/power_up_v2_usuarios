package com.reto.usuario.infrastructure.exceptionhandler;

public enum ExceptionResponse {

    EMAIL_EXISTS_EXCEPTION("The email already exists"),
    INVALID_EMAIL_FORMAT_EXCEPTION("Wrong email structure"),
    EMPTY_FIELDS_EXCEPTION("Fields cannot be empty"),
    AUTHENTICATION_CREDENTIALS_NOT_FOUND_EXCEPTION("Bad credentials"),
    EMAIL_NOT_FOUND_EXCEPTION("Email not found"),
    USER_NOT_FOUND_EXCEPTION("User Not Found"),
    INVALID_CELL_PHONE_FORMAT_EXCEPTION("The cell phone format is wrong"),
    ROL_NOT_FOUND_EXCEPTION("Rol Not Found or invalid role"),
    AUTHENTICATION_FAILED("Failed login by token");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
