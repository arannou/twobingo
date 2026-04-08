// API Service for Backend Communication
const API_BASE_URL = 'http://localhost:8081/api/twobingo';

export const twobingoApi = {
    // Get all
    async getAllItems() {
        const response = await fetch(`${API_BASE_URL}`);
        if (!response.ok) throw new Error('Failed to fetch items');
        return response.json();
    },


};
