package com.conecta_saude.conecta_saude_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.services.ProfissionalDeSaudeService;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalDeSaudeController {

    @Autowired
    private ProfissionalDeSaudeService profissionalDeSaudeService;
    
    public ProfissionalDeSaudeController(ProfissionalDeSaudeService profissionalDeSaudeService) {
        this.profissionalDeSaudeService = profissionalDeSaudeService;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfissionalDeSaude> createProfissionalDeSaude(@RequestBody ProfissionalDeSaude profissionalDeSaude) {
        ProfissionalDeSaude savedProfissional = profissionalDeSaudeService.save(profissionalDeSaude);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfissional);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProfissionalDeSaude>> getAllProfissional() {
        List<ProfissionalDeSaude> profissionais = profissionalDeSaudeService.findAllProfissionaisDeSaude();
        return ResponseEntity.ok(profissionais);
    }

       
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ProfissionalDeSaude> getProfissionalDeSaudeById(@PathVariable Long id) {
    	return profissionalDeSaudeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }   

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<ProfissionalDeSaude> updateProfissionalDeSaude(@PathVariable Long id, @RequestBody ProfissionalDeSaude profissionalDeSaude) {
    	return profissionalDeSaudeService.findById(id)
                .map(existingProfissional -> {
                    
                    existingProfissional.setNome(profissionalDeSaude.getNome());
                    existingProfissional.setSobrenome(profissionalDeSaude.getSobrenome());
                    existingProfissional.setTelefone(profissionalDeSaude.getTelefone());
                    existingProfissional.setEspecialidade(profissionalDeSaude.getEspecialidade());
                    existingProfissional.setCrmCrpOutros(profissionalDeSaude.getCrmCrpOutros());
                    existingProfissional.setEnderecoConsultorio(profissionalDeSaude.getEnderecoConsultorio());
                    existingProfissional.setCepConsultorio(profissionalDeSaude.getCepConsultorio());
                    existingProfissional.setCidadeConsultorio(profissionalDeSaude.getCidadeConsultorio());
                    existingProfissional.setEstadoConsultorio(profissionalDeSaude.getEstadoConsultorio());
                    existingProfissional.setAcessibilidadeConsultorio(profissionalDeSaude.getAcessibilidadeConsultorio());
                    existingProfissional.setIdiomasComunicacao(profissionalDeSaude.getIdiomasComunicacao());
                    existingProfissional.setServicosOferecidos(profissionalDeSaude.getServicosOferecidos());
                    existingProfissional.setSobreMim(profissionalDeSaude.getSobreMim());
                    existingProfissional.setFotoPerfilUrl(profissionalDeSaude.getFotoPerfilUrl());
                    return new ResponseEntity<>(profissionalDeSaudeService.save(existingProfissional), HttpStatus.OK);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProfissionalDeSaude(@PathVariable Long id) {
        if (profissionalDeSaudeService.findProfissionalDeSaudeById(id).isPresent()) {
            profissionalDeSaudeService.deleteProfissionalDeSaude(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/especialidade") 
    public ResponseEntity<List<ProfissionalDeSaude>> getProfissionaisByEspecialidade(@RequestParam String especialidade) {
        List<ProfissionalDeSaude> profissionais = profissionalDeSaudeService.findProfissionaisByEspecialidade(especialidade);
        return ResponseEntity.ok(profissionais);
    }
}