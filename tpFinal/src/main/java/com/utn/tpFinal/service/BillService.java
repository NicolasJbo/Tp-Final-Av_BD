package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.proyection.Consumption;
import com.utn.tpFinal.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;
    @Autowired
    ClientService clientService;

    public List<Bill> getClientBillsByDates(Integer idClient, Date from, Date to) throws IncorrectDatesException {
        if(from.after(to)){
            throw IncorrectDatesException.builder().route(this.getClass().getSimpleName()).method("getClientBillByDates").build();
        }
        return billRepository.getClientBillsByDate(idClient,from,to);
    }

   public List<Bill> getClientUnpaidBills(Integer idClient) {
        //if(clientService.clientExists(idClient))
            return billRepository.getClientUnpaidBills(idClient);
        //else
            //404
    }

    public Consumption getClientTotalEnergyAndAmountByDates(Integer idClient, Date from, Date to) throws IncorrectDatesException {
        if(from.after(to)){
            throw IncorrectDatesException.builder().route(this.getClass().getSimpleName()).method("getClientBillByDates").build();
        }
        return billRepository.getClientTotalEnergyAndAmountByDates(idClient,from,to);
    }
}
