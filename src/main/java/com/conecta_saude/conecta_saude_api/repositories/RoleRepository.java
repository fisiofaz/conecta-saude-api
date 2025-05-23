package com.conecta_saude.conecta_saude_api.repositories;

import com.conecta_saude.conecta_saude_api.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
   Optional<Role> findByName(String name);
}