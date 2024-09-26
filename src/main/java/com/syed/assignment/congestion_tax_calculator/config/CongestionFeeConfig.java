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

import static com.syed.assignment.congestion_tax_calculator.constants.CongestionTaxConstants.GOTHENBURG;

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
        String defaultCity = congestionTaxConfig.getDefaultCity();

        Map<String, CongestionFeeStrategy> strategyMap = congestionTaxConfig.getCities().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> getStrategyOrDefault(entry.getValue(), defaultCity)
                ));

        return new CongestionFeeContext(strategyMap);
    }

    /**
     * Helper method to get the strategy based on the city name.
     * If the strategy is not found for the city, it will automatically return the strategy for the default city.
     * Here we are defaulting if cities config is missing then we still need strategy for execution
     * so it goes to default city, if even that is missing then exception is thrown
     * @param strategyName the name of the strategy as configured in YAML
     * @param defaultCity the name of the default city to fall back to
     * @return CongestionFeeStrategy - the fee calculation strategy for the city or default city.
     */
    private CongestionFeeStrategy getStrategyOrDefault(String strategyName, String defaultCity) {
        return getStrategy(strategyName != null ? strategyName : defaultCity);
    }

    /**
     * Helper method to get strategy based on the city name.
     *
     * @param strategyName the name of the strategy
     * @return CongestionFeeStrategy - the fee calculation strategy for the city.
     */
    private CongestionFeeStrategy getStrategy(String strategyName) {
        return switch (strategyName) {
            case GOTHENBURG -> new GothenburgCongestionFeeStrategy(feeCalculatorService);
            // TODO: We can add other city here
            default -> throw new IllegalStateException("Unexpected value: " + strategyName);
        };
    }
}
