package com.twobingo.controller;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private SocketIOServer socketServer;

    void SocketIOController(SocketIOServer socketServer) {
        this.socketServer = socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);

        /**
         * Here we create only one event listener
         * but we can create any number of listener
         * messageSendToUser is socket end point after socket connection user have to
         * send message payload on messageSendToUser event
         */
        // this.socketServer.addEventListener("messageSendToUser", Message.class,
        // onSendMessage);

    }

    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {

            // log.info("Perform operation on user connect in controller");
        }
    };

    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
            // log.info("Perform operation on user disconnect in controller");
        }
    };

    public DataListener<Message> onSendMessage = new DataListener<Message>() {
        @Override
        public void onData(SocketIOClient client, Message message, AckRequest acknowledge) throws Exception {

            /**
             * Sending message to target user
             * target user should subscribe the socket event with his/her name.
             * Send the same payload to user
             */

            // log.info(message.getSenderName()+" user send message to user
            // "+message.getTargetUserName()+" and message is "+message.getMessage());
            socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(), client, message);

            /**
             * After sending message to target user we can send acknowledge to sender
             */
            acknowledge.sendAckData("Message send to target user successfully");
        }
    };

    /**
     * Send notification to a specific user
     */
    // @PostMapping("/send/{userId}")
    // public ResponseEntity<Map<String, Object>> sendNotificationToUser(
    // @PathVariable String userId,
    // @RequestBody NotificationRequest request) {

    // try {
    // notificationCenter.notifyUser(userId, request.getEvent(), request.getData());

    // Map<String, Object> response = new HashMap<>();
    // response.put("success", true);
    // response.put("message", "Notification sent to user: " + userId);
    // response.put("userConnected", notificationCenter.isUserConnected(userId));

    // logger.info("Notification sent to user: {} with event: {}", userId,
    // request.getEvent());
    // return ResponseEntity.ok(response);
    // } catch (Exception e) {
    // logger.error("Error sending notification to user: {}", userId, e);
    // Map<String, Object> error = new HashMap<>();
    // error.put("success", false);
    // error.put("message", "Error sending notification: " + e.getMessage());
    // return ResponseEntity.status(500).body(error);
    // }
    // }

    /**
     * Request body for sending notifications
     */
    public static class NotificationRequest {
        private String event;
        private Object data;

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    public class Message {

        private String senderName;
        private String targetUserName;
        private String message;

        public String getTargetUserName() {
            return targetUserName;
        }

    }
}
