package com.harrison.telemetry_dashboard.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "telemetry_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetrySession {

    //Give the entity ID, name, description, start and end time
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Instant startTime = Instant.now();

    private Instant endTime;

    @Column(nullable = false)
    private boolean isActive = true;

    //One session can have many data points
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TelemetryDataPoint> dataPoints = new ArrayList<>();

    public void endSession() {
        this.endTime = Instant.now();
        this.isActive = false;
    }


}
