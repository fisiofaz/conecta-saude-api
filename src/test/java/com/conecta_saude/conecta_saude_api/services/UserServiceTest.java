package com.conecta_saude.conecta_saude_api.services;


import com.conecta_saude.conecta_saude_api.models.User;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import com.conecta_saude.conecta_saude_api.models.AdminUser; 


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Long existingUserId;
    private String newRawPassword;
    private User mockExistingUser;

    @BeforeEach
    void setUp() {
        existingUserId = 1L;
        newRawPassword = "novaSenha123";

        
        mockExistingUser = new AdminUser(); 
        mockExistingUser.setId(existingUserId);
        mockExistingUser.setEmail("test@example.com");
        mockExistingUser.setPassword("senhaAntigaCodificada"); 
        mockExistingUser.setEnabled(true);
    }

    @Test
    void quandoUpdatePasswordComUsuarioExistente_entaoDeveCodificarNovaSenhaESalvarUsuario() {
        
        String newEncodedPassword = "novaSenhaCodificadaSuperSecreta";

        when(userRepository.findById(existingUserId)).thenReturn(Optional.of(mockExistingUser));

        when(passwordEncoder.encode(newRawPassword)).thenReturn(newEncodedPassword);

        userService.updatePassword(existingUserId, newRawPassword);

        verify(userRepository, times(1)).findById(existingUserId);

        
        verify(passwordEncoder, times(1)).encode(newRawPassword);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();

        assertNotNull(savedUser, "O usuário salvo não deveria ser nulo.");
        assertEquals(newEncodedPassword, savedUser.getPassword(), "A senha do usuário não foi atualizada para a nova senha codificada.");
        assertEquals(existingUserId, savedUser.getId(), "O ID do usuário salvo não corresponde ao esperado.");
    }
    
        
    @Test
    void quandoUpdatePasswordParaUsuarioNaoExistente_entaoDeveLancarRuntimeException() {
       
        Long nonExistentUserId = 999L; 
        String umaNovaSenhaQualquer = "novaSenha123";

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        RuntimeException exceptionLancada = assertThrows(RuntimeException.class, () -> {
        
            userService.updatePassword(nonExistentUserId, umaNovaSenhaQualquer);
        });
        
        assertEquals("Usuário com ID " + nonExistentUserId + " não encontrado para atualização de senha.", 
                     exceptionLancada.getMessage(),
                     "A mensagem da exceção para usuário não encontrado não é a esperada.");

        verify(userRepository, times(1)).findById(nonExistentUserId);
       
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}