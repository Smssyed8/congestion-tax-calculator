package com.syed.assignment.congestion_tax_calculator.service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for calculating congestion fees.
 */
public interface FeeCalculatorService {

    int calculateTotalFee(String city, String vehicleType, List<LocalDateTime> timeStamps);
}
