# NotificationCenter with Socket.io Documentation

## Overview

The NotificationCenter is a real-time notification system built with Socket.io that enables bidirectional communication between the frontend and backend applications.

## Backend Setup

### Installation

The Socket.io dependency has been added to `pom.xml`:
```xml
<dependency>
    <groupId>com.corundumstudio.socketio</groupId>
    <artifactId>socket.io-server</artifactId>
    <version>1.7.20</version>
</dependency>
```

### Configuration

Socket.io server runs on port **9092** (configured in `SocketIOConfig.java`).

Configuration properties in `application.properties`:
```properties
socketio.host=localhost
socketio.port=9092
socketio.cors.enabled=true
socketio.cors.allowed-origins=*
```

### Components

#### 1. NotificationCenter Service (`src/main/java/com/twobingo/service/NotificationCenter.java`)

Core service for managing notifications:

**Methods:**
- `notifyUser(userId, event, data)` - Send notification to specific user
- `broadcastNotification(event, data)` - Send notification to all connected clients
- `notifyUsers(userIds, event, data)` - Send notification to multiple users
- `registerUserSession(userId, sessionId)` - Register user session
- `removeUserSession(userId, sessionId)` - Remove user session
- `isUserConnected(userId)` - Check if user is connected
- `getConnectedUsersCount()` - Get total connected users count

**Usage Example:**
```java
@Autowired
private NotificationCenter notificationCenter;

public void sendGridUpdate() {
    notificationCenter.notifyUser("user123", "grid:updated", gridData);
}
```

#### 2. SocketIOConfig (`src/main/java/com/twobingo/config/SocketIOConfig.java`)

Configures and initializes the Socket.io server:
- Binds to port 9092
- Enables CORS for cross-origin connections
- Registers event handlers
- Initializes the NotificationCenter

#### 3. SocketIOEventHandler (`src/main/java/com/twobingo/config/SocketIOEventHandler.java`)

Handles Socket.io events:
- `onConnect` - Handles new client connections
- `onDisconnect` - Handles client disconnections
- `onRegister` - Associates user ID with socket session

### Backend Event Flow

1. Client connects to Socket.io server on port 9092
2. Client emits `register` event with userId
3. Backend registers the user session
4. Backend can now send notifications to specific users or broadcast to all

---

## Frontend Setup

### Installation

Socket.io client has been added to `package.json`:
```json
"socket.io-client": "^4.7.2"
```

Install dependencies:
```bash
cd frontend
npm install
```

### Components

#### 1. NotificationCenter Service (`src/services/notificationCenter.js`)

Main client service for Socket.io communication:

**Methods:**
- `connect()` - Connect to Socket.io server
- `registerUser(userId)` - Register current user
- `on(event, callback)` - Listen to events
- `once(event, callback)` - Listen to event once
- `off(event, callback)` - Remove event listener
- `emit(event, data)` - Emit event to server
- `disconnect()` - Disconnect from server
- `isSocketConnected()` - Check connection status

**Usage:**
```javascript
import { useNotificationCenter } from '@/services/notificationCenter';

const notificationCenter = useNotificationCenter();

// Connect to server
await notificationCenter.connect();

// Register user
notificationCenter.registerUser('user123');

// Listen to notifications
notificationCenter.on('grid:updated', (data) => {
  console.log('Grid updated:', data);
});

// Emit event
notificationCenter.emit('custom-event', { message: 'Hello' });
```

#### 2. Notifications Store (`src/stores/notifications.js`)

Pinia store for managing notification state:

**Actions:**
- `initializeNotificationCenter(userId)` - Initialize and connect
- `addNotification(notification)` - Add notification to store
- `removeNotification(id)` - Remove notification
- `clearNotifications()` - Clear all notifications
- `onEvent(event, callback)` - Listen to custom events
- `emitEvent(event, data)` - Emit custom events
- `disconnect()` - Disconnect notification center

**Properties:**
- `notifications` - Array of current notifications
- `connectionStatus` - Boolean connection status
- `recentNotifications` - Computed array of recent notifications (reversed order)
- `notificationCount` - Computed count of notifications

**Usage:**
```javascript
import { useNotificationStore } from '@/stores/notifications';

const notificationStore = useNotificationStore();

// Initialize on app load (in main.js or App.vue)
await notificationStore.initializeNotificationCenter('user123');

// Add notification
notificationStore.addNotification({
  title: 'Success',
  message: 'Operation completed',
  type: 'success'
});

// Listen to custom event
notificationStore.onEvent('grid:updated', (data) => {
  console.log('Grid updated:', data);
});
```

#### 3. NotificationDisplay Component (`src/components/NotificationDisplay.vue`)

Vue component for displaying notifications:

**Features:**
- Displays connection status (Connected/Disconnected)
- Shows list of recent notifications
- Auto-dismisses notifications after 5 seconds
- Supports notification types: success, error, warning, info
- Manual dismiss button
- Responsive design

**Usage:**
```vue
<!-- Add to your App.vue or layout -->
<template>
  <div id="app">
    <NotificationDisplay />
    <!-- other content -->
  </div>
</template>

<script setup>
import NotificationDisplay from '@/components/NotificationDisplay.vue';
</script>
```

---

## Integration Guide

### 1. Setup in App.vue

