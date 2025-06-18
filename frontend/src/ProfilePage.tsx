import { useEffect, useState } from 'react';

interface ProfilePageProps {
  token: string;
  onLogout: () => void;
}

export default function ProfilePage({ token, onLogout }: ProfilePageProps) {
  const [status, setStatus] = useState<string>('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch('/api/auth/is-logged-in', {
      headers: { Authorization: `Bearer ${token}` },
      credentials: 'include', // Ensures cookies are sent if needed
    })
      .then(res => {
        if (res.ok) {
          setStatus('You are logged in!');
        } else {
          setStatus('Not authorized');
        }
      })
      .catch(() => setStatus('Not authorized'))
      .finally(() => setLoading(false));
  }, [token]);

  return (
    <div>
      <h2>Profile</h2>
      {loading ? <p>Loading...</p> : <p>{status}</p>}
      <button onClick={onLogout}>Logout</button>
    </div>
  );
}
