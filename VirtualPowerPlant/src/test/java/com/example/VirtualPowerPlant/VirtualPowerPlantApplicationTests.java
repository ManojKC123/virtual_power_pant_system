package com.example.VirtualPowerPlant;

import com.example.VirtualPowerPlant.controller.VirtualPowerPlantController;
import com.example.VirtualPowerPlant.service.BatteryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class VirtualPowerPlantApplicationTests {

	@Autowired
	private BatteryService batteryService;

	@Autowired
	private VirtualPowerPlantController batteryController;

	@Test
	void contextLoads() {
		// Assert that the batteryService and batteryController beans are injected
		assertThat(batteryService).isNotNull();
		assertThat(batteryController).isNotNull();
	}

}
