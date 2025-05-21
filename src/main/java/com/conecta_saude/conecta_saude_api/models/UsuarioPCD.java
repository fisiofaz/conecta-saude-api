package com.conecta_saude.conecta_saude_api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime; 
import java.util.Set; 
import lombok.Builder;

@Entity
@Table(name = "usuarios_pcd")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UsuarioPCD extends User {

	@Column(name = "nome")
	private String nome;
	
	@Column(name = "sobrenome")
    private String sobrenome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;
    
    @Column(name = "telefone")
    private String telefone;

    @Column(name = "tipo_deficiencia")
    private String tipoDeficiencia;

    @Column(name = "necessidades_especificas", columnDefinition = "TEXT")
    private String necessidadesEspecificas;

    @Column(name = "endereco")
    private String endereco;
    
    @Column(name = "cidade")
    private String cidade;
    
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "cep")
    private String cep;

    @Builder
    public UsuarioPCD(
        Long id, String email, String password, boolean enabled,
        LocalDateTime createdAt, LocalDateTime updatedAt, Set<Role> roles, 
        String nome, String sobrenome, LocalDate dataNascimento, String telefone,
        String tipoDeficiencia, String necessidadesEspecificas, String endereco,
        String cidade, String estado, String cep) {
        super(id, email, password, enabled, createdAt, updatedAt, roles); 
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.tipoDeficiencia = tipoDeficiencia;
        this.necessidadesEspecificas = necessidadesEspecificas;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }
}