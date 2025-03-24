package com.example.VirtualPowerPlant.service;

import com.example.VirtualPowerPlant.dto.BatteryDTO;
import com.example.VirtualPowerPlant.entities.Battery;
import com.example.VirtualPowerPlant.repositories.BatteryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class BatteryServiceTest {

    @Mock
    private BatteryRepository batteryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BatteryImp batteryService;

    private BatteryDTO batteryDTO;
    private Battery battery;

    @BeforeEach
    public void setUp() {
        // Initialize your mock data for testing
        batteryDTO = new BatteryDTO();
        batteryDTO.setName("Cannington");
        batteryDTO.setPostcode(6107);
        batteryDTO.setCapacity(13500);

        battery = new Battery();
        battery.setId(1L);
        battery.setName("Cannington");
        battery.setPostcode("6107");
        battery.setCapacity(13500L);
    }

    @Test
    public void testSaveBattery() {
        // Arrange
        when(batteryRepository.existsByName(batteryDTO.getName())).thenReturn(false);
        when(modelMapper.map(batteryDTO, Battery.class)).thenReturn(battery);
        when(batteryRepository.save(battery)).thenReturn(battery);
        when(modelMapper.map(battery, BatteryDTO.class)).thenReturn(batteryDTO);

        // Act
        BatteryDTO result = batteryService.save(batteryDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Cannington", result.getName());
        assertEquals(6107, result.getPostcode());
        assertEquals(13500, result.getCapacity());
    }

    @Test
    public void testGetAllBatteries() {
        // Arrange
        List<Battery> batteryList = Arrays.asList(battery);
        when(batteryRepository.findAll()).thenReturn(batteryList);
        when(modelMapper.map(battery, BatteryDTO.class)).thenReturn(batteryDTO);

        // Act
        List<BatteryDTO> result = batteryService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cannington", result.get(0).getName());
    }

    @Test
    public void testFindByPostcodeAndCapacityRange() {
        // Arrange
        List<Battery> batteryList = Arrays.asList(battery);
        when(batteryRepository.findByPostcodeAndCapacityRange(6000, 6500, 12000, 20000)).thenReturn(batteryList);
        when(modelMapper.map(battery, BatteryDTO.class)).thenReturn(batteryDTO);

        // Act
        List<BatteryDTO> result = batteryService.findByPostcodeAndCapacityRange(6000, 6500, 12000, 20000);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cannington", result.get(0).getName());
    }
}