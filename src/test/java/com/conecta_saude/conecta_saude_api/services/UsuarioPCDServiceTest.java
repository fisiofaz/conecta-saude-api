package com.conecta_saude.conecta_saude_api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDRegistrationDTO;
import com.conecta_saude.conecta_saude_api.models.Role;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.TipoDeficiencia;
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import com.conecta_saude.conecta_saude_api.repositories.UsuarioPCDRepository; 

//@ExtendWith(MockitoExtension.class) 
class UsuarioPCDServiceTest {

    @Mock 
    private UsuarioPCDRepository usuarioPCDRepository;

    @Mock 
    private PasswordEncoder passwordEncoder;

    @Mock 
    private RoleRepository roleRepository;

    @Mock 
    private UserRepository userRepository;

    //@InjectMocks 
    private UsuarioPCDRegistrationDTO registrationDTO;
    private Role pcdRole;
    //private UsuarioPCD expectedUser;
    private UsuarioPCDService usuarioPCDService;

    @BeforeEach 
    void setUp() {
    	MockitoAnnotations.openMocks(this); 
    	 
    	assertNotNull(usuarioPCDRepository, "usuarioPCDRepository não deveria ser nulo");
        assertNotNull(passwordEncoder, "passwordEncoder não deveria ser nulo");
        assertNotNull(roleRepository, "roleRepository não deveria ser nulo");
        assertNotNull(userRepository, "userRepository não deveria ser nulo");
        
        usuarioPCDService = new UsuarioPCDService(
                usuarioPCDRepository,
                passwordEncoder,
                roleRepository,
                userRepository
        );
        
        assertNotNull(usuarioPCDService, "UsuarioPCDService (criado manualmente) não deveria ser nulo");
        
        registrationDTO = new UsuarioPCDRegistrationDTO(
                "testepcd@example.com",
                "password123",
                "Teste",
                "PCD",
                "11999998888",
                LocalDate.of(1990, 1, 1),
                TipoDeficiencia.FISICA,
                "Nenhuma necessidade específica",
                "Rua Teste, 123",
                "Testelândia",
                "TS",
                "12345-678"
        );

        pcdRole = new Role();
        pcdRole.setId(1L); 
        pcdRole.setName("ROLE_USUARIO_PCD");

        //expectedUser = new UsuarioPCD();
       // expectedUser.setEmail(registrationDTO.email());
        //expectedUser.setNome(registrationDTO.nome());       
        //expectedUser.setRoles(Collections.singleton(pcdRole));
       // expectedUser.setEnabled(true);
    }

    @Test
    void quandoRegistrarNovoUsuarioPCD_entaoDeveRetornarUsuarioSalvoComRoleCorretaESenhaCodificada() {
        
        when(userRepository.existsByEmail(registrationDTO.email())).thenReturn(false);       
        when(passwordEncoder.encode(registrationDTO.password())).thenReturn("senhaCodificadaSuperSecreta");
        when(roleRepository.findByName("ROLE_USUARIO_PCD")).thenReturn(Optional.of(pcdRole));        
        when(usuarioPCDRepository.save(any(UsuarioPCD.class))).thenAnswer(invocation -> {
            UsuarioPCD userToSave = invocation.getArgument(0);
            userToSave.setId(1L);             
            userToSave.setPassword("senhaCodificadaSuperSecreta"); 
            return userToSave;
        });
        
        UsuarioPCD savedUser = usuarioPCDService.registerUsuarioPCD(registrationDTO);        
        assertNotNull(savedUser, "O usuário salvo não deveria ser nulo.");
        assertNotNull(savedUser.getId(), "O ID do usuário salvo não deveria ser nulo.");
        assertEquals(registrationDTO.email(), savedUser.getEmail(), "O email do usuário salvo deve ser o mesmo do DTO.");
        assertEquals("senhaCodificadaSuperSecreta", savedUser.getPassword(), "A senha do usuário salvo deve estar codificada.");
        assertTrue(savedUser.isEnabled(), "O usuário salvo deve estar habilitado.");
        assertNotNull(savedUser.getRoles(), "O usuário salvo deve ter roles atribuídas.");
        assertFalse(savedUser.getRoles().isEmpty(), "A lista de roles não deve estar vazia.");
        assertTrue(savedUser.getRoles().contains(pcdRole), "O usuário salvo deve ter a ROLE_USUARIO_PCD.");
        assertEquals(1, savedUser.getRoles().size(), "O usuário salvo deve ter exatamente uma role.");
        
        verify(userRepository, times(1)).existsByEmail(registrationDTO.email()); 
        verify(passwordEncoder, times(1)).encode(registrationDTO.password()); 
        verify(roleRepository, times(1)).findByName("ROLE_USUARIO_PCD"); 
        verify(usuarioPCDRepository, times(1)).save(any(UsuarioPCD.class)); 
    }
    
    @Test
    void quandoRegistrarUsuarioPCDComEmailQueJaExiste_entaoDeveLancarIllegalArgumentException() {
        
         when(userRepository.existsByEmail(registrationDTO.email())).thenReturn(true);
       
        IllegalArgumentException exceptionLancada = assertThrows(IllegalArgumentException.class, () -> {
           
            usuarioPCDService.registerUsuarioPCD(registrationDTO);
        });
        
        assertEquals("Este email já está cadastrado no sistema.", exceptionLancada.getMessage(), "A mensagem da exceção não é a esperada.");
        
        verify(passwordEncoder, never()).encode(anyString()); 
        verify(roleRepository, never()).findByName(anyString());
        verify(usuarioPCDRepository, never()).save(any(UsuarioPCD.class));
    }
    
    @Test
    void quandoRegistrarUsuarioPCDMasRoleNaoExiste_entaoDeveLancarRuntimeException() {        
        when(userRepository.existsByEmail(registrationDTO.email())).thenReturn(false);        
        
        when(roleRepository.findByName("ROLE_USUARIO_PCD")).thenReturn(Optional.empty()); 

        RuntimeException exceptionLancada = assertThrows(RuntimeException.class, () -> {
            usuarioPCDService.registerUsuarioPCD(registrationDTO);
        });

        assertEquals("ROLE_USUARIO_PCD não encontrada no banco de dados. Por favor, crie-a.", exceptionLancada.getMessage());

        verify(passwordEncoder, times(1)).encode(registrationDTO.password());
        verify(usuarioPCDRepository, never()).save(any(UsuarioPCD.class));
    }
}