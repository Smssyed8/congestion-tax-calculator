package com.syed.assignment.congestion_tax_calculator.service;

import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeRequest;
import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeResponse;

/**
 * Service for selecting the appropriate strategy and calculating the congestion fee.
 */
public interface CongestionService {

    /**
     * Calculate the congestion fee using the appropriate strategy based on the city.
     *
     * @param request
     * @return
     */
    CongestionFeeResponse calculateCongestionFee(CongestionFeeRequest request);
}
