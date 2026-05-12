package com.twobingo.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.SocketIOClient;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SocketService {

    private SocketIOServer socketIOServer;

    private final Map<String, SocketIOClient> userClients = new ConcurrentHashMap<>();

    /**
     * Set the SocketIOServer instance
     */
    public void setSocketIOServer(SocketIOServer server) {
        this.socketIOServer = server;
    }

    /**
     * Register a user client
     */
    public void registerUser(String userId, SocketIOClient client) {
        userClients.put(userId, client);
    }

    /**
     * Unregister a user
     */
    public void unregisterUser(String userId) {
        userClients.remove(userId);
    }

    /**
     * Send notification to specific user
     */
    public void notifyUser(String userId, String event, Object data) {
        SocketIOClient client = userClients.get(userId);
        if (client != null && client.isChannelOpen()) {
            client.sendEvent(event, data);
        }
    }

    /**
     * Send notification to multiple users
     */
    public void notifyUsers(List<String> userIds, String event, Object data) {
        for (String userId : userIds) {
            notifyUser(userId, event, data);
        }
    }

    /**
     * Send notification to a room
     */
    public void notifyRoom(String roomName, String event, Object data) {
        if (socketIOServer != null) {
            socketIOServer.getRoomOperations(roomName).sendEvent(event, data);
        }
    }

    /**
     * Get connected users count
     */
    public int getConnectedUsersCount() {
        return socketIOServer != null ? socketIOServer.getAllClients().size() : 0;
    }

    /**
     * Check if user is connected
     */
    public boolean isUserConnected(String userId) {
        SocketIOClient client = userClients.get(userId);
        return client != null && client.isChannelOpen();
    }

    /**
     * Get all connected user IDs
     */
    public Set<String> getConnectedUsers() {
        return new HashSet<>(userClients.keySet());
    }

    /**
     * Get Socket.IO server instance
     */
    public SocketIOServer getSocketIOServer() {
        return socketIOServer;
    }
}
