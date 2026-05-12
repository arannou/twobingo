import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { useNotificationCenter } from '../services/notificationCenter.js';

export const useNotificationStore = defineStore('notification', () => {
    const connectionStatus = ref(false);
    const notificationCenter = useNotificationCenter();

    /**
     * Initialize notification center
     */
    const initializeNotificationCenter = async (userId) => {
        try {
            await notificationCenter.connect();
            connectionStatus.value = notificationCenter.isSocketConnected();

            // Listen to connection status changes
            notificationCenter.on('connect', () => {
                connectionStatus.value = true;
            });

            notificationCenter.on('disconnect', () => {
                connectionStatus.value = false;
            });

        } catch (error) {
            console.error('Failed to initialize notification center:', error);
        }
    };


    /**
     * Listen to a custom event
     */
    const onEvent = (event, callback) => {
        notificationCenter.on(event, callback);
    };


    /**
     * Disconnect notification center
     */
    const disconnect = () => {
        notificationCenter.disconnect();
        connectionStatus.value = false;
    };

    return {
        initializeNotificationCenter,
        onEvent,
        disconnect,
    };
});
