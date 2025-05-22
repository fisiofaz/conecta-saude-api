package com.conecta_saude.conecta_saude_api;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration; 

    @SpringBootApplication
    public class ConectaSaudeApiApplication {

        public static void main(String[] args) {
            SpringApplication.run(ConectaSaudeApiApplication.class, args);
        }

}