package com.conecta_saude.conecta_saude_api.controllers;

import com.conecta_saude.conecta_saude_api.models.User;
import com.conecta_saude.conecta_saude_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController 
@RequestMapping("/api/users") 
public class UserController {

    @Autowired 
    private UserService userService;

    @GetMapping 
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return ResponseEntity.ok(users); 
    }

    @GetMapping("/{id}") 
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok) 
                   .orElseGet(() -> ResponseEntity.notFound().build()); 
    }

    @PostMapping 
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userService.existsUserByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser); 
    }

    @PutMapping("/{id}") 
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> userOptional = userService.findUserById(id);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            
            existingUser.setEmail(userDetails.getEmail());
           
            existingUser.setEnabled(userDetails.isEnabled());
            existingUser.setRoles(userDetails.getRoles()); 

            User updatedUser = userService.saveUser(existingUser);
            return ResponseEntity.ok(updatedUser); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.findUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
}