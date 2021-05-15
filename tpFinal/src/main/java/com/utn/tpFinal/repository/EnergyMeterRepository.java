package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.MeterBrand;
import com.utn.tpFinal.model.MeterModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnergyMeterRepository extends JpaRepository<EnergyMeter, Integer> {
    Page<EnergyMeter> findBySerialNumber(String serialNumber,Pageable pageable);
    List<EnergyMeter> findBySerialNumber(String serialNumber);
    List<EnergyMeter> findByBrand(MeterBrand brand);
    Page<EnergyMeter> findAll(Pageable pageable);
    List<EnergyMeter> findByModel(MeterModel model);
}
