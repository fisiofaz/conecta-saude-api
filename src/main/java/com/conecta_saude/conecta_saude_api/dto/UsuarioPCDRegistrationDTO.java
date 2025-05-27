
package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.enums.TipoDeficiencia;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UsuarioPCDRegistrationDTO(
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

    @NotNull(message = "A data de nascimento é obrigatória.")
    @PastOrPresent(message = "A data de nascimento não pode ser futura.")
    LocalDate dataNascimento,

    @NotNull(message = "O tipo de deficiência é obrigatório.")
    TipoDeficiencia tipoDeficiencia,

    String necessidadesEspecificas,

    @NotBlank(message = "O endereço é obrigatório.")
    String endereco,

    @NotBlank(message = "A cidade é obrigatória.")
    String cidade,

    @NotBlank(message = "O estado é obrigatório.")
    String estado,

    @NotBlank(message = "O CEP é obrigatório.")
    @Size(min = 8, max = 9, message = "O CEP deve ter 8 ou 9 caracteres (com ou sem hífen).")
    String cep
) {}