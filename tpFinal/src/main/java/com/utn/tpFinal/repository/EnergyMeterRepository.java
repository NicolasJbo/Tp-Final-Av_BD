package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.EnergyMeter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnergyMeterRepository extends JpaRepository<EnergyMeter, Integer> {
    List<EnergyMeter> findBySerialNumber(String serialNumber);
}
