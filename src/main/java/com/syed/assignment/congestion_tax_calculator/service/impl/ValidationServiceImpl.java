package com.syed.assignment.congestion_tax_calculator.service.impl;

import com.syed.assignment.congestion_tax_calculator.config.CongestionTaxConfig;
import com.syed.assignment.congestion_tax_calculator.exception.CityNotConfiguredException;
import com.syed.assignment.congestion_tax_calculator.exception.InvalidYearException;
import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeRequest;
import com.syed.assignment.congestion_tax_calculator.service.FeeSlotService;
import com.syed.assignment.congestion_tax_calculator.service.ValidationService;
import com.syed.assignment.congestion_tax_calculator.validator.ExemptDayValidator;
import com.syed.assignment.congestion_tax_calculator.validator.ValidationHandler;
import com.syed.assignment.congestion_tax_calculator.validator.VehicleExemptionValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Validation service that uses a chain of responsibility pattern to validate requests
 */
@Service
public class ValidationServiceImpl implements ValidationService {

    private final ValidationHandler validationChain;
    private final CongestionTaxConfig congestionTaxConfig;
    private final FeeSlotService feeSlotService;

    public ValidationServiceImpl(VehicleExemptionValidator vehicleExemptionValidator,
                                 ExemptDayValidator exemptDayValidator,
                                 CongestionTaxConfig congestionTaxConfig,
                                 FeeSlotService feeSlotService) {
        // Create the chain of validators
        vehicleExemptionValidator.linkWith(exemptDayValidator);
        this.validationChain = vehicleExemptionValidator;
        this.congestionTaxConfig = congestionTaxConfig;
        this.feeSlotService = feeSlotService;
    }

    /**
     * Validate the request by performing the chain of responsibility validation
     * and additional validations for the year and city configuration.
     *
     * @param request the congestion fee request
     * @return true if valid, false otherwise
     */
    public boolean validateRequest(CongestionFeeRequest request) {
        validateYear(request);
        validateCity(request);
        return validationChain.validate(request);
    }

    /**
     * Validates that the request's timestamps are within the allowed year.
     *
     * @param request the congestion fee request
     */
    private void validateYear(CongestionFeeRequest request) {
        if (request.timeStamps().stream().anyMatch(this::isInvalidYear)) {
            throw new InvalidYearException("Year " + request.timeStamps().get(0).getYear() +
                    " is not allowed for congestion fee calculation");
        }
    }

    /**
     * Checks if the year in the timestamp is differnt from the allowed year
     *
     * @param timestamp the local date-time timestamp
     * @return true if the year is not allowed, false otherwise
     */
    private boolean isInvalidYear(LocalDateTime timestamp) {
        return timestamp.getYear() != congestionTaxConfig.getAllowYear();
    }

    /**
     * Validates if the city in request is configured in the system.
     *
     * @param request the congestion fee request
     */
    private void validateCity(CongestionFeeRequest request) {
        if (!congestionTaxConfig.getCities().containsKey(request.city())) {
            throw new CityNotConfiguredException(request.city());
        }
    }
}
