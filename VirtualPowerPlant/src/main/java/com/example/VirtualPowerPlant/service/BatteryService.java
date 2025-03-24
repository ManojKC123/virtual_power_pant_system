package com.example.VirtualPowerPlant.service;

import com.example.VirtualPowerPlant.dto.BatteryDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BatteryService {

    public BatteryDTO save(BatteryDTO battery);

    public List<BatteryDTO> getAll();

    public BatteryDTO getBatteryById(Long id);

    List<BatteryDTO> findByPostcodeAndCapacityRange(int startPostcode, int endPostcode, Integer startCapacity, Integer endCapacity);

    public int getTotalCapacity(List<BatteryDTO> batteries);

    public double getAverageCapacity(List<BatteryDTO> batteries);

    public Page<BatteryDTO> findPagination(int pageSize, int pageNumber, String sortField, String direction);
}
