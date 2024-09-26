package com.syed.assignment.congestion_tax_calculator.config;


import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeSlot;
import com.syed.assignment.congestion_tax_calculator.model.ExemptDay;
import com.syed.assignment.congestion_tax_calculator.model.ExemptVehicle;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "gothenburg")
@Getter
@Setter
public class GothenburgCongestionConfig implements CityConfig {
    private List<CongestionFeeSlot> feeSlots;
    private List<ExemptDay> exemptDates;
    private List<ExemptVehicle> exemptVehicles;
}