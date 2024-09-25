package com.syed.assignment.congestion_tax_calculator.service.impl;

import com.syed.assignment.congestion_tax_calculator.constants.CongestionTaxConstants;
import com.syed.assignment.congestion_tax_calculator.service.FeeCalculatorService;
import com.syed.assignment.congestion_tax_calculator.service.FeeSlotService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Implementation of FeeCalculatorService to calculate the congestion tax fees
 * Kept this only for calculation purpose.
 */
@Service
@RequiredArgsConstructor
public class FeeCalculatorServiceImpl implements FeeCalculatorService {

    private final FeeSlotService feeSlotService;
    private final MeterRegistry meterRegistry;

    /**
     * METHOD To calculate total fee for the given params below
     * @param city
     * @param vehicleType
     * @param timeStamps
     * @return
     */
    @Override
    public int calculateTotalFee(String city, String vehicleType, List<LocalDateTime> timeStamps) {
        return timeStamps.stream()
                .collect(Collectors.groupingBy(LocalDateTime::toLocalDate))
                .values().stream()
                .mapToInt(localDateTimes -> calculateDailyFee(localDateTimes, city))
                .sum();
    }

    /**
     * Method focuses on calculating the day total fee, considering 60 min window and highest within the 60 min
     * Also considers total max 60 kr
     * @param timeStamps
     * @param city
     * @return
     */
    private int calculateDailyFee(List<LocalDateTime> timeStamps, String city) {
        var sortedTimestamps = timeStamps.stream().sorted().toList();
        var totalFee = new AtomicInteger(0);
        var highestFeeInWindow = new AtomicInteger(feeSlotService.getFeeForTime(sortedTimestamps.get(0).toLocalTime(), city));
        sortedTimestamps.stream().reduce((prev, next) -> {
            var currentFee = feeSlotService.getFeeForTime(next.toLocalTime(), city);
            if (isWithinOneHourWindow(prev, next)) {
                highestFeeInWindow.set(Math.max(highestFeeInWindow.get(), currentFee));
            } else {
                totalFee.addAndGet(highestFeeInWindow.get());
                highestFeeInWindow.set(currentFee);
            }
            return next;
        });

        totalFee.addAndGet(highestFeeInWindow.get());
        return Math.min(totalFee.get(), CongestionTaxConstants.MAX_DAILY_FEE);  // Max fee per day is 60 SEK
    }

    /**
     * Method to check if the previous and current time is within 1 hour window
     * @param prev
     * @param next
     * @return
     */
    private boolean isWithinOneHourWindow(LocalDateTime prev, LocalDateTime next) {
        return prev.plusHours(1).isAfter(next);
    }
}
