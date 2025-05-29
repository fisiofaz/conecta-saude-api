package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

public class AgendamentoUpdateStatusDTO {

	@Schema(description = "O novo status desejado para o agendamento.", implementation = StatusAgendamento.class, requiredMode = Schema.RequiredMode.REQUIRED, example = "CONFIRMADO")
	@NotNull(message = "O novo status é obrigatório.")
    private StatusAgendamento newStatus;

	@Schema(description = "Observações adicionais do profissional de saúde sobre a atualização do status.", example = "Consulta confirmada, paciente avisado.")
	private String observacoesProfissional; 

    
    public StatusAgendamento getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(StatusAgendamento newStatus) {
        this.newStatus = newStatus;
    }

    public String getObservacoesProfissional() {
        return observacoesProfissional;
    }

    public void setObservacoesProfissional(String observacoesProfissional) {
        this.observacoesProfissional = observacoesProfissional;
    }
}