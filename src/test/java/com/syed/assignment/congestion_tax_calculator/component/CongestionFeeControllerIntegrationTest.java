package com.syed.assignment.congestion_tax_calculator.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.syed.assignment.congestion_tax_calculator.model.CongestionFeeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:congestion-config.yml")
@ImportAutoConfiguration(exclude = {
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class
})
public class CongestionFeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    @Test
    @DisplayName("Test calculating fee via API")
    public void testCalculateFeeAPI() throws Exception {
        CongestionFeeRequest request = new CongestionFeeRequest(
                "gothenburg", "Car", List.of(LocalDateTime.of(2013, 9, 23, 7, 0))
        );

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFee").value(18));
    }

    @Test
    @DisplayName("Test exempt vehicle (Motorbike) via API")
    public void testExemptVehicleAPI() throws Exception {
        CongestionFeeRequest request = new CongestionFeeRequest(
                "gothenburg", "Motorbike", List.of(LocalDateTime.of(2013, 9, 23, 7, 0))
        );

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFee").value(0));
    }

    @Test
    @DisplayName("Test invalid city configuration via API")
    public void testInvalidCityAPI() throws Exception {
        CongestionFeeRequest request = new CongestionFeeRequest(
                "invalidCity", "Car", List.of(LocalDateTime.of(2013, 9, 23, 7, 0))
        );

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test multiple timestamps fee calculation via API")
    public void testMultipleTimestampsAPI() throws Exception {
        CongestionFeeRequest request = new CongestionFeeRequest(
                "gothenburg", "Car", List.of(
                LocalDateTime.of(2013, 9, 23, 7, 0),
                LocalDateTime.of(2013, 9, 23, 15, 0)
        )
        );

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFee").value(31));
    }

    @Test
    @DisplayName("Test maximum daily fee cap via API")
    public void testMaxDailyFeeCapAPI() throws Exception {
        CongestionFeeRequest request = new CongestionFeeRequest(
                "gothenburg", "Car", List.of(
                LocalDateTime.of(2013, 9, 23, 7, 0),
                LocalDateTime.of(2013, 9, 23, 8, 15),
                LocalDateTime.of(2013, 9, 23, 14, 0),
                LocalDateTime.of(2013, 9, 23, 15, 10),
                LocalDateTime.of(2013, 9, 23, 17, 0)
        )
        );

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFee").value(60));
    }

    @Test
    @DisplayName("Test future timestamps via API")
    public void testFutureTimestampsAPI() throws Exception {
        CongestionFeeRequest request = new CongestionFeeRequest(
                "gothenburg", "Car", List.of(LocalDateTime.now().plusDays(1))
        );

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test no fee charged on exempt day")
    public void testExemptDayFeeZero() throws Exception {
        CongestionFeeRequest request = new CongestionFeeRequest(
                "gothenburg", "Car", List.of(LocalDateTime.of(2013, 7, 1, 7, 0))
        );

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFee").value(0));
    }

    @Test
    @DisplayName("Test maximum fee within one hour should be picked")
    public void testMaxFeeWithinOneHour() throws Exception {
        CongestionFeeRequest request = new CongestionFeeRequest(
                "gothenburg", "Car", List.of(
                LocalDateTime.of(2013, 9, 23, 7, 0),
                LocalDateTime.of(2013, 9, 23, 7, 30)
        ));

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalFee").value(18));
    }

    @Test
    @DisplayName("Test fee calculation for an invalid year (non-2013)")
    public void testInvalidYear() throws Exception {
        CongestionFeeRequest request = new CongestionFeeRequest(
                "gothenburg", "Car", List.of(LocalDateTime.of(2014, 9, 23, 7, 0))
        );

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Year is not allowed for congestion fee calculation"));
    }

    @ParameterizedTest
    @MethodSource("invalidRequestProvider")
    @DisplayName("Test missing or invalid fields in request via API")
    public void testInvalidRequestAPI(CongestionFeeRequest request, String fieldName) throws Exception {
        String expectedMessage = messageSource.getMessage("validation.error", new Object[]{fieldName}, LocaleContextHolder.getLocale());

        mockMvc.perform(post("/v1/congestion-fee/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    private static Stream<Object[]> invalidRequestProvider() {
        return Stream.of(
                new Object[]{new CongestionFeeRequest(null, "Car", List.of(LocalDateTime.of(2013, 9, 23, 7, 0))), "city"},
                new Object[]{new CongestionFeeRequest("gothenburg", null, List.of(LocalDateTime.of(2013, 9, 23, 7, 0))), "vehicleType"},
                new Object[]{new CongestionFeeRequest("gothenburg", "Car", null), "timeStamps"}
        );
    }
}
