package com.conecta_saude.conecta_saude_api.repositories;

import com.conecta_saude.conecta_saude_api.models.Agendamento;
import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    
    List<Agendamento> findByUsuarioPCD(UsuarioPCD usuarioPCD);
   
    List<Agendamento> findByProfissionalSaude(ProfissionalDeSaude profissionalSaude);
   
    Optional<Agendamento> findByProfissionalSaudeAndDataAgendamentoAndHoraAgendamento(
        ProfissionalDeSaude profissionalSaude, LocalDate dataAgendamento, LocalTime horaAgendamento);    
   
    List<Agendamento> findByStatus(StatusAgendamento status);
    
    List<Agendamento> findByProfissionalSaudeAndDataAgendamentoBetween(ProfissionalDeSaude profissionalSaude, LocalDate startDate, LocalDate endDate);
}