package com.conecta_saude.conecta_saude_api.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;

@Entity
@Table(name = "agendamentos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"data_agendamento", "hora_agendamento", "profissional_saude_id"})
}) 

public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne
    @JoinColumn(name = "usuario_pcd_id", nullable = false) 
    private UsuarioPCD usuarioPCD;

   
    @ManyToOne
    @JoinColumn(name = "profissional_saude_id", nullable = false) 
    private ProfissionalDeSaude profissionalSaude;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDate dataAgendamento;

    @Column(name = "hora_agendamento", nullable = false)
    private LocalTime horaAgendamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status; 

    @Column(name = "observacoes_usuario", columnDefinition = "TEXT")
    private String observacoesUsuario; 

    @Column(name = "observacoes_profissional", columnDefinition = "TEXT")
    private String observacoesProfissional; 

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public Agendamento() {
    }
    
    public Agendamento(Long id, UsuarioPCD usuarioPCD, ProfissionalDeSaude profissionalSaude,
            LocalDate dataAgendamento, LocalTime horaAgendamento, StatusAgendamento status,
            String observacoesUsuario, String observacoesProfissional,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.usuarioPCD = usuarioPCD;
		this.profissionalSaude = profissionalSaude;
		this.dataAgendamento = dataAgendamento;
		this.horaAgendamento = horaAgendamento;
		this.status = status;
		this.observacoesUsuario = observacoesUsuario;
		this.observacoesProfissional = observacoesProfissional;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
    
    public Agendamento(UsuarioPCD usuarioPCD, ProfissionalDeSaude profissionalSaude,
            			LocalDate dataAgendamento, LocalTime horaAgendamento,
            			String observacoesUsuario) {
    	this.usuarioPCD = usuarioPCD;
    	this.profissionalSaude = profissionalSaude;
    	this.dataAgendamento = dataAgendamento;
    	this.horaAgendamento = horaAgendamento;
    	this.observacoesUsuario = observacoesUsuario;
    	this.status = StatusAgendamento.PENDENTE; 
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioPCD getUsuarioPCD() {
        return usuarioPCD;
    }

    public void setUsuarioPCD(UsuarioPCD usuarioPCD) {
        this.usuarioPCD = usuarioPCD;
    }

    public ProfissionalDeSaude getProfissionalSaude() {
        return profissionalSaude;
    }

    public void setProfissionalSaude(ProfissionalDeSaude profissionalSaude) {
        this.profissionalSaude = profissionalSaude;
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

    public void setStatus(StatusAgendamento string) {
        this.status = string;
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


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agendamento that = (Agendamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Agendamento{" +
               "id=" + id +
               ", dataAgendamento=" + dataAgendamento +
               ", horaAgendamento=" + horaAgendamento +
               ", status='" + status + '\'' +
               '}';
    }
}