package com.syed.assignment.congestion_tax_calculator.service.impl;

import com.syed.assignment.congestion_tax_calculator.config.CongestionTaxConfig;
import com.syed.assignment.congestion_tax_calculator.exception.CityNotConfiguredException;
import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeSlot;
import com.syed.assignment.congestion_tax_calculator.service.FeeSlotService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of FeeSlotService to manage fee slots.
 */
@Service
public class FeeSlotServiceImpl implements FeeSlotService {

    private final Map<String, List<CongestionFeeSlot>> feeSlotsByCity;

    // Inject CongestionTaxConfig that holds the feeSlots
    public FeeSlotServiceImpl(CongestionTaxConfig config) {
        // Initialize the feeSlots from the configuration
        this.feeSlotsByCity = config.getFeeSlots();
    }

    /**
     * method to check if the city is configured or not
     * @param city
     * @return
     */
    @Override
    public boolean isCityConfigured(String city) {
        return feeSlotsByCity.containsKey(city.toLowerCase());
    }

    /**
     * Method to get fee for given time
     * @param time
     * @param city
     * @return
     */
    @Override
    public int getFeeForTime(LocalTime time, String city) {
        return Optional.ofNullable(feeSlotsByCity.get(city.toLowerCase()))
            .orElseThrow(() -> new CityNotConfiguredException(city)).stream()
            .filter(slot -> isWithinTime(slot, time))
            .findFirst()
            .map(CongestionFeeSlot::fee)
            .orElse(0);
    }

    /**
     * validate if time is within the slot
     */
    private boolean isWithinTime(CongestionFeeSlot slot, LocalTime time) {
        return !time.isBefore(slot.startTime()) && !time.isAfter(slot.endTime());
    }
}