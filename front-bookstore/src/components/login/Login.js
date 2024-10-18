import './Login.css';
import React, {useState} from 'react';
import { useNavigate } from 'react-router-dom';
import { useApi} from "../../services/Interceptors";

const Login = () => {
    const api = useApi();
    const [isLogin, setIsLogin] = useState(true);
    const [credentials, setCredentials] = useState({ username: '', password: '' });
    const navigate = useNavigate();
    const [errors, setErrors] = useState({});

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCredentials(prev => ({ ...prev, [name]: value }));
    };

    const validateForm = () => {
        const newErrors = {};
        if (!credentials.username) newErrors.username = "Username is required.";
        if (!credentials.password) newErrors.password = "Password is required.";
        return newErrors;
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
        setErrors({}); // Clear previous errors

        // Step 1: Perform client-side validation
        const formErrors = validateForm();
        if (Object.keys(formErrors).length > 0) {
            setErrors(formErrors);
            return; // Stop if there are validation errors
        }
        try {
            const uri = isLogin ? '/auth/login' : '/auth/register';
            const response = await api.post(uri, credentials);
            const token = response.data.accessToken;
            if (response.status === 200) {
                localStorage.setItem('token', token); // Store JWT in local storage
                navigate('/books');
            }
        } catch (error) {
            if (error.response && error.response.data) {
                setErrors(error.response.data); // Set validation errors from the back-end
            } else {
                setErrors({ general: "An unexpected error occurred. Please try again later." });
            }
        }
    };

    return (
        <div className="auth">
            <h2>{isLogin ? 'Login' : 'Register'}</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="username"
                    value={credentials.username}
                    onChange={handleChange}
                    placeholder="Username"
                    required
                />
                {errors.username && <p className="error">{errors.username}</p>}
                <input
                    type="password"
                    name="password"
                    value={credentials.password}
                    onChange={handleChange}
                    placeholder="Password"
                    required
                />
                {errors.password && <p className="error">{errors.password}</p>}

                {errors.general && <p className="error">{errors.general}</p>}
                <button type="submit">{isLogin ? 'Login' : 'Register'}</button>
                <button type="button" onClick={() => setIsLogin(!isLogin)}>
                    Switch to {isLogin ? 'Register' : 'Login'}
                </button>
            </form>
        </div>
    );
};

export default Login;
