package com.twobingo.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PreDestroy;

@Configuration
public class SocketIOConfig {

    private static final Logger logger = LoggerFactory.getLogger(SocketIOConfig.class);

    @Value("${socketio.port:9092}")
    private int port;

    @Value("${socketio.host:0.0.0.0}")
    private String host;

    private SocketIOServer socketIOServer;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(port);
        config.setHostname(host);
        config.setOrigin("*");
        config.setMaxHttpContentLength(1024 * 1024 * 100); // 100MB
        config.setMaxFramePayloadLength(1024 * 1024 * 100); // 100MB

        socketIOServer = new SocketIOServer(config);

        // Handle client connection
        socketIOServer.addConnectListener(client -> {
            logger.info("Client connected: SessionId={}", client.getSessionId());
            client.sendEvent("connected", "Welcome to Twobingo NotificationCenter");
        });

        // Handle client disconnection
        socketIOServer.addDisconnectListener(client -> {
            String userId = (String) client.get("userId");
            logger.info("Client disconnected: SessionId={}, UserId={}", client.getSessionId(), userId);
            // if (userId != null) {
            // notificationCenter.unregisterUser(userId);
            // }
        });

        // Handle user registration
        // socketIOServer.addEventListener("register", String.class, (client, userId,
        // ackRequest) -> {
        // logger.info("User registration request: UserId={}, SessionId={}", userId,
        // client.getSessionId());
        // client.set("userId", userId);
        // notificationCenter.registerUser(userId, client);
        // client.sendEvent("registered", userId);
        // });

        socketIOServer.start();

        logger.info("Socket.IO server started on {}:{}", host, port);

        return socketIOServer;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            logger.info("Socket.IO server stopped");
        }
    }
}
