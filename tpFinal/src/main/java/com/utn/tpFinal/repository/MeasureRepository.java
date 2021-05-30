package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.proyection.MeasureProyection;
import com.utn.tpFinal.model.proyection.MeasuresById;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MeasureRepository extends JpaRepository<Measure, Integer> {

    Page<Measure> findByResidenceIdAndDateBetween(Integer idResidence, Date from, Date to, Pageable pageable);

    Page<Measure> findByDateBetweenAndResidenceIdIn(Date from, Date to,List<Integer>residencesIds, Pageable pageable);
}
