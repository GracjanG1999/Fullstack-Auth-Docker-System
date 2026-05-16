import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

// Mówimy przeglądarce, żeby do każdego pytania do serwera dołączała nasze "poświadczenia" (np. ciasteczka sesji)
axios.defaults.withCredentials = true;
// To jest adres naszej bazy/serwera (backendu)
const API_URL = "http://localhost:8080/api/auth";

function App() {
  // --- NASZA PAMIĘĆ (STANY) ---
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Czy jesteśmy w środku? (Tak/Nie)
  const [users, setUsers] = useState([]); // Tu trzymamy listę ludzi pobraną z bazy
  const [currentUser, setCurrentUser] = useState(null); // Tu pamiętamy, kto się zalogował
  const [formData, setFormData] = useState({ username: '', password: '', email: '', confirmPassword: '' }); // Dane wpisane w okienka
  const [isRegistering, setIsRegistering] = useState(false); // Czy pokazujemy ekran rejestracji?
  const [message, setMessage] = useState(''); // Tekst z informacją dla nas (np. "Błąd!")

  // Pamięć dla trybu edycji (gdy poprawiamy kogoś w tabeli)
  const [editId, setEditId] = useState(null);
  const [editFormData, setEditFormData] = useState({ username: '', email: '', password: '' });

  // MOJE MYŚLENIE: Ta funkcja idzie do backendu i prosi o listę wszystkich ludzi.
  // Wynikiem zastępuje starą listę (setUsers), żeby dane się nie dublowały na ekranie.
  const fetchUsers = async () => {
    try {
      const res = await axios.get(`${API_URL}/users`);
      setUsers(res.data); 
    } catch (err) {
      console.error("Błąd pobierania użytkowników", err);
    }
  };

  // Ten "szpieg" (useEffect) pilnuje: jeśli tylko się zalogujemy, od razu pobiera listę ludzi.
  useEffect(() => {
    if (isLoggedIn) fetchUsers();
  }, [isLoggedIn]);

  // Obsługa guzika "Zaloguj"
  const handleLogin = async (e) => {
    e.preventDefault(); // Powstrzymaj stronę przed odświeżeniem
    try {
      const res = await axios.post(`${API_URL}/login`, {
        username: formData.username,
        password: formData.password
      });
      setIsLoggedIn(true); // Hurra, udało się!
      setCurrentUser(res.data); // Zapamiętaj moją rolę i login
    } catch (err) {
      setMessage("Błąd logowania: Sprawdź dane.");
    }
  };

  // Obsługa guzika "Zarejestruj"
  const handleRegister = async (e) => {
    e.preventDefault();
    // MOJE MYŚLENIE: Sprawdzam, czy hasło i "powtórz hasło" są takie same.
    if (formData.password !== formData.confirmPassword) {
      setMessage("Błąd: Hasła nie są identyczne!");
      return;
    }
    try {
      await axios.post(`${API_URL}/register`, formData);
      setMessage("Zarejestrowano pomyślnie! Możesz się zalogować.");
      setIsRegistering(false); // Przełącz na logowanie
    } catch (err) {
      setMessage("Błąd rejestracji: " + (err.response?.data || "Serwer nie odpowiada"));
    }
  };

  // Usuwanie użytkownika z bazy
  const deleteUser = async (id) => {
    // Okienko z pytaniem "Czy na pewno?"
    if(window.confirm("Czy na pewno usunąć tego użytkownika?")) {
      try {
        await axios.delete(`${API_URL}/users/${id}`);
        fetchUsers(); // Po wszystkim pobierz nową listę bez tej osoby
      } catch (err) {
        setMessage("Nie udało się usunąć użytkownika.");
      }
    }
  };

  // Włączenie trybu poprawiania danych (Edycji)
  const startEdit = (user) => {
    setEditId(user.id);
    setEditFormData({ username: user.username, email: user.email, password: '' });
  };

  // Wyłączenie trybu edycji bez zapisywania
  const cancelEdit = () => {
    setEditId(null);
  };

  // Zapisywanie poprawionych danych
  const saveEdit = async (id) => {
    try {
      const dataToUpdate = {
        username: editFormData.username,
        email: editFormData.email,
        password: editFormData.password || null 
      };
      const res = await axios.put(`${API_URL}/users/${id}`, dataToUpdate);
      // Znajdź tę osobę na liście i podmień jej dane na te nowe z serwera
      setUsers(prevUsers => prevUsers.map(u => (u.id === id ? res.data : u)));
      setEditId(null); // Zamknij tryb edycji
      setMessage("Zmiany zapisane pomyślnie!");
    } catch (err) {
      setMessage("Nie udało się zapisać zmian.");
    }
  };

  // --- WIDOK 1: LOGOWANIE / REJESTRACJA ---
  if (!isLoggedIn) {
    return (
      <div className="auth-container">
        <div className="auth-card">
          <h2>{isRegistering ? "Rejestracja" : "Logowanie"}</h2>
          <form onSubmit={isRegistering ? handleRegister : handleLogin}>
            
            <input 
              type="text" 
              placeholder="Login" 
              className="simple-input"
              value={formData.username}
              onChange={e => setFormData({...formData, username: e.target.value})} 
              required
            />

            {isRegistering && (
              <input 
                type="email" 
                placeholder="Email" 
                className="simple-input"
                value={formData.email}
                onChange={e => setFormData({...formData, email: e.target.value})} 
                required
              />
            )}

            <input 
              type="password" 
              placeholder="Hasło" 
              className="simple-input"
              value={formData.password}
              onChange={e => setFormData({...formData, password: e.target.value})} 
              required
            />

            {isRegistering && (
              <input 
                type="password" 
                placeholder="Powtórz hasło" 
                className="simple-input"
                value={formData.confirmPassword}
                onChange={e => setFormData({...formData, confirmPassword: e.target.value})} 
                required
              />
            )}

            <button type="submit" className="simple-btn">
              {isRegistering ? "Zarejestruj się" : "Zaloguj się"}
            </button>
          </form>

          {/* Wyświetlanie komunikatów o błędach lub sukcesie */}
          {message && (
            <p className={`simple-message ${message.includes("Błąd") ? "text-error" : ""}`}>
              {message}
            </p>
          )}

          {/* Przycisk do przełączania między Logowaniem a Rejestracją */}
          <button onClick={() => { setIsRegistering(!isRegistering); setMessage(''); }} className="simple-link">
            {isRegistering ? "Masz już konto? Zaloguj się" : "Nie masz konta? Zarejestruj się"}
          </button>
        </div>
      </div>
    );
  }

  // --- WIDOK 2: PANEL Z TABELĄ (PO ZALOGOWANIU) ---
  return (
    <div className="admin-container">
      <header className="admin-header">
        <h1>Panel Użytkowników</h1>
        <div className="user-info">
          <span>Zalogowany: <strong>{currentUser?.username}</strong></span>
          <button className="btn-logout" onClick={() => setIsLoggedIn(false)}>Wyloguj</button>
        </div>
      </header>

      {message && <div className="alert-info">{message}</div>}

      <div className="table-wrapper">
        <table className="styled-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Login</th>
              <th>Email</th>
              {/* Te kolumny widzi tylko Szef (Admin) */}
              {currentUser?.role === 'ROLE_ADMIN' && (
                <>
                  <th>Rola</th>
                  <th>Akcje</th>
                </>
              )}
            </tr>
          </thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id}>
                <td>{u.id}</td>
                
                {/* Pole Login - albo zwykły tekst, albo okienko do pisania (jeśli edytujemy) */}
                <td>
                  {currentUser?.role === 'ROLE_ADMIN' && editId === u.id ? (
                    <input 
                      className="table-input"
                      value={editFormData.username} 
                      onChange={e => setEditFormData({...editFormData, username: e.target.value})} 
                    />
                  ) : u.username}
                </td>

                {/* Pole Email */}
                <td>
                  {currentUser?.role === 'ROLE_ADMIN' && editId === u.id ? (
                    <input 
                      className="table-input"
                      value={editFormData.email} 
                      onChange={e => setEditFormData({...editFormData, email: e.target.value})} 
                    />
                  ) : u.email}
                </td>

                {/* Sekcja dostępna tylko dla Admina */}
                {currentUser?.role === 'ROLE_ADMIN' && (
                  <>
                    <td>
                      <span className={`role-badge ${u.role}`}>{u.role}</span>
                    </td>
                    <td className="actions-cell">
                      {editId === u.id ? (
                        <>
                          <button onClick={() => saveEdit(u.id)} className="btn btn-save">Zapisz</button>
                          <button onClick={cancelEdit} className="btn btn-cancel">Anuluj</button>
                        </>
                      ) : (
                        <>
                          <button onClick={() => startEdit(u)} className="btn btn-edit">Edytuj</button>
                          <button onClick={() => deleteUser(u.id)} className="btn btn-delete">Usuń</button>
                        </>
                      )}
                    </td>
                  </>
                )}
              </tr>
            ))}
          </tbody>
        </table>
        
        {/* Podpowiedź dla zwykłego użytkownika (nie-admina) */}
        {currentUser?.role !== 'ROLE_ADMIN' && (
          <p className="text-muted" style={{textAlign: 'center', marginTop: '15px'}}>
            Jako użytkownik masz uprawnienia tylko do odczytu podstawowych danych.
          </p>
        )}
      </div>
    </div>
  );
}

export default App;