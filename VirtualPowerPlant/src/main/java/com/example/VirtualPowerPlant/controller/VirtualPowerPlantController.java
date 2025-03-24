package com.example.VirtualPowerPlant.controller;

import com.example.VirtualPowerPlant.batteryUtils.BatteryUtilities;
import com.example.VirtualPowerPlant.dto.BatteryDTO;
import com.example.VirtualPowerPlant.exception.GlobalExceptionHandler;
import com.example.VirtualPowerPlant.service.BatteryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/batteries")
@RequiredArgsConstructor
public class VirtualPowerPlantController {

    private static final Logger logger = LoggerFactory.getLogger(VirtualPowerPlantController.class);

    private final BatteryService batteryService;

    @PostMapping
    public ResponseEntity<?> addBattery(@Valid @RequestBody BatteryDTO batteryDTO, BindingResult result) {
        logger.info("Received request to add a battery: {}", batteryDTO.getName());

        if (result.hasErrors()) {
            Map<String, String> errors = result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            logger.error("Validation errors: {}", errors);
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            BatteryDTO savedBattery = batteryService.save(batteryDTO);
            logger.info("Successfully added battery with name: {}", batteryDTO.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBattery);
        } catch (IllegalArgumentException ex) {
            logger.error("Error while saving battery: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<BatteryDTO>> getAllBatteries() {
        logger.info("Fetching all batteries");
        return new ResponseEntity<>(batteryService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/getBatteryById/{id}")
    public ResponseEntity<BatteryDTO> getById(@PathVariable("id") Long id) {
        logger.info("Fetching battery with ID: {}", id);
        return ResponseEntity.ok(batteryService.getBatteryById(id));
    }

    @GetMapping("/range")
    public ResponseEntity<Map<String, Object>> getBatteriesInRange(
            @RequestParam String startPostCode,
            @RequestParam String endPostCode,
            @RequestParam(required = false) String startCapacity,
            @RequestParam(required = false) String endCapacity) {

        try {
            // Parse postcodes
            int startPostCodeInt = Integer.parseInt(startPostCode);
            int endPostCodeInt = Integer.parseInt(endPostCode);

            if (startPostCodeInt > endPostCodeInt) {
                logger.error("Start postcode {} is greater than end postcode {}", startPostCodeInt, endPostCodeInt);
                return GlobalExceptionHandler.badRequest("Start postcode must be less than or equal to end postcode");
            }

            // Parse capacities if provided, else set default values
            Integer startCapacityInt = null;
            Integer endCapacityInt = null;

            if (startCapacity != null && !startCapacity.trim().isEmpty()) {
                startCapacityInt = BatteryUtilities.parseCapacity(startCapacity, "Start capacity");
            }
            if (endCapacity != null && !endCapacity.trim().isEmpty()) {
                endCapacityInt = BatteryUtilities.parseCapacity(endCapacity, "End capacity");
            }

            // If startCapacity is provided and endCapacity is missing, default endCapacity to Integer.MAX_VALUE
            if (startCapacityInt != null && endCapacityInt == null) {
                endCapacityInt = Integer.MAX_VALUE; // No upper limit on capacity
            }

            // If endCapacity is provided and startCapacity is missing, default startCapacity to 0
            if (endCapacityInt != null && startCapacityInt == null) {
                startCapacityInt = 0; // No lower limit on capacity
            }

            // Check if start capacity is greater than end capacity after setting defaults
            if (startCapacityInt != null && startCapacityInt > endCapacityInt) {
                logger.error("Start capacity {} is greater than end capacity {}", startCapacityInt, endCapacityInt);
                return GlobalExceptionHandler.badRequest("Start capacity must be less than or equal to end capacity");
            }

            // Fetch data
            List<BatteryDTO> batteries = batteryService.findByPostcodeAndCapacityRange(
                    startPostCodeInt, endPostCodeInt, startCapacityInt, endCapacityInt);

            if (batteries.isEmpty()) {
                logger.info("No batteries found within the specified range");
                return ResponseEntity.ok(Map.of("message", "There are no batteries within the specified range"));
            }

            // Build response
            logger.info("Found {} batteries in the specified range", batteries.size());
            return ResponseEntity.ok(Map.of(
                    "batteries", batteries.stream().map(BatteryDTO::getName).toList(),
                    "totalCapacity", batteryService.getTotalCapacity(batteries),
                    "averageCapacity", batteryService.getAverageCapacity(batteries)
            ));

        } catch (NumberFormatException e) {
            logger.error("Invalid number format: {}", e.getMessage());
            return GlobalExceptionHandler.badRequest("Start postcode and end postcode must be valid integers");
        } catch (Exception e) {
            logger.error("Error occurred while querying batteries: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred while querying the batteries: " + e.getMessage()));
        }
    }


}
