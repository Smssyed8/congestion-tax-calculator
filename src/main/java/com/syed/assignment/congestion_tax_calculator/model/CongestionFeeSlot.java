package com.syed.assignment.congestion_tax_calculator.model;

import java.time.LocalTime;

/**
 * Model that represents the fee slot with start and end time, and the fee amount.
 */
public record CongestionFeeSlot(LocalTime startTime, LocalTime endTime, int fee) {
}
