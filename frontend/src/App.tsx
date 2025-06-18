import { useState } from 'react'
import LoginForm from './LoginForm'
import ProfilePage from './ProfilePage'
import RegisterForm from './RegisterForm'
import './App.css'

function App() {
  const [token, setToken] = useState(() => localStorage.getItem('jwt') || '');
  const [showRegister, setShowRegister] = useState(false);

  const handleLogin = (jwt: string) => {
    setToken(jwt);
    localStorage.setItem('jwt', jwt);
  };
  const handleLogout = () => {
    setToken('');
    localStorage.removeItem('jwt');
  };
  const handleRegister = (jwt: string) => {
    setToken(jwt);
    localStorage.setItem('jwt', jwt);
    setShowRegister(false);
  };

  if (token) {
    return <ProfilePage token={token} onLogout={handleLogout} />;
  }
  if (showRegister) {
    return <>
      <RegisterForm onRegister={handleRegister} />
      <button onClick={() => setShowRegister(false)}>Already have an account? Login</button>
    </>;
  }
  return <>
    <LoginForm onLogin={handleLogin} />
    <button onClick={() => setShowRegister(true)}>Need an account? Register</button>
  </>;
}

export default App
