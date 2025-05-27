package com.conecta_saude.conecta_saude_api.repositories;

import java.util.List;
import java.util.Optional;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalDeSaudeRepository extends JpaRepository<ProfissionalDeSaude, Long> {
	Optional<ProfissionalDeSaude> findByEmail(String email);
    Optional<ProfissionalDeSaude> findByCrmCrpOutros(String crmCrpOutros); 
    List<ProfissionalDeSaude> findByEspecialidade(String especialidade);
    List<ProfissionalDeSaude> findByCidadeConsultorio(String cidadeConsultorio);
    List<ProfissionalDeSaude> findByCidadeConsultorioAndAcessibilidadeConsultorioContainingIgnoreCase(String cidadeConsultorio, String acessibilidadeConsultorio);
    List<ProfissionalDeSaude> findByEspecialidadeContainingIgnoreCase(String especialidade);
}