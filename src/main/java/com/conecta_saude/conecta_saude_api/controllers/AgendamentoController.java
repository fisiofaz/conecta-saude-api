package com.conecta_saude.conecta_saude_api.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.conecta_saude.conecta_saude_api.dto.AgendamentoRequestDTO;
import com.conecta_saude.conecta_saude_api.dto.AgendamentoResponseDTO;
import com.conecta_saude.conecta_saude_api.dto.AgendamentoUpdateStatusDTO;
import com.conecta_saude.conecta_saude_api.models.Agendamento;
import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;
import com.conecta_saude.conecta_saude_api.services.AgendamentoService;
import com.conecta_saude.conecta_saude_api.services.ProfissionalDeSaudeService;
import com.conecta_saude.conecta_saude_api.services.UsuarioPCDService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Agendamentos", description = "Endpoints para gerenciar agendamentos de consultas entre usuários PCD e profissionais de saúde.")
@RestController
@RequestMapping("/api/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final UsuarioPCDService usuarioPCDService;
    private final ProfissionalDeSaudeService profissionalDeSaudeService; 

    public AgendamentoController(AgendamentoService agendamentoService,
    							UsuarioPCDService usuarioPCDService,
                                 ProfissionalDeSaudeService profissionalDeSaudeService) {
        this.agendamentoService = agendamentoService;
        this.usuarioPCDService = usuarioPCDService;
        this.profissionalDeSaudeService = profissionalDeSaudeService;
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

    @Operation(summary = "Cria um novo agendamento",
            description = "Permite que um usuário PCD autenticado solicite um novo agendamento com um profissional de saúde.") 
    @ApiResponses(value = { // NOVA ANOTAÇÃO
    @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso",
                  content = { @Content(mediaType = "application/json",
                                       schema = @Schema(implementation = AgendamentoResponseDTO.class)) }),
     @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: dados ausentes, horário conflitante)",
                  content = { @Content(mediaType = "application/json",
                                       schema = @Schema(implementation = Map.class)) }), 
     @ApiResponse(responseCode = "401", description = "Não autenticado"),
     @ApiResponse(responseCode = "403", description = "Não autorizado (somente usuário PCD pode criar)"),
     @ApiResponse(responseCode = "404", description = "Profissional de saúde não encontrado"),
     @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
 })
    @PostMapping
    @PreAuthorize("hasRole('USUARIO_PCD')") 
    public ResponseEntity<AgendamentoResponseDTO> createAgendamento(@Valid @RequestBody AgendamentoRequestDTO requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        
        UsuarioPCD usuarioPCDLogado = usuarioPCDService.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário PCD logado não encontrado."));

        try {            
            Agendamento novoAgendamento = agendamentoService.createAgendamento(
            	usuarioPCDLogado,
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
    
    @Operation(summary = "Lista agendamentos",
            description = "Retorna uma lista de agendamentos. Administradores veem todos os agendamentos. Usuários PCD e Profissionais de Saúde veem apenas os seus próprios agendamentos.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de agendamentos retornada com sucesso",
                  content = @Content(mediaType = "application/json",
                                     array = @ArraySchema(schema = @Schema(implementation = AgendamentoResponseDTO.class)))),
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Não autorizado (requer autenticação como USUARIO_PCD, PROFISSIONAL ou ADMIN)"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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
            UsuarioPCD usuarioPCD = usuarioPCDService.findByEmail(currentUserEmail)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário PCD logado não encontrado."));
            agendamentos = agendamentoService.findAgendamentosByUsuarioPCDId(usuarioPCD.getId());
        } else if (isProfissional) {
        	ProfissionalDeSaude profissional = profissionalDeSaudeService.findByEmail(currentUserEmail)
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
    
    @Operation(summary = "Busca um agendamento por ID",
            description = "Retorna um agendamento específico. Administradores veem qualquer agendamento. Usuários PCD e Profissionais de Saúde veem apenas agendamentos aos quais estão associados.")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Agendamento encontrado com sucesso",
                  content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = AgendamentoResponseDTO.class))),
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Não autorizado (usuário autenticado não tem permissão para acessar este agendamento)"),
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })    
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
    
    @Operation(summary = "Atualiza o status de um agendamento",
            description = "Permite que administradores alterem qualquer status. Profissionais de saúde podem CONFIRMAR, CANCELAR_POR_PROFISSIONAL. Usuários PCD podem CANCELAR_POR_USUARIO (somente se o status for PENDENTE).")
    @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Status do agendamento atualizado com sucesso",
                  content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = AgendamentoResponseDTO.class))),
    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: transição de status não permitida, status ausente)",
                  content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Não autorizado (usuário não tem permissão para alterar este agendamento ou status)"),
    @ApiResponse(responseCode = "404", description = "Agendamento não encontrado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PROFISSIONAL', 'ROLE_USUARIO_PCD')") // Correção das roles
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