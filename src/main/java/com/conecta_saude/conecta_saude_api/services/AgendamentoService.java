package com.conecta_saude.conecta_saude_api.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import com.conecta_saude.conecta_saude_api.models.Agendamento;
import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;
import com.conecta_saude.conecta_saude_api.repositories.AgendamentoRepository;
import com.conecta_saude.conecta_saude_api.repositories.ProfissionalDeSaudeRepository;
import com.conecta_saude.conecta_saude_api.repositories.UsuarioPCDRepository;    

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioPCDRepository usuarioPCDRepository; 
    private final ProfissionalDeSaudeRepository profissionalDeSaudeRepository;

  
    public AgendamentoService(AgendamentoRepository agendamentoRepository,
                              UsuarioPCDRepository usuarioPCDRepository,
                              ProfissionalDeSaudeRepository profissionalDeSaudeRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioPCDRepository = usuarioPCDRepository;
        this.profissionalDeSaudeRepository = profissionalDeSaudeRepository;
    }
    
    @Transactional
    public Agendamento createAgendamento(Long usuarioPcdId, Long profissionalId, LocalDate data, LocalTime hora, String observacoesUsuario) {
       
        UsuarioPCD usuarioPCD = usuarioPCDRepository.findById(usuarioPcdId)
                .orElseThrow(() -> new RuntimeException("Usuário PCD não encontrado."));
        ProfissionalDeSaude profissionalDeSaude = profissionalDeSaudeRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Profissional de Saúde não encontrado."));

        Optional<Agendamento> agendamentoExistente = agendamentoRepository
                .findByProfissionalSaudeAndDataAgendamentoAndHoraAgendamento(profissionalDeSaude, data, hora);

        if (agendamentoExistente.isPresent() &&
            (agendamentoExistente.get().getStatus() == StatusAgendamento.CONFIRMADO ||
             agendamentoExistente.get().getStatus() == StatusAgendamento.PENDENTE)) {
            throw new IllegalArgumentException("Horário já agendado ou pendente para este profissional.");
        }

        
        Agendamento novoAgendamento = new Agendamento(usuarioPCD, profissionalDeSaude, data, hora, observacoesUsuario);
        if (novoAgendamento.getStatus() == null) {
            novoAgendamento.setStatus(StatusAgendamento.PENDENTE);
        }
        
        return agendamentoRepository.save(novoAgendamento);
    }

    public List<Agendamento> findAllAgendamentos() {
        return agendamentoRepository.findAll();
    }

    public Optional<Agendamento> findAgendamentoById(Long id) {
        return agendamentoRepository.findById(id);
    }

    @Transactional 
    public Agendamento saveAgendamento(Agendamento agendamento) {
        
        Optional<Agendamento> agendamentoExistente = agendamentoRepository.findByProfissionalSaudeAndDataAgendamentoAndHoraAgendamento(
            agendamento.getProfissionalSaude(), agendamento.getDataAgendamento(), agendamento.getHoraAgendamento());

        if (agendamentoExistente.isPresent() &&
            (agendamentoExistente.get().getStatus() == StatusAgendamento.CONFIRMADO ||
             agendamentoExistente.get().getStatus() == StatusAgendamento.PENDENTE)) {
            throw new IllegalArgumentException("Horário já agendado ou pendente para este profissional.");
        }

        
        if (agendamento.getStatus() == null) {
            agendamento.setStatus(StatusAgendamento.PENDENTE);
        }

        return agendamentoRepository.save(agendamento);
    }

    @Transactional 
    public boolean deleteAgendamento(Long id) {
        if (agendamentoRepository.existsById(id)) {
            agendamentoRepository.deleteById(id);
            return true;
        }
        return false; 
    }

    
    public List<Agendamento> findAgendamentosByUsuarioPCDId(Long usuarioPcdId) {
        UsuarioPCD usuarioPCD = usuarioPCDRepository.findById(usuarioPcdId)
                .orElseThrow(() -> new RuntimeException("Usuário PCD não encontrado.")); 
        return agendamentoRepository.findByUsuarioPCD(usuarioPCD);
    }

    
    public List<Agendamento> findAgendamentosByProfissionalSaudeId(Long profissionalId) {
        ProfissionalDeSaude profissionalDeSaude = profissionalDeSaudeRepository.findById(profissionalId)
                .orElseThrow(() -> new RuntimeException("Profissional de Saúde não encontrado.")); 
        return agendamentoRepository.findByProfissionalSaude(profissionalDeSaude);
    }

   
    public List<Agendamento> findAgendamentosByStatus(StatusAgendamento status) {
        return agendamentoRepository.findByStatus(status);
    }

   
    @Transactional
    public Optional<Agendamento> updateAgendamentoStatus(Long agendamentoId, StatusAgendamento newStatus, String observacoesProfissional) {
        Optional<Agendamento> agendamentoOptional = agendamentoRepository.findById(agendamentoId);

        if (agendamentoOptional.isPresent()) {
            Agendamento agendamento = agendamentoOptional.get();
            agendamento.setStatus(newStatus);
            if (observacoesProfissional != null && !observacoesProfissional.isEmpty()) {
                agendamento.setObservacoesProfissional(observacoesProfissional);
            }
            return Optional.of(agendamentoRepository.save(agendamento));
        }
        return Optional.empty();
    }

	
}