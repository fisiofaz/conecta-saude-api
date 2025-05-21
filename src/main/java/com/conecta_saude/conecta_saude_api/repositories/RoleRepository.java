package com.conecta_saude.conecta_saude_api.repositories;

import com.conecta_saude.conecta_saude_api.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); 
}