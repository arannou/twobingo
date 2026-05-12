# NotificationCenter with Socket.io Documentation

**Version:** 2.0.13 (Latest netty-socketio)  
**Sources:**
- GitHub: https://github.com/mrniko/netty-socketio  
- Tutorial: https://medium.com/geekculture/socket-io-with-springboot-application-5a59358f7d48

---

## Overview

The NotificationCenter is a real-time notification system built with **netty-socketio 2.0.13** and Spring Boot that enables bidirectional communication between the frontend and backend applications.

### Features

- ✅ Real-time bidirectional communication via WebSocket
- ✅ Fallback to XHR-polling for older browsers
- ✅ User-based and broadcast notifications
- ✅ Room-based communication support
- ✅ Automatic reconnection with exponential backoff
- ✅ Connection status tracking
- ✅ Multiple notification types (success, error, warning, info)
- ✅ RESTful API for sending notifications
- ✅ Responsive UI component

---

## Backend Setup

### Installation & Dependencies

**Maven Dependency:**
```xml
<dependency>
    <groupId>com.corundumstudio.socketio</groupId>
    <artifactId>netty-socketio</artifactId>
    <version>2.0.13</version>
</dependency>
```

**Requirements:**
- Java 8+ (JDK 11+ for building)
- Spring Boot 3.5.x
- Socket.IO Protocol v1-v4 support

### Configuration Files

#### 1. `application.properties`
```properties
# Socket.io Configuration
socketio.port=9092
socketio.host=0.0.0.0
```

#### 2. `SocketIOConfig.java` - Main Configuration
- Creates SocketIOServer bean
- Configures port, host, CORS, payload limits
- Registers core event listeners
- Manages server lifecycle

#### 3. `NotificationCenter.java` - Core Service
**Key Methods:**
```java
// User Management
registerUser(userId, client)           // Register user
unregisterUser(userId)                 // Unregister user
isUserConnected(userId)                // Check status
getConnectedUsers()                    // List all users
getConnectedUsersCount()               // Count

// Notifications
notifyUser(userId, event, data)        // Send to user
broadcastNotification(event, data)     // Send to all
notifyUsers(userIds, event, data)      // Send to multiple
notifyRoom(roomName, event, data)      // Send to room
```

#### 4. `NotificationController.java` - REST API
**Endpoints:**
- `POST /api/notification/send/{userId}` - Send to user
- `POST /api/notification/broadcast` - Broadcast to all
- `GET /api/notification/status` - Get server status
- `GET /api/notification/user/{userId}/connected` - Check user

### Backend Event Flow

```
Client → emit("register", userId)
         ↓
Server → receives registration
         ↓
Server → stores userId ↔ SocketIOClient mapping
         ↓
Server → can now send notifications to specific user
```

### Backend Usage Examples

**Send to All Users:**
```java
@Autowired
private NotificationCenter notificationCenter;

notificationCenter.broadcastNotification("grid:updated", gridData);
```

**Send to Specific User:**
```java
notificationCenter.notifyUser("user123", "message:new", messageData);
```

**Send to Multiple Users:**
```java
List<String> userIds = Arrays.asList("user1", "user2", "user3");
notificationCenter.notifyUsers(userIds, "event:name", data);
```

---

## Frontend Setup

### Installation

Add Socket.IO client to `package.json`:
```json
"socket.io-client": "^4.7.2"
```

Install:
```bash
cd frontend
npm install
```

### Components

#### 1. `notificationCenter.js` - Client Service
**Methods:**
```javascript
connect()                              // Connect to server
registerUser(userId)                   // Register user
on(event, callback)                    // Listen to event
off(event, callback)                   // Remove listener
emit(event, data)                      // Send event
disconnect()                           // Disconnect
isSocketConnected()                    // Check status
```

#### 2. `notifications.js` - Pinia Store
**State:**
- `notifications` - Array of notifications
- `connectionStatus` - Boolean connection status

**Actions:**
- `initializeNotificationCenter(userId)` - Setup
- `addNotification(notification)` - Add to UI
- `removeNotification(id)` - Remove from UI
- `onEvent(event, callback)` - Listen
- `emitEvent(event, data)` - Send
- `disconnect()` - Cleanup

#### 3. `NotificationDisplay.vue` - UI Component
**Features:**
- Connection status badge
- Notification list with timestamps
- Auto-dismiss after 5 seconds
- Manual dismiss buttons
- 4 notification types with colors
- Responsive design
- Smooth animations

### Frontend Usage

**In App.vue:**
```javascript
import { useNotificationStore } from '@/stores/notifications';
import { onMounted, onUnmounted } from 'vue';

export default {
  setup() {
    const notificationStore = useNotificationStore();

    onMounted(async () => {
      await notificationStore.initializeNotificationCenter('user123');
    });

    onUnmounted(() => {
      notificationStore.disconnect();
    });

    return { notificationStore };
  }
};
```

