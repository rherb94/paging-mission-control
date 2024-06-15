// package com.rmh.pmc.service;

// import com.rmh.pmc.domain.Alarm;
// import com.rmh.pmc.domain.TelemetryData;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class AlarmServiceTest {
// @Mock
// private FileService fileService;

// @InjectMocks
// private AlarmService alarmService;

// @Test
// void shouldHaveNoAlarms() throws Exception {
// List<TelemetryData> tdList = buildNoAlarmData();
// when(fileService.loadTelemetryData(anyString())).thenReturn(tdList);

// List<Alarm> alarms = alarmService.getAlarms("testData.txt");

// assertEquals(0, alarms.size());
// verify(fileService, times(1)).loadTelemetryData(anyString());
// }

// @Test
// void shouldHaveOneRedHighAlarm() throws Exception {
// // RED HIGH alarms are only on component TSTAT
// List<TelemetryData> tdList = buildRedHighAlarmData();
// when(fileService.loadTelemetryData(anyString())).thenReturn(tdList);

// List<Alarm> alarms = alarmService.getAlarms("testData.txt");
// assertEquals(1, alarms.size());
// assertEquals("RED HIGH", alarms.get(0).getSeverity());
// assertEquals("TSTAT", alarms.get(0).getComponent());
// verify(fileService, times(1)).loadTelemetryData(anyString());
// }

// @Test
// void shouldHaveOneRedLowAlarm() throws Exception {
// // RED LOW alarms are only on component BATT
// List<TelemetryData> tdList = buildRedLowAlarmData();
// when(fileService.loadTelemetryData(anyString())).thenReturn(tdList);

// List<Alarm> alarms = alarmService.getAlarms("testData.txt");
// assertEquals(1, alarms.size());
// assertEquals("RED LOW", alarms.get(0).getSeverity());
// assertEquals("BATT", alarms.get(0).getComponent());
// verify(fileService, times(1)).loadTelemetryData(anyString());
// }

// @Test
// void shouldRespectFiveMinuteWindowForAlarmCreation() throws Exception {
// // generate a list of 3 TelemetryData data where the 3rd item timestamp is
// not in the 5 minute window
// // so no alarms should be generated
// List<TelemetryData> tdList = buildFiveMinuteWindowData();
// when(fileService.loadTelemetryData(anyString())).thenReturn(tdList);

// List<Alarm> alarms = alarmService.getAlarms("testData.txt");
// assertEquals(0, alarms.size());
// verify(fileService, times(1)).loadTelemetryData(anyString());
// }

// private List<TelemetryData> buildNoAlarmData() {
// List<TelemetryData> tdList = new ArrayList<>();
// TelemetryData td =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1001").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(99.9).component("TSTAT").build();
// tdList.add(td);

// return tdList;
// }

// private List<TelemetryData> buildRedHighAlarmData() {
// List<TelemetryData> tdList = new ArrayList<>();
// TelemetryData td1 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(109.9).component("TSTAT").build();
// TelemetryData td2 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(109.9).component("TSTAT").build();
// TelemetryData td3 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(109.9).component("TSTAT").build();
// tdList.add(td1);
// tdList.add(td2);
// tdList.add(td3);
// TelemetryData td4 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(109.9).component("BATT").build();
// TelemetryData td5 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(109.9).component("BATT").build();
// TelemetryData td6 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(109.9).component("BATT").build();
// tdList.add(td4);
// tdList.add(td5);
// tdList.add(td6);

// return tdList;
// }

// private List<TelemetryData> buildRedLowAlarmData() {
// List<TelemetryData> tdList = new ArrayList<>();
// TelemetryData td1 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("BATT").build();
// TelemetryData td2 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("BATT").build();
// TelemetryData td3 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("BATT").build();
// tdList.add(td1);
// tdList.add(td2);
// tdList.add(td3);
// TelemetryData td4 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("TSTAT").build();
// TelemetryData td5 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("TSTAT").build();
// TelemetryData td6 =
// TelemetryData.builder().timeStamp(Instant.now()).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("TSTAT").build();
// tdList.add(td4);
// tdList.add(td5);
// tdList.add(td6);

// return tdList;
// }

// private List<TelemetryData> buildFiveMinuteWindowData() {
// List<TelemetryData> tdList = new ArrayList<>();
// Instant theTime = Instant.now();

// TelemetryData td1 =
// TelemetryData.builder().timeStamp(theTime).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("TSTAT").build();
// TelemetryData td2 =
// TelemetryData.builder().timeStamp(theTime).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("TSTAT").build();
// tdList.add(td1);
// tdList.add(td2);

// theTime = theTime.plus(7, ChronoUnit.MINUTES);
// TelemetryData td3 =
// TelemetryData.builder().timeStamp(theTime).satelliteId("1000").redHigh(101D).yellowHigh(98D).yellowLow(25D).redLow(20D).rawValue(19.9).component("TSTAT").build();
// tdList.add(td3);

// return tdList;
// }
// }