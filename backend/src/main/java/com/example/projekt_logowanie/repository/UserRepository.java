package com.example.projekt_logowanie.repository;

import com.example.projekt_logowanie.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// To jest nasz "łącznik" z bazą danych. 
// Rozciągamy go o JpaRepository, żeby mieć gotowe przyciski typu: "Zapisz", "Usuń", "Znajdź".
public interface UserRepository extends JpaRepository<User, Long> {

    // MOJE MYŚLENIE: Standardowe narzędzia pozwalają szukać tylko po numerku ID.
    // Ale my potrzebujemy szukać ludzi po ich  Loginie, żeby móc ich zalogować!
    // Dlatego dopisałem tę jedną magiczną linijkę.
    
    // Optional oznacza: "Szukaj użytkownika o tym imieniu. Jeśli go nie ma, nie psuj programu, 
    // tylko daj mi znać, że pudełko jest puste".
    Optional<User> findByUsername(String username);
}