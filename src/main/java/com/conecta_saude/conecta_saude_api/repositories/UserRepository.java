package com.conecta_saude.conecta_saude_api.repositories;

import com.conecta_saude.conecta_saude_api.models.User; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}