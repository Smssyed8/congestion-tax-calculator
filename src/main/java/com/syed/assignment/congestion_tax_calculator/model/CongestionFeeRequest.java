package com.syed.assignment.congestion_tax_calculator.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This is the request model for calculating the congestion fee
 * It holds city, vehicle type, and timestamps
 * Added validating annotations in the request
 * Can add more, custom annotations for example: for timestamp, can add annotation to chekc future and present
 */
public record CongestionFeeRequest(
        @NotNull(message = "City cannot be null")
        @NotBlank(message = "City cannot be empty")
        String city,

        @NotNull(message = "Vehicle type cannot be null")
        @NotBlank(message = "Vehicle type cannot be empty")
        String vehicleType,

        @NotNull(message = "Timestamps cannot be null")
        @Size(min = 1, message = "At least one timestamp must be provided")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        List<@PastOrPresent LocalDateTime> timeStamps
) {

}