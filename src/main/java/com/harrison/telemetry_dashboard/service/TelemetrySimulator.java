package com.harrison.telemetry_dashboard.service;

import com.harrison.telemetry_dashboard.model.TelemetryData;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TelemetrySimulator {

    private final Random random = new Random();
    private double simulatedAltitude = 0.0;
    private double simulatedVelocity = 0.0;


    public TelemetryData generateData() {
        simulatedAltitude += simulatedVelocity * 0.1 + random.nextDouble(-0.5, 1.5);
        simulatedVelocity += random.nextDouble(-0.8, 1.2);

        if(simulatedAltitude < 0) {
            simulatedAltitude = 0;
            simulatedVelocity = Math.abs(simulatedVelocity);
        }

        TelemetryData data = new TelemetryData();
        data.setAltitude(Math.max(0, simulatedAltitude));
        data.setVelocity(simulatedVelocity);
        data.setAcceleration(random.nextDouble(-2.0, 5.0));
        data.setTemperature(22.5 + random.nextDouble(-5.0, 8.0));
        data.setPressure(1013.25 - (simulatedAltitude / 8.5));

        data.setAccelX(random.nextDouble(-1.0, 1.0));
        data.setAccelY(random.nextDouble(-1.0, 1.0));
        data.setAccelZ(9.81 + random.nextDouble(-2.0, 2.0));

        return data;
    }
}
