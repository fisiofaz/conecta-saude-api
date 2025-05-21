package com.conecta_saude.conecta_saude_api.repositories;

import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository 
public interface UsuarioPCDRepository extends JpaRepository<UsuarioPCD, Long> {
    
}