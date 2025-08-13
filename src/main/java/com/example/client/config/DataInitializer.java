package com.example.client.config;

import com.example.client.entity.Role;
import com.example.client.entity.User;
import com.example.client.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(
                "admin",
                passwordEncoder.encode("admin123"),
                "admin@example.com",
                Set.of(Role.ADMIN, Role.USER)
            );
            userRepository.save(admin);
            System.out.println("Default admin user created: admin/admin123");
        }
        
        // Create default regular user if not exists
        if (!userRepository.existsByUsername("user")) {
            User user = new User(
                "user",
                passwordEncoder.encode("user123"),
                "user@example.com",
                Set.of(Role.USER)
            );
            userRepository.save(user);
            System.out.println("Default user created: user/user123");
        }
        
        // Create default moderator user if not exists
        if (!userRepository.existsByUsername("moderator")) {
            User moderator = new User(
                "moderator",
                passwordEncoder.encode("mod123"),
                "moderator@example.com",
                Set.of(Role.MODERATOR, Role.USER)
            );
            userRepository.save(moderator);
            System.out.println("Default moderator user created: moderator/mod123");
        }
    }
}
