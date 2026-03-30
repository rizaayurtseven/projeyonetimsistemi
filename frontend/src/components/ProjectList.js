import React, { useEffect, useState, useContext } from 'react';
import { Link } from 'react-router-dom';
import api from '../axiosConfig';
import { ThemeContext } from '../ThemeContext';

const statusLabels = {
  'YENI': 'Yeni',
  'DEVAM_EDIYOR': 'Devam Ediyor',
  'TAMAMLANDI': 'Tamamlandı'
};

const statusColors = {
  'YENI': '#3b82f6',
  'DEVAM_EDIYOR': '#f59e0b',
  'TAMAMLANDI': '#10b981'
};

const ProjectList = () => {
  const [projects, setProjects] = useState([]);
  const [error, setError] = useState('');
  const token = localStorage.getItem('token');
  const { theme } = useContext(ThemeContext);

  // Kullanıcı bilgisi ve admin kontrolü
  let username = '';
  let isAdmin = false;
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      username = payload.sub || payload.username || '';
      isAdmin = (payload.roles && (payload.roles.includes('ROLE_ADMIN') || payload.roles.includes('ADMIN')))
        || (payload.authorities && (payload.authorities.includes('ROLE_ADMIN') || payload.authorities.includes('ADMIN')))
        || (payload.role && (payload.role === 'ADMIN' || payload.role === 'ROLE_ADMIN'));
    } catch {}
  }

  useEffect(() => {
    if (!token) {
      setError('Lütfen giriş yapın.');
      setProjects([]);
      return;
    }
    setError('');
    api.get('/api/projects')
      .then(res => setProjects(res.data))
      .catch(err => setError('Projeler alınamadı!'));
  }, [token]);

  if (!token) return <div style={{marginTop:40, textAlign:'center'}}>Lütfen giriş yapın.</div>;
  if (error) return <div style={{marginTop:40, color:'red', textAlign:'center'}}>{error}</div>;

  const handleDelete = (projectId) => {
    if (window.confirm('Bu projeyi silmek istediğinize emin misiniz?')) {
      api.delete(`/api/projects/${projectId}`)
        .then(() => {
          setProjects(projects.filter(p => p.id !== projectId));
          alert('Proje başarıyla silindi.');
        })
        .catch(err => {
          setError('Proje silinemedi!');
          console.error(err);
        });
    }
  };

  return (
    <div>
      <h2>Projeler</h2>
      {projects.length === 0 && <p style={{textAlign:'center', color:'var(--text)'}}>Henüz proje bulunmuyor.</p>}
      <ul style={{listStyle:'none', padding:0}}>
        {projects.map(p => (
          <li key={p.id} style={{marginBottom:20, background:'var(--card)', borderRadius:8, padding:16, boxShadow:'0 2px 8px 0 rgba(59,130,246,0.08)'}}>
            <div style={{display:'flex', alignItems:'center', justifyContent:'space-between'}}>
              <div style={{display:'flex', alignItems:'center', gap:12}}>
                <span
                  style={{
                    color: p.files && p.files.length > 0 ? 'var(--primary)' : 'var(--text)',
                    textDecoration: p.files && p.files.length > 0 ? 'underline' : 'none',
                    cursor: p.files && p.files.length > 0 ? 'pointer' : 'default',
                    fontWeight: 600,
                    fontSize: '1.1rem'
                  }}
                  onClick={() => {
                    if (p.files && p.files.length > 0) {
                      const fileUrl = `http://localhost:9000/${p.files[0].filePath.replace('\\','/')}`;
                      window.open(fileUrl, '_blank');
                    }
                  }}
                >
                  {p.name}
                </span>
                {p.status && (
                  <span style={{
                    fontSize:'0.75rem', fontWeight:600, padding:'3px 10px',
                    borderRadius:12, color:'#fff',
                    background: statusColors[p.status] || '#6b7280'
                  }}>
                    {statusLabels[p.status] || p.status}
                  </span>
                )}
              </div>
              <div style={{display:'flex', gap:6}}>
                <Link to={`/edit/${p.id}`}>
                  <button style={{background:'#6366f1', color:'#fff'}}>Düzenle</button>
                </Link>
                {(isAdmin || (p.owner && p.owner.username === username)) && (
                  <button onClick={() => handleDelete(p.id)} style={{ background: '#ef4444', color: '#fff' }}>Sil</button>
                )}
              </div>
            </div>
            <div style={{marginTop:6, color:'var(--text)'}}>{p.description}</div>
            {p.files && p.files.length > 0 && (
              <div style={{marginTop:8, fontSize:'0.85rem', color:'var(--primary)'}}>
                📎 {p.files.length} dosya ekli
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ProjectList; 