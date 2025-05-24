package com.conecta_saude.conecta_saude_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;

@Repository
public interface UsuarioPCDRepository extends JpaRepository<UsuarioPCD, Long> {
    Optional<UsuarioPCD> findByEmail(String email);
}