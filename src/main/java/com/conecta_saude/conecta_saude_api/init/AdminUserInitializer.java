package com.conecta_saude.conecta_saude_api.init;

import com.conecta_saude.conecta_saude_api.models.AdminUser;
import com.conecta_saude.conecta_saude_api.models.Role;
import com.conecta_saude.conecta_saude_api.repositories.RoleRepository;
import com.conecta_saude.conecta_saude_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.default.email}")
    private String adminEmail;

    @Value("${admin.default.password}")
    private String adminPassword;

    public AdminUserInitializer(UserRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional 
    public void run(String... args) throws Exception {
        
        if (!userRepository.existsByEmail(adminEmail)) {
            Optional<Role> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");

            if (adminRoleOptional.isEmpty()) {
                
                System.err.println("ERRO CRÍTICO: ROLE_ADMIN não encontrada! Crie-a manualmente ou via DataLoader.");
                
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