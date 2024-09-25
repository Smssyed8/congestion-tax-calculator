package com.syed.assignment.congestion_tax_calculator.validator;

import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeRequest;

/**
 * This abstract class is added for chain of responsibility pattern
 * ensuring that the request passes through a series of validation handlers and
 * each handler is responsible for one validation
 * This also helps in decoupling the validations and add more as required
 * Can be improved by not having linkwith, but direct list of handlers
 */
public abstract class ValidationHandler {

    private ValidationHandler nextHandler;

    public ValidationHandler linkWith(ValidationHandler nextHandler){
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public boolean validate(CongestionFeeRequest request) {
        if (!doValidate(request)) {
            return false;  // Stop the chain if the current validation fails
        }

        // If there's a next handler, pass request to it, otherwise return true
        return nextHandler == null || nextHandler.validate(request);
    }

    /**
     * method implemented by validators.
     * @param request
     * @return
     */
    public abstract boolean doValidate(CongestionFeeRequest request);

}
