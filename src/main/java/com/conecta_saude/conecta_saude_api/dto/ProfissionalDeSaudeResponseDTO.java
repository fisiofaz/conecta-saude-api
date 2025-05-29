package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProfissionalDeSaudeResponseDTO(
		
		@Schema(description = "ID único do profissional.", example = "10")
	    Long id,
	    @Schema(description = "Nome do profissional.", example = "Ana")
	    String nome,
	    @Schema(description = "Sobrenome do profissional.", example = "Silva")
	    String sobrenome,
	    @Schema(description = "Email do profissional.", example = "dr.ana.silva@example.com")
	    String email,
	    @Schema(description = "Telefone de contato do profissional.", example = "5511987654321")
	    String telefone,
	    @Schema(description = "Especialidade do profissional.", example = "Cardiologia")
	    String especialidade,
	    @Schema(description = "Registro profissional (CRM, CRP, etc.).", example = "CRM/SP 123456")
	    String crmCrpOutros,
	    @Schema(description = "Endereço completo do consultório.", example = "Rua das Flores, 123 - Centro")
	    String enderecoConsultorio,
	    @Schema(description = "CEP do consultório.", example = "01000-000")
	    String cepConsultorio,
	    @Schema(description = "Cidade do consultório.", example = "São Paulo")
	    String cidadeConsultorio,
	    @Schema(description = "Estado do consultório.", example = "SP")
	    String estadoConsultorio,
	    @Schema(description = "Informações sobre a acessibilidade do consultório.", example = "Rampa de acesso, elevador.")
	    String acessibilidadeConsultorio,
	    @Schema(description = "Idiomas de comunicação do profissional.", example = "Português, Inglês")
	    String idiomasComunicacao,
	    @Schema(description = "Serviços oferecidos pelo profissional.", example = "Consultas, exames.")
	    String servicosOferecidos,
	    @Schema(description = "Breve descrição 'Sobre mim'.", example = "Experiência em pediatria.")
	    String sobreMim,
	    @Schema(description = "URL da foto de perfil do profissional.", example = "http://minhaapi.com/fotos/prof_ana.jpg")
	    String fotoPerfilUrl
) {
    public ProfissionalDeSaudeResponseDTO(ProfissionalDeSaude profissional) {
        this(
        		profissional.getId(),
                profissional.getNome(),
                profissional.getSobrenome(),
                profissional.getEmail(),
                profissional.getTelefone(),
                profissional.getEspecialidade(),
                profissional.getCrmCrpOutros(),
                profissional.getEnderecoConsultorio(),
                profissional.getCepConsultorio(),
                profissional.getCidadeConsultorio(),
                profissional.getEstadoConsultorio(),
                profissional.getAcessibilidadeConsultorio(),
                profissional.getIdiomasComunicacao(),
                profissional.getServicosOferecidos(),
                profissional.getSobreMim(),
                profissional.getFotoPerfilUrl()
        );
    }
}