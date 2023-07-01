package com.meh.pmc.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.meh.pmc.domain.Alarm;
import com.meh.pmc.domain.TelemetryData;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class AlarmService {
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public List<Alarm> printAlarms(String inputFile) {
        List<TelemetryData> telemetryDataList = fileService.loadTelemetryData(inputFile);

        Map<String, List<TelemetryData>> tdAlarmMap = getAlarmsMap(telemetryDataList);
        log.debug("alarmsMap: " + tdAlarmMap);

        return getAlarmsFromTelemetryData(tdAlarmMap);
    }

    private Map<String, List<TelemetryData>> getAlarmsMap(List<TelemetryData> telemetryDataList) {
        Map<String, List<TelemetryData>> tdAlarmMap = new HashMap<>();

        // filter the data so that we have a map with the satellite id and alarm type as the key and a list of those alarms
        for (TelemetryData td : telemetryDataList) {
            String satelliteId = td.getSatelliteId();
            String alarmText = td.getAlarm();
            if (alarmText != null) {
                String key = satelliteId + "-" + alarmText;
                if (tdAlarmMap.containsKey(key)) {
                    tdAlarmMap.get(key).add(td);
                } else {
                    List<TelemetryData> dataList = new ArrayList<>();
                    dataList.add(td);
                    tdAlarmMap.put(key, dataList);
                }
            }
        }
        return tdAlarmMap;
    }

    private List<Alarm> getAlarmsFromTelemetryData(Map<String, List<TelemetryData>> tdAlarmMap) {
        List<Alarm> alarms = new ArrayList<>();

        for (String key : tdAlarmMap.keySet()) {
            Instant startInstant = null;
            List<TelemetryData> tdsForAlarmForThisSatellite = new ArrayList<>();
            List<TelemetryData> alarmList = tdAlarmMap.get(key);
            for (TelemetryData td : alarmList) {
                if (startInstant == null) {
                    startInstant = td.getTimeStamp();
                    tdsForAlarmForThisSatellite.add(td);
                } else {
                    long duration = Duration.between(startInstant, td.getTimeStamp()).toMinutes();
                    if (duration <= 5) {
                        tdsForAlarmForThisSatellite.add(td);
                        if (tdsForAlarmForThisSatellite.size() == 3) {
                            alarms.add(getAlarm(tdsForAlarmForThisSatellite.get(0)));
                            tdsForAlarmForThisSatellite.clear();
                            startInstant = td.getTimeStamp().plus(Duration.ofMinutes(5));
                        }
                    }
                }
            }
        }
        return alarms;
    }

    private Alarm getAlarm(TelemetryData td) {
        return Alarm.builder()
                .satelliteId(td.getSatelliteId())
                .severity(td.getAlarm())
                .component(td.getComponent())
                .timestamp(td.getTimeStamp().toString())
                .build();
    }
}
