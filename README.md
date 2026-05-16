## System Zarzadzania Uzytkownikami (Fullstack)
Projekt aplikacji typu Fullstack umozliwiajacej zarzadzanie baza uzytkowników z podzialem na role (Admin/User).

### Funkcjonalnosci
Logowanie i Rejestracja: System autentykacji z walidacja danych.

Panel Admina: Pelne operacje CRUD (Tworzenie, Odczyt, Edycja, Usuwanie).
Panel Uzytkownika: Podglad listy uzytkowników (ograniczony zakres danych) bez mozliwosci edycji.
Auto-initialization: Automatyczne ladowanie danych startowych do bazy przy uruchomieniu.

### Technologie
Frontend: React.js, Axios, CSS3.

Backend: Java 21, Spring Boot 3, Spring Security, JPA/Hibernate.

Baza danych: PostgreSQL (Docker).

### Jak uruchomic?
Wymagania:
Zainstalowany Docker
Srodowisko Java 21.
Node.js oraz npm.

Dane startowe do bazy (Importowane automatycznie):
 - Admin: login: admin, haslo: admin
 - User: login: user, haslo: user

## Kroki instalacji:
1. Sklonuj repozytorium:
```git clone https://github.com/GracjanG1999/Fullstack-Auth-Docker-System.git```

2. Baza danych (Docker):
W glównym folderze projektu uruchom terminal i wpisz: ```docker-compose up -d```

3. Backend:
Przejdz do folderu ```/backend``` i uruchom projekt za pomoca Maven Wrapper:
```.\mvnw.cmd spring-boot:run (Windows)```
lub
```./mvnw spring-boot:run (Linux/Mac)```

4. Frontend:
Przejdz do folderu ```/frontend``` i wpisz kolejno:
```npm install```
```npm start```

Adresy:
Aplikacja dostepna pod adresem: http://localhost:3000
API Backendowe dostepne pod adresem: http://localhost:8080
