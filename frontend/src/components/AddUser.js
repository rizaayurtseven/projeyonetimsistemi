import React, { useState } from 'react';
import api from '../axiosConfig';

const AddUserPage = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage('');
    setError('');
    try {
      const response = await api.post('/api/admin/users', { username, email, password });
      setMessage(response.data);
      setUsername('');
      setEmail('');
      setPassword('');
    } catch (err) {
      console.error('Kullanıcı ekleme hatası:', err);
      if (err.response && err.response.data) {
        setError(err.response.data);
      } else {
        setError('Kullanıcı eklenirken bir hata oluştu.');
      }
    }
  };

  return (
    <div>
      <h2>Yeni Kullanıcı Ekle</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Kullanıcı Adı:</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div>
          <label>E-posta:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Şifre:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Kullanıcı Ekle</button>
      </form>
      {message && <p style={{ color: 'green' }}>{message}</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </div>
  );
};

export default AddUserPage;