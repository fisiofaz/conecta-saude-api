package com.conecta_saude.conecta_saude_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "agendamentos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"data_agendamento", "hora_agendamento", "profissional_saude_id"})
}) 
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "usuario_pcd_id", nullable = false) 
    private UsuarioPCD usuarioPCD;

   
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "profissional_saude_id", nullable = false) 
    private ProfissionalDeSaude profissionalSaude;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDate dataAgendamento;

    @Column(name = "hora_agendamento", nullable = false)
    private LocalTime horaAgendamento;

    @Column(nullable = false)
    private String status = "PENDENTE"; 

    @Column(name = "observacoes_usuario", columnDefinition = "TEXT")
    private String observacoesUsuario; 

    @Column(name = "observacoes_profissional", columnDefinition = "TEXT")
    private String observacoesProfissional; 

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}