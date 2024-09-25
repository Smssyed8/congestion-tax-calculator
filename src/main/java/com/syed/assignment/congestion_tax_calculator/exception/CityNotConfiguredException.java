package com.syed.assignment.congestion_tax_calculator.exception;

/**
 * Exception thrown when invalid city is sent
 */
public class CityNotConfiguredException extends RuntimeException {
    public CityNotConfiguredException(String message) {
        super(message);
    }
}