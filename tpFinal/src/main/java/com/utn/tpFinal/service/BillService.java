package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.proyection.Consumption;
import com.utn.tpFinal.model.proyection.MeasureProyection;
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

    public List<BillDto> getClientBillsByDates(Integer idClient, Date from, Date to) throws IncorrectDatesException, NoContentException {
        if(from.after(to))
            throw new IncorrectDatesException(this.getClass().getSimpleName(),"getClientBillsByDates");
        //todo lista vacia exception
        List<Bill> billsList = billRepository.getClientBillsByDate(idClient,from,to);
        if(billsList.isEmpty())
            throw new NoContentException(this.getClass().getSimpleName(),"getClientBillsByDates");

        List<BillDto>billsDtoList = BillDto.from(billsList) ;
        return billsDtoList;
    }

    public List<BillDto> getClientUnpaidBills(Integer idClient) { //todo verificar si la lista tiene contenido
        List<Bill> billsList = billRepository.getClientUnpaidBills(idClient);
        List<BillDto>billsDtoList = BillDto.from(billsList) ;
        return billsDtoList;
    }

    public Consumption getClientTotalEnergyAndAmountByDates(Integer idClient, Date from, Date to) throws IncorrectDatesException {
        if(from.equals(to) || from.after(to))
            throw new IncorrectDatesException(this.getClass().getSimpleName(),"getClientTotalEnergyAndAmountByDates");

        return billRepository.getClientTotalEnergyAndAmountByDates(idClient,from,to);
    }

    public List<MeasureProyection> getClientMeasuresByDates(Integer idClient, Date from, Date to) throws Exception {
        if (from.equals(to) || from.after(to))
            throw new IncorrectDatesException(this.getClass().getSimpleName(), "getClientMeasuresByDates");

        List<MeasureProyection> measures = billRepository.getClientMeasuresByDates(idClient, from, to);
        if (measures.isEmpty()) //todo= no lanza bien la exception (solo muestra el codigo)
            throw new NoContentException(this.getClass().getSimpleName(), "getClientMeasuresByDates");

        return measures;
    }


}
