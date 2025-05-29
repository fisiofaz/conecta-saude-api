package com.conecta_saude.conecta_saude_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;
import java.util.Map;

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

@Tag(name = "Profissionais de Saúde", description = "Endpoints para gerenciamento e busca de profissionais de saúde no sistema.") // NOVA ANOTAÇÃO
@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalDeSaudeController {

    @Autowired
    private ProfissionalDeSaudeService profissionalDeSaudeService;
    
    public ProfissionalDeSaudeController(ProfissionalDeSaudeService profissionalDeSaudeService) {
        this.profissionalDeSaudeService = profissionalDeSaudeService;
    }
    
    @Operation(summary = "Cria um novo profissional de saúde",
            description = "Permite que um administrador crie o cadastro de um novo profissional de saúde no sistema.") 
    @ApiResponses(value = { // NOVA ANOTAÇÃO
    @ApiResponse(responseCode = "201", description = "Profissional de saúde criado com sucesso",
                  content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = ProfissionalDeSaude.class))), 
    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: dados ausentes, email/CRM duplicado)",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Não autorizado (requer autenticação como ADMIN)"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProfissionalDeSaude> createProfissionalDeSaude(@RequestBody ProfissionalDeSaude profissionalDeSaude) {
        ProfissionalDeSaude savedProfissional = profissionalDeSaudeService.save(profissionalDeSaude);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProfissional);
    }
    
    @Operation(summary = "Lista todos os profissionais de saúde",
            description = "Retorna uma lista completa de todos os profissionais de saúde cadastrados no sistema. Acesso restrito a administradores.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de profissionais retornada com sucesso",
                  content = @Content(mediaType = "application/json",
                                     array = @ArraySchema(schema = @Schema(implementation = ProfissionalDeSaude.class)))), // Retorna lista de entidades
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Não autorizado (requer autenticação como ADMIN)"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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