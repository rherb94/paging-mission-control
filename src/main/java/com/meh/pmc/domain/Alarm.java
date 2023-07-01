package com.meh.pmc.domain;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Alarm {
    private String satelliteId;
    private String severity;
    private String component;
    private String timestamp;
}