import { useState } from 'react';

interface RegisterFormProps {
  onRegister: (token: string) => void;
}

export default function RegisterForm({ onRegister }: RegisterFormProps) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const res = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });
      if (!res.ok) throw new Error('Registration failed');
      const data = await res.json();
      onRegister(data.token);
    } catch (err: any) {
      setError(err.message || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Register</h2>
      <div>
        <label>Username: <input value={username} onChange={e => setUsername(e.target.value)} required /></label>
      </div>
      <div>
        <label>Password: <input type="password" value={password} onChange={e => setPassword(e.target.value)} required /></label>
      </div>
      <button type="submit" disabled={loading}>{loading ? 'Registering...' : 'Register'}</button>
      {error && <div style={{color:'red'}}>{error}</div>}
    </form>
  );
}
