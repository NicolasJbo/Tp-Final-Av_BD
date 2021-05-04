package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Residence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResidenceRepository extends JpaRepository<Residence, Integer> {

    List<Residence> findByStreet(String street);
}
