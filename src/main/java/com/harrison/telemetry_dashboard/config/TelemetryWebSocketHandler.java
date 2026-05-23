package com.harrison.telemetry_dashboard.config;

import com.harrison.telemetry_dashboard.model.TelemetryData;
import com.harrison.telemetry_dashboard.service.TelemetryRecordingService;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.CopyOnWriteArrayList;

public class TelemetryWebSocketHandler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    //updated field to inject the TelemetryRecordingService
    private final TelemetryRecordingService recordingService;

    public TelemetryWebSocketHandler(TelemetryRecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("Connected to " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("Received message: " + message.getPayload());
    }

    //Send data to the bowser
    public void broadcast(TelemetryData data) {
        try {
            String json = objectMapper.writeValueAsString(data);
            TextMessage message = new TextMessage(json);
            System.out.println("Broadcasting data" + json);

            for(WebSocketSession session : sessions) {
                if(session.isOpen()) {
                    session.sendMessage(message);
                }
            }
            //save data if we are currently recording
            recordingService.recordData(data);

        } catch (Exception e) {
            System.err.println("Error broadcasting message: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
        System.out.println("Disconnected from " + session.getId());
    }
}
