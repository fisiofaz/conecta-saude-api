package com.conecta_saude.conecta_saude_api.controllers;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.services.ProfissionalDeSaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalDeSaudeController {

    @Autowired
    private ProfissionalDeSaudeService profissionalDeSaudeService;

    @GetMapping
    public ResponseEntity<List<ProfissionalDeSaude>> getAllProfissionaisDeSaude() {
        List<ProfissionalDeSaude> profissionais = profissionalDeSaudeService.findAllProfissionaisDeSaude();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalDeSaude> getProfissionalDeSaudeById(@PathVariable Long id) {
        Optional<ProfissionalDeSaude> profissional = profissionalDeSaudeService.findProfissionalDeSaudeById(id);
        return profissional.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProfissionalDeSaude> createProfissionalDeSaude(@RequestBody ProfissionalDeSaude profissional) {
        ProfissionalDeSaude savedProfissional = profissionalDeSaudeService.saveProfissionalDeSaude(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfissional);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalDeSaude> updateProfissionalDeSaude(@PathVariable Long id, @RequestBody ProfissionalDeSaude profissionalDetails) {
        Optional<ProfissionalDeSaude> profissionalOptional = profissionalDeSaudeService.findProfissionalDeSaudeById(id);

        if (profissionalOptional.isPresent()) {
            ProfissionalDeSaude existingProfissional = profissionalOptional.get();
            // Atualiza campos específicos do ProfissionalDeSaude
            existingProfissional.setNome(profissionalDetails.getNome());
            existingProfissional.setSobrenome(profissionalDetails.getSobrenome());
            existingProfissional.setEspecialidade(profissionalDetails.getEspecialidade());
            existingProfissional.setCrmCrpOutros(profissionalDetails.getCrmCrpOutros());
            existingProfissional.setTelefone(profissionalDetails.getTelefone());
            existingProfissional.setEnderecoConsultorio(profissionalDetails.getEnderecoConsultorio());
            existingProfissional.setCidadeConsultorio(profissionalDetails.getCidadeConsultorio());
            existingProfissional.setEstadoConsultorio(profissionalDetails.getEstadoConsultorio());
            existingProfissional.setCepConsultorio(profissionalDetails.getCepConsultorio());
            existingProfissional.setSobreMim(profissionalDetails.getSobreMim());
            existingProfissional.setFotoPerfilUrl(profissionalDetails.getFotoPerfilUrl());
            existingProfissional.setAcessibilidadeConsultorio(profissionalDetails.getAcessibilidadeConsultorio());
            existingProfissional.setIdiomasComunicacao(profissionalDetails.getIdiomasComunicacao());
            existingProfissional.setServicosOferecidos(profissionalDetails.getServicosOferecidos());

           
            existingProfissional.setEmail(profissionalDetails.getEmail());
            existingProfissional.setEnabled(profissionalDetails.isEnabled());
            existingProfissional.setRoles(profissionalDetails.getRoles());

            ProfissionalDeSaude updatedProfissional = profissionalDeSaudeService.saveProfissionalDeSaude(existingProfissional);
            return ResponseEntity.ok(updatedProfissional);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
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