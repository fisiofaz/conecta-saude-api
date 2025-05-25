package com.conecta_saude.conecta_saude_api.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.conecta_saude.conecta_saude_api.dto.UsuarioPCDResponseDTO;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.services.UsuarioPCDService;

@RestController
@RequestMapping("/api/usuarios-pcd")
public class UsuarioPCDController {

    private final UsuarioPCDService usuarioPCDService;

    public UsuarioPCDController(UsuarioPCDService usuarioPCDService) {
        this.usuarioPCDService = usuarioPCDService;
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USUARIO_PCD')")
    public ResponseEntity<UsuarioPCDResponseDTO> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        Optional<UsuarioPCD> usuarioPCD = usuarioPCDService.findByEmail(userEmail);
        if (usuarioPCD.isPresent()) {
            return ResponseEntity.ok(new UsuarioPCDResponseDTO(usuarioPCD.get()));
        }

        return ResponseEntity.notFound().build();
    }


    @PutMapping("/me")
    @PreAuthorize("hasRole('ROLE_USUARIO_PCD')")
    public ResponseEntity<UsuarioPCDResponseDTO> updateMyProfile(@RequestBody UsuarioPCDResponseDTO usuarioPCDRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        UsuarioPCD updatedUsuario = usuarioPCDService.updateUsuarioPCD(userEmail, usuarioPCDRequestDTO);
        return ResponseEntity.ok(new UsuarioPCDResponseDTO(updatedUsuario));
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public ResponseEntity<List<UsuarioPCDResponseDTO>> getAllUsuariosPCD() {
        List<UsuarioPCD> usuarios = usuarioPCDService.findAllUsuariosPCD();
        List<UsuarioPCDResponseDTO> dtos = usuarios.stream()
                                                    .map(UsuarioPCDResponseDTO::new)
                                                    .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public ResponseEntity<UsuarioPCDResponseDTO> getUsuarioPCDById(@PathVariable Long id) {
        Optional<UsuarioPCD> usuarioOptional = usuarioPCDService.findUsuarioPCDById(id);

        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(new UsuarioPCDResponseDTO(usuarioOptional.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UsuarioPCDResponseDTO> createUsuarioPCD(@RequestBody UsuarioPCDResponseDTO usuarioPCDRequestDTO) {        
        UsuarioPCD savedUsuario = usuarioPCDService.createUsuarioPCD(usuarioPCDRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioPCDResponseDTO(savedUsuario));
    }

    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") 
    public ResponseEntity<UsuarioPCDResponseDTO> updateUsuarioPCD(@PathVariable Long id, @RequestBody UsuarioPCDResponseDTO usuarioPCDRequestDTO) {
        Optional<UsuarioPCD> usuarioOptional = usuarioPCDService.findUsuarioPCDById(id);

        if (usuarioOptional.isPresent()) {
            UsuarioPCD updatedUsuario = usuarioPCDService.updateUsuarioPCDById(id, usuarioPCDRequestDTO); 
            return ResponseEntity.ok(new UsuarioPCDResponseDTO(updatedUsuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUsuarioPCD(@PathVariable Long id) {
        if (usuarioPCDService.findUsuarioPCDById(id).isPresent()) {
            usuarioPCDService.deleteUsuarioPCD(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}