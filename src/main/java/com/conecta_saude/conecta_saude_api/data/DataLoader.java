package com.conecta_saude.conecta_saude_api.data;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.conecta_saude.conecta_saude_api.models.AdminUser;
import com.conecta_saude.conecta_saude_api.models.Role;
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository; 

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${admin.default.email}") 
    private String adminEmail;
    
    @Value("${admin.default.password}")
    private String adminPassword;

    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
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
        
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
            System.out.println("ROLE_ADMIN criada no banco de dados.");
        }
        
        if (!userRepository.existsByEmail(adminEmail)) {
            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");

            if (adminRoleOptional.isEmpty()) {
                
                System.err.println("ERRO INESPERADO: ROLE_ADMIN não encontrada após tentativa de criação!");
                return;
            }
            Role adminRole = adminRoleOptional.get();

            AdminUser adminUser = new AdminUser();
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode(adminPassword)); 
            adminUser.setRoles(Collections.singleton(adminRole));
            adminUser.setEnabled(true);

            userRepository.save(adminUser);
            System.out.println("Usuário Admin padrão criado: " + adminEmail);
        } else {
            System.out.println("Usuário Admin (" + adminEmail + ") já existe.");
        }
    }
}