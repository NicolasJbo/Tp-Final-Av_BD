package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.MeterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeterModelRepository extends JpaRepository<MeterModel, Integer>, JpaSpecificationExecutor<MeterModel> {

}
