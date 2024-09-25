package com.syed.assignment.congestion_tax_calculator.service;

import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeRequest;

/**
 * Interface for validating the congestion fee request.
 */
public interface ValidationService {

    boolean validateRequest(CongestionFeeRequest request);
}
