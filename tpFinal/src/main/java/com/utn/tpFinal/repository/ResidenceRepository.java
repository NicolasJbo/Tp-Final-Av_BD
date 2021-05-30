package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.MeterModel;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.proyection.MeasuresById;
import com.utn.tpFinal.model.proyection.Top10Clients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Integer>, JpaSpecificationExecutor<Residence> {

    Page<Residence> findByClientId(Integer idClient, Pageable pageable);
    Residence findByEnergyMeterId(Integer idEnergyMeter);
    Page<Residence> findByTariffId(Integer idTariff, Pageable pageable);


}
