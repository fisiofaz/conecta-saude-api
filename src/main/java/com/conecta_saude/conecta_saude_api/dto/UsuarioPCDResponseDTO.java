package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.TipoDeficiencia;

import io.swagger.v3.oas.annotations.media.Schema; 

import java.time.LocalDate;

public record UsuarioPCDResponseDTO(
		
		@Schema(description = "ID único do usuário PCD.", example = "1")
	    Long id,
	    
	    @Schema(description = "Email do usuário PCD.", example = "maria.pcd@example.com")
	    String email,	    
	    
	    @Schema(description = "Nome do usuário PCD.", example = "Maria")
	    String nome,
	    
	    @Schema(description = "Sobrenome do usuário PCD.", example = "Souza")
	    String sobrenome,
	    
	    @Schema(description = "Telefone de contato do usuário PCD.", example = "5521998765432")
	    String telefone,
	    
	    @Schema(description = "CEP do usuário PCD.", example = "20000-000")
	    String cep,
	   
	    @Schema(description = "Endereço completo do usuário PCD.", example = "Rua das Rosas, 45")
	    String endereco,
	    
	    @Schema(description = "Cidade do usuário PCD.", example = "Rio de Janeiro")
	    String cidade,
	    
	    @Schema(description = "Estado do usuário PCD (UF).", example = "RJ")
	    String estado,
	    
	    @Schema(description = "Tipo de deficiência do usuário PCD.", implementation = TipoDeficiencia.class, example = "FISICA")
	    TipoDeficiencia tipoDeficiencia,
	    
	    @Schema(description = "Necessidades específicas do usuário PCD.", example = "Cadeira de rodas.")
	    String necessidadesEspecificas,
	    
	    @Schema(description = "Data de nascimento do usuário PCD (AAAA-MM-DD).", example = "1990-01-01")
	    LocalDate dataNascimento
) {
    public UsuarioPCDResponseDTO(UsuarioPCD usuario) {
        this(
            usuario.getId(),
            usuario.getEmail(),
            usuario.getNome(),
            usuario.getSobrenome(),
            usuario.getTelefone(),
            usuario.getCep(),
            usuario.getEndereco(),
            usuario.getCidade(),
            usuario.getEstado(),
            usuario.getTipoDeficiencia(),
            usuario.getNecessidadesEspecificas(),
            usuario.getDataNascimento()
        );
    }
}