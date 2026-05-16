import axios from 'axios';

axios.defaults.withCredentials = true;

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api/auth';

export const login = (username, password) =>
  axios.post(`${API_URL}/login`, { username, password });

export const register = (formData) =>
  axios.post(`${API_URL}/register`, formData);

export const fetchUsers = () =>
  axios.get(`${API_URL}/users`);

export const deleteUser = (id) =>
  axios.delete(`${API_URL}/users/${id}`);

export const updateUser = (id, data) =>
  axios.put(`${API_URL}/users/${id}`, data);
