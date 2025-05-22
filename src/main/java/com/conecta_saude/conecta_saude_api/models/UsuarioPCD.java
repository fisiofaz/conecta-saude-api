package com.conecta_saude.conecta_saude_api.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity; 


@Entity
@DiscriminatorValue("PCD")
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
    
    public UsuarioPCD() {
        super(); 
    }

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
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipoDeficiencia() {
        return tipoDeficiencia;
    }

    public void setTipoDeficiencia(String tipoDeficiencia) {
        this.tipoDeficiencia = tipoDeficiencia;
    }

    public String getNecessidadesEspecificas() {
        return necessidadesEspecificas;
    }

    public void setNecessidadesEspecificas(String necessidadesEspecificas) {
        this.necessidadesEspecificas = necessidadesEspecificas;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        if (getClass() != o.getClass()) return false;
        UsuarioPCD that = (UsuarioPCD) o;
        return Objects.equals(dataNascimento, that.dataNascimento) &&
               Objects.equals(tipoDeficiencia, that.tipoDeficiencia); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataNascimento, tipoDeficiencia); 
    }

    @Override
    public String toString() {
        return "UsuarioPCD{" +
               "id=" + getId() + 
               ", email='" + getEmail() + '\'' + 
               ", nome='" + nome + '\'' +
               ", dataNascimento=" + dataNascimento +
               '}';
    }
}