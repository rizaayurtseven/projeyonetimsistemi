import React, { useEffect, useState } from 'react';
import api from '../axiosConfig';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/api/admin/users')
      .then(res => setUsers(res.data))
      .catch(err => setError('Kullanıcılar yüklenemedi. (Sadece admin erişebilir)'));
  }, []);

  return (
    <div>
      <h2>Kullanıcı Listesi (Admin)</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <ul style={{ listStyle: 'none', padding: 0 }}>
        {users.map(user => (
          <li key={user.id} style={{ 
            background: 'var(--card)', padding: '16px', borderRadius: '8px', 
            marginBottom: '10px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' 
          }}>
            <div>
              <strong style={{ fontSize: '1.1rem' }}>{user.username}</strong>
              <div style={{ fontSize: '0.85rem', color: 'gray' }}>{user.email}</div>
            </div>
            <div>
              {user.roles && user.roles.map(role => (
                <span key={role} style={{ 
                  background: role.includes('ADMIN') ? '#ef4444' : '#3b82f6', 
                  color: 'white', padding: '4px 8px', borderRadius: '12px', fontSize: '0.75rem', marginLeft: '5px' 
                }}>
                  {role.replace('ROLE_', '')}
                </span>
              ))}
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UserList;
