package com.conecta_saude.conecta_saude_api.config.security;

import com.conecta_saude.conecta_saude_api.models.ProfissionalDeSaude;
import com.conecta_saude.conecta_saude_api.models.UsuarioPCD;
import com.conecta_saude.conecta_saude_api.repositories.ProfissionalDeSaudeRepository;
import com.conecta_saude.conecta_saude_api.repositories.UsuarioPCDRepository;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    private final UsuarioPCDRepository usuarioPCDRepository;
    private final ProfissionalDeSaudeRepository profissionalDeSaudeRepository;

    
    public ApplicationConfig(UsuarioPCDRepository usuarioPCDRepository, ProfissionalDeSaudeRepository profissionalDeSaudeRepository) {
        this.usuarioPCDRepository = usuarioPCDRepository;
        this.profissionalDeSaudeRepository = profissionalDeSaudeRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            
            Optional<UsuarioPCD> usuarioPCD = usuarioPCDRepository.findByEmail(username);
            if (usuarioPCD.isPresent()) {
                return usuarioPCD.get();
            }

            
            Optional<ProfissionalDeSaude> profissional = profissionalDeSaudeRepository.findByEmail(username);
            if (profissional.isPresent()) {
                return profissional.get();
            }

            
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}