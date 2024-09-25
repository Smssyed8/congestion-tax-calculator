package com.syed.assignment.congestion_tax_calculator.config;

import com.syed.assignment.congestion_tax_calculator.constants.CongestionTaxConstants;
import com.syed.assignment.congestion_tax_calculator.exception.CityNotConfiguredException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * CongestionTaxConfig holds the configuration values for the application.
 *
 * It pulls data from the YAML file, like fee slots, exempt vehicles, and exempt dates,
 * and makes them available throughout the application.
 * Can be improvised by segregating later
 */
@Configuration
@ConfigurationProperties(prefix = "congestion-tax")
@Getter
@Setter
@Import({GothenburgCongestionConfig.class})
public class CongestionTaxConfig {

    private Integer maxFeePerDay;
    private Integer allowYear;
    private String defaultCity;
    private Map<String, String> cities;

    private final GothenburgCongestionConfig gothenburgCongestionConfig;

    private Map<String, CityConfig> cityConfigMap = new HashMap<>();

    public CongestionTaxConfig(GothenburgCongestionConfig gothenburgCongestionConfig) {
        this.gothenburgCongestionConfig = gothenburgCongestionConfig;
        this.cityConfigMap.put(CongestionTaxConstants.GOTHENBURG, gothenburgCongestionConfig);
    }

    /**
     * Here we are not defaulting fallback but throwing exception as user is expected city A
     * but we can default to city B config
     *
     * @param city
     * @return
     */
    public CityConfig getCityConfig(String city) {
        return Optional.ofNullable(cityConfigMap.get(city.toLowerCase()))
                .orElseThrow(() -> new CityNotConfiguredException("Configuration for the requested city: " + city + " is not available."));
    }
}
