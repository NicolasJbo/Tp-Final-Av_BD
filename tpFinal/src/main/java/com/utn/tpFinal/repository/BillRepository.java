package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.proyection.Consumption;
import com.utn.tpFinal.model.proyection.MeasureProyection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;


import java.util.Date;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query(value = "CALL getClientBillsByDates(:idClient, :from, :to)", nativeQuery = true)
    List<Bill> getClientBillsByDate(Integer idClient, Date from, Date to);

    @Query(value = "CALL getClientUnpaidBills(:idClient)", nativeQuery = true)
    List<Bill> getClientUnpaidBills(Integer idClient);

    @Query(value = "CALL getClientTotalEnergyAndAmountByDates(:idClient, :from, :to)", nativeQuery = true)
    Consumption getClientTotalEnergyAndAmountByDates(Integer idClient, Date from, Date to);

    @Query(value = "CALL getClientMeasuresByDates(:idClient, :from, :to)", nativeQuery = true)
    List<MeasureProyection> getClientMeasuresByDates(Integer idClient, Date from, Date to);

    @Query(value = "CALL getClientUnpaidBillsByResidence(:idResidence)", nativeQuery = true)
    List<Bill> getClientUnpaidBillsByResidence(Integer idResidence);


    Page<Bill> findByIsPaidFalseAndResidenceIdIn(List<Integer> residencesIds, Pageable pageable);

    Page<Bill> findByIsPaidFalseAndResidenceId(Integer idResidence, Pageable pageable);
}