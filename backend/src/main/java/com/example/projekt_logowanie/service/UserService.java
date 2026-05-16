package com.example.projekt_logowanie.service;

import com.example.projekt_logowanie.model.User;
import com.example.projekt_logowanie.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

// To jest nasz "Główny Pomocnik". On wie, jak rejestrować, zmieniać i sprawdzać ludzi.
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // MOJE MYŚLENIE: System bezpieczeństwa pyta: "Czy znasz kogoś o imieniu X?". 
    // Ta metoda idzie do bazy, wyciąga gościa i mówi systemowi, co ten gość może robić.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika: " + username));

        // Sprawdzamy, jaką gość ma plakietkę (np. ADMIN albo USER)
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole())
        );

        // Przekazujemy te dane do "strażnika" Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    // --- REJESTRACJA NOWEJ OSOBY ---
    public User registerUser(User user) {
        // Zanim włożymy gościa do bazy, mielimy jego hasło w maszynce, żeby było tajne
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Jeśli zapomnieliśmy nadać mu rolę, to automatycznie zostaje zwykłym "USEREM"
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }
        return userRepository.save(user); // Pstryk do bazy!
    }

    // Wyciągamy wszystkich ludzi z bazy naraz
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Szukamy kogoś po imieniu
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Usuwamy gościa z bazy po jego numerku ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // --- ZMIANA DANYCH ---
    public User updateUser(Long id, User userDetails) {
        // Szukamy gościa, którego chcemy poprawić
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika o id: " + id));
        
        // Wpisujemy nowe imię i nowy email
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        
        // Jeśli wpisaliśmy też nowe hasło, to je szyfrujemy i podmieniamy
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        return userRepository.save(user); // Zapisujemy poprawioną postać
    }

    // Specjalna funkcja do sprawdzania, czy hasło z klawiatury pasuje do tego zakodowanego w bazie
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}