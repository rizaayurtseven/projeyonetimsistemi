import { render, screen } from '@testing-library/react';
import App from './App';

test('renders login link initially', () => {
  render(<App />);
  const linkElement = screen.getByText(/Giriş Yap/i);
  expect(linkElement).toBeInTheDocument();
});
