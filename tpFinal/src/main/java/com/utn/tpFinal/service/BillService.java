package com.utn.tpFinal.service;

import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;

    public List<Bill> getBillsByDates(Date from, Date to) {
        return billRepository.getBillsByDate(from,to);
    }
}
