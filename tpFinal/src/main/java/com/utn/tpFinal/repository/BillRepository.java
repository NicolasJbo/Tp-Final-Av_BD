package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Consumption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query(value = "CALL getClientBillsByDates(:idClient, :from, :to)", nativeQuery = true)
    List<Bill> getClientBillsByDate(Integer idClient,Date from , Date to);

    @Query(value = "CALL getClientUnpaidBills(:idClient)", nativeQuery = true)
    List<Bill> getClientUnpaidBills(Integer idClient);

    @Query(value = "CALL getClientTotalEnergyByDates(:idClient, :from, :to)", nativeQuery = true)
    Consumption getClientTotalEnergyByDates(Integer idClient, Date from, Date to);
}
