package com.meh.pmc;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.meh.pmc.domain.Alarm;
import com.meh.pmc.service.AlarmService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@Slf4j
@AllArgsConstructor
public class PmcApplication implements CommandLineRunner {
    private final AlarmService alarmService;
    private final ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(PmcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            log.error("Error, no file name passed in.");
            System.exit(0);
        }
        List<Alarm> alarms = alarmService.getAlarms(args[0]);
        log.info("alarms: \n" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(alarms));
    }
}
