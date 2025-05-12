import React, { useState } from 'react';
import axios from 'axios';
import '../pages/LoginForm.css';
import { useNavigate } from 'react-router-dom'; // ← Add this
import { Link } from 'react-router-dom';

const LoginForm = () => {
  const [credentials, setCredentials] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const navigate = useNavigate(); // ← Initialize the navigator

  const handleChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    try {
      const response = await axios.post('http://localhost:8080/api/users/login', credentials);
      const token = response.data.token;

      localStorage.setItem('token', token);
      setSuccess('Login successful!');
      navigate('/dashboard'); // ← Navigate after success
    } catch (err) {
      console.error(err);
      setError('Invalid credentials. Please try again.');
    }
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleLogin}>
        <h2>Login</h2>
        {success && <p className="success">{success}</p>}
        {error && <p className="error">{error}</p>}

        <input
          type="email"
          name="email"
          placeholder="Email"
          value={credentials.email}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={credentials.password}
          onChange={handleChange}
          required
        />
        <button type="submit">Log In</button>
        <p className="redirect">
  Don't have an account? <Link to="/register">Register</Link>
</p>

      </form>
    </div>
  );
};

export default LoginForm;
