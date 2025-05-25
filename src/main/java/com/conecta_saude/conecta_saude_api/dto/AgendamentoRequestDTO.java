package com.conecta_saude.conecta_saude_api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoRequestDTO {

    @NotNull(message = "O ID do profissional de saúde é obrigatório.")
    private Long profissionalSaudeId;

    @NotNull(message = "A data do agendamento é obrigatória.")
    private LocalDate dataAgendamento;

    @NotNull(message = "A hora do agendamento é obrigatória.")
    private LocalTime horaAgendamento;

    private String observacoesUsuario;

       
    public Long getProfissionalSaudeId() {
        return profissionalSaudeId;
    }

    public void setProfissionalSaudeId(Long profissionalSaudeId) {
        this.profissionalSaudeId = profissionalSaudeId;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public LocalTime getHoraAgendamento() {
        return horaAgendamento;
    }

    public void setHoraAgendamento(LocalTime horaAgendamento) {
        this.horaAgendamento = horaAgendamento;
    }

    public String getObservacoesUsuario() {
        return observacoesUsuario;
    }

    public void setObservacoesUsuario(String observacoesUsuario) {
        this.observacoesUsuario = observacoesUsuario;
    }
}