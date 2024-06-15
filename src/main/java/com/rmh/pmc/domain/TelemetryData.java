package com.rmh.pmc.domain;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TelemetryData {
    private Instant timeStamp;
    private String satelliteId;
    private Double redHighLimit;
    private Double yellowHighLimit;
    private Double yellowLowLimit;
    private Double redLowLimit;
    private Double rawValue;
    private String component;

    // we are only interested in RED HIGH on TSTAT and RED LOW on BATT
    public Severity getAlarm() {
        if (rawValue < redLowLimit && component.equals("BATT")) {
            return Severity.RED_LOW;
        } else if (rawValue > redHighLimit && component.equals("TSTAT")) {
            return Severity.RED_HIGH;
        }
        return null;
    }
}
