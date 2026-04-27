package com.klu;

import com.klu.entity.User;
import com.klu.repository.UserRepository;
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
    public void run(String... args) {
        createIfNotExists("Admin", "admin@tourmate.com", "Admin@123", "admin", null);
        createIfNotExists("Demo Guide", "guide@test.com", "1234", "guide", null);
        createIfNotExists("Demo Host", "host@test.com", "1234", "host", null);
        createIfNotExists("Admin Portal", "admin@test.com", "admin", "admin", null);
    }

    private void createIfNotExists(String name, String email, String password, String role, String city) {
        if (userRepository.findByEmail(email).isPresent()) return;
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(password));
        u.setRole(role);
        u.setCity(city);
        u.setVerified(true);
        u.setApproved(true);
        userRepository.save(u);
        System.out.println("✅ Created user: " + email);
    }
}
