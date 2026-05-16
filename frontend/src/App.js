import React, { useState, useEffect } from 'react';
import './App.css';
import AuthForm from './components/AuthForm';
import UserTable from './components/UserTable';
import * as api from './api/authApi';

function App() {
  const [currentUser, setCurrentUser] = useState(null);
  const [users, setUsers] = useState([]);
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (currentUser) {
      api.fetchUsers().then(res => setUsers(res.data)).catch(() => {});
    }
  }, [currentUser]);

  if (!currentUser) {
    return <AuthForm onLoginSuccess={setCurrentUser} />;
  }

  return (
    <div className="admin-container">
      <header className="admin-header">
        <h1>Panel Użytkowników</h1>
        <div className="user-info">
          <span>Zalogowany: <strong>{currentUser.username}</strong></span>
          <button className="btn-logout" onClick={() => setCurrentUser(null)}>Wyloguj</button>
        </div>
      </header>
      {message && <div className="alert-info">{message}</div>}
      <UserTable
        users={users}
        currentUser={currentUser}
        onUsersChange={setUsers}
        onMessage={setMessage}
      />
    </div>
  );
}

export default App;
