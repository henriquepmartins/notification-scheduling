package com.henrique.notificationscheduling;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.henrique.notificationscheduling.application.SchedulingRequest;
import com.henrique.notificationscheduling.application.SchedulingResponse;
import com.henrique.notificationscheduling.application.SchedulingService;
import com.henrique.notificationscheduling.infrastructure.SchedulingController;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SchedulingControllerTest {

    @Mock
    SchedulingService schedulingService;

    @InjectMocks
    SchedulingController schedulingController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SchedulingRequest schedulingRequest;
    private SchedulingResponse schedulingResponse;

    @BeforeEach
    void setUp() {
        schedulingRequest = new SchedulingRequest(
            "email@gmail.com",
            "1234567890",
            "Hello, how are you?",
            LocalDateTime.parse("2025-01-01T10:00:00")
        );
        schedulingResponse = new SchedulingResponse(
            1L,
            "email@gmail.com",
            "1234567890",
            "Hello, how are you?",
            LocalDateTime.parse("2025-01-01T10:00:00")
        );

        mockMvc = MockMvcBuilders.standaloneSetup(schedulingController).build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void mightCreateScheduling() throws Exception {
        when(schedulingService.saveScheduling(schedulingRequest)).thenReturn(schedulingResponse);

        mockMvc.perform(post("/agendamento")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(schedulingRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.recipientMail").value("email@gmail.com"))
        .andExpect(jsonPath("$.recipientName").value("John Doe"))
        .andExpect(jsonPath("$.recipientPhone").value("1234567890"))
        .andExpect(jsonPath("$.sentDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.scheduleDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.modifiedDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.message").value("Hello, how are you?"))
        .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void mightNotCreateScheduling() throws Exception {
        when(schedulingService.saveScheduling(schedulingRequest)).thenReturn(null);

        mockMvc.perform(post("/agendamento")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(schedulingRequest)))
        .andExpect(status().isBadRequest());
    }

    @Test
    void mightGetSchedulingById() throws Exception {
        when(schedulingService.getSchedulingById(1L)).thenReturn(schedulingResponse);

        mockMvc.perform(get("/agendamento/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.recipientMail").value("email@gmail.com"))
        .andExpect(jsonPath("$.recipientName").value("John Doe"))
        .andExpect(jsonPath("$.recipientPhone").value("1234567890"))
        .andExpect(jsonPath("$.sentDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.scheduleDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.modifiedDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.message").value("Hello, how are you?"))
        .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void mightGetAllSchedulings() throws Exception {

        when(schedulingService.getAllScheduling()).thenReturn(List.of(schedulingResponse));

        mockMvc.perform(get("/agendamento"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.recipientMail").value("email@gmail.com"))
        .andExpect(jsonPath("$.recipientName").value("John Doe"))
        .andExpect(jsonPath("$.recipientPhone").value("1234567890"))
        .andExpect(jsonPath("$.sentDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.scheduleDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.modifiedDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.message").value("Hello, how are you?"))
        .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }
    
    @Test
    void mightCancelScheduling() throws Exception {
        when(schedulingService.cancelScheduling(1L)).thenReturn(schedulingResponse);

        mockMvc.perform(delete("/agendamento/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(schedulingRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.recipientMail").value("email@gmail.com"))
        .andExpect(jsonPath("$.recipientName").value("John Doe"))
        .andExpect(jsonPath("$.recipientPhone").value("1234567890"))
        .andExpect(jsonPath("$.sentDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.scheduleDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.modifiedDate").value("2025-01-01T10:00:00"))
        .andExpect(jsonPath("$.message").value("Hello, how are you?"))
        .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
}
