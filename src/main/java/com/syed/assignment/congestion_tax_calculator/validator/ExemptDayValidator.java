package com.syed.assignment.congestion_tax_calculator.validator;

import com.syed.assignment.congestion_tax_calculator.config.CongestionTaxConfig;
import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExemptDayValidator extends ValidationHandler {

    private final CongestionTaxConfig congestionTaxConfig;

    public ExemptDayValidator(CongestionTaxConfig congestionTaxConfig) {
        this.congestionTaxConfig = congestionTaxConfig;
    }

    @Override
    public boolean doValidate(CongestionFeeRequest request) {
        return request.timeStamps().stream().noneMatch(this::isExemptDay);
    }

    /**
     * check if the day is exempted, holiday or red day
     * @param date
     * @return
     */
    public boolean isExemptDay(LocalDateTime date){
        return congestionTaxConfig.getExemptDates().stream().anyMatch(exemptDay ->
                exemptDay.year() == date.getYear() &&
                exemptDay.month() == date.getMonthValue() &&
                        exemptDay.days().contains(date.getDayOfMonth())
        );
    }
}
