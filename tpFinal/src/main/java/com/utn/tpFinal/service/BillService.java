package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Consumption;
import com.utn.tpFinal.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;

    public List<Bill> getClientBillsByDates(Integer idClient, Date from, Date to) {
        if(from.after(to)){
            //todo exception
            //throws IncorrectDatesException.builder().method(this.getClass().getEnclosingMethod().getName()).route();
        }
        return billRepository.getClientBillsByDate(idClient,from,to);
    }

    public List<Bill> getClientUnpaidBills(Integer idClient) {
        return billRepository.getClientUnpaidBills(idClient);
    }

    public Consumption getClientTotalEnergyByDates(Integer idClient, Date from, Date to) {
        if(from.after(to)){
            //todo exception
            //throws IncorrectDatesException.builder().method(this.getClass().getEnclosingMethod().getName()).route();
        }
        return billRepository.getClientTotalEnergyByDates(idClient,from,to);
    }
}
