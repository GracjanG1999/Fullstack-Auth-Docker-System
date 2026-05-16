# Fullstack Auth Docker System

System zarządzania użytkownikami z podziałem na role (Admin/User), zbudowany w architekturze fullstack z obsługą Docker.

---

## Funkcjonalności

- **Logowanie i rejestracja** — autentykacja z walidacją danych
- **Panel Admina** — pełne operacje CRUD (tworzenie, odczyt, edycja, usuwanie użytkowników)
- **Panel Użytkownika** — podgląd listy użytkowników bez możliwości edycji
- **Role** — `ROLE_ADMIN` / `ROLE_USER` z automatycznym przypisaniem przy rejestracji
- **Auto-inicjalizacja** — domyślne konta ładowane przy pierwszym uruchomieniu

---

## Technologie

| Warstwa     | Technologia                                      |
|-------------|--------------------------------------------------|
| Frontend    | React 18, Axios, CSS3                            |
| Backend     | Java 21, Spring Boot 3, Spring Security, Lombok  |
| Baza danych | PostgreSQL 15 (Docker)                           |
| Kontener    | Docker, Docker Compose                           |

---

## Architektura (SOLID / DRY / KISS)

```
backend/
├── controller/   AuthController          # HTTP — przyjmuje i zwraca dane
├── service/      UserService             # logika biznesowa
│                 UserDetailsServiceImpl  # Spring Security (oddzielona od biznesu)
├── dto/          LoginRequest            # DTO dla endpointu logowania
├── model/        User, Role              # encja + stałe ról
├── repository/   UserRepository          # dostęp do bazy
└── config/       SecurityConfig          # CORS, filtry bezpieczeństwa
                  PasswordEncoderConfig   # BCrypt bean
                  DataInitializer         # dane startowe

frontend/
├── api/          authApi.js              # wszystkie wywołania HTTP w jednym miejscu
├── components/   AuthForm.js             # ekran logowania / rejestracji
│                 UserTable.js            # panel z tabelą użytkowników
└── App.js                                # główny komponent — orkiestracja
```

---

## Uruchomienie

### Wymagania

- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- Java 21
- Node.js + npm

### 1. Baza danych (Docker)

```bash
docker-compose up -d
```

Uruchamia PostgreSQL na porcie `5432` oraz Adminera (podgląd bazy) na `http://localhost:8081`.

### 2. Backend

```bash
cd backend

# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

---

## Adresy

| Usługa        | Adres                    |
|---------------|--------------------------|
| Aplikacja     | http://localhost:3000    |
| API Backend   | http://localhost:8080    |
| Adminer (DB)  | http://localhost:8081    |

---

## Dane startowe

Przy pierwszym uruchomieniu aplikacja automatycznie tworzy dwa konta:

| Login   | Hasło   | Rola         |
|---------|---------|--------------|
| `admin` | `admin` | `ROLE_ADMIN` |
| `user`  | `user`  | `ROLE_USER`  |

---

## Zmienne środowiskowe (opcjonalne)

Aplikacja działa z domyślnymi wartościami bez konfiguracji. Można je nadpisać zmiennymi środowiskowymi:

| Zmienna          | Domyślna wartość          | Opis                    |
|------------------|---------------------------|-------------------------|
| `DB_HOST`        | `localhost`               | Host bazy PostgreSQL    |
| `DB_NAME`        | `cyber_db`                | Nazwa bazy danych       |
| `DB_USER`        | `admin`                   | Użytkownik bazy         |
| `DB_PASSWORD`    | `admindb`                 | Hasło do bazy           |
| `CORS_ORIGIN`    | `http://localhost:3000`   | Dozwolone źródło CORS   |
