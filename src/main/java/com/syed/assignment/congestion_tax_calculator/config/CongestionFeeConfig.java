package com.syed.assignment.congestion_tax_calculator.config;

import com.syed.assignment.congestion_tax_calculator.service.FeeCalculatorService;
import com.syed.assignment.congestion_tax_calculator.strategy.CongestionFeeStrategy;
import com.syed.assignment.congestion_tax_calculator.strategy.GothenburgCongestionFeeStrategy;
import com.syed.assignment.congestion_tax_calculator.utils.CongestionFeeContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

import static com.syed.assignment.congestion_tax_calculator.constants.CongestionTaxConstants.GOTHENBURG_STRATEGY;

/**
 * Configuration class for creating CongestionFeeContext and managing strategy beans.
 *
 * This class is responsible for loading fee strategies based on city names and providing them
 * to the application at runtime. If the city-specific strategy isn't found, it defaults to Gothenburg.
 */
@Configuration
@RequiredArgsConstructor
public class CongestionFeeConfig {

    private final CongestionTaxConfig congestionTaxConfig;
    private final FeeCalculatorService feeCalculatorService;

    /**
     * Creates the CongestionFeeContext by mapping cities to their corresponding strategies from YAML config.
     * At the momemt have kept only gothenburg. Can add other cities as required
     *
     * @return CongestionFeeContext - the context holding city-strategy mappings.
     */
    @Bean
    public CongestionFeeContext congestionFeeContext() {
        Map<String, CongestionFeeStrategy> strategyMap = congestionTaxConfig.getCities().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> createStrategy(entry.getValue()) //  it loads the strategy dynamically
                ));

        return new CongestionFeeContext(strategyMap);
    }

    /**
     * Creates a strategy based on the city name.
     * FOr now supports Gothenburg but can be extended to other cities.
     *
     * @param strategyName the name of the strategy as configured in YAML
     * @return CongestionFeeStrategy - the fee calculation strategy for the city.
     */
    private CongestionFeeStrategy createStrategy(String strategyName) {
        return switch (strategyName) {
            case GOTHENBURG_STRATEGY -> new GothenburgCongestionFeeStrategy(feeCalculatorService);
            // TODO: Add other city strategies here as needed, e.g., Stockholm.
            default -> throw new IllegalArgumentException("Unknown strategy: " + strategyName);
        };
    }
}
