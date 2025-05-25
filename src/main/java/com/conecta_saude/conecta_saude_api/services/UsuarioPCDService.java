package com.conecta_saude.conecta_saude_api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDResponseDTO;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.repositories.UsuarioPCDRepository;

@Service
public class UsuarioPCDService {

    private final UsuarioPCDRepository usuarioPCDRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired 
    public UsuarioPCDService(UsuarioPCDRepository usuarioPCDRepository, PasswordEncoder passwordEncoder) {
        this.usuarioPCDRepository = usuarioPCDRepository;
        this.passwordEncoder = passwordEncoder; 
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
    public UsuarioPCD createUsuarioPCD(UsuarioPCDResponseDTO dto) {
        UsuarioPCD usuario = new UsuarioPCD();    
        if (usuarioPCDRepository.findByEmail(dto.email()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }        
        usuario.setEmail(dto.email()); 
        
        if (dto.password() != null && !dto.password().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.password()));
        } else {
            throw new IllegalArgumentException("A senha é obrigatória para criar um novo usuário.");
        }
        
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