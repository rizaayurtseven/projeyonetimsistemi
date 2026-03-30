import React, { useState, useContext } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import ProjectList from './components/ProjectList';
import AddProject from './components/AddProject';
import EditProject from './components/EditProject';
import Login from './components/Login';
import AddUser from './components/AddUser';
import Dashboard from './components/Dashboard';
import EmployeeList from './components/EmployeeList';
import UserList from './components/UserList';
import { ThemeProvider, ThemeContext } from './ThemeContext';
import './App.css';

function ThemeToggle() {
  const { theme, setTheme } = useContext(ThemeContext);
  return (
    <button onClick={() => setTheme(theme === 'light' ? 'dark' : 'light')}
      style={{position:'fixed',top:10,right:10,zIndex:9999,background:'#fff',color:'#222',border:'1px solid #ccc',padding:'8px 16px',borderRadius:6}}>
      {theme === 'light' ? '🌙 Koyu Mod' : '☀️ Açık Mod'}
    </button>
  );
}

function AppContent() {
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));
  const token = localStorage.getItem('token');
  let isAdmin = false;
  if (token) {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      isAdmin = (payload.roles && (payload.roles.includes('ROLE_ADMIN') || payload.roles.includes('ADMIN')))
        || (payload.authorities && (payload.authorities.includes('ROLE_ADMIN') || payload.authorities.includes('ADMIN')))
        || (payload.role && (payload.role === 'ADMIN' || payload.role === 'ROLE_ADMIN'));
    } catch {}
  }

  const handleLogout = () => {
    localStorage.removeItem('token');
    setIsLoggedIn(false);
    window.location.href = '/';
  };

  const renderNav = () => (
    <nav style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
      {isLoggedIn && <Link to="/dashboard">Dashboard</Link>}
      {isLoggedIn && <Link to="/">Projeler</Link>}
      {isLoggedIn && <Link to="/add">Proje Ekle</Link>}
      {isLoggedIn && <Link to="/employees">Çalışanlar</Link>}
      {isLoggedIn && isAdmin && <Link to="/users">Kullanıcılar</Link>}
      {isLoggedIn && isAdmin && <Link to="/add-user">Kullanıcı Ekle</Link>}
      {!isLoggedIn && <Link to="/login">Giriş Yap</Link>}
      {isLoggedIn && <button onClick={handleLogout} style={{ marginLeft: 'auto', background: '#ef4444', color: 'white', padding: '6px 12px', border: 'none', borderRadius: '4px', cursor: 'pointer' }}>Çıkış Yap</button>}
    </nav>
  );

  return (
    <Router>
      <ThemeToggle />
      <header className="navbar">
        <div className="container">
          {renderNav()}
        </div>
      </header>
      <main className="container">
        <Routes>
          {!isLoggedIn && (
            <>
              <Route path="*" element={<Navigate to="/login" />} />
              <Route path="/login" element={<Login onLogin={() => setIsLoggedIn(true)} />} />
            </>
          )}
          {isLoggedIn && (
            <>
              <Route path="/" element={<ProjectList />} />
              <Route path="/dashboard" element={<Dashboard />} />
              <Route path="/add" element={<AddProject />} />
              <Route path="/edit/:id" element={<EditProject />} />
              <Route path="/employees" element={<EmployeeList />} />
              {isAdmin && <Route path="/users" element={<UserList />} />}
              {isAdmin && <Route path="/add-user" element={<AddUser />} />}
              <Route path="/login" element={<Navigate to="/" />} />
            </>
          )}
        </Routes>
      </main>
    </Router>
  );
}

function App() {
  return (
    <ThemeProvider>
      <AppContent />
    </ThemeProvider>
  );
}

export default App;
