package com.conecta_saude.conecta_saude_api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthenticationResponse {

	@Schema(description = "Token JWT gerado após autenticação bem-sucedida.", 
            example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbk...")
	private String token;
    
	@Schema(description = "Mensagem indicando o resultado da autenticação.", example = "Autenticação bem-sucedida!")
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