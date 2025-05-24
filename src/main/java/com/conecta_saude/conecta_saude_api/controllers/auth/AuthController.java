package com.conecta_saude.conecta_saude_api.controllers.auth;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conecta_saude.conecta_saude_api.dto.auth.AuthenticationRequest; 
import com.conecta_saude.conecta_saude_api.dto.auth.AuthenticationResponse;
import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.Role;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.TipoDeficiencia;
import com.conecta_saude.conecta_saude_api.repositories.ProfissionalDeSaudeRepository;
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import com.conecta_saude.conecta_saude_api.repositories.UsuarioPCDRepository;
import com.conecta_saude.conecta_saude_api.security.jwt.JwtService;

import jakarta.validation.Valid; 

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository; 
    private final UsuarioPCDRepository usuarioPCDRepository; 
    private final ProfissionalDeSaudeRepository profissionalDeSaudeRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
            UserRepository userRepository, PasswordEncoder passwordEncoder,
            RoleRepository roleRepository, 
            UsuarioPCDRepository usuarioPCDRepository, 
            ProfissionalDeSaudeRepository profissionalDeSaudeRepository) { 
    	this.authenticationManager = authenticationManager;
    	this.jwtService = jwtService;
    	this.userRepository = userRepository; 
    	this.passwordEncoder = passwordEncoder;
    	this.roleRepository = roleRepository;
    	this.usuarioPCDRepository = usuarioPCDRepository; 
    	this.profissionalDeSaudeRepository = profissionalDeSaudeRepository; 
}

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
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthenticationResponse(null, "Erro interno do servidor durante a autenticação."));
        }
    }

    @PostMapping("/register/pcd") 
    public ResponseEntity<Map<String, String>> registerPCD(@RequestBody UsuarioPCDRegisterRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Email já cadastrado.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        UsuarioPCD newUsuarioPCD = new UsuarioPCD(); 
        newUsuarioPCD.setEmail(request.email);
        newUsuarioPCD.setPassword(passwordEncoder.encode(request.password));
        newUsuarioPCD.setEnabled(true);
        newUsuarioPCD.setNome(request.nome);
        newUsuarioPCD.setSobrenome(request.sobrenome);
        newUsuarioPCD.setDataNascimento(LocalDate.parse(request.dataNascimento)); 
        newUsuarioPCD.setTelefone(request.telefone);
        newUsuarioPCD.setEndereco(request.endereco);
        newUsuarioPCD.setCep(request.cep);
        newUsuarioPCD.setCidade(request.cidade);
        newUsuarioPCD.setEstado(request.estado);
        newUsuarioPCD.setTipoDeficiencia(TipoDeficiencia.valueOf(request.tipoDeficiencia)); 
        newUsuarioPCD.setNecessidadesEspecificas(request.necessidadesEspecificas);

        
        Optional<Role> pcdRole = roleRepository.findByName("ROLE_USUARIO_PCD"); 
        if (pcdRole.isPresent()) {
            newUsuarioPCD.getRoles().add(pcdRole.get());
        } else {
            throw new RuntimeException("Role 'ROLE_USUARIO_PCD' não encontrada no banco de dados. Por favor, crie-a.");
        }

        usuarioPCDRepository.save(newUsuarioPCD); 

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário PCD registrado com sucesso!");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register/profissional") 
    public ResponseEntity<Map<String, String>> registerProfissional(@RequestBody ProfissionalDeSaudeRegisterRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Email já cadastrado.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        ProfissionalDeSaude newProfissional = new ProfissionalDeSaude(); 
        newProfissional.setEmail(request.email);
        newProfissional.setPassword(passwordEncoder.encode(request.password));
        newProfissional.setEnabled(true);
        newProfissional.setNome(request.nome);
        newProfissional.setSobrenome(request.sobrenome);
        newProfissional.setEspecialidade(request.especialidade);
        newProfissional.setCrmCrpOutros(request.crmCrpOutros);
        newProfissional.setCepConsultorio(request.cepConsultorio);
        newProfissional.setCidadeConsultorio(request.cidadeConsultorio);
        newProfissional.setEstadoConsultorio(request.estadoConsultorio);
        newProfissional.setAcessibilidadeConsultorio(request.acessibilidadeConsultorio);
        newProfissional.setIdiomasComunicacao(request.idiomasComunicacao);
        newProfissional.setServicosOferecidos(request.servicosOferecidos);

       
        Optional<Role> profissionalRole = roleRepository.findByName("ROLE_PROFISSIONAL"); 
        if (profissionalRole.isPresent()) {
            newProfissional.getRoles().add(profissionalRole.get());
        } else {
            throw new RuntimeException("Role 'ROLE_PROFISSIONAL' não encontrada no banco de dados. Por favor, crie-a.");
        }

        profissionalDeSaudeRepository.save(newProfissional); 

        Map<String, String> response = new HashMap<>();
        response.put("message", "Profissional de Saúde registrado com sucesso!");
        return ResponseEntity.ok(response);
    }
}