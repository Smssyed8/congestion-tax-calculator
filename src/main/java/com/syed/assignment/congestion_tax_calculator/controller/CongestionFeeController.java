package com.syed.assignment.congestion_tax_calculator.controller;

import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeRequest;
import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeResponse;
import com.syed.assignment.congestion_tax_calculator.service.CongestionService;
import com.syed.assignment.congestion_tax_calculator.utils.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling congestion fee calculation requests.
 */
@RestController
@RequestMapping("v1/congestion-fee")
public class CongestionFeeController {

    private final CongestionService congestionService;
    private static final Logger logger = LoggerFactory.getLogger(CongestionFeeController.class);

    public CongestionFeeController(CongestionService congestionService) {
        this.congestionService = congestionService;
    }

    /**
     * Endpoint to calculate congestion fee.
     *
     * @param request the fee calculation request
     * @return the calculated congestion fee
     */
    @Operation(summary = "Calculate congestion fee", description = "Calculates the congestion fee based on the provided request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful calculation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/calculate")
    public ResponseEntity<CongestionFeeResponse> calculateFee(@Valid @RequestBody CongestionFeeRequest request) {
        logger.info("Received request to calculate fee for city: {}, vehicleType: {}, timeStamps: {}",
                request.city(), request.vehicleType(), request.timeStamps());

        CongestionFeeResponse response = congestionService.calculateCongestionFee(request);

        return ResponseUtil.success(response);
    }
}
