package com.syed.assignment.congestion_tax_calculator.exception;

/**
 * Exception thrown when the congestion fee request is invalid.
 */
public class InvalidCongestionFeeRequestException extends RuntimeException {
    public InvalidCongestionFeeRequestException(String message) {
        super(message);
    }
}
