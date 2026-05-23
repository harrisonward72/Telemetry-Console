package com.harrison.telemetry_dashboard.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryData {
    private Instant timestamp = Instant.now();

    private Double altitude;
    private Double velocity;
    private Double acceleration;
    private Double temperature;
    private Double pressure;


    private Double accelX;
    private Double accelY;
    private Double accelZ;

    private String status = "NOMINAL";
}
