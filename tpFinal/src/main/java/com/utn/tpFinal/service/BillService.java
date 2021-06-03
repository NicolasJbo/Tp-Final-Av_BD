package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.exception.NoConsumptionsFoundException;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.proyection.Consumption;
import com.utn.tpFinal.repository.BillRepository;
import com.utn.tpFinal.repository.ClientRepository;
import com.utn.tpFinal.repository.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class BillService {

    @Autowired
    BillRepository billRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientService clientService;
    @Autowired
    MeasureRepository measureRepository;


    public Page<BillDto> getClientBillsByDates(Integer idClient, Date from, Date to, Integer page, Integer size, List<Sort.Order> orders) throws IncorrectDatesException, NoContentException, ClientNotExists {
        if(from.after(to))
            throw new IncorrectDatesException(this.getClass().getSimpleName(),"getClientBillsByDates");

        Client c = clientService.getClientById(idClient);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        List<Integer>residencesIds = new ArrayList<Integer>();
        for(Residence r: c.getResidencesList())
            residencesIds.add(r.getId());

        Page<Bill> bills = billRepository.findByInitialDateBetweenAndResidenceIdIn(from, to, residencesIds, pageable);

        if(bills.isEmpty())
            throw new NoContentException(this.getClass().getSimpleName(),"getClientBillsByDates");

        Page<BillDto> billsDto = bills.map(b-> BillDto.from(b));

        return billsDto;
    }

    public Consumption getClientTotalEnergyAndAmountByDates(Integer idClient, Date from, Date to) throws IncorrectDatesException, NoConsumptionsFoundException {
        if(from.equals(to) || from.after(to))
            throw new IncorrectDatesException(this.getClass().getSimpleName(),"getClientTotalEnergyAndAmountByDates");


        Consumption consumption = billRepository.getClientTotalEnergyAndAmountByDates(idClient,from,to);

        return consumption;
    }

    public Page<MeasureDto> getClientMeasuresByDates(Integer idClient, Date from, Date to, Integer page, Integer size, List<Sort.Order> orders) throws Exception {
        if (from.equals(to) || from.after(to))
            throw new IncorrectDatesException(this.getClass().getSimpleName(), "getClientMeasuresByDates");

        Client c = clientService.getClientById(idClient);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        List<Integer>residencesIds = new ArrayList<Integer>();
        for(Residence r: c.getResidencesList())
            residencesIds.add(r.getId());

        Page<Measure> measures = measureRepository.findByDateBetweenAndResidenceIdIn(from, to, residencesIds, pageable);
        if (measures.isEmpty())
            throw new NoContentException(this.getClass().getSimpleName(), "getClientMeasuresByDates");

        Page<MeasureDto> measureDtos = measures.map(m -> MeasureDto.from(m));

        return measureDtos;
    }




}
