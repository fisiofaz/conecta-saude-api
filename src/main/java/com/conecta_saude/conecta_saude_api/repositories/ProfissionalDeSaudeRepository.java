package com.conecta_saude.conecta_saude_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;

@Repository
public interface ProfissionalDeSaudeRepository extends JpaRepository<ProfissionalDeSaude, Long> {
	Optional<ProfissionalDeSaude> findByEmail(String email);
	List<ProfissionalDeSaude> findByEspecialidadeContainingIgnoreCase(String especialidade);
    List<ProfissionalDeSaude> findByCidadeConsultorioAndAcessibilidadeConsultorioContainingIgnoreCase(String cidade, String acessibilidade);
}