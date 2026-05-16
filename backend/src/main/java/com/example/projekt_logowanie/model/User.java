package com.example.projekt_logowanie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// Ten napis mówi: "To nie jest zwykły plik, to jest projekt tabeli w naszej bazie danych"
@Entity
@Table(name = "users") // Tak będzie się nazywać nasza tabela w PostgreSQL
@Data // Magiczny skrót, który sam robi za nas nudne rzeczy (gettery i settery)
public class User {

    // To jest unikalny numer każdego człowieka, jak PESEL. 
    // Komputer sam go nadaje (1, 2, 3...), więc my nie musimy o tym pamiętać.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Login musi być jedyny na świecie (unique) i nie może być pusty.
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Login jest wymagany")
    private String username;

    // Tutaj pilnujemy, żeby nikt nie wpisał bzdur zamiast prawdziwego maila z małpką @.
    @Column(nullable = false)
    @Email(message = "Błędny format email")
    private String email;

    // MOJE MYŚLENIE: Hasło musi być długie i trudne (min. 8 znaków), żeby nikt go nie zgadł. 
    // Bezpieczeństwo przede wszystkim!
    @Column(nullable = false)
    @Size(min = 8, message = "Hasło musi mieć min. 8 znaków")
    private String password;

    // Każdy nowy człowiek dostaje na start plakietkę "USER". 
    // Dopiero admin może mu ją zmienić.
    private String role = "ROLE_USER";
}