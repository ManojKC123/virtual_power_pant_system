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

    private BatteryDTO batteryDTO1;
    private BatteryDTO batteryDTO2;
    private Battery battery1;
    private Battery battery2;

    @BeforeEach
    public void setUp() {
        // Initialize your mock data for testing
        batteryDTO1 = new BatteryDTO();
        batteryDTO1.setName("Cannington");
        batteryDTO1.setPostcode(6107);
        batteryDTO1.setCapacity(13500);

        batteryDTO2 = new BatteryDTO();
        batteryDTO2.setName("Victoria");
        batteryDTO2.setPostcode(6108);
        batteryDTO2.setCapacity(15000);

        battery1 = new Battery();
        battery1.setId(1L);
        battery1.setName("Cannington");
        battery1.setPostcode("6107");
        battery1.setCapacity(13500L);

        battery2 = new Battery();
        battery2.setId(2L);
        battery2.setName("Victoria");
        battery2.setPostcode("6108");
        battery2.setCapacity(15000L);
    }

    @Test
    public void testSaveBattery() {
        // Arrange
        when(batteryRepository.existsByName(batteryDTO1.getName())).thenReturn(false);
        when(modelMapper.map(batteryDTO1, Battery.class)).thenReturn(battery1);
        when(batteryRepository.save(battery1)).thenReturn(battery1);
        when(modelMapper.map(battery1, BatteryDTO.class)).thenReturn(batteryDTO1);

        // Act
        BatteryDTO result = batteryService.save(batteryDTO1);

        // Assert
        assertNotNull(result);
        assertEquals("Cannington", result.getName());
        assertEquals(6107, result.getPostcode());
        assertEquals(13500, result.getCapacity());
    }

    @Test
    public void testGetAllBatteries() {
        // Arrange
        List<Battery> batteryList = Arrays.asList(battery1,battery2);
        when(batteryRepository.findAll()).thenReturn(batteryList);
        when(modelMapper.map(battery1, BatteryDTO.class)).thenReturn(batteryDTO1);
        when(modelMapper.map(battery2, BatteryDTO.class)).thenReturn(batteryDTO2);

        // Act
        List<BatteryDTO> result = batteryService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cannington", result.get(0).getName());
        assertEquals("Victoria", result.get(1).getName());
    }

    @Test
    public void testFindByPostcodeAndCapacityRange() {
        // Arrange
        List<Battery> batteryList = Arrays.asList(battery1, battery2);
        when(batteryRepository.findByPostcodeAndCapacityRange(6000, 6500, 12000, 20000)).thenReturn(batteryList);
        when(modelMapper.map(battery1, BatteryDTO.class)).thenReturn(batteryDTO1);
        when(modelMapper.map(battery2, BatteryDTO.class)).thenReturn(batteryDTO2);

        // Act
        List<BatteryDTO> result = batteryService.findByPostcodeAndCapacityRange(6000, 6500, 12000, 20000);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cannington", result.get(0).getName());
        assertEquals("Victoria", result.get(1).getName());
    }

    @Test
    public void testGetTotalCapacity() {
        List<BatteryDTO> batteries = Arrays.asList(batteryDTO1, batteryDTO2);

        int totalCapacity = batteryService.getTotalCapacity(batteries);

        assertEquals(13500 + 15000, totalCapacity);
    }

    @Test
    public void testGetAverageCapacity() {
        List<BatteryDTO> batteries = Arrays.asList(batteryDTO1, batteryDTO2);

        double averageCapacity = batteryService.getAverageCapacity(batteries);

        assertEquals((13500 + 15000) / 2.0, averageCapacity);
    }

    @Test
    public void testGetAverageCapacity_EmptyList() {
        List<BatteryDTO> batteries = Arrays.asList();

        double averageCapacity = batteryService.getAverageCapacity(batteries);

        assertEquals(0.0, averageCapacity);
    }

    @Test
    public void testGetTotalCapacity_EmptyList() {
        List<BatteryDTO> batteries = Arrays.asList();

        int totalCapacity = batteryService.getTotalCapacity(batteries);

        assertEquals(0, totalCapacity);
    }
}