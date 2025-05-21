package com.conecta_saude.conecta_saude_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.conecta_saude.conecta_saude_api.models.Agendamento;
import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.repositories.AgendamentoRepository;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    public List<Agendamento> findAllAgendamentos() {
        return agendamentoRepository.findAll();
    }

    public Optional<Agendamento> findAgendamentoById(Long id) {
        return agendamentoRepository.findById(id);
    }

    public Agendamento saveAgendamento(Agendamento agendamento) {
        
        Optional<Agendamento> agendamentoExistente = agendamentoRepository.findByProfissionalSaudeAndDataAgendamentoAndHoraAgendamento(
            agendamento.getProfissionalSaude(), agendamento.getDataAgendamento(), agendamento.getHoraAgendamento());
        
        if (agendamentoExistente.isPresent()) {
            throw new IllegalArgumentException("Horário já agendado para este profissional.");
        }
        
 
        if (agendamento.getStatus() == null || agendamento.getStatus().isEmpty()) {
            agendamento.setStatus("PENDENTE");
        }

        return agendamentoRepository.save(agendamento);
    }

    public void deleteAgendamento(Long id) {
        agendamentoRepository.deleteById(id);
    }

    public List<Agendamento> findAgendamentosByUsuarioPCD(UsuarioPCD usuarioPCD) {
        return agendamentoRepository.findByUsuarioPCD(usuarioPCD);
    }

    public List<Agendamento> findAgendamentosByProfissionalSaude(ProfissionalDeSaude profissionalSaude) {
        return agendamentoRepository.findByProfissionalSaude(profissionalSaude);
    }

    public List<Agendamento> findAgendamentosByStatus(String status) {
        return agendamentoRepository.findByStatus(status);
    }
}