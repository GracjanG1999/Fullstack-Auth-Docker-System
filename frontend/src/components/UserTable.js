import React, { useState } from 'react';
import * as api from '../api/authApi';

function UserTable({ users, currentUser, onUsersChange, onMessage }) {
  const [editId, setEditId] = useState(null);
  const [editFormData, setEditFormData] = useState({ username: '', email: '', password: '' });
  const isAdmin = currentUser?.role === 'ROLE_ADMIN';

  const handleDelete = async (id) => {
    if (!window.confirm('Czy na pewno usunąć tego użytkownika?')) return;
    try {
      await api.deleteUser(id);
      onUsersChange(prev => prev.filter(u => u.id !== id));
    } catch {
      onMessage('Nie udało się usunąć użytkownika.');
    }
  };

  const startEdit = (user) => {
    setEditId(user.id);
    setEditFormData({ username: user.username, email: user.email, password: '' });
  };

  const saveEdit = async (id) => {
    try {
      const res = await api.updateUser(id, { ...editFormData, password: editFormData.password || null });
      onUsersChange(prev => prev.map(u => (u.id === id ? res.data : u)));
      setEditId(null);
      onMessage('Zmiany zapisane pomyślnie!');
    } catch {
      onMessage('Nie udało się zapisać zmian.');
    }
  };

  return (
    <div className="table-wrapper">
      <table className="styled-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Login</th>
            <th>Email</th>
            {isAdmin && <><th>Rola</th><th>Akcje</th></>}
          </tr>
        </thead>
        <tbody>
          {users.map(u => (
            <tr key={u.id}>
              <td>{u.id}</td>
              <td>
                {isAdmin && editId === u.id
                  ? <input className="table-input" value={editFormData.username}
                      onChange={e => setEditFormData({ ...editFormData, username: e.target.value })} />
                  : u.username}
              </td>
              <td>
                {isAdmin && editId === u.id
                  ? <input className="table-input" value={editFormData.email}
                      onChange={e => setEditFormData({ ...editFormData, email: e.target.value })} />
                  : u.email}
              </td>
              {isAdmin && (
                <>
                  <td><span className={`role-badge ${u.role}`}>{u.role}</span></td>
                  <td className="actions-cell">
                    {editId === u.id ? (
                      <>
                        <button onClick={() => saveEdit(u.id)} className="btn btn-save">Zapisz</button>
                        <button onClick={() => setEditId(null)} className="btn btn-cancel">Anuluj</button>
                      </>
                    ) : (
                      <>
                        <button onClick={() => startEdit(u)} className="btn btn-edit">Edytuj</button>
                        <button onClick={() => handleDelete(u.id)} className="btn btn-delete">Usuń</button>
                      </>
                    )}
                  </td>
                </>
              )}
            </tr>
          ))}
        </tbody>
      </table>
      {!isAdmin && (
        <p className="text-muted" style={{ textAlign: 'center', marginTop: '15px' }}>
          Jako użytkownik masz uprawnienia tylko do odczytu podstawowych danych.
        </p>
      )}
    </div>
  );
}

export default UserTable;
