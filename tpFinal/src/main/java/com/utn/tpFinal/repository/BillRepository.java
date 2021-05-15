package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.proyection.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query(value = "CALL getClientBillsByDates(:idClient, :from, :to)", nativeQuery = true)
    List<Bill> getClientBillsByDate(Integer idClient,Date from , Date to);

    @Query(value = "CALL getClientUnpaidBills(:idClient)", nativeQuery = true)
    List<Bill> getClientUnpaidBills(Integer idClient);

    @Query(value = "CALL getClientTotalEnergyAndAmountByDates(:idClient, :from, :to)", nativeQuery = true)
    Consumption getClientTotalEnergyAndAmountByDates(Integer idClient, Date from, Date to);
}
