package com.conecta_saude.conecta_saude_api.repositories;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfissionalDeSaudeRepository extends JpaRepository<ProfissionalDeSaude, Long> {
    List<ProfissionalDeSaude> findByEspecialidadeContainingIgnoreCase(String especialidade);
    List<ProfissionalDeSaude> findByCidadeConsultorioAndAcessibilidadeConsultorioContainingIgnoreCase(String cidade, String acessibilidade);
}