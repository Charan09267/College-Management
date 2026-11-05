package com.CollegeHub.admin;

import com.CollegeHub.admin.entity.Role;
import com.CollegeHub.admin.entity.Users;
import com.CollegeHub.admin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        // Check whether the initial admin already exists
        if (userRepository.findByEmail(adminEmail).isPresent()) {
            System.out.println("Initial admin already exists.");
            return;
        }

        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException(
                    "ADMIN_PASSWORD must be configured to create the initial admin"
            );
        }

        Users admin = new Users();

        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);

        userRepository.save(admin);

        System.out.println("Initial admin created successfully.");
    }
}
