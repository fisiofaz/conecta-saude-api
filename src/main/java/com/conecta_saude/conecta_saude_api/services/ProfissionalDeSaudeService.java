package com.conecta_saude.conecta_saude_api.services;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.repositories.ProfissionalDeSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.conecta_saude.conecta_saude_api.dto.ProfissionalDeSaudeRegistrationDTO;
import org.springframework.transaction.annotation.Transactional;
import com.conecta_saude.conecta_saude_api.models.Role;

import com.conecta_saude.conecta_saude_api.repositories.RoleRepository;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalDeSaudeService {

    @Autowired
    private ProfissionalDeSaudeRepository profissionalDeSaudeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository; 
    private final UserRepository userRepository;
    
    @Autowired
    public ProfissionalDeSaudeService(ProfissionalDeSaudeRepository profissionalDeSaudeRepository,
                                      PasswordEncoder passwordEncoder,
                                      RoleRepository roleRepository,
                                      UserRepository userRepository) { 
        this.profissionalDeSaudeRepository = profissionalDeSaudeRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository; 
        this.userRepository = userRepository;
    }
    
    public ProfissionalDeSaude save(ProfissionalDeSaude profissionalDeSaude) {
        return profissionalDeSaudeRepository.save(profissionalDeSaude);
    }
    
    public Optional<ProfissionalDeSaude> findByEmail(String email) {
        return profissionalDeSaudeRepository.findByEmail(email);
    }

    public List<ProfissionalDeSaude> findAllProfissionaisDeSaude() {
        return profissionalDeSaudeRepository.findAll();
    }

    public Optional<ProfissionalDeSaude> findProfissionalDeSaudeById(Long id) {
        return profissionalDeSaudeRepository.findById(id);
    }

    public void deleteProfissionalDeSaude(Long id) {
        profissionalDeSaudeRepository.deleteById(id);
    }

    public List<ProfissionalDeSaude> findProfissionaisByEspecialidade(String especialidade) {
        return profissionalDeSaudeRepository.findByEspecialidadeContainingIgnoreCase(especialidade);
    }

    public List<ProfissionalDeSaude> findProfissionaisByCidadeAndAcessibilidade(String cidade, String acessibilidade) {
        return profissionalDeSaudeRepository.findByCidadeConsultorioAndAcessibilidadeConsultorioContainingIgnoreCase(cidade, acessibilidade);
    }

	@Transactional 
    public ProfissionalDeSaude registerProfissionalDeSaude(ProfissionalDeSaudeRegistrationDTO registrationDTO) { 
		if (userRepository.existsByEmail(registrationDTO.email())) {
            throw new IllegalArgumentException("Este email já está cadastrado no sistema.");
        }
		if (profissionalDeSaudeRepository.findByCrmCrpOutros(registrationDTO.crmCrpOutros()).isPresent()) {
            throw new IllegalArgumentException("Registro profissional (CRM/CRP/Outros) já cadastrado.");
        }

        ProfissionalDeSaude profissional = new ProfissionalDeSaude();
        profissional.setEmail(registrationDTO.email());
        profissional.setPassword(passwordEncoder.encode(registrationDTO.password()));
        profissional.setNome(registrationDTO.nome());
        profissional.setSobrenome(registrationDTO.sobrenome());
        profissional.setTelefone(registrationDTO.telefone());
        profissional.setEspecialidade(registrationDTO.especialidade());
        profissional.setCrmCrpOutros(registrationDTO.crmCrpOutros());
        profissional.setEnderecoConsultorio(registrationDTO.enderecoConsultorio());
        profissional.setCidadeConsultorio(registrationDTO.cidadeConsultorio());
        profissional.setEstadoConsultorio(registrationDTO.estadoConsultorio());
        profissional.setCepConsultorio(registrationDTO.cepConsultorio());
        profissional.setAcessibilidadeConsultorio(registrationDTO.acessibilidadeConsultorio());
        profissional.setIdiomasComunicacao(registrationDTO.idiomasComunicacao());
        profissional.setServicosOferecidos(registrationDTO.servicosOferecidos());
        profissional.setSobreMim(registrationDTO.sobreMim());
        profissional.setFotoPerfilUrl(registrationDTO.fotoPerfilUrl());
        profissional.setEnabled(true); 

       
        Role profissionalRole = roleRepository.findByName("ROLE_PROFISSIONAL")
                                            .orElseThrow(() -> new RuntimeException("ROLE_PROFISSIONAL não encontrada."));
        profissional.setRoles(Collections.singleton(profissionalRole));

        return profissionalDeSaudeRepository.save(profissional);
    }
	
	public Optional<ProfissionalDeSaude> findById(Long id) {
        return profissionalDeSaudeRepository.findById(id);
    }    
}