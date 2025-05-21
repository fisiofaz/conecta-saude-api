package com.conecta_saude.conecta_saude_api.controllers;

import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.services.UsuarioPCDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios-pcd")
public class UsuarioPCDController {

    @Autowired
    private UsuarioPCDService usuarioPCDService;

    @GetMapping
    public ResponseEntity<List<UsuarioPCD>> getAllUsuariosPCD() {
        List<UsuarioPCD> usuarios = usuarioPCDService.findAllUsuariosPCD();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPCD> getUsuarioPCDById(@PathVariable Long id) {
        Optional<UsuarioPCD> usuario = usuarioPCDService.findUsuarioPCDById(id);
        return usuario.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioPCD> createUsuarioPCD(@RequestBody UsuarioPCD usuarioPCD) {
        UsuarioPCD savedUsuario = usuarioPCDService.saveUsuarioPCD(usuarioPCD);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioPCD> updateUsuarioPCD(@PathVariable Long id, @RequestBody UsuarioPCD usuarioPCDDetails) {
        Optional<UsuarioPCD> usuarioOptional = usuarioPCDService.findUsuarioPCDById(id);

        if (usuarioOptional.isPresent()) {
            UsuarioPCD existingUsuario = usuarioOptional.get();
           
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

           
            existingUsuario.setEmail(usuarioPCDDetails.getEmail());
            existingUsuario.setEnabled(usuarioPCDDetails.isEnabled());
            existingUsuario.setRoles(usuarioPCDDetails.getRoles());

            UsuarioPCD updatedUsuario = usuarioPCDService.saveUsuarioPCD(existingUsuario);
            return ResponseEntity.ok(updatedUsuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuarioPCD(@PathVariable Long id) {
        if (usuarioPCDService.findUsuarioPCDById(id).isPresent()) {
            usuarioPCDService.deleteUsuarioPCD(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}