package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class AgendamentoResponseDTO {
    
	@Schema(description = "ID único do agendamento.", example = "100")
    private Long id;
    @Schema(description = "ID do usuário PCD associado ao agendamento.", example = "1")
    private Long usuarioPcdId;
    @Schema(description = "Nome completo do usuário PCD.", example = "João da Silva")
    private String usuarioPcdNome;
    @Schema(description = "ID do profissional de saúde associado ao agendamento.", example = "10")
    private Long profissionalSaudeId;
    @Schema(description = "Nome completo do profissional de saúde.", example = "Dr(a). Maria Oliveira")
    private String profissionalSaudeNome;
    @Schema(description = "Especialidade do profissional de saúde.", example = "Cardiologia")
    private String profissionalSaudeEspecialidade;
    @Schema(description = "Data do agendamento (AAAA-MM-DD).", example = "2025-06-15")
    private LocalDate dataAgendamento;
    @Schema(description = "Hora do agendamento (HH:MM:SS).", example = "14:30:00")
    private LocalTime horaAgendamento;
    @Schema(description = "Status atual do agendamento.", implementation = StatusAgendamento.class, example = "PENDENTE")
    private StatusAgendamento status;
    @Schema(description = "Observações adicionais feitas pelo usuário PCD.", example = "Tenho algumas dúvidas sobre o pós-consulta.")
    private String observacoesUsuario;
    @Schema(description = "Observações adicionais feitas pelo profissional de saúde.", example = "Paciente deve chegar 15 min antes.")
    private String observacoesProfissional;
    @Schema(description = "Data e hora de criação do agendamento.", example = "2025-06-01T10:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "Última data e hora de atualização do agendamento.", example = "2025-06-01T10:00:00")
    private LocalDateTime updatedAt;

    
    public AgendamentoResponseDTO(Long id, Long usuarioPcdId, String usuarioPcdNome,
                                  Long profissionalSaudeId, String profissionalSaudeNome,
                                  String profissionalSaudeEspecialidade,
                                  LocalDate dataAgendamento, LocalTime horaAgendamento,
                                  StatusAgendamento status, String observacoesUsuario,
                                  String observacoesProfissional, LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {
        this.id = id;
        this.usuarioPcdId = usuarioPcdId;
        this.usuarioPcdNome = usuarioPcdNome;
        this.profissionalSaudeId = profissionalSaudeId;
        this.profissionalSaudeNome = profissionalSaudeNome;
        this.profissionalSaudeEspecialidade = profissionalSaudeEspecialidade;
        this.dataAgendamento = dataAgendamento;
        this.horaAgendamento = horaAgendamento;
        this.status = status;
        this.observacoesUsuario = observacoesUsuario;
        this.observacoesProfissional = observacoesProfissional;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioPcdId() {
        return usuarioPcdId;
    }

    public void setUsuarioPcdId(Long usuarioPcdId) {
        this.usuarioPcdId = usuarioPcdId;
    }

    public String getUsuarioPcdNome() {
        return usuarioPcdNome;
    }

    public void setUsuarioPcdNome(String usuarioPcdNome) {
        this.usuarioPcdNome = usuarioPcdNome;
    }

    public Long getProfissionalSaudeId() {
        return profissionalSaudeId;
    }

    public void setProfissionalSaudeId(Long profissionalSaudeId) {
        this.profissionalSaudeId = profissionalSaudeId;
    }

    public String getProfissionalSaudeNome() {
        return profissionalSaudeNome;
    }

    public void setProfissionalSaudeNome(String profissionalSaudeNome) {
        this.profissionalSaudeNome = profissionalSaudeNome;
    }

    public String getProfissionalSaudeEspecialidade() {
        return profissionalSaudeEspecialidade;
    }

    public void setProfissionalSaudeEspecialidade(String profissionalSaudeEspecialidade) {
        this.profissionalSaudeEspecialidade = profissionalSaudeEspecialidade;
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

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public String getObservacoesUsuario() {
        return observacoesUsuario;
    }

    public void setObservacoesUsuario(String observacoesUsuario) {
        this.observacoesUsuario = observacoesUsuario;
    }

    public String getObservacoesProfissional() {
        return observacoesProfissional;
    }

    public void setObservacoesProfissional(String observacoesProfissional) {
        this.observacoesProfissional = observacoesProfissional;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}