import React, { useState } from 'react';
import axios from 'axios';
import '../pages/RegisterForm.css'; 
import { Link } from 'react-router-dom';


const RegisterForm = () => {
  const [formData, setFormData] = useState({ name: '', email: '', password: '' });
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setSuccess('');
    setError('');

    try {
      await axios.post('http://localhost:8080/api/users/register', formData);
      setSuccess('Registration successful! You can now log in.');
      setFormData({ name: '', email: '', password: '' });
    } catch (err) {
      console.error(err);
      setError('Registration failed. Try again with a different email.');
    }
  };

  return (
    <div className="register-container">
      
      {success && <p style={{ color: 'green' }}>{success}</p>}
      {error && <p style={{ color: 'red' }}>{error}</p>}
      <form  className="register-form" onSubmit={handleRegister}>
      <h2>Register</h2>
        <input type="text" name="name" placeholder="Name" value={formData.name} onChange={handleChange} required />
        <input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleChange} required />
        <input type="password" name="password" placeholder="Password" value={formData.password} onChange={handleChange} required />
        <button type="submit">Register</button>
        <p className="redirect">
  Already have an account? <Link to="/login">Login</Link>
</p>

      </form>
    </div>
  );
};

export default RegisterForm;
