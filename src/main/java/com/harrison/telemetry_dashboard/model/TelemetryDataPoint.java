package com.harrison.telemetry_dashboard.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "telemetry_data_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryDataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    @JsonBackReference
    private TelemetrySession session;

    private Instant timestamp = Instant.now();

    private Double altitude;
    private Double velocity;
    private Double acceleration;
    private Double temperature;
    private Double pressure;

    private Double accelX;
    private Double accelY;
    private Double accelZ;

    private String status;

}
