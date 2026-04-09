// API Service for Backend Communication
const API_BASE_URL = 'http://localhost:8081/api/twobingo';

export const twobingoApi = {
    // Get all
    async getAllItems() {
        const response = await fetch(`${API_BASE_URL}`);
        if (!response.ok) throw new Error('Failed to fetch items');
        return response.json();
    },

    // user login
    async login(username, password) {
        const response = await fetch(`${API_BASE_URL}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });
        if (!response.ok) throw new Error('Login failed');
        return response.json();
    },

    // user logout
    async logout(token) {
        const response = await fetch(`${API_BASE_URL}/users/logout`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            headers: { 'Authorization': `${token}` },
        });
        if (!response.ok) throw new Error('Logout failed');
        return response.json();
    },


    // user registration
    async register(username, password) {
        const response = await fetch(`${API_BASE_URL}/users/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });
        if (!response.ok) throw new Error('Registration failed');
        return response.json();
    },

    // token validation
    async validateToken(token) {
        // authenticate with token as auth header
        const response = await fetch(`${API_BASE_URL}/validate-token`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            headers: { 'Authorization': `${token}` }
        });
        if (!response.ok) throw new Error('Token validation failed');
        return response.json();
    }


};
