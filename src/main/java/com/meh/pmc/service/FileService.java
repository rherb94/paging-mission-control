package com.meh.pmc.service;

import com.meh.pmc.domain.TelemetryData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileService {
    private final String timePattern = "yyyyMMdd HH:mm:ss.SSS";
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(timePattern);

    public List<TelemetryData> loadTelemetryData(String inputFile) throws FileNotFoundException {
        List<TelemetryData> telemetryDataList = new ArrayList<>();

        log.debug("loading data file: " + inputFile);

        Path path = Paths.get(inputFile);
        if (! Files.exists(path)) {
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
                        .redHigh(Double.valueOf(data[2]))
                        .yellowHigh(Double.valueOf(data[3]))
                        .yellowLow(Double.valueOf(data[4]))
                        .redLow(Double.valueOf(data[5]))
                        .rawValue(Double.valueOf(data[6]))
                        .component(data[7])
                        .build();

                telemetryDataList.add(td);
            }
        } catch (Exception e) {
            log.error("Exception occurred while reading data file, error: " + e.getMessage());
        }

        // sort the array by timestamp ascending just in case
        log.debug("before sort: " + telemetryDataList);

        telemetryDataList.sort((td1, td2) -> td1.getTimeStamp().compareTo(td2.getTimeStamp()));

        log.debug("after sort: " + telemetryDataList);

        return telemetryDataList;
    }
}
