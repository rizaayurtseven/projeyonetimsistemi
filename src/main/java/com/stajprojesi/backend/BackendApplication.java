package com.stajprojesi.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.stajprojesi.backend.model.Role;
import com.stajprojesi.backend.repository.RoleRepository;
import com.stajprojesi.backend.model.User;
import com.stajprojesi.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import java.util.Optional; // Bu import'un olduğundan emin olun
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Rolleri oluştur
            if (roleRepository.findByName("USER").isEmpty()) {
                Role userRole = new Role();
                userRole.setName("USER");
                roleRepository.save(userRole);
            }
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepository.save(adminRole);
            }

            // Varsayılan admin kullanıcı (yoksa oluştur)
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin1907"));
                admin.setEmail("admin@projeyonetim.com");
                Optional<Role> adminRole = roleRepository.findByName("ADMIN");
                adminRole.ifPresent(role -> admin.setRoles(Set.of(role)));
                userRepository.save(admin);
            }
        };
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:uploads/");
            }
        };
    }
}