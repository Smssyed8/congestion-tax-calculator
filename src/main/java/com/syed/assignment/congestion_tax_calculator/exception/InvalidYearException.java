package com.syed.assignment.congestion_tax_calculator.exception;

/**
 * Exception thrown when a year other than 2013 is used in the request.
 */
public class InvalidYearException extends RuntimeException {
    public InvalidYearException(String message) {
        super(message);
    }
}