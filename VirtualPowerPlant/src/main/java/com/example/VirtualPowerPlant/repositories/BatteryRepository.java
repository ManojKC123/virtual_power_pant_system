package com.example.VirtualPowerPlant.repositories;

import com.example.VirtualPowerPlant.entities.Battery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface BatteryRepository extends JpaRepository<Battery, Long> {
    static final Logger logger = LoggerFactory.getLogger(BatteryRepository.class);
    boolean existsByName(String name);

    //Custom Query to get the battery within the given range
    @Query(value = "SELECT * FROM battery b WHERE "
            + "(CAST(b.postcode AS int) BETWEEN :startPostcode AND :endPostcode) "
            + "AND ("
            + "  (:startCapacity IS NULL OR b.capacity >= :startCapacity) "
            + "  AND (:endCapacity IS NULL OR b.capacity <= :endCapacity)"
            + ")", nativeQuery = true)
    List<Battery> findByPostcodeAndCapacityRange(
            @Param("startPostcode") int startPostcode,
            @Param("endPostcode") int endPostcode,
            @Param("startCapacity") Integer startCapacity,
            @Param("endCapacity") Integer endCapacity);

}
