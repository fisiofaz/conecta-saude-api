package com.conecta_saude.conecta_saude_api.dto.auth;

public class AuthenticationResponse {

    private String token;
    private String message; 

    public AuthenticationResponse(String token) {
        this.token = token;
        this.message = "Autenticação realizada com sucesso!";
    }

    public AuthenticationResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}