package com.conecta_saude.conecta_saude_api.controllers;

import com.conecta_saude.conecta_saude_api.models.Agendamento;
import com.conecta_saude.conecta_saude_api.services.AgendamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @GetMapping
    public ResponseEntity<List<Agendamento>> getAllAgendamentos() {
        List<Agendamento> agendamentos = agendamentoService.findAllAgendamentos();
        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> getAgendamentoById(@PathVariable Long id) {
        Optional<Agendamento> agendamento = agendamentoService.findAgendamentoById(id);
        return agendamento.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAgendamento(@RequestBody Agendamento agendamento) {
        try {
            Agendamento savedAgendamento = agendamentoService.saveAgendamento(agendamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAgendamento);
        } catch (IllegalArgumentException e) {            
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {          
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar agendamento: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAgendamento(@PathVariable Long id, @RequestBody Agendamento agendamentoDetails) {
        Optional<Agendamento> agendamentoOptional = agendamentoService.findAgendamentoById(id);

        if (agendamentoOptional.isPresent()) {
            Agendamento existingAgendamento = agendamentoOptional.get();
           
            existingAgendamento.setUsuarioPCD(agendamentoDetails.getUsuarioPCD());
            existingAgendamento.setProfissionalSaude(agendamentoDetails.getProfissionalSaude());
            existingAgendamento.setDataAgendamento(agendamentoDetails.getDataAgendamento());
            existingAgendamento.setHoraAgendamento(agendamentoDetails.getHoraAgendamento());
            existingAgendamento.setStatus(agendamentoDetails.getStatus());
            existingAgendamento.setObservacoesUsuario(agendamentoDetails.getObservacoesUsuario());
            existingAgendamento.setObservacoesProfissional(agendamentoDetails.getObservacoesProfissional());

            
            try {
                Agendamento updatedAgendamento = agendamentoService.saveAgendamento(existingAgendamento);
                return ResponseEntity.ok(updatedAgendamento);
            } catch (IllegalArgumentException e) {
               
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgendamento(@PathVariable Long id) {
        if (agendamentoService.findAgendamentoById(id).isPresent()) {
            agendamentoService.deleteAgendamento(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}