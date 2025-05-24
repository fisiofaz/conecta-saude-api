package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;
import jakarta.validation.constraints.NotNull;

public class AgendamentoUpdateStatusDTO {

    @NotNull(message = "O novo status é obrigatório.")
    private StatusAgendamento newStatus;

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