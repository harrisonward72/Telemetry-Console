package com.harrison.telemetry_dashboard.service;

import com.harrison.telemetry_dashboard.model.TelemetryData;
import com.harrison.telemetry_dashboard.model.TelemetryDataPoint;
import com.harrison.telemetry_dashboard.model.TelemetrySession;
import com.harrison.telemetry_dashboard.repository.TelemetryDataPointRepository;
import com.harrison.telemetry_dashboard.repository.TelemetrySessionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TelemetryRecordingService {

    private final TelemetrySessionRepository sessionRepository;
    private final TelemetryDataPointRepository dataPointRepository;

    private TelemetrySession activeSession = null;

    public TelemetryRecordingService(TelemetrySessionRepository sessionRepository,
                                     TelemetryDataPointRepository dataPointRepository) {
        this.sessionRepository = sessionRepository;
        this.dataPointRepository = dataPointRepository;
    }

    public TelemetrySession startRecording(String name, String description) {
        if (activeSession != null) {
            endRecording();
        }

        TelemetrySession session = new TelemetrySession();
        session.setName(name);
        session.setDescription(description);
        session.setStartTime(Instant.now());
        session.setActive(true);

        activeSession = sessionRepository.save(session);
        return activeSession;
    }

    public void recordData(TelemetryData data) {
        if (activeSession == null) return;

        TelemetryDataPoint point = new TelemetryDataPoint();
        point.setSession(activeSession);
        point.setTimestamp(data.getTimestamp());
        point.setAltitude(data.getAltitude()!= null ? data.getAltitude() : 0.0);
        //velocity cannot be accurately calculated w/o gps - cannot be null so set to 0.0 as default
        point.setVelocity(data.getVelocity() != null ? data.getVelocity() : 0.0);
        point.setAcceleration(data.getAcceleration() != null ? data.getAcceleration() : 0.0);
        point.setTemperature(data.getTemperature()!= null ? data.getTemperature() : 0.0);
        point.setPressure(data.getPressure()!= null ? data.getPressure() : 0.0);
        point.setAccelX(data.getAccelX()!= null ? data.getAccelX() : 0.0);
        point.setAccelY(data.getAccelY()!= null ? data.getAccelY() : 0.0);
        point.setAccelZ(data.getAccelZ()!= null ? data.getAccelZ() : 0.0);
        point.setStatus(data.getStatus()!= null ? data.getStatus() : "null");

        dataPointRepository.save(point);
    }

    public TelemetrySession endRecording() {
        if (activeSession == null) return null;

        activeSession.endSession();
        TelemetrySession saved = sessionRepository.save(activeSession);
        activeSession = null;
        return saved;
    }

    public List<TelemetrySession> getAllSessions() {
        return sessionRepository.findAllByOrderByStartTimeDesc();
    }

    public Optional<TelemetrySession> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public boolean isRecording() {
        return activeSession != null;
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }
}