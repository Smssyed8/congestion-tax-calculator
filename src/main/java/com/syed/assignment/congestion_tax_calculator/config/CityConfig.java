package com.syed.assignment.congestion_tax_calculator.config;

import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeSlot;
import com.syed.assignment.congestion_tax_calculator.model.ExemptDay;
import com.syed.assignment.congestion_tax_calculator.model.ExemptVehicle;

import java.util.List;

public interface CityConfig {
    List<CongestionFeeSlot> getFeeSlots();

    List<ExemptDay> getExemptDates();

    List<ExemptVehicle> getExemptVehicles();
}