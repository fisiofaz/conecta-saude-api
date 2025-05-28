package com.conecta_saude.conecta_saude_api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthenticationRequest {
	
	@Schema(description = "Email do usuário para login.", example = "usuario@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O email deve ser válido.")
    private String email;

	@Schema(description = "Senha do usuário (mínimo 6 caracteres).", example = "senha123", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.") 
    private String password;

    
    public AuthenticationRequest() {
    }
    
    public AuthenticationRequest(String email, String password) {
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

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
               "email='" + email + '\'' +
               ", password='[PROTECTED]'" + 
               '}';
    }
}