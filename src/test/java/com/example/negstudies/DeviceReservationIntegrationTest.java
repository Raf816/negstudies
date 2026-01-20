package com.example.negstudies;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DeviceReservationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUpdateAndDeleteDevice() throws Exception {
        String createPayload = objectMapper.writeValueAsString(Map.of(
                "name", "MacBook Pro 14",
                "serialNumber", "MBP-TEST-001",
                "model", "Apple M3 Pro",
                "status", "AVAILABLE"));

        String createResponse = mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("MacBook Pro 14")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode createdDevice = objectMapper.readTree(createResponse);
        long deviceId = createdDevice.get("id").asLong();

        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        String updatePayload = objectMapper.writeValueAsString(Map.of(
                "name", "MacBook Pro 14 - Updated",
                "serialNumber", "MBP-TEST-001",
                "model", "Apple M3 Pro",
                "status", "MAINTENANCE"));

        mockMvc.perform(put("/api/devices/{id}", deviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("MAINTENANCE")));

        mockMvc.perform(delete("/api/devices/{id}", deviceId))
                .andExpect(status().isNoContent());
    }

    @Test
    void reservationConflictAndCancelFlow() throws Exception {
        String createPayload = objectMapper.writeValueAsString(Map.of(
                "name", "iPad Pro",
                "serialNumber", "IPAD-TEST-001",
                "model", "Apple M2",
                "status", "AVAILABLE"));

        String createResponse = mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        long deviceId = objectMapper.readTree(createResponse).get("id").asLong();
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(2);

        String reservationPayload = objectMapper.writeValueAsString(Map.of(
                "reservedBy", "Ava Reynolds",
                "startDate", startDate.toString(),
                "endDate", endDate.toString(),
                "purpose", "QA regression"));

        String reservationResponse = mockMvc.perform(post("/api/devices/{id}/reservations", deviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.deviceId", is((int) deviceId)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(post("/api/devices/{id}/reservations", deviceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationPayload))
                .andExpect(status().isConflict());

        long reservationId = objectMapper.readTree(reservationResponse).get("id").asLong();

        mockMvc.perform(patch("/api/reservations/{id}/cancel", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("CANCELLED")));
    }
}