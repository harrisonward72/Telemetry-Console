package com.harrison.telemetry_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //enables the scheduled annotation in the TelemetryController class
public class TelemetryDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelemetryDashboardApplication.class, args);
	}

}
