import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { twobingoApi } from '../services/api';

export const useUserStore = defineStore('user', () => {
    // State
    const currentUser = ref({});
    const loading = ref(false);
    const error = ref(null);


    // Actions
    const getLoggedUser = async () => {
        loading.value = true;
        error.value = null;

        try {
            const token = localStorage.getItem('connectionToken');
            if (!token) {
                return null;
            }
            const response = await twobingoApi.validateToken(token);
            currentUser.value = response;
            return response;
        } catch (err) {
            error.value = err.message;
            console.error('Error getting connection', err);
        } finally {
            loading.value = false;
        }
    };

    // register user
    const register = async (username, password, repeatedPassword) => {
        loading.value = true;
        error.value = null;

        // ensure passwords match
        if (password !== repeatedPassword) {
            error.value = 'Passwords do not match';
            loading.value = false;
            return;
        }

        // encrypt password
        const encryptedPassword = btoa(password);

        // api call
        try {
            const user = await twobingoApi.register(
                username,
                encryptedPassword
            );
            currentUser.value = user;
            localStorage.setItem('connectionToken', user.connectionToken);
        } catch (err) {
            error.value = err.message;
            console.error('Error registering user', err);
        } finally {
            loading.value = false;
        }
    };

    // login user
    const login = async (username, password) => {
        loading.value = true;
        error.value = null;
        console.log('Attempting to log in user:', username);
        // encrypt password
        const encryptedPassword = btoa(password);
        try {
            const user = await twobingoApi.login(
                username,
                encryptedPassword
            );
            currentUser.value = user;
            localStorage.setItem('connectionToken', user.connectionToken);
            return user;
        } catch (err) {
            error.value = err.message;
        } finally {
            loading.value = false;
        }
    };

    // logout user
    const logout = async () => {
        loading.value = true;
        error.value = null;

        try {
            const token = localStorage.getItem('connectionToken');
            if (!token) {
                return false;
            }
            await twobingoApi.logout(token);

        } catch (err) {
            error.value = err.message;
        } finally {
            loading.value = false;
            currentUser.value = {};
            localStorage.removeItem('connectionToken');
        }

    };

    return {
        // State
        currentUser,
        loading,
        error,

        // Computed


        // Actions
        getLoggedUser,
        register,
        login,
        logout

    };
});
