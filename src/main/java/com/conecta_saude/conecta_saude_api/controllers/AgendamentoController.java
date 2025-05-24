package com.conecta_saude.conecta_saude_api.controllers;

import com.conecta_saude.conecta_saude_api.dto.AgendamentoRequestDTO;
import com.conecta_saude.conecta_saude_api.dto.AgendamentoResponseDTO;
import com.conecta_saude.conecta_saude_api.dto.AgendamentoUpdateStatusDTO;
import com.conecta_saude.conecta_saude_api.models.Agendamento;
import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;
import com.conecta_saude.conecta_saude_api.services.AgendamentoService;
import com.conecta_saude.conecta_saude_api.repositories.UsuarioPCDRepository; 
import com.conecta_saude.conecta_saude_api.repositories.ProfissionalDeSaudeRepository; 

import jakarta.validation.Valid; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final UsuarioPCDRepository usuarioPCDRepository; 
    private final ProfissionalDeSaudeRepository profissionalDeSaudeRepository; 

    public AgendamentoController(AgendamentoService agendamentoService,
                                 UsuarioPCDRepository usuarioPCDRepository,
                                 ProfissionalDeSaudeRepository profissionalDeSaudeRepository) {
        this.agendamentoService = agendamentoService;
        this.usuarioPCDRepository = usuarioPCDRepository;
        this.profissionalDeSaudeRepository = profissionalDeSaudeRepository;
    }

    
    private AgendamentoResponseDTO convertToDto(Agendamento agendamento) {
        
        String usuarioPcdNome = (agendamento.getUsuarioPCD() != null) ? agendamento.getUsuarioPCD().getNome() : null;
        Long usuarioPcdId = (agendamento.getUsuarioPCD() != null) ? agendamento.getUsuarioPCD().getId() : null;

        String profissionalSaudeNome = (agendamento.getProfissionalSaude() != null) ? agendamento.getProfissionalSaude().getNome() : null;
        String profissionalSaudeEspecialidade = (agendamento.getProfissionalSaude() != null) ? agendamento.getProfissionalSaude().getEspecialidade() : null;
        Long profissionalSaudeId = (agendamento.getProfissionalSaude() != null) ? agendamento.getProfissionalSaude().getId() : null;

        return new AgendamentoResponseDTO(
            agendamento.getId(),
            usuarioPcdId,
            usuarioPcdNome,
            profissionalSaudeId,
            profissionalSaudeNome,
            profissionalSaudeEspecialidade,
            agendamento.getDataAgendamento(),
            agendamento.getHoraAgendamento(),
            agendamento.getStatus(),
            agendamento.getObservacoesUsuario(),
            agendamento.getObservacoesProfissional(),
            agendamento.getCreatedAt(),
            agendamento.getUpdatedAt()
        );
    }

    
    @PostMapping
    @PreAuthorize("hasRole('USUARIO_PCD')") 
    public ResponseEntity<AgendamentoResponseDTO> createAgendamento(@Valid @RequestBody AgendamentoRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        UsuarioPCD usuarioPCDLogado = usuarioPCDRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário PCD logado não encontrado."));

        if (!usuarioPCDLogado.getId().equals(requestDTO.getUsuarioPcdId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode criar agendamentos para seu próprio perfil.");
        }

        try {            
            Agendamento novoAgendamento = agendamentoService.createAgendamento(
                requestDTO.getUsuarioPcdId(),
                requestDTO.getProfissionalSaudeId(),
                requestDTO.getDataAgendamento(),
                requestDTO.getHoraAgendamento(),
                requestDTO.getObservacoesUsuario()
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novoAgendamento));
        } catch (IllegalArgumentException e) {
            
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) { 

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO_PCD', 'PROFISSIONAL')")
    public ResponseEntity<List<AgendamentoResponseDTO>> getAllAgendamentos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isUsuarioPCD = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USUARIO_PCD"));
        boolean isProfissional = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFISSIONAL"));

        List<Agendamento> agendamentos;

        if (isAdmin) {
            agendamentos = agendamentoService.findAllAgendamentos();
        } else if (isUsuarioPCD) {
            UsuarioPCD usuarioPCD = usuarioPCDRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário PCD logado não encontrado."));
            agendamentos = agendamentoService.findAgendamentosByUsuarioPCDId(usuarioPCD.getId());
        } else if (isProfissional) {
            ProfissionalDeSaude profissional = profissionalDeSaudeRepository.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional de Saúde logado não encontrado."));
            agendamentos = agendamentoService.findAgendamentosByProfissionalSaudeId(profissional.getId());
        } else {
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<AgendamentoResponseDTO> dtoList = agendamentos.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USUARIO_PCD', 'PROFISSIONAL')")
    public ResponseEntity<AgendamentoResponseDTO> getAgendamentoById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Agendamento agendamento = agendamentoService.findAgendamentoById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado."));

        if (isAdmin) {
            return ResponseEntity.ok(convertToDto(agendamento));
        }

        boolean isUsuarioPCDDono = agendamento.getUsuarioPCD().getEmail().equals(currentUserEmail);
        boolean isProfissionalDono = agendamento.getProfissionalSaude().getEmail().equals(currentUserEmail);

        if (isUsuarioPCDDono || isProfissionalDono) {
            return ResponseEntity.ok(convertToDto(agendamento));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este agendamento.");
        }
    }

 
    @PatchMapping("/{id}/status") 
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFISSIONAL', 'USUARIO_PCD')")
    public ResponseEntity<AgendamentoResponseDTO> updateAgendamentoStatus(
        @PathVariable Long id,
        @Valid @RequestBody AgendamentoUpdateStatusDTO updateStatusDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isProfissional = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFISSIONAL"));
        boolean isUsuarioPCD = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USUARIO_PCD"));

        Agendamento agendamento = agendamentoService.findAgendamentoById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado."));

       
        if (isAdmin) {
            
            agendamentoService.updateAgendamentoStatus(id, updateStatusDTO.getNewStatus(), updateStatusDTO.getObservacoesProfissional())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar agendamento."));
        } else if (isProfissional) {
            
            if (!agendamento.getProfissionalSaude().getEmail().equals(currentUserEmail)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar o status deste agendamento.");
            }
            
            if (updateStatusDTO.getNewStatus() == StatusAgendamento.CANCELADO_POR_USUARIO) {
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profissionais não podem definir status de CANCELADO_POR_USUARIO.");
            }
             agendamentoService.updateAgendamentoStatus(id, updateStatusDTO.getNewStatus(), updateStatusDTO.getObservacoesProfissional())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao atualizar agendamento."));

        } else if (isUsuarioPCD) {

            if (!agendamento.getUsuarioPCD().getEmail().equals(currentUserEmail)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para alterar o status deste agendamento.");
            }
            if (updateStatusDTO.getNewStatus() == StatusAgendamento.CANCELADO_POR_USUARIO &&
                agendamento.getStatus() == StatusAgendamento.PENDENTE) { 
                agendamentoService.updateAgendamentoStatus(id, StatusAgendamento.CANCELADO_POR_USUARIO, updateStatusDTO.getObservacoesProfissional())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao cancelar agendamento."));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuários PCD só podem cancelar agendamentos PENDENTES com o status CANCELADO_POR_USUARIO.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para realizar esta operação.");
        }

        
        return agendamentoService.findAgendamentoById(id)
            .map(this::convertToDto)
            .map(ResponseEntity::ok)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado após atualização."));
    }


    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<Void> deleteAgendamento(@PathVariable Long id) {
        if (!agendamentoService.deleteAgendamento(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado para exclusão.");
        }
        return ResponseEntity.noContent().build();
    }
}