package com.syed.assignment.congestion_tax_calculator.utils;

import com.syed.assignment.congestion_tax_calculator.strategy.CongestionFeeStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * This context will decide which strategy to apply
 */
public class CongestionFeeContext {

    private static final Logger logger = LoggerFactory.getLogger(CongestionFeeContext.class);

    private final Map<String, CongestionFeeStrategy> strategies;

    public CongestionFeeContext(Map<String, CongestionFeeStrategy> strategies) {
        this.strategies = strategies;
    }

    /**
     * This context method executes the strategy selected
     *
     * @param city
     * @param vehicleType
     * @param timeStamps
     * @return
     */
    public int executeStrategy(String city, String vehicleType, List<LocalDateTime> timeStamps) {
        String cityLowerCase = city.toLowerCase();
        CongestionFeeStrategy feeStrategy = strategies.getOrDefault(cityLowerCase, strategies.get("default"));

        if (feeStrategy == null) {
            logger.error("No strategy found for city: {} and no default strategy is configured", city);
            throw new IllegalArgumentException("No strategy configured for the given city: " + city);
        }

        if (!strategies.containsKey(cityLowerCase)) {
            logger.info("Using default strategy for city: {}", city);
        }

        return feeStrategy.calculateFee(city, vehicleType, timeStamps);
    }
}
