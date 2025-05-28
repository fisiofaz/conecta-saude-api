package com.conecta_saude.conecta_saude_api.controllers;

import com.conecta_saude.conecta_saude_api.dto.AgendamentoRequestDTO;
import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDRegistrationDTO;
import com.conecta_saude.conecta_saude_api.dto.auth.AuthenticationRequest;
import com.conecta_saude.conecta_saude_api.dto.auth.AuthenticationResponse;
import com.conecta_saude.conecta_saude_api.models.Agendamento;
import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.Role;
import com.conecta_saude.conecta_saude_api.models.User;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;
import com.conecta_saude.conecta_saude_api.models.enums.TipoDeficiencia;
import com.conecta_saude.conecta_saude_api.repositories.AgendamentoRepository;
import com.conecta_saude.conecta_saude_api.repositories.ProfissionalDeSaudeRepository;
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import com.conecta_saude.conecta_saude_api.services.UsuarioPCDService; 
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AgendamentoControllerIntegrationTest {

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

    @Autowired
    private ProfissionalDeSaudeRepository profissionalDeSaudeRepository; 

    @Autowired
    private AgendamentoRepository agendamentoRepository; 

    @Autowired
    private UsuarioPCDService usuarioPCDService;


    private UsuarioPCDRegistrationDTO pcdRegistrationDTO;
    private String pcdRawPassword = "pcdPassword123";
    private ProfissionalDeSaude profissionalDeTeste;

    @BeforeEach
    void setUp() {
        
        if (roleRepository.findByName("ROLE_USUARIO_PCD").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_USUARIO_PCD"));
        }
        if (roleRepository.findByName("ROLE_PROFISSIONAL").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_PROFISSIONAL"));
        }

        profissionalDeTeste = new ProfissionalDeSaude();
        profissionalDeTeste.setEmail("profissional.agenda@example.com");
        profissionalDeTeste.setPassword(passwordEncoder.encode("profSenha123"));
        profissionalDeTeste.setNome("Dr(a).");
        profissionalDeTeste.setSobrenome("Agenda");
        profissionalDeTeste.setEspecialidade("Clinica Geral");
        profissionalDeTeste.setCrmCrpOutros("CRMTESTE123");
        profissionalDeTeste.setEnabled(true);
        Role profRole = roleRepository.findByName("ROLE_PROFISSIONAL").get();
        profissionalDeTeste.setRoles(Collections.singleton(profRole));
        profissionalDeTeste = userRepository.save(profissionalDeTeste); 

        pcdRegistrationDTO = new UsuarioPCDRegistrationDTO(
                "pcd.agenda.teste@example.com",
                pcdRawPassword,
                "PCD Agenda",
                "Teste",
                "11999990000",
                LocalDate.of(1990, 1, 15),
                TipoDeficiencia.FISICA,
                "Necessita agendar consulta",
                "Rua do Teste de Agendamento, 10",
                "Agendópolis",
                "AG",
                "70000-001"
        );
    }

    @Test
    void quandoPCDRegistradoLogadoTentaCriarAgendamento_comDadosValidos_entaoSucesso() throws Exception {
        
        UsuarioPCD pcdSalvo = usuarioPCDService.registerUsuarioPCD(pcdRegistrationDTO);
        assertNotNull(pcdSalvo.getId()); 

        AuthenticationRequest loginRequest = new AuthenticationRequest(pcdRegistrationDTO.email(), pcdRawPassword);
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = loginResult.getResponse().getContentAsString();
        AuthenticationResponse authResponse = objectMapper.readValue(responseString, AuthenticationResponse.class);
        String pcdUserToken = authResponse.getToken();
        assertNotNull(pcdUserToken, "Token não deveria ser nulo após login.");

        AgendamentoRequestDTO agendamentoRequestDTO = new AgendamentoRequestDTO();
        agendamentoRequestDTO.setProfissionalSaudeId(profissionalDeTeste.getId()); 
        agendamentoRequestDTO.setDataAgendamento(LocalDate.now().plusDays(7)); 
        agendamentoRequestDTO.setHoraAgendamento(LocalTime.of(14, 30));
        agendamentoRequestDTO.setObservacoesUsuario("Minha primeira consulta de agendamento via teste!");

        mockMvc.perform(post("/api/agendamentos") 
                .header("Authorization", "Bearer " + pcdUserToken) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agendamentoRequestDTO)))
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.id").exists()) 
                .andExpect(jsonPath("$.usuarioPcdId").value(pcdSalvo.getId()))
                .andExpect(jsonPath("$.profissionalSaudeId").value(profissionalDeTeste.getId()))
                .andExpect(jsonPath("$.status").value(StatusAgendamento.PENDENTE.toString())); 

        List<Agendamento> agendamentosDoPCD = agendamentoRepository.findByUsuarioPCD(pcdSalvo);
        assertFalse(agendamentosDoPCD.isEmpty(), "Deveria haver um agendamento para o PCD no banco.");
        assertEquals(1, agendamentosDoPCD.size(), "Deveria haver exatamente um agendamento para o PCD.");
        Agendamento agendamentoSalvo = agendamentosDoPCD.get(0);
        assertEquals(agendamentoRequestDTO.getDataAgendamento(), agendamentoSalvo.getDataAgendamento());
        assertEquals(agendamentoRequestDTO.getHoraAgendamento(), agendamentoSalvo.getHoraAgendamento());
        assertEquals(StatusAgendamento.PENDENTE, agendamentoSalvo.getStatus());
    }
    
    @Test
    void quandoPCDLogadoListaAgendamentos_entaoRetornaApenasSeusAgendamentosComStatusOk() throws Exception {
        
        String pcd1RawPassword = "senhaPCD1";
        UsuarioPCDRegistrationDTO pcd1Dto = new UsuarioPCDRegistrationDTO(
                "pcd1.lista@example.com", pcd1RawPassword, "PCD Um", "Lista", "111111111",
                LocalDate.of(1990, 1, 1), TipoDeficiencia.VISUAL, "Nenhuma",
                "Rua A, 1", "Cidade A", "AA", "10000-001"
        );
        UsuarioPCD pcd1Salvo = usuarioPCDService.registerUsuarioPCD(pcd1Dto); 

        UsuarioPCDRegistrationDTO pcd2Dto = new UsuarioPCDRegistrationDTO(
                "pcd2.outros@example.com", "senhaPCD2", "PCD Dois", "Outros", "222222222",
                LocalDate.of(1992, 2, 2), TipoDeficiencia.AUDITIVA, "Nenhuma",
                "Rua B, 2", "Cidade B", "BB", "20000-002"
        );
        UsuarioPCD pcd2Salvo = usuarioPCDService.registerUsuarioPCD(pcd2Dto);

        Agendamento ag1PCD1 = new Agendamento(pcd1Salvo, profissionalDeTeste,
                LocalDate.now().plusDays(5), LocalTime.of(10, 0), "Consulta de rotina PCD1");
        agendamentoRepository.save(ag1PCD1);

        Agendamento ag2PCD1 = new Agendamento(pcd1Salvo, profissionalDeTeste,
                LocalDate.now().plusDays(6), LocalTime.of(11, 0), "Retorno PCD1");
        agendamentoRepository.save(ag2PCD1);

        Agendamento ag1PCD2 = new Agendamento(pcd2Salvo, profissionalDeTeste,
                LocalDate.now().plusDays(7), LocalTime.of(14, 0), "Consulta PCD2");
        agendamentoRepository.save(ag1PCD2);


        AuthenticationRequest loginRequestPCD1 = new AuthenticationRequest(pcd1Dto.email(), pcd1RawPassword);
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestPCD1)))
                .andExpect(status().isOk())
                .andReturn();
        String responseString = loginResult.getResponse().getContentAsString();
        AuthenticationResponse authResponse = objectMapper.readValue(responseString, AuthenticationResponse.class);
        String pcd1Token = authResponse.getToken();


        mockMvc.perform(get("/api/agendamentos") 
                .header("Authorization", "Bearer " + pcd1Token)) 
                // Assert
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$", hasSize(2))) 
                .andExpect(jsonPath("$[0].usuarioPcdId").value(pcd1Salvo.getId())) 
                .andExpect(jsonPath("$[1].usuarioPcdId").value(pcd1Salvo.getId())) 
                .andExpect(jsonPath("$[0].observacoesUsuario").value("Consulta de rotina PCD1")) 
                .andExpect(jsonPath("$[1].observacoesUsuario").value("Retorno PCD1"));
    }
}