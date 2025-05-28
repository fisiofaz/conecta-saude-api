package com.conecta_saude.conecta_saude_api.services;


import com.conecta_saude.conecta_saude_api.models.Agendamento;
import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.models.enums.StatusAgendamento;
import com.conecta_saude.conecta_saude_api.repositories.AgendamentoRepository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private UsuarioPCDService usuarioPCDService; 

    @Mock
    private ProfissionalDeSaudeService profissionalDeSaudeService; 

    @InjectMocks
    private AgendamentoService agendamentoService;


    private UsuarioPCD mockUsuarioPCD;
    private ProfissionalDeSaude mockProfissional;
    private Long profissionalId;
    private LocalDate dataAgendamento;
    private LocalTime horaAgendamento;
    private String observacoesUsuario;

    @BeforeEach
    void setUp() {
        
        mockUsuarioPCD = new UsuarioPCD(); 
        mockUsuarioPCD.setId(1L);
        mockUsuarioPCD.setNome("Usuário Teste PCD");

        mockProfissional = new ProfissionalDeSaude();
        mockProfissional.setId(10L);
        mockProfissional.setNome("Dr. House");
        mockProfissional.setEspecialidade("Diagnóstico");

        profissionalId = 10L; 
        dataAgendamento = LocalDate.now().plusDays(5); 
        horaAgendamento = LocalTime.of(10, 0); 
        observacoesUsuario = "Consulta de rotina";
    }

    @Test
    void quandoCriarAgendamentoComDadosValidosEHorarioDisponivel_entaoDeveRetornarAgendamentoSalvo() {
        
        when(profissionalDeSaudeService.findProfissionalDeSaudeById(profissionalId))
                .thenReturn(Optional.of(mockProfissional));

        when(agendamentoRepository.findByProfissionalSaudeAndDataAgendamentoAndHoraAgendamento(
                mockProfissional, dataAgendamento, horaAgendamento))
                .thenReturn(Optional.empty()); 
        when(agendamentoRepository.save(any(Agendamento.class))).thenAnswer(invocation -> {
            Agendamento agendamentoParaSalvar = invocation.getArgument(0);
           
            Agendamento agendamentoSalvo = new Agendamento(
                    agendamentoParaSalvar.getUsuarioPCD(),
                    agendamentoParaSalvar.getProfissionalSaude(),
                    agendamentoParaSalvar.getDataAgendamento(),
                    agendamentoParaSalvar.getHoraAgendamento(),
                    agendamentoParaSalvar.getObservacoesUsuario()
            );
            agendamentoSalvo.setId(100L); 
            return agendamentoSalvo;
        });

        Agendamento agendamentoCriado = agendamentoService.createAgendamento(
                mockUsuarioPCD,
                profissionalId,
                dataAgendamento,
                horaAgendamento,
                observacoesUsuario
        );

        assertNotNull(agendamentoCriado, "O agendamento criado não deveria ser nulo.");
        assertNotNull(agendamentoCriado.getId(), "O ID do agendamento criado não deveria ser nulo.");
        assertEquals(100L, agendamentoCriado.getId());
        assertEquals(mockUsuarioPCD, agendamentoCriado.getUsuarioPCD(), "O UsuarioPCD do agendamento não é o esperado.");
        assertEquals(mockProfissional, agendamentoCriado.getProfissionalSaude(), "O ProfissionalDeSaude do agendamento não é o esperado.");
        assertEquals(dataAgendamento, agendamentoCriado.getDataAgendamento(), "A data do agendamento não é a esperada.");
        assertEquals(horaAgendamento, agendamentoCriado.getHoraAgendamento(), "A hora do agendamento não é a esperada.");
        assertEquals(StatusAgendamento.PENDENTE, agendamentoCriado.getStatus(), "O status inicial do agendamento deve ser PENDENTE.");
        assertEquals(observacoesUsuario, agendamentoCriado.getObservacoesUsuario(), "As observações do usuário não são as esperadas.");

        verify(profissionalDeSaudeService, times(1)).findProfissionalDeSaudeById(profissionalId);
        verify(agendamentoRepository, times(1)).findByProfissionalSaudeAndDataAgendamentoAndHoraAgendamento(
                mockProfissional, dataAgendamento, horaAgendamento);
        verify(agendamentoRepository, times(1)).save(any(Agendamento.class));
    }
    
    @Test
    void quandoCriarAgendamentoMasHorarioConflitante_entaoDeveLancarIllegalArgumentException() {
        
        when(profissionalDeSaudeService.findProfissionalDeSaudeById(profissionalId))
                .thenReturn(Optional.of(mockProfissional));

        Agendamento agendamentoConflitanteExistente = new Agendamento();
        agendamentoConflitanteExistente.setId(200L); 
        agendamentoConflitanteExistente.setProfissionalSaude(mockProfissional);
        agendamentoConflitanteExistente.setDataAgendamento(dataAgendamento);
        agendamentoConflitanteExistente.setHoraAgendamento(horaAgendamento);
        agendamentoConflitanteExistente.setStatus(StatusAgendamento.CONFIRMADO); 

        when(agendamentoRepository.findByProfissionalSaudeAndDataAgendamentoAndHoraAgendamento(
                mockProfissional, dataAgendamento, horaAgendamento))
                .thenReturn(Optional.of(agendamentoConflitanteExistente));

        IllegalArgumentException exceptionLancada = assertThrows(IllegalArgumentException.class, () -> {
            agendamentoService.createAgendamento(
                    mockUsuarioPCD,
                    profissionalId,
                    dataAgendamento,
                    horaAgendamento,
                    observacoesUsuario
            );
        });

        assertEquals("Horário já agendado ou pendente para este profissional.", exceptionLancada.getMessage(),
                "A mensagem da exceção para horário conflitante não é a esperada.");

        
        verify(profissionalDeSaudeService, times(1)).findProfissionalDeSaudeById(profissionalId);
        
        verify(agendamentoRepository, times(1)).findByProfissionalSaudeAndDataAgendamentoAndHoraAgendamento(
                mockProfissional, dataAgendamento, horaAgendamento);
       
        verify(agendamentoRepository, never()).save(any(Agendamento.class));
    }
   
}