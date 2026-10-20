package com.college.sms.config;

import com.college.sms.model.Admin;
import com.college.sms.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner createDefaultAdmin(AdminRepository admins, PasswordEncoder encoder) {
        return args -> {
            if (admins.findByUsername("admin").isEmpty()) {
                Admin admin = new Admin();
                admin.setUsername("admin");
                // Change this demo password after your first run.
                admin.setPasswordHash(encoder.encode("Admin@123"));
                admins.save(admin);
            }
        };
    }
}
