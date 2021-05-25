package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Measure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasureRepository extends JpaRepository<Measure, Integer> {


}
