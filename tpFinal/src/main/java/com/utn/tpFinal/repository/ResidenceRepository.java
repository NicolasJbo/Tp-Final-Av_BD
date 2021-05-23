package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Residence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Integer> {

    Page<Residence> findByStreet(String street, Pageable pageable);
    Page<Residence> findByClient(Client client, Pageable pageable);

    Page<Residence> findByClientId(Integer idClient, Pageable pageable);
}
