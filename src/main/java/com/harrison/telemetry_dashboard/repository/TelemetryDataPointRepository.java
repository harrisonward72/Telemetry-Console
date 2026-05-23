package com.harrison.telemetry_dashboard.repository;

import com.harrison.telemetry_dashboard.model.TelemetryDataPoint;
import com.harrison.telemetry_dashboard.model.TelemetrySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelemetryDataPointRepository extends JpaRepository<TelemetryDataPoint, Long> {

    List<TelemetryDataPoint> findBySessionOrderByTimestampAsc(TelemetrySession session);
}