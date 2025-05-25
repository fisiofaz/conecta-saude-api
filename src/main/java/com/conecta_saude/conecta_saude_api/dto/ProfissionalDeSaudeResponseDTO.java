package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;

public record ProfissionalDeSaudeResponseDTO(
		Long id,
	    String nome,
	    String sobrenome,
	    String email, 
	    String telefone,
	    String especialidade,
	    String crmCrpOutros,
	    String enderecoConsultorio,
	    String cepConsultorio,
	    String cidadeConsultorio,
	    String estadoConsultorio,
	    String acessibilidadeConsultorio,
	    String idiomasComunicacao,
	    String servicosOferecidos, 
	    String sobreMim,           
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