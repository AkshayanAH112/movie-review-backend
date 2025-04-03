package com.moviereview.api.config;

import com.moviereview.api.model.User;
import com.moviereview.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user already exists
        if (userRepository.findByEmail("admin@moviereview.com").isEmpty()) {
            // Create admin user
            User adminUser = new User();
            adminUser.setName("Admin User");
            adminUser.setEmail("admin@moviereview.com");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole("ADMIN");
            
            userRepository.save(adminUser);
            
            System.out.println("Admin user created successfully!");
            System.out.println("Email: admin@moviereview.com");
            System.out.println("Password: admin123");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
