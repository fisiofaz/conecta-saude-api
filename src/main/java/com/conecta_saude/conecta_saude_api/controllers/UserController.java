package com.conecta_saude.conecta_saude_api.controllers;

import com.conecta_saude.conecta_saude_api.models.User;
import com.conecta_saude.conecta_saude_api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController 
@RequestMapping("/api/users") 
public class UserController {

    
	@Autowired 
    private UserService userService;
    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.saveUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping 
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}") 
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok) 
                   .orElseGet(() -> ResponseEntity.notFound().build()); 
    }
    
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") 
    public ResponseEntity<?> getAuthenticatedUser() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	Object principal = authentication.getPrincipal();
    	
    	if (principal instanceof User) {
    		User currentUser = (User) principal;
    		return ResponseEntity.ok(currentUser.getEmail());
    	} else {
            return ResponseEntity.status(404).body("User not found or not authenticated properly.");
        }
    	
    }


    @PutMapping("/{id}") 
    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.findUserById(id).isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
}