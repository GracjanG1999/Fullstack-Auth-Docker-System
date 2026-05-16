package com.example.projekt_logowanie.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.projekt_logowanie.repository.UserRepository;

// Ten plik to nasz "sprawdzacz". Ma tylko jedno zadanie: potwierdzić tożsamość.
public class AuthService {

    // To daje nam dostęp do listy użytkowników w bazie danych
    @Autowired
    private UserRepository userRepository;

    // Główna funkcja: sprawdza, czy login i hasło się zgadzają
    public boolean authenticate(String username, String password) {
        
        // MOJE MYŚLENIE: Szukam kogoś o takim imieniu w naszej bazie.
        return userRepository.findByUsername(username)
            // Jak go znajdę, to patrzę, czy jego hasło z bazy jest identyczne z tym wpisanym.
            .map(user -> user.getPassword().equals(password)) 
            
            // Jeśli nie ma takiego imienia albo hasło się nie zgadza, zwracam: "Nieprawda".
            .orElse(false);
    }
}