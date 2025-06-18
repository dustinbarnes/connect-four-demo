import '@testing-library/jest-dom';
import { render, screen, waitFor } from '@testing-library/react';
import ProfilePage from './ProfilePage';

describe('ProfilePage', () => {
  const token = 'test-token';
  const onLogout = jest.fn();

  it('shows loading then logged in on 200', async () => {
    global.fetch = jest.fn().mockResolvedValue({ ok: true }) as any;
    render(<ProfilePage token={token} onLogout={onLogout} />);
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
    await waitFor(() => expect(screen.getByText(/you are logged in/i)).toBeInTheDocument());
  });

  it('shows not authorized on 401', async () => {
    global.fetch = jest.fn().mockResolvedValue({ ok: false }) as any;
    render(<ProfilePage token={token} onLogout={onLogout} />);
    await waitFor(() => expect(screen.getByText(/not authorized/i)).toBeInTheDocument());
  });

  it('calls onLogout when logout button is clicked', async () => {
    global.fetch = jest.fn().mockResolvedValue({ ok: true }) as any;
    render(<ProfilePage token={token} onLogout={onLogout} />);
    await waitFor(() => expect(screen.getByText(/you are logged in/i)).toBeInTheDocument());
    screen.getByRole('button', { name: /logout/i }).click();
    expect(onLogout).toHaveBeenCalled();
  });
});
