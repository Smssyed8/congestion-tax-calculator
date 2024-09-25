package com.syed.assignment.congestion_tax_calculator.model;

import java.util.List;

/**
 * This holds the exempt days for a particular year and month.
 */
public record ExemptDay(int year, int month, List<Integer> days) {
}
