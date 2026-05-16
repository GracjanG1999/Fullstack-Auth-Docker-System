package com.example.projekt_logowanie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Ten napis to "magiczny przycisk START". 
// Mówi komputerowi: "To jest główny projekt, znajdź w nim wszystko i uruchom".
@SpringBootApplication
public class ProjektLogowanieApplication {

    // Ta mała funkcja to miejsce, w którym procesor zaczyna czytać nasz kod.
    public static void main(String[] args) {
        
        // MOJE MYŚLENIE: To jest komenda odpalenia całego Springa. 
        // W tym momencie budzi się baza danych, włączają się zabezpieczenia 
        // i serwer zaczyna nas słuchać na porcie 8080.
        SpringApplication.run(ProjektLogowanieApplication.class, args);
        
        // Kiedy to ruszy, w konsoli zobaczy Pan wielki napis "SPRING" zrobiony z liter.
    }

}