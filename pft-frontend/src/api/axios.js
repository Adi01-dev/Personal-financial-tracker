import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8080", // Adjust if backend runs on different port
});

// Add token from localStorage to all requests
instance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default instance;
