package com.conecta_saude.conecta_saude_api.controllers.auth;


import com.conecta_saude.conecta_saude_api.dto.auth.AuthenticationRequest;
import com.conecta_saude.conecta_saude_api.dto.auth.AuthenticationResponse;
import com.conecta_saude.conecta_saude_api.dto.ProfissionalDeSaudeRegistrationDTO; 
import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDRegistrationDTO; 
import com.conecta_saude.conecta_saude_api.services.ProfissionalDeSaudeService;
import com.conecta_saude.conecta_saude_api.services.UsuarioPCDService;
import com.conecta_saude.conecta_saude_api.security.jwt.JwtService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation; 
import io.swagger.v3.oas.annotations.media.Content; 
import io.swagger.v3.oas.annotations.media.Schema; 
import io.swagger.v3.oas.annotations.responses.ApiResponse; 
import io.swagger.v3.oas.annotations.responses.ApiResponses; 

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários no sistema Conecta Saúde.")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioPCDService usuarioPCDService;
    private final ProfissionalDeSaudeService profissionalDeSaudeService; 

    
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
                          UsuarioPCDService usuarioPCDService,
                          ProfissionalDeSaudeService profissionalDeSaudeService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioPCDService = usuarioPCDService;
        this.profissionalDeSaudeService = profissionalDeSaudeService;
    }

    
    @Operation(summary = "Autentica um usuário", 
            description = "Realiza a autenticação do usuário com base no email e senha fornecidos. Retorna um token JWT em caso de sucesso.") 
    @ApiResponses(value = { 
     @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida",
                  content = { @Content(mediaType = "application/json",
                                       schema = @Schema(implementation = AuthenticationResponse.class)) }),
     @ApiResponse(responseCode = "401", description = "Credenciais inválidas (email ou senha incorretos)",
                  content = { @Content(mediaType = "application/json",
                                       schema = @Schema(implementation = AuthenticationResponse.class)) }), 
     @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: campos do DTO ausentes ou malformatados)"), 
 })
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        try {
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		request.getEmail(),  
                		request.getPassword() 
                )
            );

            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(jwtToken, "Autenticação bem-sucedida!"));

        } catch (AuthenticationException e) {
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(null, "Credenciais inválidas."));
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthenticationResponse(null, "Erro interno do servidor durante a autenticação: " + e.getMessage()));
        }
    }

    @PostMapping("/register/pcd")
    public ResponseEntity<String> registerPCD(@Valid @RequestBody UsuarioPCDRegistrationDTO registrationDTO) { // Usando o DTO de registro
        try {
            
            usuarioPCDService.registerUsuarioPCD(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário PCD registrado com sucesso!");
        } catch (IllegalArgumentException e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
           
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao registrar usuário PCD: " + e.getMessage());
        }
    }

    @PostMapping("/register/profissional")
    public ResponseEntity<String> registerProfissional(@Valid @RequestBody ProfissionalDeSaudeRegistrationDTO registrationDTO) { // Usando o DTO de registro
        try {
            
            profissionalDeSaudeService.registerProfissionalDeSaude(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Profissional de Saúde registrado com sucesso!");
        } catch (IllegalArgumentException e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao registrar profissional de saúde: " + e.getMessage());
        }
    }
}