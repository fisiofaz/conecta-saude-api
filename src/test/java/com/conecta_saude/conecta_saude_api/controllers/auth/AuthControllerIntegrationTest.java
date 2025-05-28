package com.conecta_saude.conecta_saude_api.controllers.auth;

import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDRegistrationDTO;
import com.conecta_saude.conecta_saude_api.models.AdminUser;
import com.conecta_saude.conecta_saude_api.models.Role;
import com.conecta_saude.conecta_saude_api.models.User;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.TipoDeficiencia;
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; 
import org.springframework.boot.test.context.SpringBootTest; 
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.test.context.ActiveProfiles; 
import org.springframework.test.web.servlet.MockMvc; 
import org.springframework.transaction.annotation.Transactional; 

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; 
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) 
@AutoConfigureMockMvc 
@ActiveProfiles("test") 
@Transactional 
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; 

    @Autowired
    private ObjectMapper objectMapper; 

    @Autowired
    private UserRepository userRepository; 

    @Autowired
    private RoleRepository roleRepository; 
    
    @Autowired
    private PasswordEncoder passwordEncoder; 

    private UsuarioPCDRegistrationDTO registrationDTO;

    @BeforeEach
    void setUp() {
        
        registrationDTO = new UsuarioPCDRegistrationDTO(
                "novo.pcd@example.com",
                "senhaFort123",
                "Novo",
                "Usuário PCD",
                "21912345678",
                LocalDate.of(1985, 5, 15),
                TipoDeficiencia.VISUAL,
                "Precisa de leitor de tela",
                "Rua Nova, 456",
                "Cidade Teste",
                "RJ",
                "23456-789"
        );
    }

    @Test
    void quandoRegistrarNovoPCD_comDadosValidos_entaoRetornaStatusCreatedEUsuarioExisteNoBanco() throws Exception {
        
        mockMvc.perform(post("/api/auth/register/pcd") 
                .contentType(MediaType.APPLICATION_JSON) 
                .content(objectMapper.writeValueAsString(registrationDTO))) 
                .andExpect(status().isCreated()) 
                .andExpect(content().string("Usuário PCD registrado com sucesso!")); 

        Optional<User> userOptional = userRepository.findByEmail(registrationDTO.email());
        assertTrue(userOptional.isPresent(), "O usuário deveria ter sido salvo no banco de dados.");

        User savedUser = userOptional.get();
        assertTrue(savedUser instanceof UsuarioPCD, "O usuário salvo deveria ser do tipo UsuarioPCD.");
        UsuarioPCD savedPCD = (UsuarioPCD) savedUser;

        assertEquals(registrationDTO.nome(), savedPCD.getNome(), "O nome do usuário não corresponde ao DTO.");
        assertEquals(registrationDTO.email(), savedPCD.getEmail(), "O email não corresponde ao DTO.");
        assertTrue(passwordEncoder.matches(registrationDTO.password(), savedPCD.getPassword()), "A senha não foi codificada ou não corresponde.");
        assertTrue(savedPCD.isEnabled(), "O usuário deveria estar habilitado.");

        assertNotNull(savedPCD.getRoles(), "O usuário deveria ter roles atribuídas.");
        assertFalse(savedPCD.getRoles().isEmpty(), "A lista de roles não deveria estar vazia.");
        assertTrue(savedPCD.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_USUARIO_PCD")),
                   "O usuário deveria ter a ROLE_USUARIO_PCD.");
    }
    
    @Test
    void quandoRegistrarPCDComEmailExistente_entaoRetornaStatusBadRequestEMensagemDeErro() throws Exception {
        
        String emailExistente = "ja.existe@example.com";
        AdminUser usuarioExistente = new AdminUser(); 
        usuarioExistente.setEmail(emailExistente);
        usuarioExistente.setPassword(passwordEncoder.encode("senha123")); 
       
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN"))); 
        usuarioExistente.setRoles(Collections.singleton(adminRole));
        usuarioExistente.setEnabled(true);
        userRepository.save(usuarioExistente); 

        
        UsuarioPCDRegistrationDTO dtoComEmailExistente = new UsuarioPCDRegistrationDTO(
                emailExistente, 
                "outraSenha456",
                "Conflito",
                "De Email",
                "21987654321",
                LocalDate.of(1995, 10, 20),
                TipoDeficiencia.FISICA,
                "Teste de email duplicado",
                "Rua do Conflito, 0",
                "Cidade Duplicada",
                "XX",
                "00000-111"
        );

        mockMvc.perform(post("/api/auth/register/pcd")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoComEmailExistente)))
                .andExpect(status().isBadRequest()) 
                .andExpect(content().string("Este email já está cadastrado no sistema."));

       
        long countUsuariosComEsteEmail = userRepository.findAll().stream()
                                             .filter(user -> user.getEmail().equals(emailExistente))
                                             .count();
        assertEquals(1, countUsuariosComEsteEmail, 
                     "Deveria haver apenas um usuário com o email " + emailExistente + " no banco.");
    }
}