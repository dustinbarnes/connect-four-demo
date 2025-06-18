import '@testing-library/jest-dom';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import RegisterForm from './RegisterForm';

describe('RegisterForm', () => {
  it('renders register form', () => {
    render(<RegisterForm onRegister={() => {}} />);
    expect(screen.getByRole('button', {name: /register/i})).toBeInTheDocument();
    expect(screen.getByLabelText(/username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
  });

  it('calls onRegister with token on successful registration', async () => {
    const mockOnRegister = jest.fn();
    global.fetch = jest.fn().mockResolvedValue({
      ok: true,
      json: async () => ({ token: 'test-token' }),
    }) as any;
    render(<RegisterForm onRegister={mockOnRegister} />);
    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'user' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'pass' } });
    fireEvent.click(screen.getByRole('button', { name: /register/i }));
    await waitFor(() => expect(mockOnRegister).toHaveBeenCalledWith('test-token'));
  });

  it('shows error on failed registration', async () => {
    global.fetch = jest.fn().mockResolvedValue({ ok: false }) as any;
    render(<RegisterForm onRegister={() => {}} />);
    fireEvent.change(screen.getByLabelText(/username/i), { target: { value: 'user' } });
    fireEvent.change(screen.getByLabelText(/password/i), { target: { value: 'pass' } });
    fireEvent.click(screen.getByRole('button', { name: /register/i }));
    await waitFor(() => expect(screen.getByText(/registration failed/i)).toBeInTheDocument());
  });
});
