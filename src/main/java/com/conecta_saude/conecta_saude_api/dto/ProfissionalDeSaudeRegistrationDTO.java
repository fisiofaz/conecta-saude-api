package com.conecta_saude.conecta_saude_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfissionalDeSaudeRegistrationDTO(
    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "Formato de email inválido.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    String password,

    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @NotBlank(message = "O sobrenome é obrigatório.")
    String sobrenome,

    @NotBlank(message = "O telefone é obrigatório.")
    String telefone,

    @NotBlank(message = "A especialidade é obrigatória.")
    String especialidade,

    @NotBlank(message = "O CRM/CRP ou outro registro profissional é obrigatório.")
    @Size(max = 50, message = "O registro profissional deve ter no máximo 50 caracteres.")
    String crmCrpOutros,

    @NotBlank(message = "O endereço do consultório é obrigatório.")
    String enderecoConsultorio,

    @NotBlank(message = "A cidade do consultório é obrigatória.")
    String cidadeConsultorio,

    @NotBlank(message = "O estado do consultório é obrigatório.")
    String estadoConsultorio,

    @NotBlank(message = "O CEP do consultório é obrigatório.")
    @Size(min = 8, max = 9, message = "O CEP deve ter 8 ou 9 caracteres (com ou sem hífen).")
    String cepConsultorio,

    String acessibilidadeConsultorio, 
    String idiomasComunicacao,    
    String servicosOferecidos,     
    String sobreMim,               
    String fotoPerfilUrl           
) {}