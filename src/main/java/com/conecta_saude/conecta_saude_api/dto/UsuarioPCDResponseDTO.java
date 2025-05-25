package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.TipoDeficiencia;
import java.time.LocalDate;

public record UsuarioPCDResponseDTO(
    Long id,
    String email,
    String password,
    String nome,
    String sobrenome,
    String telefone,
    String cep,
    String endereco,
    String cidade,
    String estado,
    TipoDeficiencia tipoDeficiencia,
    String necessidadesEspecificas,
    LocalDate dataNascimento
) {
    public UsuarioPCDResponseDTO(UsuarioPCD usuario) {
        this(
            usuario.getId(),
            usuario.getEmail(),
            usuario.getPassword(),
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