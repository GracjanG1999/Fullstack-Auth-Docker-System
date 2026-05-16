package com.example.projekt_logowanie.controller;

import com.example.projekt_logowanie.model.User;
import com.example.projekt_logowanie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
// Dodajemy jawnie metody, aby przeglądarka ich nie blokowała
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class AuthController {

    private final UserService userService;

    // --- REJESTRACJA ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if(userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Użytkownik już istnieje!");
        }
        return ResponseEntity.ok(userService.registerUser(user));
    }

    // --- LOGOWANIE ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginData) {
        Optional<User> userOpt = userService.findByUsername(loginData.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (userService.checkPassword(loginData.getPassword(), user.getPassword())) {
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(401).body("Błędny login lub hasło");
    }

    // --- POBIERANIE LISTY ---
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // --- USUWANIE ---
    // Usunęliśmy @PreAuthorize, aby sprawdzić czy to ono blokowało dostęp
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("Użytkownik usunięty");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas usuwania: " + e.getMessage());
        }
    }

    // --- ZMIANA DANYCH ---
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas edycji: " + e.getMessage());
        }
    }
}