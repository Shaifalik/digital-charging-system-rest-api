package me.dcs;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.dcs.controller.ChargeDetailRecordController;
import me.dcs.exception.InvalidCDRException;
import me.dcs.exception.NotFoundException;
import me.dcs.model.ChargeDetailRecordDTO;
import me.dcs.service.ChargeDetailRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static me.dcs.utils.ApplicationTestConstants.*;
import static me.dcs.utils.ApplicationConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ChargeDetailRecordController.class)
public class ChargeDetailRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChargeDetailRecordService cdrService;
    private ChargeDetailRecordDTO cdrDTO;


    @BeforeEach
    void setUp() {
        cdrDTO = ChargeDetailRecordDTO.builder()
                .vehicleIdentification(VEHICLE_IDENTIFICATION).
                        startTime(LocalDateTime.parse(START_TIME)).
                        endTime(LocalDateTime.parse(END_TIME)).cost(cost).build();
    }

    @Test
    void createCDR_ShouldReturnSuccess_WhenCDRIsValid() throws Exception {
        when(cdrService.save(any(ChargeDetailRecordDTO.class))).thenReturn(cdrDTO);
        mockMvc.perform(post(POST_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(
                        new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(cdrDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vehicleIdentification", is(VEHICLE_IDENTIFICATION)))
                .andExpect(jsonPath("$.startTime", is(START_TIME)))
                .andExpect(jsonPath("$.endTime", is(END_TIME)))
                .andExpect(jsonPath("$.cost", is(cost)));
    }

    @Test
    public void createCDR_ShouldReturnBadRequest_WhenVehicleIdentificationIsEmpty() throws Exception {
        cdrDTO.setVehicleIdentification("");

        mockMvc.perform(post(POST_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(
                        new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(cdrDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(VEHICLE_IDENTIFICATION_MUST_NOT_BE_NULL)));
    }

    @Test
    public void createCDR_ShouldReturnBadRequest_WhenCostIsNegative() throws Exception {
        cdrDTO.setCost(-1.0);

        mockMvc.perform(post(POST_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(
                        new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(cdrDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(COST_MUST_BE_POSITIVE)));
    }

    @Test
    public void createCDR_ShouldReturnBadRequest_WhenRecordStartTimeIsAfterEndTime() throws Exception {
        cdrDTO.setStartTime(LocalDateTime.parse(END_TIME));
        cdrDTO.setEndTime(LocalDateTime.parse(START_TIME));

        mockMvc.perform(post(POST_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(
                        new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(cdrDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(INCORRECT_TIME)));
    }


    @Test
    public void createCDR_ShouldReturnBadRequest_WhenNewRecordEndTimeIsAfterPreviousRecordStartTime() throws Exception {
        when(cdrService.save(any(ChargeDetailRecordDTO.class))).thenThrow(new InvalidCDRException(OUTDATED_RECORD));

        mockMvc.perform(post(POST_API)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(
                        new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .writeValueAsString(cdrDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(OUTDATED_RECORD)));
    }


    @Test
    void getCDR_ShouldReturnSuccess_WhenQueriedBySessionIdTest() throws Exception {
        when(cdrService.findBySessionId(SESSION_IDENTIFICATION)).thenReturn(cdrDTO);
        ResultActions response = mockMvc.perform(get(GET_API, SESSION_IDENTIFICATION));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleIdentification", is(cdrDTO.getVehicleIdentification())))
                .andExpect(jsonPath("$.startTime", is(String.valueOf(cdrDTO.getStartTime()))))
                .andExpect(jsonPath("$.endTime", is(String.valueOf(cdrDTO.getEndTime()))))
                .andExpect(jsonPath("$.cost", is(cdrDTO.getCost())));
    }

    @Test
    void getCDR_ShouldReturnNotFound_WhenQueriedByInvalidSessionIdTest() throws Exception {
        when(cdrService.findBySessionId(2L)).thenThrow(new NotFoundException(INVALID_SESSION_ID));
        ResultActions response = mockMvc.perform(get(GET_API, 2));
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(INVALID_SESSION_ID)));
    }

    @Test
    void getAllCDRs_ShouldReturnSuccess_WhenQueriedByCorrectVehicleIdTest() throws Exception {
        when(cdrService.findAllByVehicleId(cdrDTO.getVehicleIdentification())).thenReturn(List.of(cdrDTO));
        ResultActions response = mockMvc.perform(get(SEARCH_API, cdrDTO.getVehicleIdentification()));
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()",
                        is(1)));
    }

    @Test
    void getCDR_ShouldReturnNotFound_WhenQueriedByInvalidVehicleIdTest() throws Exception {
        when(cdrService.findAllByVehicleId("12345")).thenThrow(new NotFoundException(INVALID_VEHICLE_ID));
        ResultActions response = mockMvc.perform(get(SEARCH_API, "12345"));
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(INVALID_VEHICLE_ID)));
    }


}
