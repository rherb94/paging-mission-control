package com.rmh.pmc.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Alarm {
	private String satelliteId;
	private Severity severity;
	private String component;
	private String timestamp;

	public Alarm(String satelliteId, Severity severity, String component, String timestamp) {
		this.satelliteId = satelliteId;
		this.severity = severity;
		this.component = component;
		this.timestamp = timestamp;
	}

}