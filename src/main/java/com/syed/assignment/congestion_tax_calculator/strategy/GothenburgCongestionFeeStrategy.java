package com.syed.assignment.congestion_tax_calculator.strategy;

import com.syed.assignment.congestion_tax_calculator.service.FeeCalculatorService;

import java.time.LocalDateTime;
import java.util.List;

public class GothenburgCongestionFeeStrategy implements CongestionFeeStrategy {

    private final FeeCalculatorService feeCalculatorService;

    public GothenburgCongestionFeeStrategy(FeeCalculatorService feeCalculatorService){
        this.feeCalculatorService = feeCalculatorService;
    }

    /**
     * strategy for gothenburg invoking the feecalulator service
     * @param city
     * @param vehicleType
     * @param timeStamps
     * @return
     */
    @Override
    public int calculateFee(String city, String vehicleType, List<LocalDateTime> timeStamps) {
        return feeCalculatorService.calculateTotalFee(city, vehicleType, timeStamps);
    }
}
