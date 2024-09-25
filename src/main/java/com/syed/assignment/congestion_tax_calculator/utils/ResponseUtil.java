package com.syed.assignment.congestion_tax_calculator.utils;

import org.springframework.http.ResponseEntity;

/**
 * Utility class for standard API responses.
 */
public class ResponseUtil {

    public static <T> ResponseEntity<T> success(T body) {
        return ResponseEntity.ok(body);
    }
}