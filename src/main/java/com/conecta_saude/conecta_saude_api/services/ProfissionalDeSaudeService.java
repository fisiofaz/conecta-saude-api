package com.conecta_saude.conecta_saude_api.services;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.repositories.ProfissionalDeSaudeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalDeSaudeService {

    @Autowired
    private ProfissionalDeSaudeRepository profissionalDeSaudeRepository;

    public List<ProfissionalDeSaude> findAllProfissionaisDeSaude() {
        return profissionalDeSaudeRepository.findAll();
    }

    public Optional<ProfissionalDeSaude> findProfissionalDeSaudeById(Long id) {
        return profissionalDeSaudeRepository.findById(id);
    }

    public ProfissionalDeSaude saveProfissionalDeSaude(ProfissionalDeSaude profissionalDeSaude) {
        return profissionalDeSaudeRepository.save(profissionalDeSaude);
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
}