package com.syed.assignment.congestion_tax_calculator.service;

import java.time.LocalTime;

/**
 * Interface for managing fee slots for cities.
 */
public interface FeeSlotService {

    boolean isCityConfigured(String city);

    int getFeeForTime(LocalTime time, String city);
}
