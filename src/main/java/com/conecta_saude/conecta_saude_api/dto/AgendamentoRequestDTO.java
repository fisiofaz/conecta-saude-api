package com.conecta_saude.conecta_saude_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public class AgendamentoRequestDTO {

	@Schema(description = "ID do profissional de saúde para o agendamento.", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "O ID do profissional de saúde é obrigatório.")
    private Long profissionalSaudeId;

	@Schema(description = "Data do agendamento no formato AAAA-MM-DD.", example = "2025-06-15", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "A data do agendamento é obrigatória.")
    private LocalDate dataAgendamento;

	@Schema(description = "Hora do agendamento no formato HH:MM:SS (ex: 14:30:00).", example = "14:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "A hora do agendamento é obrigatória.")
    private LocalTime horaAgendamento;
	
	@Schema(description = "Observações adicionais do usuário sobre o agendamento.", example = "Necessito de profissional com experiência em fisioterapia motora.")
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