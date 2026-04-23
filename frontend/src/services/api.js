
// API Service for Backend Communication
const API_BASE_URL = 'http://localhost:8081/api/twobingo';


export const twobingoApi = {

    // heathcheck endpoint
    async healthCheck() {
        try {
            const response = await fetch(`${API_BASE_URL}/health`);
            if (response.ok) {
                console.info('API is online');
                sessionStorage.setItem("twobingo:online", true);
                return true;
            } else throw new Error('Health check failed');
        } catch (error) {
            sessionStorage.setItem("twobingo:online", false);
            console.error('API is offline:', error);
            return false;
        }

    },

    // user login
    async login(username, password) {
        const response = await fetch(`${API_BASE_URL}/users/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });


        if (!response.ok) {
            const reponseText = await response.text();
            if (!reponseText) {
                reponseText = 'Login failed';
            }
            throw new Error(reponseText);
        }
        return response.json();
    },

    // user logout
    async logout(token) {
        const response = await fetch(`${API_BASE_URL}/users/logout`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
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
        await this.healthCheck();
        // authenticate with token as auth header
        const response = await fetch(`${API_BASE_URL}/validate-token`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        if (!response.ok) throw new Error('Token validation failed');
        return response.json();
    },


    /* -------------- GRIDS --------------*/

    async getGridsForUser() {
        const response = await fetch(`${API_BASE_URL}/grids/user`, {
            headers: { 'Authorization': `Bearer ${localStorage.getItem('connectionToken')}` }
        });
        if (!response.ok) throw new Error('Failed to fetch grids');
        return response.json();
    },

    async getGridById(id) {
        const response = await fetch(`${API_BASE_URL}/grids/${id}`, {
            headers: { 'Authorization': `Bearer ${localStorage.getItem('connectionToken')}` }
        });
        if (!response.ok) throw new Error('Failed to fetch grid');
        return response.json();
    },

    async createGrid(gridData) {
        const token = localStorage.getItem('connectionToken');
        if (!token) {
            throw new Error('No authentication token found');
        }
        const response = await fetch(`${API_BASE_URL}/grids`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(gridData),
        });
        if (!response.ok) throw new Error('Failed to create grid');
        return response.json();
    },

    async updateGrid(id, gridData) {
        const token = localStorage.getItem('connectionToken');
        if (!token) {
            throw new Error('No authentication token found');
        }
        const response = await fetch(`${API_BASE_URL}/grids/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(gridData),
        });
        if (!response.ok) throw new Error('Failed to update grid');
        return response.json();
    },

    async deleteGrid(id) {
        const token = localStorage.getItem('connectionToken');
        if (!token) {
            throw new Error('No authentication token found');
        }

        const response = await fetch(`${API_BASE_URL}/grids/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` },
        });
        if (!response.ok) throw new Error('Failed to delete grid');
        return true;
    }


};
