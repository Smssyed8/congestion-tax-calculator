package com.syed.assignment.congestion_tax_calculator.strategy;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Adding strategy pattern for allowing expansion to other cities
 */
public interface CongestionFeeStrategy {
    int calculateFee(String city, String vehicleType, List<LocalDateTime> timeStamps);
}
