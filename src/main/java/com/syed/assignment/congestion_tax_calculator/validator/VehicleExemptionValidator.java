package com.syed.assignment.congestion_tax_calculator.validator;

import com.syed.assignment.congestion_tax_calculator.config.CongestionTaxConfig;
import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Validator to check if a vehicle is exempt from congestion fees.
 * The logic that was previously in VehicleExemptionService is now placed directly here
 */
@Component
@Primary
public class VehicleExemptionValidator extends ValidationHandler {

    private final CongestionTaxConfig congestionTaxConfig;

    public VehicleExemptionValidator(CongestionTaxConfig congestionTaxConfig) {
        this.congestionTaxConfig = congestionTaxConfig;
    }


    @Override
    public boolean doValidate(CongestionFeeRequest request) {
        return !isVehicleExempt(request.vehicleType());
    }

    /**
     * Checks if the vehicle type is exempt from congestion fees.
     * true if the vehicle is exempt, false otherwise
     * @param vehicleType
     * @return
     */
    private boolean isVehicleExempt(String vehicleType) {
        return congestionTaxConfig.getExemptVehicles().stream()
                .anyMatch(vehicle -> vehicle.vehicleType().equalsIgnoreCase(vehicleType) && vehicle.isExempt());
    }
}
