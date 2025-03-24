package com.example.VirtualPowerPlant.service;

import com.example.VirtualPowerPlant.dto.BatteryDTO;
import com.example.VirtualPowerPlant.entities.Battery;
import com.example.VirtualPowerPlant.repositories.BatteryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatteryImp implements BatteryService {

    private static final Logger logger = LoggerFactory.getLogger(BatteryImp.class);

    private final BatteryRepository batteryRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public BatteryDTO save(BatteryDTO batteryDTO) {
        logger.info("Attempting to save battery with name: {}", batteryDTO.getName());

        if (batteryRepository.existsByName(batteryDTO.getName())) {
            logger.error("Battery with name {} already exists", batteryDTO.getName());
            throw new IllegalArgumentException("Battery with the same name already exists");
        }

        Battery battery = modelMapper.map(batteryDTO, Battery.class);
        Battery savedBattery = batteryRepository.save(battery);

        logger.info("Successfully saved battery with ID: {}", savedBattery.getId());
        return modelMapper.map(savedBattery, BatteryDTO.class);
    }

    @Override
    public List<BatteryDTO> getAll() {
        logger.info("Fetching all batteries");

        List<Battery> batteries = batteryRepository.findAll();
        return batteries.stream()
                .map(battery -> modelMapper.map(battery, BatteryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BatteryDTO getBatteryById(Long id) {
        logger.info("Fetching battery with ID: {}", id);

        Battery battery = batteryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Battery with ID {} not found", id);
                    return new IllegalArgumentException("Battery not found");
                });

        return modelMapper.map(battery, BatteryDTO.class);
    }

    @Override
    public List<BatteryDTO> findByPostcodeAndCapacityRange(int startPostcode, int endPostcode, Integer startCapacity, Integer endCapacity) {
        logger.info("Fetching batteries by postcode range: {}-{} and capacity range: {}-{}",
                startPostcode, endPostcode, startCapacity, endCapacity);

        List<Battery> batteries = batteryRepository.findByPostcodeAndCapacityRange(startPostcode, endPostcode, startCapacity, endCapacity);
        System.out.println(batteries.toString());

        logger.info("Found {} batteries matching the criteria", batteries.size());

        return batteries.stream()
                .sorted(Comparator.comparing(Battery::getName))
                .map(battery -> modelMapper.map(battery, BatteryDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public int getTotalCapacity(List<BatteryDTO> batteries) {
        logger.info("Calculating total capacity for {} batteries", batteries.size());
        return batteries.stream().mapToInt(BatteryDTO::getCapacity).sum();
    }

    @Override
    public double getAverageCapacity(List<BatteryDTO> batteries) {
        logger.info("Calculating average capacity for {} batteries", batteries.size());
        return batteries.stream()
                .mapToInt(BatteryDTO::getCapacity)
                .average()
                .orElse(0);
    }

    @Override
    public Page<BatteryDTO> findPagination(int pageSize, int pageNumber, String sortField, String direction) {
        logger.info("Fetching battery data with pagination: pageSize={}, pageNumber={}, sortField={}, direction={}",
                pageSize, pageNumber, sortField, direction);

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize,
                direction.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending());
        Page<Battery> batteriesPage = batteryRepository.findAll(pageable);

        return batteriesPage.map(battery -> modelMapper.map(battery, BatteryDTO.class));
    }
}
