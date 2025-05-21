package com.conecta_saude.conecta_saude_api.services;

import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.repositories.UsuarioPCDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioPCDService {

    @Autowired
    private UsuarioPCDRepository usuarioPCDRepository;

    public List<UsuarioPCD> findAllUsuariosPCD() {
        return usuarioPCDRepository.findAll();
    }

    public Optional<UsuarioPCD> findUsuarioPCDById(Long id) {
        return usuarioPCDRepository.findById(id);
    }

    public UsuarioPCD saveUsuarioPCD(UsuarioPCD usuarioPCD) {
        return usuarioPCDRepository.save(usuarioPCD);
    }

    public void deleteUsuarioPCD(Long id) {
        usuarioPCDRepository.deleteById(id);
    }
}