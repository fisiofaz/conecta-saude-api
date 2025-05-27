package com.conecta_saude.conecta_saude_api.services;

import com.conecta_saude.conecta_saude_api.dto.ProfissionalDeSaudeRegistrationDTO;
import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.Role;
import com.conecta_saude.conecta_saude_api.repositories.ProfissionalDeSaudeRepository;
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) 
class ProfissionalDeSaudeServiceTest {

    
    @Mock
    private ProfissionalDeSaudeRepository profissionalDeSaudeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository; 
    
    @InjectMocks
    private ProfissionalDeSaudeService profissionalDeSaudeService;

    private ProfissionalDeSaudeRegistrationDTO registrationDTO;
    private Role profissionalRole;

    @BeforeEach
    void setUp() {
        
        registrationDTO = new ProfissionalDeSaudeRegistrationDTO(
                "dr.ana.silva@example.com", 
                "senhaSegura123",         
                "Ana",                     
                "Silva",                  
                "11988887777",             
                "Cardiologia",             
                "CRM/SP 123456",           
                "Rua das Palmeiras, 100",  
                "São Paulo",               
                "SP",                     
                "01000-000",              
                "Acessível para cadeirantes", 
                "Português, Inglês",      
                "Consultas, Eletrocardiograma", 
                "Médica cardiologista com foco em saúde preventiva.", 
                "http://example.com/foto_ana.jpg" 
        );

        profissionalRole = new Role();
        profissionalRole.setId(2L); 
        profissionalRole.setName("ROLE_PROFISSIONAL");
    }

    @Test
    void quandoRegistrarNovoProfissional_entaoDeveRetornarProfissionalSalvoComRoleESenhaCodificada() {

        when(userRepository.existsByEmail(registrationDTO.email())).thenReturn(false);

        when(profissionalDeSaudeRepository.findByCrmCrpOutros(registrationDTO.crmCrpOutros())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(registrationDTO.password())).thenReturn("senhaProfissionalCodificada123");

        when(roleRepository.findByName("ROLE_PROFISSIONAL")).thenReturn(Optional.of(profissionalRole));

        when(profissionalDeSaudeRepository.save(any(ProfissionalDeSaude.class))).thenAnswer(invocation -> {
            ProfissionalDeSaude profissionalSalvar = invocation.getArgument(0);
            profissionalSalvar.setId(10L); 
            profissionalSalvar.setPassword("senhaProfissionalCodificada123");
            return profissionalSalvar;
        });

        ProfissionalDeSaude savedProfissional = profissionalDeSaudeService.registerProfissionalDeSaude(registrationDTO);

        
        assertNotNull(savedProfissional, "O profissional salvo não deveria ser nulo.");
        assertNotNull(savedProfissional.getId(), "O ID do profissional salvo não deveria ser nulo.");
        assertEquals(10L, savedProfissional.getId()); 
        assertEquals(registrationDTO.email(), savedProfissional.getEmail(), "O email deve ser o mesmo do DTO.");
        assertEquals("senhaProfissionalCodificada123", savedProfissional.getPassword(), "A senha deve estar codificada.");
        assertTrue(savedProfissional.isEnabled(), "O profissional deve estar habilitado.");
        assertEquals(registrationDTO.nome(), savedProfissional.getNome());
        assertEquals(registrationDTO.especialidade(), savedProfissional.getEspecialidade());
        assertEquals(registrationDTO.crmCrpOutros(), savedProfissional.getCrmCrpOutros());

        assertNotNull(savedProfissional.getRoles(), "O profissional deve ter roles atribuídas.");
        assertFalse(savedProfissional.getRoles().isEmpty(), "A lista de roles não deve estar vazia.");
        assertTrue(savedProfissional.getRoles().contains(profissionalRole), "O profissional deve ter a ROLE_PROFISSIONAL.");
        assertEquals(1, savedProfissional.getRoles().size(), "O profissional deve ter exatamente uma role.");
        
        verify(userRepository, times(1)).existsByEmail(registrationDTO.email());
        verify(profissionalDeSaudeRepository, times(1)).findByCrmCrpOutros(registrationDTO.crmCrpOutros());
        verify(passwordEncoder, times(1)).encode(registrationDTO.password());
        verify(roleRepository, times(1)).findByName("ROLE_PROFISSIONAL");
        verify(profissionalDeSaudeRepository, times(1)).save(any(ProfissionalDeSaude.class));
    }
    
