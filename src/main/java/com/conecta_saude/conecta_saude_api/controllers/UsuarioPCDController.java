package com.conecta_saude.conecta_saude_api.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.services.UsuarioPCDService;

@RestController
@RequestMapping("/api/usuarios-pcd")
public class UsuarioPCDController {

    private final UsuarioPCDService usuarioPCDService;

   
    public UsuarioPCDController(UsuarioPCDService usuarioPCDService) {
        this.usuarioPCDService = usuarioPCDService;
    }

   
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioPCD>> getAllUsuariosPCD() {
        List<UsuarioPCD> usuarios = usuarioPCDService.findAllUsuariosPCD();
        return ResponseEntity.ok(usuarios);
    }

    
    @GetMapping("/{id}") 
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") 
    public ResponseEntity<UsuarioPCD> getUsuarioPCDById(@PathVariable Long id) { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); 

        Optional<UsuarioPCD> usuarioOptional = usuarioPCDService.findUsuarioPCDById(id);

        if (usuarioOptional.isPresent()) {
            UsuarioPCD usuario = usuarioOptional.get();

            
            boolean isAdmin = authentication.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin || usuario.getEmail().equals(currentUserEmail)) {
                return ResponseEntity.ok(usuario);
            } else {
               
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 
            }
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    
    @PostMapping 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioPCD> createUsuarioPCD(@RequestBody UsuarioPCD usuarioPCD) { 
        UsuarioPCD savedUsuario = usuarioPCDService.saveUsuarioPCD(usuarioPCD);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UsuarioPCD> updateUsuarioPCD(@PathVariable Long id, @RequestBody UsuarioPCD usuarioPCDDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName(); 

        Optional<UsuarioPCD> usuarioOptional = usuarioPCDService.findUsuarioPCDById(id);

        if (usuarioOptional.isPresent()) {
            UsuarioPCD existingUsuario = usuarioOptional.get();

            boolean isAdmin = authentication.getAuthorities().stream()
                                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            
            if (isAdmin || existingUsuario.getEmail().equals(currentUserEmail)) {
                existingUsuario.setNome(usuarioPCDDetails.getNome());
                existingUsuario.setSobrenome(usuarioPCDDetails.getSobrenome());
                existingUsuario.setDataNascimento(usuarioPCDDetails.getDataNascimento());
                existingUsuario.setTelefone(usuarioPCDDetails.getTelefone());
                existingUsuario.setTipoDeficiencia(usuarioPCDDetails.getTipoDeficiencia());
                existingUsuario.setNecessidadesEspecificas(usuarioPCDDetails.getNecessidadesEspecificas());
                existingUsuario.setEndereco(usuarioPCDDetails.getEndereco());
                existingUsuario.setCidade(usuarioPCDDetails.getCidade());
                existingUsuario.setEstado(usuarioPCDDetails.getEstado());
                existingUsuario.setCep(usuarioPCDDetails.getCep());

                
                UsuarioPCD updatedUsuario = usuarioPCDService.saveUsuarioPCD(existingUsuario);
                return ResponseEntity.ok(updatedUsuario);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUsuarioPCD(@PathVariable Long id) {
        
        if (usuarioPCDService.findUsuarioPCDById(id).isPresent()) {
            usuarioPCDService.deleteUsuarioPCD(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}