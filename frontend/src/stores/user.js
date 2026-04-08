import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { twobingoApi } from '../services/api';

export const useUserStore = defineStore('user', () => {
    // State
    const currentUser = ref({});
    const loading = ref(false);
    const error = ref(null);


    // Actions
    const isUserLoggedIn = async () => {
        loading.value = true;
        error.value = null;

        try {
            //to do
        } catch (err) {
            error.value = err.message;
            console.error('Error getting connection', err);
        } finally {
            loading.value = false;
        }
    };





    return {
        // State
        currentUser,
        loading,
        error,

        // Computed


        // Actions
        isUserLoggedIn

    };
});
