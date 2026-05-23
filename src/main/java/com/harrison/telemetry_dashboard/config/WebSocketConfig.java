package com.harrison.telemetry_dashboard.config;


import com.harrison.telemetry_dashboard.service.TelemetryRecordingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final TelemetryRecordingService recordingService;

    public WebSocketConfig(TelemetryRecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @Bean
    public TelemetryWebSocketHandler telemetryWebSocketHandler() {
        return new TelemetryWebSocketHandler(recordingService);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(telemetryWebSocketHandler(), "/telemetry")
                .setAllowedOrigins("*");
    }
}
