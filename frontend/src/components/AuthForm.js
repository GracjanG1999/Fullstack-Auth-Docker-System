import React, { useState } from 'react';
import * as api from '../api/authApi';

function AuthForm({ onLoginSuccess }) {
  const [isRegistering, setIsRegistering] = useState(false);
  const [formData, setFormData] = useState({ username: '', password: '', email: '', confirmPassword: '' });
  const [message, setMessage] = useState('');

  const handleChange = (e) => setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await api.login(formData.username, formData.password);
      onLoginSuccess(res.data);
    } catch {
      setMessage('Błąd logowania: Sprawdź dane.');
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      setMessage('Błąd: Hasła nie są identyczne!');
      return;
    }
    try {
      await api.register(formData);
      setMessage('Zarejestrowano pomyślnie! Możesz się zalogować.');
      setIsRegistering(false);
    } catch (err) {
      setMessage('Błąd rejestracji: ' + (err.response?.data || 'Serwer nie odpowiada'));
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2>{isRegistering ? 'Rejestracja' : 'Logowanie'}</h2>
        <form onSubmit={isRegistering ? handleRegister : handleLogin}>
          <input type="text" name="username" placeholder="Login" className="simple-input"
            value={formData.username} onChange={handleChange} required />
          {isRegistering && (
            <input type="email" name="email" placeholder="Email" className="simple-input"
              value={formData.email} onChange={handleChange} required />
          )}
          <input type="password" name="password" placeholder="Hasło" className="simple-input"
            value={formData.password} onChange={handleChange} required />
          {isRegistering && (
            <input type="password" name="confirmPassword" placeholder="Powtórz hasło" className="simple-input"
              value={formData.confirmPassword} onChange={handleChange} required />
          )}
          <button type="submit" className="simple-btn">
            {isRegistering ? 'Zarejestruj się' : 'Zaloguj się'}
          </button>
        </form>
        {message && (
          <p className={`simple-message ${message.includes('Błąd') ? 'text-error' : ''}`}>{message}</p>
        )}
        <button onClick={() => { setIsRegistering(!isRegistering); setMessage(''); }} className="simple-link">
          {isRegistering ? 'Masz już konto? Zaloguj się' : 'Nie masz konta? Zarejestruj się'}
        </button>
      </div>
    </div>
  );
}

export default AuthForm;
