package com.syed.assignment.congestion_tax_calculator.unit;

import com.syed.assignment.congestion_tax_calculator.service.impl.FeeCalculatorServiceImpl;
import com.syed.assignment.congestion_tax_calculator.service.impl.FeeSlotServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeeCalculatorTest {

    @Mock
    private FeeSlotServiceImpl feeSlotServiceImpl;

    @InjectMocks
    private FeeCalculatorServiceImpl feeCalculatorService;

    @Test
    @DisplayName("Test calculation with multiple timestamps in the same day")
    public void testCalculateTotalFeeMultipleTimestampsSameDay() {
        String city = "gothenburg";
        String vehicleType = "Car";
        LocalDateTime timestamp1 = LocalDateTime.of(2013, 9, 23, 7, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2013, 9, 23, 15, 30);
        List<LocalDateTime> timestamps = List.of(timestamp1, timestamp2);

        when(feeSlotServiceImpl.getFeeForTime(any(), any())).thenReturn(18, 13);

        int totalFee = feeCalculatorService.calculateTotalFee(city, vehicleType, timestamps);

        assertEquals(31, totalFee);

        verify(feeSlotServiceImpl).getFeeForTime(timestamp1.toLocalTime(), city);
        verify(feeSlotServiceImpl).getFeeForTime(timestamp2.toLocalTime(), city);
    }

    @Test
    @DisplayName("Test calculation with empty timestamps list")
    public void testCalculateTotalFeeEmptyTimestamps() {
        String city = "gothenburg";
        String vehicleType = "Car";
        List<LocalDateTime> timestamps = List.of();

        int totalFee = feeCalculatorService.calculateTotalFee(city, vehicleType, timestamps);

        assertEquals(0, totalFee);
    }

    @Test
    @DisplayName("Test calculation where total fee exceeds 60 SEK (daily max)")
    public void testCalculateTotalFeeExceedsMax() {
        String city = "gothenburg";
        String vehicleType = "Car";
        LocalDateTime timestamp1 = LocalDateTime.of(2013, 9, 23, 7, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2013, 9, 23, 8, 0);
        LocalDateTime timestamp3 = LocalDateTime.of(2013, 9, 23, 9, 0);
        LocalDateTime timestamp4 = LocalDateTime.of(2013, 9, 23, 16, 0);
        List<LocalDateTime> timestamps = List.of(timestamp1, timestamp2, timestamp3, timestamp4);

        when(feeSlotServiceImpl.getFeeForTime(any(), any())).thenReturn(18, 18, 18, 18);

        int totalFee = feeCalculatorService.calculateTotalFee(city, vehicleType, timestamps);

        assertEquals(60, totalFee);
    }

    @Test
    @DisplayName("Test calculation with timestamps crossing midnight")
    public void testCalculateTotalFeeCrossingMidnight() {
        String city = "gothenburg";
        String vehicleType = "Car";
        LocalDateTime timestamp1 = LocalDateTime.of(2013, 9, 23, 23, 30);
        LocalDateTime timestamp2 = LocalDateTime.of(2013, 9, 24, 0, 30);
        List<LocalDateTime> timestamps = List.of(timestamp1, timestamp2);

        when(feeSlotServiceImpl.getFeeForTime(any(), any())).thenReturn(18, 18);

        int totalFee = feeCalculatorService.calculateTotalFee(city, vehicleType, timestamps);

        assertEquals(36, totalFee);
    }

    @Test
    @DisplayName("Test fee calculation across multiple days")
    public void testCalculateTotalFeeAcrossMultipleDays() {
        String city = "gothenburg";
        String vehicleType = "Car";
        LocalDateTime day1Timestamp = LocalDateTime.of(2013, 9, 23, 7, 0);
        LocalDateTime day2Timestamp = LocalDateTime.of(2013, 9, 24, 7, 0);
        List<LocalDateTime> timestamps = List.of(day1Timestamp, day2Timestamp);

        when(feeSlotServiceImpl.getFeeForTime(any(), any())).thenReturn(18, 18);

        int totalFee = feeCalculatorService.calculateTotalFee(city, vehicleType, timestamps);

        assertEquals(36, totalFee);
    }

    @Test
    @DisplayName("Test fee calculation with multiple timestamps within the same hour")
    public void testCalculateTotalFeeWithinSameHour() {
        String city = "gothenburg";
        String vehicleType = "Car";
        LocalDateTime timestamp1 = LocalDateTime.of(2013, 9, 23, 7, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2013, 9, 23, 7, 30);
        List<LocalDateTime> timestamps = List.of(timestamp1, timestamp2);

        when(feeSlotServiceImpl.getFeeForTime(any(), any())).thenReturn(18, 13);

        int totalFee = feeCalculatorService.calculateTotalFee(city, vehicleType, timestamps);

        assertEquals(18, totalFee);
    }

    @Test
    @DisplayName("Test fee calculation with max daily fee cap")
    public void testCalculateTotalFeeWithMaxDailyFeeCap() {
        String city = "gothenburg";
        String vehicleType = "Car";
        LocalDateTime timestamp1 = LocalDateTime.of(2013, 9, 23, 7, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2013, 9, 23, 8, 0);
        LocalDateTime timestamp3 = LocalDateTime.of(2013, 9, 23, 9, 0);
        LocalDateTime timestamp4 = LocalDateTime.of(2013, 9, 23, 16, 0);
        List<LocalDateTime> timestamps = List.of(timestamp1, timestamp2, timestamp3, timestamp4);

        when(feeSlotServiceImpl.getFeeForTime(any(), any())).thenReturn(18, 18, 18, 18);

        int totalFee = feeCalculatorService.calculateTotalFee(city, vehicleType, timestamps);

        assertEquals(60, totalFee);
    }

    @Test
    @DisplayName("Test fee calculation with future timestamps")
    public void testCalculateTotalFeeWithFutureTimestamps() {
        String city = "gothenburg";
        String vehicleType = "Car";
        LocalDateTime futureTimestamp = LocalDateTime.now().plusDays(1);
        List<LocalDateTime> timestamps = List.of(futureTimestamp);

        when(feeSlotServiceImpl.getFeeForTime(any(), any())).thenReturn(0);

        int totalFee = feeCalculatorService.calculateTotalFee(city, vehicleType, timestamps);

        assertEquals(0, totalFee);
    }

    @Test
    @DisplayName("Test fee calculation with mixed peak and non-peak hours")
    public void testCalculateTotalFeeWithMixedPeakAndNonPeakHours() {
        String city = "gothenburg";
        String vehicleType = "Car";
        LocalDateTime peakTime = LocalDateTime.of(2013, 9, 23, 8, 30);
        LocalDateTime nonPeakTime = LocalDateTime.of(2013, 9, 23, 11, 30);
        List<LocalDateTime> timestamps = List.of(peakTime, nonPeakTime);

        when(feeSlotServiceImpl.getFeeForTime(any(), any())).thenReturn(18, 8);

        int totalFee = feeCalculatorService.calculateTotalFee(city, vehicleType, timestamps);

        assertEquals(26, totalFee);
    }
}
