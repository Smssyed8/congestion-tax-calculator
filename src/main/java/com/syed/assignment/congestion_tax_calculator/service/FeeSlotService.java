package com.syed.assignment.congestion_tax_calculator.service;

import java.time.LocalTime;

/**
 * Interface for managing fee slots for cities.
 */
public interface FeeSlotService {

    int getFeeForTime(LocalTime time, String city);
}