**Add Notification Display:**
```vue
<template>
  <div id="app">
    <NotificationDisplay />
    <!-- rest of content -->
  </div>
</template>

<script setup>
import NotificationDisplay from '@/components/NotificationDisplay.vue';
</script>
```

**Listen to Events:**
```javascript
notificationStore.onEvent('grid:updated', (data) => {
  console.log('Grid updated:', data);
  // Refresh grid
});

notificationStore.onEvent('user:joined', (userData) => {
  console.log('User joined:', userData);
});
```

---

## Testing with REST API

### Broadcast Notification
```bash
curl -X POST http://localhost:8081/api/notification/broadcast \
  -H "Content-Type: application/json" \
  -d '{
    "event": "test:notification",
    "data": {"message": "Hello"}
  }'
```

### Send to Specific User
```bash
curl -X POST http://localhost:8081/api/notification/send/user123 \
  -H "Content-Type: application/json" \
  -d '{
    "event": "personal:message",
    "data": {"message": "Just for you"}
  }'
```

### Get Server Status
```bash
curl http://localhost:8081/api/notification/status
```

### Check User Connection
```bash
curl http://localhost:8081/api/notification/user/user123/connected
```

---

## Configuration

### Backend Configuration
```properties
# Port and host
socketio.port=9092
socketio.host=0.0.0.0

# CORS (currently allows all - restrict in production)
config.setOrigin("*");
```

### Frontend Configuration
**Environment Variable (.env.local):**
```
VITE_SOCKETIO_URL=http://localhost:9092
```

**Reconnection Settings (notificationCenter.js):**
```javascript
reconnection: true,
reconnectionDelay: 1000,      // Start delay
reconnectionDelayMax: 5000,   // Max delay
reconnectionAttempts: 5       // Max attempts
```

---

## Notification Types

All types auto-dismiss after 5 seconds (configurable):

```javascript
// Success
notificationStore.addNotification({
  title: 'Success',
  message: 'Operation completed',
  type: 'success'
});

// Error
notificationStore.addNotification({
  title: 'Error',
  message: 'Something went wrong',
  type: 'error',
  autoClose: false  // Don't auto-dismiss
});

// Warning
notificationStore.addNotification({
  title: 'Warning',
  message: 'Please review',
  type: 'warning'
});

// Info (default)
notificationStore.addNotification({
  title: 'Info',
  message: 'FYI',
  type: 'info'
});
```

---

## Architecture

```
┌──────────────────┐                   ┌────────────────────┐
│  Browser (Vue.js)│                   │  Spring Boot       │
├──────────────────┤                   ├────────────────────┤
│  App.vue         │                   │  REST Controllers  │
│    ↓             │                   │      ↓             │
│ NotificationStore│◄──Socket.io───────┤ NotificationCenter │
│    ↓             │  (WebSocket)      │      ↓             │
│ NotifDisplay     │  Port 9092        │  Services/DB       │
└──────────────────┘                   └────────────────────┘
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Cannot connect | Check port 9092 open, backend running, CORS enabled |
| Notifications not showing | Verify user registered, event names match, NotificationDisplay mounted |
| Frequent disconnects | Increase reconnection attempts, check network |
| No WebSocket | Try XHR-polling fallback, check browser console for errors |

---

## Performance Notes

- **Payload Limit:** 100MB (configurable)
- **Max Connections:** Depends on server resources
- **Latency:** <100ms typical for local deployment
- **Bandwidth:** ~1KB per notification (varies with data)

---

## Security Considerations

### Current State (Development)
- CORS: Allows all origins (`*`)
- Authentication: None
- Encryption: HTTP (not HTTPS)

### Production Recommendations
1. **Restrict CORS:**
   ```java
   config.setOrigin("https://yourdomain.com");
   ```

2. **Add Authentication:**
   ```java
   config.setAuthorizationListener(data -> {
     String token = data.get("token").toString();
     return validateToken(token);
   });
   ```

3. **Enable SSL/TLS:**
   ```java
   config.setSslContext(sslContext);
   ```

4. **Validate Input:** Always validate events and data on backend

---

## Running the Application

### Prerequisites
- JDK 11 or higher
- Maven 3.8+
- Node.js 14+ and npm

### Start Backend
```bash
cd workplace/twobingo
mvn clean install
mvn spring-boot:run
```

### Start Frontend
```bash
cd frontend
npm install
npm run dev
```

### Access Application
- Frontend: http://localhost:5173
- Backend API: http://localhost:8081
- Socket.io Server: http://localhost:9092

---

## Resources

- **netty-socketio Repository:** https://github.com/mrniko/netty-socketio
- **Socket.io Official:** https://socket.io/
- **Spring Boot Documentation:** https://spring.io/projects/spring-boot
- **Vue 3 Documentation:** https://vuejs.org/
- **Tutorial Reference:** https://medium.com/geekculture/socket-io-with-springboot-application-5a59358f7d48

---

## Version Info

- **Netty-SocketIO:** 2.0.13 (March 19, 2025)
- **Socket.io-Client:** 4.7.2
- **Spring Boot:** 3.5.13+
- **Java:** 25

