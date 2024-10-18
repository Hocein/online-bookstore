import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import {useEffect} from "react";

// Create an Axios instance
const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

// Function to set up interceptors
export const setupInterceptors = (navigate) => {
    // Request interceptor
    api.interceptors.request.use(
        (config) => {
            const token = localStorage.getItem('token'); // Get the JWT token from localStorage
            if (token) {
                config.headers['Authorization'] = `Bearer ${token}`; // Set the Authorization header with Bearer token
            }
            return config;
        },
        (error) => {
            return Promise.reject(error);
        }
    );

// Response interceptor
    api.interceptors.response.use(
        (response) => {
            return response;
        },
        (error) => {
            if (error.response && error.response.status === 403) {
                // Token expired or invalid
                localStorage.removeItem('token'); // Remove the JWT token from localStorage
                navigate('/login'); // Redirect to login page
            }
            return Promise.reject(error);
        }
    );
};

// Custom hook to use the configured Axios instance
export const useApi = () => {
    const navigate = useNavigate();

    useEffect(() => {
        setupInterceptors(navigate); // Set up interceptors on component mount
    }, [navigate]);

    return api;
};