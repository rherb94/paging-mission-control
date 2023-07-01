package com.meh.pmc.service;

import com.meh.pmc.domain.Alarm;
import com.meh.pmc.domain.TelemetryData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlarmServiceTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private AlarmService alarmService;

    @Test
    void shouldHaveNoAlarms() {
        List<TelemetryData> tdList = buildNoAlarmData();
        when(fileService.loadTelemetryData(anyString())).thenReturn(tdList);

        List<Alarm> alarms = alarmService.getAlarms("testData.txt");

        assertEquals(0, alarms.size());
        verify(fileService, times(1)).loadTelemetryData(anyString());
    }

    @Test
    void shouldHaveOneRedHighAlarm() {
        List<TelemetryData> tdList = buildRedHighAlarmData();
        when(fileService.loadTelemetryData(anyString())).thenReturn(tdList);

        List<Alarm> alarms = alarmService.getAlarms("testData.txt");
        assertEquals(1, alarms.size());
        assertEquals("RED HIGH", alarms.get(0).getSeverity());
        verify(fileService, times(1)).loadTelemetryData(anyString());
    }

    @Test
    void shouldHaveOneRedLowAlarm() {
        List<TelemetryData> tdList = buildRedLowAlarmData();
        when(fileService.loadTelemetryData(anyString())).thenReturn(tdList);

        List<Alarm> alarms = alarmService.getAlarms("testData.txt");
        assertEquals(1, alarms.size());
        assertEquals("RED LOW", alarms.get(0).getSeverity());
        verify(fileService, times(1)).loadTelemetryData(anyString());
    }

    private List<TelemetryData> buildNoAlarmData() {
        List<TelemetryData> tdList = new ArrayList<>();
        TelemetryData td = TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1001").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(99.9).component("TSTAT").build();
        tdList.add(td);

        return tdList;
    }

    private List<TelemetryData> buildRedHighAlarmData() {
        List<TelemetryData> tdList = new ArrayList<>();
        TelemetryData td1 = TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(109.9).component("TSTAT").build();
        TelemetryData td2 = TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(109.9).component("TSTAT").build();
        TelemetryData td3 = TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(109.9).component("TSTAT").build();
        tdList.add(td1);
        tdList.add(td2);
        tdList.add(td3);

        return tdList;
    }

    private List<TelemetryData> buildRedLowAlarmData() {
        List<TelemetryData> tdList = new ArrayList<>();
        TelemetryData td1 = TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("TSTAT").build();
        TelemetryData td2 = TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("TSTAT").build();
        TelemetryData td3 = TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("TSTAT").build();
        tdList.add(td1);
        tdList.add(td2);
        tdList.add(td3);

        return tdList;
    }

}