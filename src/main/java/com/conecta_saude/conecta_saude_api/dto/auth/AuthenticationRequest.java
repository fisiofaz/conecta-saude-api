package com.conecta_saude.conecta_saude_api.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthenticationRequest {

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O email deve ser válido.")
    private String email;

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