package com.conecta_saude.conecta_saude_api.dto;

import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class AgendamentoResponseDTO {
    private Long id;
    private Long usuarioPcdId;
    private String usuarioPcdNome; 
    private Long profissionalSaudeId;
    private String profissionalSaudeNome; 
    private String profissionalSaudeEspecialidade; 
    private LocalDate dataAgendamento;
    private LocalTime horaAgendamento;
    private StatusAgendamento status;
    private String observacoesUsuario;
    private String observacoesProfissional;
    private LocalDateTime createdAt;
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