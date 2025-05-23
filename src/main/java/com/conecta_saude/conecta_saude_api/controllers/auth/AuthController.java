package com.conecta_saude.conecta_saude_api.controllers.auth;

import com.conecta_saude.conecta_saude_api.security.jwt.JwtService;
import com.conecta_saude.conecta_saude_api.models.User; 
import com.conecta_saude.conecta_saude_api.models.Role; 
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional; 

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository; 

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService,
                          UserRepository userRepository, PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository; 
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }

    public static class RegisterRequest {
        public String email;
        public String password;       
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email, request.password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("jwtToken", jwtToken);
        response.put("message", "Autenticação bem-sucedida!");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Email já cadastrado.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        User newUser = new User();
        newUser.setEmail(request.email);
        newUser.setPassword(passwordEncoder.encode(request.password));
        newUser.setEnabled(true);
        
        Optional<Role> defaultRole = roleRepository.findByName("ROLE_USER");

        if (defaultRole.isPresent()) {
            newUser.getRoles().add(defaultRole.get());
        } else {            
            throw new RuntimeException("Role 'ROLE_USER' não encontrada no banco de dados. Por favor, crie-a.");
        }        

        userRepository.save(newUser);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário registrado com sucesso!");
        return ResponseEntity.ok(response);
    }
}