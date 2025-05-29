
package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.enums.TipoDeficiencia;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UsuarioPCDRegistrationDTO(
		
		@Schema(description = "Email do usuário PCD (único no sistema).", example = "maria.pcd@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank @Email String email,

	    @Schema(description = "Senha do usuário PCD (mínimo 6 caracteres).", example = "senhaSeguraPCD", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank @Size(min = 6) String password,

	    @Schema(description = "Nome do usuário PCD.", example = "Maria", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank String nome,

	    @Schema(description = "Sobrenome do usuário PCD.", example = "Souza", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank String sobrenome,

	    @Schema(description = "Telefone de contato do usuário PCD.", example = "5521998765432", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank String telefone,

	    @Schema(description = "Data de nascimento do usuário PCD (AAAA-MM-DD).", example = "1990-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotNull @PastOrPresent LocalDate dataNascimento,

	    @Schema(description = "Tipo de deficiência do usuário PCD.", implementation = TipoDeficiencia.class, requiredMode = Schema.RequiredMode.REQUIRED, example = "FISICA")
	    @NotNull TipoDeficiencia tipoDeficiencia,

	    @Schema(description = "Necessidades específicas do usuário PCD (opcional).", example = "Cadeira de rodas, intérprete de libras.")
	    String necessidadesEspecificas,

	    @Schema(description = "Endereço completo do usuário PCD.", example = "Rua das Rosas, 45 - Centro")
	    @NotBlank String endereco,

	    @Schema(description = "Cidade do usuário PCD.", example = "Rio de Janeiro")
	    @NotBlank String cidade,

	    @Schema(description = "Estado do usuário PCD (UF).", example = "RJ")
	    @NotBlank String estado,

	    @Schema(description = "CEP do usuário PCD.", example = "20000-000", requiredMode = Schema.RequiredMode.REQUIRED)
	    @NotBlank @Size(min = 8, max = 9) String cep
) {}