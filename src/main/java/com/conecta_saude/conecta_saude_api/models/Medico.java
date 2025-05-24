package com.conecta_saude.conecta_saude_api.models;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue; 
@Entity
@DiscriminatorValue("Medico") 
public class Medico extends User { 
	
	private static final long serialVersionUID = -4259094873189869258L;
	@Column(name = "especialidade", nullable = true) 
    private String especialidade;

   
    public Medico() {
    }

    public Medico(Long id, String email, String password, boolean enabled,
                  LocalDateTime createdAt, LocalDateTime updatedAt, Set<Role> roles,
                  String especialidade) {
        super(id, email, password, enabled, createdAt, updatedAt, roles);
        this.especialidade = especialidade;
    }

   
    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return "Medico{" +
                "id=" + getId() + 
                ", email='" + getEmail() + '\'' + 
                ", especialidade='" + especialidade + '\'' +
                '}';
    }
}