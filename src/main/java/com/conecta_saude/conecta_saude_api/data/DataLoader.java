package com.conecta_saude.conecta_saude_api.data;

import com.conecta_saude.conecta_saude_api.models.Role; 
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository; 
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
       
        if (roleRepository.findByName("ROLE_PROFISSIONAL").isEmpty()) {
            Role profissionalRole = new Role();
            profissionalRole.setName("ROLE_PROFISSIONAL");
            roleRepository.save(profissionalRole);
            System.out.println("ROLE_PROFISSIONAL criada no banco de dados.");
        }

        
        if (roleRepository.findByName("ROLE_USUARIO_PCD").isEmpty()) {
            Role pcdRole = new Role();
            pcdRole.setName("ROLE_USUARIO_PCD");
            roleRepository.save(pcdRole);
            System.out.println("ROLE_USUARIO_PCD criada no banco de dados.");
        }

        
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
            System.out.println("ROLE_USER criada no banco de dados.");
        }
    }
}