package com.example.projekt_logowanie.controller;

import com.example.projekt_logowanie.dto.LoginRequest;
import com.example.projekt_logowanie.model.User;
import com.example.projekt_logowanie.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Użytkownik już istnieje!");
        }
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginData) {
        Optional<User> userOpt = userService.findByUsername(loginData.getUsername());
        if (userOpt.isPresent() && userService.checkPassword(loginData.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.ok(userOpt.get());
        }
        return ResponseEntity.status(401).body("Błędny login lub hasło");
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().body("Użytkownik usunięty");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas usuwania: " + e.getMessage());
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, userDetails));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Błąd podczas edycji: " + e.getMessage());
        }
    }
}
