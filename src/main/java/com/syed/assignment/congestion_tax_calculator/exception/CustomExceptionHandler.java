package com.syed.assignment.congestion_tax_calculator.exception;

import com.syed.assignment.congestion_tax_calculator.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Global custom exception handler to catch all custom and general exceptions.
 * This handler uses MessageSource for localized error messages and provides detailed error information.
 * TODO move codes to constants
 */
@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    private final MessageSource messageSource;

    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(InvalidCongestionFeeRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequest(InvalidCongestionFeeRequestException ex, Locale locale) {
        String errorMessage = messageSource.getMessage("invalid.congestion.fee.request", null, locale);
        logError(ex);
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, "ERR001", errorMessage, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VehicleExemptionException.class)
    public ResponseEntity<ErrorResponse> handleVehicleExemption(VehicleExemptionException ex, Locale locale) {
        String errorMessage = messageSource.getMessage("vehicle.exempt", null, locale);
        logError(ex);
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, "ERR002", errorMessage, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CityNotConfiguredException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCityException(CityNotConfiguredException ex, Locale locale) {
        String errorMessage = messageSource.getMessage("city.not.configured", null, locale);
        logError(ex);
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, "ERR003", errorMessage, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, Locale locale) {
        String field = ((FieldError) ex.getBindingResult().getAllErrors().get(0)).getField();
        String errorMessage = messageSource.getMessage("validation.error", new Object[]{field}, locale);
        logError(ex);
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, "ERR004", errorMessage, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex, Locale locale) {
        String errorMessage = messageSource.getMessage("general.error", null, locale);
        logError(ex);
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "ERR005", errorMessage, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidYearException.class)
    public ResponseEntity<ErrorResponse> handleInvalidYearException(InvalidYearException ex, Locale locale) {
        String errorMessage = messageSource.getMessage("invalid.year", null, locale);
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, "ERR006", errorMessage, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Helper method to create a error response
    private ErrorResponse createErrorResponse(HttpStatus status, String errorCode, String message, String debugMessage) {
        return new ErrorResponse(LocalDateTime.now(), status.value(), errorCode, message, debugMessage);
    }

    // Log the error
    private void logError(Exception ex) {
        logger.error("Exception occurred: {}", ex.getMessage(), ex);
    }
}
