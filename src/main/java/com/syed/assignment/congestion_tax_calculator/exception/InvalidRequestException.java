package com.syed.assignment.congestion_tax_calculator.exception;

/**
 * General invalid request exception.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
