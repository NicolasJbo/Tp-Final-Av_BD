package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Integer> {

    @Query(value = "CALL getBillsByDates(:from, :to)", nativeQuery = true)
    List<Bill> getBillsByDate(Date from , Date to);
}
