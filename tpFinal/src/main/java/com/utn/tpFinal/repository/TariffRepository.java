package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TariffRepository extends JpaRepository<Tariff,Integer> {
}
