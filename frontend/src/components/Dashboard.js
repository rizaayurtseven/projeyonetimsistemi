import React, { useEffect, useState } from 'react';
import api from '../axiosConfig';

const Dashboard = () => {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    api.get('/api/dashboard/stats')
      .then(res => {
        setStats(res.data);
        setLoading(false);
      })
      .catch(err => {
        setError('İstatistikler yüklenemedi.');
        setLoading(false);
      });
  }, []);

  if (loading) return <div style={{textAlign:'center', marginTop:40}}>Yükleniyor...</div>;
  if (error) return <div style={{textAlign:'center', marginTop:40, color:'red'}}>{error}</div>;

  return (
    <div>
      <h2>Dashboard</h2>
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
        gap: '20px',
        marginTop: '20px'
      }}>
        <StatCard title="Toplam Proje" value={stats.totalProjects} color="#3b82f6" icon="📊" />
        <StatCard title="Çalışan Sayısı" value={stats.totalEmployees} color="#10b981" icon="👥" />
        <StatCard title="Kullanıcı Sayısı" value={stats.totalUsers} color="#8b5cf6" icon="👤" />
        <StatCard title="Yüklenen Dosya" value={stats.totalFiles} color="#f59e0b" icon="📎" />
      </div>
    </div>
  );
};

const StatCard = ({ title, value, color, icon }) => (
  <div style={{
    background: 'var(--card)',
    padding: '24px',
    borderRadius: '12px',
    boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)',
    display: 'flex',
    alignItems: 'center',
    gap: '16px'
  }}>
    <div style={{
      fontSize: '2rem',
      background: `${color}20`,
      color: color,
      width: '60px',
      height: '60px',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: '50%'
    }}>
      {icon}
    </div>
    <div>
      <div style={{ color: 'var(--text)', fontSize: '0.9rem', fontWeight: 600, opacity: 0.8 }}>{title}</div>
      <div style={{ fontSize: '1.8rem', fontWeight: 700, marginTop: '4px' }}>{value}</div>
    </div>
  </div>
);

export default Dashboard;
