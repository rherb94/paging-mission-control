package com.rmh.pmc.service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.rmh.pmc.domain.Alarm;
import com.rmh.pmc.domain.TelemetryData;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AlarmService {
    private final FileService fileService;

    @SneakyThrows
    public Map<String, List<TelemetryData>> getAlarms(String inputFile) {
        Map<String, List<TelemetryData>> alarmMap = fileService.loadTelemetryData(inputFile);
        return alarmMap;
    }

    // Return a Map with the satellite id as the key and a list of telem data as the
    // value
    private Map<String, List<TelemetryData>> getAlarmsMap(List<TelemetryData> telemetryDataList) {
        Map<String, List<TelemetryData>> alarmMap = new HashMap<>();
        for (TelemetryData td : telemetryDataList) {
            if (alarmMap.containsKey(td.getSatelliteId())) {
                alarmMap.get(td.getSatelliteId()).add(td);
            } else {
                alarmMap.put(td.getSatelliteId(), new ArrayList<>());
                alarmMap.get(td.getSatelliteId()).add(td);

            }
        }
        return alarmMap;
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
                            alarms.add(createAlarm(tdsForAlarmForThisSatellite.get(0)));
                            tdsForAlarmForThisSatellite.clear();
                            startInstant = td.getTimeStamp().plus(Duration.ofMinutes(5));
                        }
                    } else {
                        startInstant = td.getTimeStamp();
                        tdsForAlarmForThisSatellite.clear();
                        tdsForAlarmForThisSatellite.add(td);
                    }
                }
            }
        }
        return alarms;
    }

    private Alarm createAlarm(TelemetryData td) {
        return Alarm.builder()
                .satelliteId(td.getSatelliteId())
                .severity(td.getAlarm())
                .component(td.getComponent())
                .timestamp(td.getTimeStamp().toString())
                .build();
    }
}
