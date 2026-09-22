package com.nexuscode.registro;

import com.nexuscode.registro.category.*;
import com.nexuscode.registro.user.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean CommandLineRunner seed(UserRepository users, IncidentCategoryRepository categories, PasswordEncoder encoder, @Value("${ADMIN_PASSWORD}") String adminPassword) {
        return args -> {
            var existingAdmin = users.findByEmail("admin@nexuscode.com");
            if (existingAdmin.isPresent()) {
                User admin = existingAdmin.get();
                admin.setEmail("admin@sitienergia.com");
                admin.setNombre("Administrador SitiEnergia");
                admin.setPasswordHash(encoder.encode(adminPassword));
                admin.setRol(Role.ADMIN);
                admin.setActivo(true);
                users.save(admin);
            } else if (!users.existsByEmail("admin@sitienergia.com")) {
                users.save(new User("Administrador SitiEnergia", "admin@sitienergia.com", encoder.encode(adminPassword), Role.ADMIN, "3000000000"));
            }
            if (categories.count() == 0) {
                categories.save(new IncidentCategory("Falla en red de baja tension", "Redes Electricas"));
                categories.save(new IncidentCategory("Poste averiado", "Obras Civiles"));
                categories.save(new IncidentCategory("Revision de medidor", "Comercial"));
            }
        };
    }
}