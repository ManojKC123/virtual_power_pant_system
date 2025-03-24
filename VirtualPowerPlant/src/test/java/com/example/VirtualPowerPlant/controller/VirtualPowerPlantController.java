package com.example.VirtualPowerPlant.controller;



import static org.mockito.Mockito.*;
import com.example.VirtualPowerPlant.dto.BatteryDTO;
import com.example.VirtualPowerPlant.service.BatteryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VirtualPowerPlantControllerTest {

    @Mock
    private BatteryService batteryService; // Mock the service

    @InjectMocks
    private VirtualPowerPlantController virtualPowerPlantController; // Inject mocks into the controller

    private MockMvc mockMvc; // MockMvc for sending HTTP requests

    private BatteryDTO batteryDTO1;
    private BatteryDTO batteryDTO2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(virtualPowerPlantController).build(); // Build MockMvc

        batteryDTO1 = new BatteryDTO();
        batteryDTO1.setName("Test Battery 1");
        batteryDTO1.setCapacity(500);
        batteryDTO1.setPostcode(400);

        batteryDTO2 = new BatteryDTO();
        batteryDTO2.setName("Test Battery 2");
        batteryDTO2.setCapacity(600);
        batteryDTO2.setPostcode(500);
    }

    @Test
    public void testAddBattery() throws Exception {
        // Mock the behavior of the batteryService.save() method
        when(batteryService.save(any(BatteryDTO.class))).thenReturn(batteryDTO1);

        // Perform a POST request and verify the response
        mockMvc.perform(post("/batteries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Battery 1\",\"capacity\":500,\"postcode\":400}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Battery 1"))
                .andExpect(jsonPath("$.capacity").value(500))
                .andExpect(jsonPath("$.postcode").value(400));

        // Verify that batteryService.save() was called once
        verify(batteryService, times(1)).save(any(BatteryDTO.class));
    }

    @Test
    public void testGetAllBatteries() throws Exception {
        // Mock the behavior of the batteryService.getAll() method
        List<BatteryDTO> batteryList = List.of(batteryDTO1, batteryDTO2);
        when(batteryService.getAll()).thenReturn(batteryList);

        // Perform a GET request and verify the response
        mockMvc.perform(get("/batteries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Battery 1"))
                .andExpect(jsonPath("$[0].capacity").value(500))
                .andExpect(jsonPath("$[0].postcode").value(400))
                .andExpect(jsonPath("$[1].name").value("Test Battery 2"))
                .andExpect(jsonPath("$[1].capacity").value(600))
                .andExpect(jsonPath("$[1].postcode").value(500));

        // Verify that batteryService.getAll() was called once
        verify(batteryService, times(1)).getAll();
    }
}

