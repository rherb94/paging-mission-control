package com.rmh.pmc.service;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rmh.pmc.domain.TelemetryData;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileService {
    // private ObjectMapper objectMapper = new ObjectMapper();
    private final String timePattern = "yyyyMMdd HH:mm:ss.SSS";
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timePattern);

    public Map<String, List<TelemetryData>> loadTelemetryData(String inputFile) throws Exception {
        Map<String, List<TelemetryData>> alarmMap = new HashMap<>();
        log.debug("loading data file: " + inputFile);

        Path path = Paths.get(inputFile);
        if (!Files.exists(path)) {
            throw new FileNotFoundException(inputFile + " not found");
        }

        try {
            List<String> allLines = Files.readAllLines(path);
            int lineNumber = 0;
            for (String line : allLines) {
                lineNumber++;
                String[] data = line.split("\\|");
                if (data.length != 8) {
                    log.error("Error parsing data on line: " + lineNumber);
                    continue;
                }
                LocalDateTime localDateTime = LocalDateTime.parse(data[0], dateTimeFormatter);

                TelemetryData td = TelemetryData.builder()
                        .timeStamp(localDateTime.toInstant(ZoneOffset.UTC))
                        .satelliteId(data[1])
                        .redHighLimit(Double.valueOf(data[2]))
                        .yellowHighLimit(Double.valueOf(data[3]))
                        .yellowLowLimit(Double.valueOf(data[4]))
                        .redLowLimit(Double.valueOf(data[5]))
                        .rawValue(Double.valueOf(data[6]))
                        .component(data[7])
                        .build();
                if (alarmMap.containsKey(td.getSatelliteId())) {
                    alarmMap.get(td.getSatelliteId()).add(td);
                } else {
                    alarmMap.put(td.getSatelliteId(), new ArrayList<>());
                    alarmMap.get(td.getSatelliteId()).add(td);

                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while reading data file, error: " + e.getMessage());
        }

        // sort the array by timestamp ascending just in case
        // log.debug("before sort: " + telemetryDataList);

        // telemetryDataList.sort((td1, td2) ->
        // td1.getTimeStamp().compareTo(td2.getTimeStamp()));

        // log.debug("after sort: " + telemetryDataList);

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String jacksonData = objectMapper.writeValueAsString(alarmMap);
        log.info("td map is: " + jacksonData);
        return alarmMap;
    }
}
