package com.example.projekt_logowanie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Ten napis mówi komputerowi: "Tu są ważne ustawienia dla naszej maszyny"
@Configuration
public class PasswordEncoderConfig {

    // To jest nasza "maszynka do mielenia haseł". 
    // Robimy ją raz, żeby cała aplikacja mogła z niej korzystać.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        
        // MOJE MYŚLENIE: Wybieram BCrypt, bo to najmocniejsza kłódka na rynku. 
        // Nawet jak ktoś ukradnie listę haseł z bazy, to nic z nich nie wyczyta, 
        // bo będą zamienione w totalny bełkot.
        return new BCryptPasswordEncoder();
    }
}