package com.conecta_saude.conecta_saude_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.conecta_saude.conecta_saude_api.models.User;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); 
    boolean existsByEmail(String email); 
}