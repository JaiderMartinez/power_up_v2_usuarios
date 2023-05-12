package com.reto.usuario.infrastructure.exceptionhandler;

public enum ExceptionResponse {

    EMAIL_EXISTS("The email already exists"),
    INVALID_EMAIL_FORMAT("Wrong email structure"),
    EMPTY_FIELDS("Fields cannot be empty"),
    AUTHENTICATION_CREDENTIALS_NOT_FOUND("Bad credentials"),
    EMAIL_NOT_FOUND("Email not found"),
    USER_NOT_FOUND("User Not Found"),
    INVALID_CELL_PHONE_FORMAT("The cell phone format is wrong"),
    ROL_NOT_FOUND("Role Not Found or invalid role"),
    AUTHENTICATION_FAILED("Error token could not be read"),
    ROLE_IN_TOKEN_IS_INVALID("The user role is incorrect, please log in again"),
    TOKEN_INVALID("token not supported"),
    EMPLOYEE_RESTAURANT_EXIST("The employee user already exists in the restaurant."),
    UNEXPECTED_ERROR("Unexpected error"),
    RESTAURANT_NOT_FOUND("No restaurant found for you as owner"),
    ACCESS_DENIED("Access denied by insufficient permissions");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
