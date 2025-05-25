package com.conecta_saude.conecta_saude_api.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity; 

@Entity
@DiscriminatorValue("PROFISSIONAL")
public class ProfissionalDeSaude extends User {

	
	private static final long serialVersionUID = 1691934482956311466L;

	@Column(name = "nome")
	private String nome;

	@Column(name = "sobrenome")
    private String sobrenome;
	
	 @Column(name = "telefone")
	 private String telefone;

    @Column(nullable = false)
    private String especialidade;

    @Column(name = "crm_crp_outros", unique = true)
    private String crmCrpOutros;

    @Column(name = "endereco_consultorio")
    private String enderecoConsultorio;
    
    @Column(name = "cidade_consultorio")
    private String cidadeConsultorio;

    @Column(name = "estado_consultorio")
    private String estadoConsultorio;

    @Column(name = "cep_consultorio")
    private String cepConsultorio;
    
    @Column(name = "acessibilidade_consultorio", columnDefinition = "TEXT")
    private String acessibilidadeConsultorio;
    
    @Column(name = "idiomas_comunicacao")
    private String idiomasComunicacao;

    @Column(name = "servicos_oferecidos", columnDefinition = "TEXT")
    private String servicosOferecidos;
    

    @Column(name = "sobre_mim", columnDefinition = "TEXT")
    private String sobreMim;
    
    @Column(name = "foto_perfil_url")
    private String fotoPerfilUrl;
    
    @Column(columnDefinition = "TEXT") 
    private String descricao;

    public ProfissionalDeSaude() {
        super(); 
    }

   
    public ProfissionalDeSaude(
            Long id, String email, String password, boolean enabled,
            LocalDateTime createdAt, LocalDateTime updatedAt, Set<Role> roles,
            String nome, String sobrenome, String especialidade, String crmCrpOutros,
            String telefone, String enderecoConsultorio, String cidadeConsultorio,
            String estadoConsultorio, String cepConsultorio, String sobreMim,
            String fotoPerfilUrl, String acessibilidadeConsultorio, String idiomasComunicacao,
            String servicosOferecidos) {
            super(id, email, password, enabled, createdAt, updatedAt, roles); 
            this.nome = nome;
            this.sobrenome = sobrenome;
            this.especialidade = especialidade;
            this.crmCrpOutros = crmCrpOutros;
            this.telefone = telefone;
            this.enderecoConsultorio = enderecoConsultorio;
            this.cidadeConsultorio = cidadeConsultorio;
            this.estadoConsultorio = estadoConsultorio;
            this.cepConsultorio = cepConsultorio;
            this.sobreMim = sobreMim;
            this.fotoPerfilUrl = fotoPerfilUrl;
            this.acessibilidadeConsultorio = acessibilidadeConsultorio;
            this.idiomasComunicacao = idiomasComunicacao;
            this.servicosOferecidos = servicosOferecidos;
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

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrmCrpOutros() {
        return crmCrpOutros;
    }

    public void setCrmCrpOutros(String crmCrpOutros) {
        this.crmCrpOutros = crmCrpOutros;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEnderecoConsultorio() {
        return enderecoConsultorio;
    }

    public void setEnderecoConsultorio(String enderecoConsultorio) {
        this.enderecoConsultorio = enderecoConsultorio;
    }

    public String getCidadeConsultorio() {
        return cidadeConsultorio;
    }

    public void setCidadeConsultorio(String cidadeConsultorio) {
        this.cidadeConsultorio = cidadeConsultorio;
    }

    public String getEstadoConsultorio() {
        return estadoConsultorio;
    }

    public void setEstadoConsultorio(String estadoConsultorio) {
        this.estadoConsultorio = estadoConsultorio;
    }

    public String getCepConsultorio() {
        return cepConsultorio;
    }

    public void setCepConsultorio(String cepConsultorio) {
        this.cepConsultorio = cepConsultorio;
    }

    public String getSobreMim() {
        return sobreMim;
    }

    public void setSobreMim(String sobreMim) {
        this.sobreMim = sobreMim;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public String getAcessibilidadeConsultorio() {
        return acessibilidadeConsultorio;
    }

    public void setAcessibilidadeConsultorio(String acessibilidadeConsultorio) {
        this.acessibilidadeConsultorio = acessibilidadeConsultorio;
    }

    public String getIdiomasComunicacao() {
        return idiomasComunicacao;
    }

    public void setIdiomasComunicacao(String idiomasComunicacao) {
        this.idiomasComunicacao = idiomasComunicacao;
    }

    public String getServicosOferecidos() {
        return servicosOferecidos;
    }

    public void setServicosOferecidos(String servicosOferecidos) {
        this.servicosOferecidos = servicosOferecidos;
    }

    
    public boolean equals(Object o) {
        if (this == o) return true;        
        if (!super.equals(o)) return false;
        if (getClass() != o.getClass()) return false;
        ProfissionalDeSaude that = (ProfissionalDeSaude) o;
        return Objects.equals(especialidade, that.especialidade) &&
               Objects.equals(crmCrpOutros, that.crmCrpOutros); 
    }

    @Override
    public int hashCode() {
        
        return Objects.hash(super.hashCode(), especialidade, crmCrpOutros);
    }

    @Override
    public String toString() {
        return "ProfissionalDeSaude{" +
               "id=" + getId() + 
               ", email='" + getEmail() + '\'' + 
               ", nome='" + nome + '\'' +
               ", especialidade='" + especialidade + '\'' +
               ", crmCrpOutros='" + crmCrpOutros + '\'' +
               '}';
    }

	
}