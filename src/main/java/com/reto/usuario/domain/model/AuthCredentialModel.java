package com.reto.usuario.domain.model;

public class AuthCredentialModel {

    private String email;
    private String password;

    public AuthCredentialModel() {}

    public AuthCredentialModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
