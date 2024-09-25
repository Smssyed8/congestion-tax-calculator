package com.syed.assignment.congestion_tax_calculator.config;

import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeSlot;
import com.syed.assignment.congestion_tax_calculator.model.ExemptDay;
import com.syed.assignment.congestion_tax_calculator.model.ExemptVehicle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

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
public class CongestionTaxConfig {

    private Integer maxFeePerDay;
    private Integer allowYear;
    private String defaultCity;
    private Map<String, String> cities;
    private Map<String, List<CongestionFeeSlot>> feeSlots;
    private List<ExemptDay> exemptDates;
    private List<ExemptVehicle> exemptVehicles;

}
