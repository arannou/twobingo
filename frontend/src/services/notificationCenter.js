import { io } from 'socket.io-client';
import { ref, computed } from 'vue';

export class NotificationCenter {
  constructor(url = 'http://localhost:9092') {
    this.socket = null;
    this.url = url;
    this.isConnected = ref(false);
    this.userId = null;
    this.listeners = new Map();
  }

  /**
   * Initialize and connect to the Socket.io server
   */
  connect() {
    if (this.socket) {
      return Promise.resolve();
    }

    return new Promise((resolve, reject) => {
      try {
        this.socket = io(this.url, {
          reconnection: true,
          reconnectionDelay: 1000,
          reconnectionDelayMax: 5000,
          reconnectionAttempts: 5,
        });

        this.socket.on('connect', () => {
          console.log('NotificationCenter connected:', this.socket.id);
          this.isConnected.value = true;
          resolve();
        });

        this.socket.on('disconnect', () => {
          console.log('NotificationCenter disconnected');
          this.isConnected.value = false;
        });

        this.socket.on('connected', (message) => {
          console.log('Server message:', message);
        });

        this.socket.on('registered', (userId) => {
          console.log('User registered:', userId);
          this.userId = userId;
        });

        this.socket.on('error', (error) => {
          console.error('Socket.io error:', error);
          reject(error);
        });
      } catch (error) {
        console.error('Failed to connect:', error);
        reject(error);
      }
    });
  }


  /**
   * Listen to a notification event
   */
  on(event, callback) {
    if (this.socket) {
      if (!this.listeners.has(event)) {
        this.listeners.set(event, []);
      }
      this.listeners.get(event).push(callback);
      this.socket.on(event, callback);
    }
  }

  /**
   * Listen to an event only once
   */
  once(event, callback) {
    if (this.socket) {
      this.socket.once(event, callback);
    }
  }

  /**
   * Remove listener from an event
   */
  off(event, callback) {
    if (this.socket) {
      this.socket.off(event, callback);
      if (this.listeners.has(event)) {
        const callbacks = this.listeners.get(event);
        const index = callbacks.indexOf(callback);
        if (index > -1) {
          callbacks.splice(index, 1);
        }
      }
    }
  }


  /**
   * Disconnect from the server
   */
  disconnect() {
    if (this.socket) {
      this.socket.disconnect();
      this.isConnected.value = false;
    }
  }

  /**
   * Get connection status
   */
  getConnectionStatus() {
    return this.isConnected;
  }

  /**
   * Check if connected
   */
  isSocketConnected() {
    return this.isConnected.value;
  }

  /**
   * Get current user ID
   */
  getCurrentUserId() {
    return this.userId;
  }
}

// Create a singleton instance
let instance = null;

export function useNotificationCenter(url = 'http://localhost:9092') {
  if (!instance) {
    instance = new NotificationCenter(url);
  }
  return instance;
}
