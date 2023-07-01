package com.meh.pmc.service;

import com.meh.pmc.domain.TelemetryData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @InjectMocks
    private FileService fileService;

    @Test
    void shouldThrowFilesNotFoundException()  {
        Exception exception = assertThrows(FileNotFoundException.class, () -> {
            List<TelemetryData> tdList = fileService.loadTelemetryData("doesNotExist.txt");
        });

        String expectedMessage = "doesNotExist.txt not found";
        String actualMessage = exception.getMessage();

        assertEquals(true, actualMessage.contains(expectedMessage));
    }
    @Test
    void shouldCreateTelemetryDataList() throws Exception {
        // use a test file with no illegal char separators on lines, should load 28 entries
        List<TelemetryData> tdList = fileService.loadTelemetryData("testData2.txt");
        assertEquals(28, tdList.size());
    }

    @Test
    void shouldSkipBadLinesInFile() throws Exception {
        // use a test file with illegal char separators on 2 lines, should load 26 entries
        List<TelemetryData> tdList = fileService.loadTelemetryData("testDataErrors.txt");
        assertEquals(26, tdList.size());
    }
}