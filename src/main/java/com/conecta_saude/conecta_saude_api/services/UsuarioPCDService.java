package com.conecta_saude.conecta_saude_api.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDRegistrationDTO;
import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDResponseDTO;
import com.conecta_saude.conecta_saude_api.models.Role;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository;
import com.conecta_saude.conecta_saude_api.repositories.UsuarioPCDRepository;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;



@Service
public class UsuarioPCDService {

    private final UsuarioPCDRepository usuarioPCDRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public UsuarioPCDService(UsuarioPCDRepository usuarioPCDRepository,
                             PasswordEncoder passwordEncoder,
                             RoleRepository roleRepository, 
                             UserRepository userRepository) { 
        this.usuarioPCDRepository = usuarioPCDRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository; 
    }

    public Optional<UsuarioPCD> findByEmail(String email) {
        return usuarioPCDRepository.findByEmail(email);
    }

    public List<UsuarioPCD> findAllUsuariosPCD() {
        return usuarioPCDRepository.findAll();
    }

    public Optional<UsuarioPCD> findUsuarioPCDById(Long id) {
        return usuarioPCDRepository.findById(id);
    }

   
    @Transactional
    public UsuarioPCD registerUsuarioPCD(UsuarioPCDRegistrationDTO registrationDTO) {
    	if (userRepository.existsByEmail(registrationDTO.email())) {
            throw new IllegalArgumentException("Este email já está cadastrado no sistema.");
        }

        UsuarioPCD usuario = new UsuarioPCD();
        usuario.setEmail(registrationDTO.email());
        usuario.setPassword(passwordEncoder.encode(registrationDTO.password())); 
        usuario.setEnabled(true);

        usuario.setNome(registrationDTO.nome());
        usuario.setSobrenome(registrationDTO.sobrenome());
        usuario.setTelefone(registrationDTO.telefone());
        usuario.setDataNascimento(registrationDTO.dataNascimento());
        usuario.setTipoDeficiencia(registrationDTO.tipoDeficiencia());
        usuario.setNecessidadesEspecificas(registrationDTO.necessidadesEspecificas());
        usuario.setEndereco(registrationDTO.endereco());
        usuario.setCidade(registrationDTO.cidade());
        usuario.setEstado(registrationDTO.estado());
        usuario.setCep(registrationDTO.cep());
     
        Role userPCDRole = roleRepository.findByName("ROLE_USUARIO_PCD")
                                        .orElseThrow(() -> new RuntimeException("ROLE_USUARIO_PCD não encontrada no banco de dados. Por favor, crie-a."));
        usuario.setRoles(Collections.singleton(userPCDRole)); 

        return usuarioPCDRepository.save(usuario);
    }
    
   
    @Transactional
    public UsuarioPCD updateUsuarioPCD(String email, UsuarioPCDResponseDTO dto) {
        UsuarioPCD usuario = usuarioPCDRepository.findByEmail(email)
                                             .orElseThrow(() -> new RuntimeException("Usuário PCD não encontrado."));

       
        usuario.setNome(dto.nome()); 
        usuario.setSobrenome(dto.sobrenome());
        usuario.setTelefone(dto.telefone());
        usuario.setCep(dto.cep());
        usuario.setEndereco(dto.endereco());
        usuario.setCidade(dto.cidade());
        usuario.setEstado(dto.estado());
        usuario.setTipoDeficiencia(dto.tipoDeficiencia());
        usuario.setNecessidadesEspecificas(dto.necessidadesEspecificas());
        usuario.setDataNascimento(dto.dataNascimento());

        return usuarioPCDRepository.save(usuario);
    }

    @Transactional
    public UsuarioPCD updateUsuarioPCDById(Long id, UsuarioPCDResponseDTO dto) {
        UsuarioPCD usuario = usuarioPCDRepository.findById(id)
                                             .orElseThrow(() -> new RuntimeException("Usuário PCD não encontrado."));

        
        usuario.setNome(dto.nome()); 
        usuario.setSobrenome(dto.sobrenome());
        usuario.setTelefone(dto.telefone());
        usuario.setCep(dto.cep());
        usuario.setEndereco(dto.endereco());
        usuario.setCidade(dto.cidade());
        usuario.setEstado(dto.estado());
        usuario.setTipoDeficiencia(dto.tipoDeficiencia());
        usuario.setNecessidadesEspecificas(dto.necessidadesEspecificas());
        usuario.setDataNascimento(dto.dataNascimento());

        return usuarioPCDRepository.save(usuario);
    }

    @Transactional
    public void deleteUsuarioPCD(Long id) {
        usuarioPCDRepository.deleteById(id);
    }
}