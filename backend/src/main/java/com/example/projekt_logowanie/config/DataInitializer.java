package com.example.projekt_logowanie.config;

import com.example.projekt_logowanie.model.User;
import com.example.projekt_logowanie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Ten napis mówi komputerowi: "Włącz ten plik zaraz po starcie"
@Component
public class DataInitializer implements CommandLineRunner {

    // To łączy nas z szufladą, w której trzymamy listę ludzi (bazą danych)
    @Autowired
    private UserRepository userRepository;

    // To jest nasza maszynka do robienia tajnych haseł
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // MOJE MYŚLENIE: Sprawdzam, czy szuflada jest pusta. 
        // Jeśli tak, to wkładam tam pierwszych ludzi, żeby było na czym pracować.
        if (userRepository.count() == 0) {
            
            // Tworzę postać: Szef (Admin)
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@admin.pl");
            
            // Zamieniam zwykłe hasło "admin" na tajny kod, żeby nikt go nie podejrzał
            admin.setPassword(passwordEncoder.encode("admin"));
            
            // Daję mu plakietkę "ADMIN", żeby mógł wszystko zmieniać
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin); // Wrzucam go do bazy

            // Tworzę postać: Zwykły gość (User)
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@user.pl");
            user.setPassword(passwordEncoder.encode("user"));
            
            // On ma plakietkę "USER" - może tylko patrzeć, nie może psuć
            user.setRole("ROLE_USER");
            userRepository.save(user); // Też ląduje w bazie

            // Daję znać w czarnym okienku, że wszystko się udało
            System.out.println("Zadne zostały załadowane");
        }
    }
}