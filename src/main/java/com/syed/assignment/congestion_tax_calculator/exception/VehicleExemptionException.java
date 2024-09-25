package com.syed.assignment.congestion_tax_calculator.exception;

/**
 * Exception thrown when a vehicle is exempt from tax fee but was still processed
 */
public class VehicleExemptionException extends RuntimeException {
    public VehicleExemptionException(String message) {
        super(message);
    }
}
