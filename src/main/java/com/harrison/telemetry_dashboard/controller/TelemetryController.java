package com.harrison.telemetry_dashboard.controller;

import com.harrison.telemetry_dashboard.config.TelemetryWebSocketHandler;
import com.harrison.telemetry_dashboard.model.TelemetryData;
import com.harrison.telemetry_dashboard.model.TelemetrySession;
import com.harrison.telemetry_dashboard.service.TelemetryRecordingService;
import com.harrison.telemetry_dashboard.service.TelemetrySimulator;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.SortedMap;

@Controller
public class TelemetryController {

    private final TelemetrySimulator simulator;
    private final TelemetryWebSocketHandler telemetryWebSocketHandler;
    //inject new recording service
    private final TelemetryRecordingService recordingService;
    private boolean simulatorEnabled = false;

    public TelemetryController(TelemetrySimulator simulator, TelemetryWebSocketHandler telemetryWebSocketHandler, TelemetryRecordingService recordingService) {
        this.simulator = simulator;
        this.telemetryWebSocketHandler = telemetryWebSocketHandler;
        this.recordingService = recordingService;
    }

    //send data every 200ms
    @Scheduled(fixedRate = 200)
    public void sendTelemetryData() {
        //only runs if we say so
        if (!simulatorEnabled) return;

        TelemetryData data = simulator.generateData();
        telemetryWebSocketHandler.broadcast(data);

//        System.out.println("Broadcasting: Altitude = " + data.getAltitude());
    }

    //new endpoints for recording

    //start recording endpoint
    @PostMapping("/api/record/start")
    @ResponseBody
    public TelemetrySession startRecording(@RequestParam String name,
                                           @RequestParam(required = false) String description) {
        return recordingService.startRecording(name, description);
    }

    //end recording
    @PostMapping("/api/record/stop")
    @ResponseBody
    public TelemetrySession stopRecording() {
        return recordingService.endRecording();
    }

    //Delete recording
    @DeleteMapping("/api/recording/sessions/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        recordingService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    //recording-detail get a single recording
    @GetMapping("/api/recording/sessions/{id}")
    @ResponseBody
    public ResponseEntity<TelemetrySession> getSession(@PathVariable Long id) {
        return recordingService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //for a list of sessions
    @GetMapping("/api/recording/sessions")
    @ResponseBody
    public List<TelemetrySession> getSessions() {
        return recordingService.getAllSessions();
    }


    //test data controls
    @PostMapping("/api/test/start")
    @ResponseBody
    public String startTestData() {
        simulatorEnabled = true;
        return "Simulator started";
    }

    @PostMapping("/api/test/stop")
    @ResponseBody
    public String stopTestData() {
        simulatorEnabled = false;
        return "Simulator stopped";
    }

    //post mapping for live sensor data
    @PostMapping("/api/sensor/data")
    @ResponseBody
    public ResponseEntity<String> receiveSensorData(@RequestBody TelemetryData data) {
        //if test data is enabled do not broadcast the realtime data
        if(simulatorEnabled) {
            return ResponseEntity.ok("ignored - test mode active");
        }
        //broadcast to websocket so the dashboard updates live data
        telemetryWebSocketHandler.broadcast(data);
        return ResponseEntity.ok("received");

    }
}
