import React, { useEffect, useState } from 'react';
import api from '../axiosConfig';

const EmployeeList = () => {
  const [employees, setEmployees] = useState([]);
  const [form, setForm] = useState({ firstName: '', lastName: '', email: '' });
  const [error, setError] = useState('');

  const loadEmployees = () => {
    api.get('/api/employees')
      .then(res => setEmployees(res.data))
      .catch(err => setError('Çalışanlar yüklenemedi.'));
  };

  useEffect(() => {
    loadEmployees();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/api/employees', form);
      setForm({ firstName: '', lastName: '', email: '' });
      loadEmployees();
    } catch (err) {
      setError('Çalışan eklenemedi.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Emin misiniz?')) {
      try {
        await api.delete(`/api/employees/${id}`);
        loadEmployees();
      } catch (err) {
        setError('Silinemedi.');
      }
    }
  };

  return (
    <div>
      <h2>Çalışan Yönetimi</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      
      <div style={{ background: 'var(--card)', padding: '20px', borderRadius: '8px', marginBottom: '24px' }}>
        <h3>Yeni Çalışan Ekle</h3>
        <form onSubmit={handleSubmit} style={{ display: 'flex', gap: '10px', flexWrap: 'wrap' }}>
          <input type="text" placeholder="Ad" value={form.firstName} onChange={e => setForm({...form, firstName: e.target.value})} required />
          <input type="text" placeholder="Soyad" value={form.lastName} onChange={e => setForm({...form, lastName: e.target.value})} required />
          <input type="email" placeholder="E-posta" value={form.email} onChange={e => setForm({...form, email: e.target.value})} />
          <button type="submit">Ekle</button>
        </form>
      </div>

      <ul style={{ listStyle: 'none', padding: 0 }}>
        {employees.map(emp => (
          <li key={emp.id} style={{ 
            background: 'var(--card)', padding: '16px', borderRadius: '8px', 
            marginBottom: '10px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' 
          }}>
            <div>
              <strong>{emp.firstName} {emp.lastName}</strong>
              <div style={{ fontSize: '0.85rem', color: 'gray' }}>{emp.email || 'E-posta yok'}</div>
            </div>
            <button onClick={() => handleDelete(emp.id)} style={{ background: '#ef4444', color: '#fff', padding: '6px 12px' }}>Sil</button>
          </li>
        ))}
      </ul>
      {employees.length === 0 && <p>Henüz çalışan yok.</p>}
    </div>
  );
};

export default EmployeeList;
