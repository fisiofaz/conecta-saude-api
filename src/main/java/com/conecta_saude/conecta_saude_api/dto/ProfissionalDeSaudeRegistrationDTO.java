package com.conecta_saude.conecta_saude_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfissionalDeSaudeRegistrationDTO(
		
		@Schema(description = "Email do profissional (único).", example = "dr.ana.silva@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank @Email String email,

	    @Schema(description = "Senha do profissional (mínimo 6 caracteres).", example = "senhaSegura123", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank @Size(min = 6) String password,

	    @Schema(description = "Nome do profissional.", example = "Ana", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank String nome,

	    @Schema(description = "Sobrenome do profissional.", example = "Silva", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank String sobrenome,

	    @Schema(description = "Telefone de contato do profissional.", example = "5511987654321", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank String telefone,

	    @Schema(description = "Especialidade do profissional (ex: Cardiologia, Fisioterapia).", example = "Cardiologia", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank String especialidade,

	    @Schema(description = "Registro profissional (CRM, CRP, etc.) do profissional (único).", example = "CRM/SP 123456", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank @Size(max = 50) String crmCrpOutros,

	    @Schema(description = "Endereço completo do consultório do profissional.", example = "Rua das Flores, 123 - Centro")
	    @NotBlank String enderecoConsultorio,

	    @Schema(description = "Cidade do consultório.", example = "São Paulo")
	    @NotBlank String cidadeConsultorio,

	    @Schema(description = "Estado do consultório (UF).", example = "SP")
	    @NotBlank String estadoConsultorio,

	    @Schema(description = "CEP do consultório.", example = "01000-000")
	    @NotBlank @Size(min = 8, max = 9) String cepConsultorio,

	    @Schema(description = "Informações sobre a acessibilidade do consultório.", example = "Rampa de acesso, elevador, banheiro adaptado.")
	    String acessibilidadeConsultorio,

	    @Schema(description = "Idiomas de comunicação do profissional.", example = "Português, Inglês")
	    String idiomasComunicacao,

	    @Schema(description = "Serviços oferecidos pelo profissional.", example = "Consultas, exames, terapias.")
	    String servicosOferecidos,

	    @Schema(description = "Breve descrição 'Sobre mim' do profissional.", example = "Médica com mais de 10 anos de experiência.")
	    String sobreMim,

	    @Schema(description = "URL da foto de perfil do profissional.", example = "http://minhaapi.com/fotos/dr_ana.jpg")
	    String fotoPerfilUrl
) {}