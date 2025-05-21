package com.conecta_saude.conecta_saude_api.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Builder;

@Entity
@DiscriminatorValue("PROFISSIONAL")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfissionalDeSaude extends User {

	@Column(name = "nome")
	private String nome;
	
	@Column(name = "sobrenome")
    private String sobrenome;

    @Column(nullable = false)
    private String especialidade;

    @Column(name = "crm_crp_outros", unique = true)
    private String crmCrpOutros;
    
    @Column(name = "telefone")
    private String telefone;

    @Column(name = "endereco_consultorio")
    private String enderecoConsultorio;

    @Column(name = "cidade_consultorio")
    private String cidadeConsultorio;

    @Column(name = "estado_consultorio")
    private String estadoConsultorio;

    @Column(name = "cep_consultorio")
    private String cepConsultorio;

    @Column(name = "sobre_mim", columnDefinition = "TEXT")
    private String sobreMim;

    @Column(name = "foto_perfil_url")
    private String fotoPerfilUrl;

    @Column(name = "acessibilidade_consultorio", columnDefinition = "TEXT")
    private String acessibilidadeConsultorio;

    @Column(name = "idiomas_comunicacao")
    private String idiomasComunicacao;

    @Column(name = "servicos_oferecidos", columnDefinition = "TEXT")
    private String servicosOferecidos;

    @Builder
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
}