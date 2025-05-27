package com.conecta_saude.conecta_saude_api.services;

import com.conecta_saude.conecta_saude_api.models.User;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service 
public class UserService {

  
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findUserByEmail(String email) {
    	return userRepository.findByEmail(email);
    }
    
    @Transactional
    public User saveUser(User user) {
    	if (user.getId() == null && user.getPassword() != null && !user.getPassword().startsWith("{bcrypt}")) { 
        	user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
    	return userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Transactional
    public void updatePassword(Long userId, String newPassword) {
           		Optional<User> userOptional = userRepository.findById(userId);
           		userOptional.ifPresent(user -> {
           			user.setPassword(passwordEncoder.encode(newPassword));
                    userRepository.save(user);
           });
           
           if (userOptional.isEmpty()) {
                 throw new RuntimeException("Usuário com ID " + userId + " não encontrado para atualização de senha.");
           }
     }
}