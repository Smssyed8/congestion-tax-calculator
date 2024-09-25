package com.syed.assignment.congestion_tax_calculator.model;

/**
 * Model for exempt vehicles with vehicle type and whether it is exempt.
 */
public record ExemptVehicle(String vehicleType, boolean isExempt) {
}