    @Test
    void quandoRegistrarProfissionalComCrmCrpJaExistente_entaoDeveLancarIllegalArgumentException() {

        when(userRepository.existsByEmail(registrationDTO.email())).thenReturn(false);
        when(profissionalDeSaudeRepository.findByCrmCrpOutros(registrationDTO.crmCrpOutros()))
                .thenReturn(Optional.of(new ProfissionalDeSaude())); 

        IllegalArgumentException exceptionLancada = assertThrows(IllegalArgumentException.class, () -> {            
            profissionalDeSaudeService.registerProfissionalDeSaude(registrationDTO);
        });

        assertEquals("Registro profissional (CRM/CRP/Outros) já cadastrado.", exceptionLancada.getMessage(),
                "A mensagem da exceção para CRM/CRP duplicado não é a esperada.");

        verify(userRepository, times(1)).existsByEmail(registrationDTO.email());
        verify(profissionalDeSaudeRepository, times(1)).findByCrmCrpOutros(registrationDTO.crmCrpOutros());
        verify(passwordEncoder, never()).encode(anyString());
        verify(roleRepository, never()).findByName(anyString());
        verify(profissionalDeSaudeRepository, never()).save(any(ProfissionalDeSaude.class));
    }
    
    @Test
    void quandoRegistrarProfissionalComEmailJaExistente_entaoDeveLancarIllegalArgumentException() {
        
        when(userRepository.existsByEmail(registrationDTO.email())).thenReturn(true);

        IllegalArgumentException exceptionLancada = assertThrows(IllegalArgumentException.class, () -> {
            profissionalDeSaudeService.registerProfissionalDeSaude(registrationDTO);
        });

        assertEquals("Este email já está cadastrado no sistema.", exceptionLancada.getMessage());
        verify(userRepository, times(1)).existsByEmail(registrationDTO.email());
        verify(profissionalDeSaudeRepository, never()).findByCrmCrpOutros(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(roleRepository, never()).findByName(anyString());
        verify(profissionalDeSaudeRepository, never()).save(any(ProfissionalDeSaude.class));
    }
    
    @Test
    void quandoRegistrarProfissionalMasRoleNaoExiste_entaoDeveLancarRuntimeException() {
      
        when(userRepository.existsByEmail(registrationDTO.email())).thenReturn(false); 
        when(profissionalDeSaudeRepository.findByCrmCrpOutros(registrationDTO.crmCrpOutros())).thenReturn(Optional.empty()); 
        when(roleRepository.findByName("ROLE_PROFISSIONAL")).thenReturn(Optional.empty()); 

        RuntimeException exceptionLancada = assertThrows(RuntimeException.class, () -> {
            profissionalDeSaudeService.registerProfissionalDeSaude(registrationDTO);
        });

        assertEquals("ROLE_PROFISSIONAL não encontrada.", exceptionLancada.getMessage()); 
        verify(userRepository, times(1)).existsByEmail(registrationDTO.email());
        verify(profissionalDeSaudeRepository, times(1)).findByCrmCrpOutros(registrationDTO.crmCrpOutros());
        verify(passwordEncoder, times(1)).encode(registrationDTO.password()); 
        verify(roleRepository, times(1)).findByName("ROLE_PROFISSIONAL");
        verify(profissionalDeSaudeRepository, never()).save(any(ProfissionalDeSaude.class));
    }
}