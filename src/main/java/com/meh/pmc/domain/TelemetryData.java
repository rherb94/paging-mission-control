package com.meh.pmc.domain;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class TelemetryData {
    private Instant timeStamp;
    private String satelliteId;
    private Double redHigh;
    private Double yellowHigh;
    private Double yellowLow;
    private Double redLow;
    private Double rawValue;
    private String component;

    public String getAlarm() {
        if (rawValue < redLow) {
            return "RED LOW";
        }
        else if (rawValue > redHigh) {
            return "RED HIGH";
        }
        return null;
    }
}
