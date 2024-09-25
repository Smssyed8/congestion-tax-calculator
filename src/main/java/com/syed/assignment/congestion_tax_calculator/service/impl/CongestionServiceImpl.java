package com.syed.assignment.congestion_tax_calculator.service.impl;

import com.syed.assignment.congestion_tax_calculator.config.CongestionTaxConfig;
import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeRequest;
import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeResponse;
import com.syed.assignment.congestion_tax_calculator.service.CongestionService;
import com.syed.assignment.congestion_tax_calculator.service.FeeSlotService;
import com.syed.assignment.congestion_tax_calculator.service.ValidationService;
import com.syed.assignment.congestion_tax_calculator.utils.CongestionFeeContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of CongestionService to handle validation, strategy selection
 */
@Service
@RequiredArgsConstructor
public class CongestionServiceImpl implements CongestionService {

    private final CongestionFeeContext feeContext;
    private final FeeSlotService feeSlotService;
    private final ValidationService validationService;
    private final CongestionTaxConfig config;

    /**
     * Method to validate the request and calculate the congestion fee.
     * The actual calculation logic is handled by the strategy loaded from context
     *
     * @param request the congestion fee request
     * @return the congestion fee response containing the total fee
     */
    @Override
    public CongestionFeeResponse calculateCongestionFee(CongestionFeeRequest request) {
        // Perform all request validations (year, city, exempt vehicles, etc.)
        if (!validationService.validateRequest(request)) {
            return new CongestionFeeResponse(0);  // Return 0 if validation fails
        }

        // Use the context to get the correct strategy and calculate the fee
        int totalFee = feeContext.executeStrategy(request.city(), request.vehicleType(), request.timeStamps());

        // Return the response containing the calculated total fee
        return new CongestionFeeResponse(totalFee);
    }
}
