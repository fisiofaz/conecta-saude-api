package com.conecta_saude.conecta_saude_api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag; 

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDRegistrationDTO;

import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDResponseDTO;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.services.UsuarioPCDService;

import jakarta.validation.Valid;

@Tag(name = "Usuários PCD", description = "Endpoints para gerenciamento de perfis e operações relacionadas a usuários PCD.")
@RestController
@RequestMapping("/api/usuarios-pcd")
public class UsuarioPCDController {

    private final UsuarioPCDService usuarioPCDService;

    public UsuarioPCDController(UsuarioPCDService usuarioPCDService) {
        this.usuarioPCDService = usuarioPCDService;
    }

    
    @Operation(summary = "Obtém o perfil do usuário PCD autenticado",
            description = "Retorna os detalhes do perfil do usuário PCD que está atualmente logado.") // NOVA ANOTAÇÃO
    @ApiResponses(value = { // NOVA ANOTAÇÃO
    @ApiResponse(responseCode = "200", description = "Perfil do usuário PCD retornado com sucesso",
                  content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UsuarioPCDResponseDTO.class))),
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Não autorizado (requer autenticação como USUARIO_PCD)"),
    @ApiResponse(responseCode = "404", description = "Usuário PCD logado não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USUARIO_PCD')")
    public ResponseEntity<UsuarioPCDResponseDTO> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Optional<UsuarioPCD> usuarioPCD = usuarioPCDService.findByEmail(userEmail);
        if (usuarioPCD.isPresent()) {
            return ResponseEntity.ok(new UsuarioPCDResponseDTO(usuarioPCD.get()));
        }

        return ResponseEntity.notFound().build();
    }

    
    @Operation(summary = "Atualiza o perfil do usuário PCD autenticado",
            description = "Permite que o usuário PCD logado atualize suas próprias informações de perfil.") // NOVA ANOTAÇÃO
    @ApiResponses(value = { // NOVA ANOTAÇÃO
    @ApiResponse(responseCode = "200", description = "Perfil do usuário PCD atualizado com sucesso",
                  content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = UsuarioPCDResponseDTO.class))),
    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: dados ausentes ou malformatados)",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Não autorizado (requer autenticação como USUARIO_PCD)"),
    @ApiResponse(responseCode = "404", description = "Usuário PCD logado não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/me")
    @PreAuthorize("hasRole('ROLE_USUARIO_PCD')")
    public ResponseEntity<UsuarioPCDResponseDTO> updateMyProfile(@RequestBody UsuarioPCDResponseDTO usuarioPCDRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        UsuarioPCD updatedUsuario = usuarioPCDService.updateUsuarioPCD(userEmail, usuarioPCDRequestDTO);
        return ResponseEntity.ok(new UsuarioPCDResponseDTO(updatedUsuario));
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public ResponseEntity<List<UsuarioPCDResponseDTO>> getAllUsuariosPCD() {
        List<UsuarioPCD> usuarios = usuarioPCDService.findAllUsuariosPCD();
        List<UsuarioPCDResponseDTO> dtos = usuarios.stream()
                                                    .map(UsuarioPCDResponseDTO::new)
                                                    .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public ResponseEntity<UsuarioPCDResponseDTO> getUsuarioPCDById(@PathVariable Long id) {
        Optional<UsuarioPCD> usuarioOptional = usuarioPCDService.findUsuarioPCDById(id);

        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(new UsuarioPCDResponseDTO(usuarioOptional.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public ResponseEntity<UsuarioPCDResponseDTO> createUsuarioPCD(
            @Valid @RequestBody UsuarioPCDRegistrationDTO registrationDTO) {         
        UsuarioPCD savedUsuario = usuarioPCDService.registerUsuarioPCD(registrationDTO); 
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioPCDResponseDTO(savedUsuario));
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public ResponseEntity<UsuarioPCDResponseDTO> updateUsuarioPCD(@PathVariable Long id, @RequestBody UsuarioPCDResponseDTO usuarioPCDRequestDTO) {
        Optional<UsuarioPCD> usuarioOptional = usuarioPCDService.findUsuarioPCDById(id);

        if (usuarioOptional.isPresent()) {
            UsuarioPCD updatedUsuario = usuarioPCDService.updateUsuarioPCDById(id, usuarioPCDRequestDTO); 
            return ResponseEntity.ok(new UsuarioPCDResponseDTO(updatedUsuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUsuarioPCD(@PathVariable Long id) {
        if (usuarioPCDService.findUsuarioPCDById(id).isPresent()) {
            usuarioPCDService.deleteUsuarioPCD(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}