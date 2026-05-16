package com.example.projekt_logowanie.config;

import com.example.projekt_logowanie.model.Role;
import com.example.projekt_logowanie.model.User;
import com.example.projekt_logowanie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(buildUser("admin", "admin@admin.pl", "admin", Role.ADMIN));
            userRepository.save(buildUser("user",  "user@user.pl",   "user",  Role.USER));
            System.out.println("Domyślne konta zostały załadowane");
        }
    }

    private User buildUser(String username, String email, String password, String role) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        return user;
    }
}
