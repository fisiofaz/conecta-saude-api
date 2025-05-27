package com.conecta_saude.conecta_saude_api.models;

import java.util.Set;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN") 
public class AdminUser extends User {

    private static final long serialVersionUID = 1L; 

    public AdminUser() {
        super();
    }

    
    public AdminUser(String email, String password, Set<Role> roles) {
        super(); 
        this.setEmail(email);
        this.setPassword(password); 
        this.setRoles(roles);
        this.setEnabled(true); 
    }

    

    @Override
    public String toString() {
        return "AdminUser{" +
               "id=" + getId() +
               ", email='" + getEmail() + '\'' +
               '}';
    }
}