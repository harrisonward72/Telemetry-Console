package com.harrison.telemetry_dashboard.repository;

import com.harrison.telemetry_dashboard.model.TelemetrySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelemetrySessionRepository extends JpaRepository<TelemetrySession, Long> {

    List<TelemetrySession> findAllByOrderByStartTimeDesc(); //most recent first
    List<TelemetrySession> findByIsActiveTrue(); //active session
}