```javascript
import { useNotificationStore } from '@/stores/notifications';

export default {
  setup() {
    const notificationStore = useNotificationStore();

    onMounted(async () => {
      const userId = getCurrentUserId(); // Get from user store or auth service
      await notificationStore.initializeNotificationCenter(userId);
    });

    onUnmounted(() => {
      notificationStore.disconnect();
    });

    return { notificationStore };
  }
}
```

### 2. Send Notifications from Backend

```java
@Autowired
private NotificationCenter notificationCenter;

@PostMapping("/grid/{gridId}")
public ResponseEntity<GridDto> updateGrid(@PathVariable String gridId, @RequestBody GridDto gridDto) {
    // Update grid logic
    Grid grid = gridService.update(gridId, gridDto);
    
    // Notify all users about the update
    notificationCenter.broadcastNotification("grid:updated", new GridDto(grid));
    
    return ResponseEntity.ok(new GridDto(grid));
}
```

### 3. Listen to Notifications in Components

```vue
<script setup>
import { useNotificationStore } from '@/stores/notifications';
import { onMounted } from 'vue';

const notificationStore = useNotificationStore();

onMounted(() => {
  // Listen for grid updates
  notificationStore.onEvent('grid:updated', (gridData) => {
    console.log('Grid was updated:', gridData);
    // Update local state
  });

  // Listen for cell changes
  notificationStore.onEvent('cell:changed', (cellData) => {
    console.log('Cell changed:', cellData);
  });
});
</script>
```

---

## Common Notification Types

The system supports different notification types that automatically style the notification:

- **success** - Green border, for successful operations
- **error** - Red border, for errors
- **warning** - Orange border, for warnings
- **info** - Blue border, for information messages (default)

**Example:**
```javascript
notificationStore.addNotification({
  title: 'Grid Update',
  message: 'The grid has been updated by another user',
  type: 'info',
  autoClose: true // Auto-dismiss after 5 seconds
});
```

---

## Connection Properties

### Reconnection Configuration

The Socket.io client is configured with automatic reconnection:
- Reconnection delay: 1000ms
- Max reconnection delay: 5000ms
- Reconnection attempts: 5

These can be modified in `notificationCenter.js`:
```javascript
const socket = io(this.url, {
  reconnection: true,
  reconnectionDelay: 1000,
  reconnectionDelayMax: 5000,
  reconnectionAttempts: 5,
});
```

### CORS Configuration

The backend allows connections from all origins by default:
```java
config.setOrigin("*");
```

For production, restrict this to your frontend domain:
```java
config.setOrigin("https://yourdomain.com");
```

---

## Environment Variables

### Backend
Add to `application.properties`:
```properties
socketio.port=9092
socketio.host=localhost
```

### Frontend
Create `.env.local` for development:
```
VITE_SOCKETIO_URL=http://localhost:9092
```

Update `notificationCenter.js` to use the environment variable:
```javascript
export function useNotificationCenter(url = import.meta.env.VITE_SOCKETIO_URL || 'http://localhost:9092') {
  // ...
}
```

---

## Debugging

### Backend Logging

Logs are configured in `application.properties`:
```properties
logging.level.com.twobingo=DEBUG
```

### Frontend Console

Socket.io client logs connection details to browser console. Enable with:
```javascript
const socket = io(this.url, {
  // ... other config
  debug: true // Enable debug logs
});
```

---

## Best Practices

1. **Always initialize the notification center early in the app lifecycle** - In `App.vue` `onMounted` or `main.js`

2. **Clean up on unmount** - Call `disconnect()` when the app unmounts

3. **Use typed events** - Define event names as constants:
   ```javascript
   export const NOTIFICATION_EVENTS = {
     GRID_UPDATED: 'grid:updated',
     CELL_CHANGED: 'cell:changed',
     USER_JOINED: 'user:joined'
   };
   ```

4. **Handle disconnections gracefully** - The UI shows connection status automatically

5. **Avoid sending large payloads** - Socket.io is optimized for real-time events, keep data lightweight

6. **Consider security** - Add authentication to Socket.io connections in production:
   ```java
   config.setAuthorizationListener(data -> {
     String token = data.get("token").toString();
     return isValidToken(token);
   });
   ```

---

## Troubleshooting

### Socket.io connection fails

**Problem:** Cannot connect to Socket.io server

**Solution:**
1. Check backend is running on port 9092
2. Verify CORS is enabled
3. Check firewall settings
4. Look at browser console for error messages

### Notifications not appearing

**Problem:** Events are sent but notifications don't show

**Solution:**
1. Ensure `NotificationDisplay.vue` is mounted in App.vue
2. Check that user is registered with `notificationStore.initializeNotificationCenter(userId)`
3. Verify event names match between backend and frontend
4. Check browser console for errors

### Frequent disconnections

**Problem:** Socket keeps disconnecting

**Solution:**
1. Increase reconnection attempts in `notificationCenter.js`
2. Check backend logs for errors
3. Verify network stability
4. Check if backend is handling connections properly

---

## Next Steps

1. Run `npm install` in the frontend folder
2. Ensure backend is compiled and running
3. Start the frontend dev server: `npm run dev`
4. Verify connection status badge shows "Connected"
5. Test by sending notifications from backend

For more details on Socket.io, visit: https://socket.io/docs/
