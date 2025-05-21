package com.conecta_saude.conecta_saude_api.services;

import com.conecta_saude.conecta_saude_api.models.User;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service 
public class UserService {

    @Autowired 
    private UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}