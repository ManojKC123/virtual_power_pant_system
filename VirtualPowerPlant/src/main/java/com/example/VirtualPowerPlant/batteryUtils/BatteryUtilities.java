package com.example.VirtualPowerPlant.batteryUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatteryUtilities {

    private static final Logger logger = LoggerFactory.getLogger(BatteryUtilities.class);

    public static Integer parseCapacity(String capacity, String fieldName) {
        try {
            int value = Integer.parseInt(capacity);
            if (value < 0) {
                logger.error("{} must be a positive number, but received: {}", fieldName, value);
                throw new IllegalArgumentException(fieldName + " must be a positive number");
            }
            return value;
        } catch (NumberFormatException e) {
            logger.error("Invalid integer format for {}: {}", fieldName, capacity);
            throw new IllegalArgumentException(fieldName + " must be a valid integer");
        }
    }
}
