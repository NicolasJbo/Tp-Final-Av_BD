package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.exception.NoConsumptionsFoundException;
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

@Service
public class BillService {

   // @Autowired
    private BillRepository billRepository;
   // @Autowired
    private ClientService clientService;
    //@Autowired
    private MeasureRepository measureRepository;

    @Autowired
    public BillService(BillRepository billRepository , ClientService clientService, MeasureRepository measureRepository) {
        this.billRepository = billRepository;
        this.clientService = clientService;
        this.measureRepository = measureRepository;
    }


    public Page<BillDto> getClientBillsByDates(Integer idClient, Date from, Date to, Integer page, Integer size, List<Sort.Order> orders) throws IncorrectDatesException, ClientNotExists {
        if(from.after(to))
            throw new IncorrectDatesException(this.getClass().getSimpleName(),"getClientBillsByDates");

        Client c = clientService.getClientById(idClient);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        List<Integer>residencesIds = new ArrayList<>();
        for(Residence r: c.getResidencesList())
            residencesIds.add(r.getId());

        Page<Bill> bills = billRepository.findByInitialDateBetweenAndResidenceIdIn(from, to, residencesIds, pageable);

        Page<BillDto> billDtos = Page.empty(pageable);

        if(!bills.isEmpty())
            billDtos = bills.map(b-> BillDto.from(b));

        return billDtos;
    }

    public Consumption getClientTotalEnergyAndAmountByDates(Integer idClient, Date from, Date to) throws IncorrectDatesException, NoConsumptionsFoundException {
        if(from.after(to))
            throw new IncorrectDatesException(this.getClass().getSimpleName(),"getClientTotalEnergyAndAmountByDates");

        Consumption consumption = billRepository.getClientTotalEnergyAndAmountByDates(idClient,from,to);

        return consumption;
    }

    public Page<MeasureDto> getClientMeasuresByDates(Integer idClient, Date from, Date to, Integer page, Integer size, List<Sort.Order> orders) throws Exception {
        if (from.after(to))
            throw new IncorrectDatesException(this.getClass().getSimpleName(), "getClientMeasuresByDates");

        Client c = clientService.getClientById(idClient);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        List<Integer>residencesIds = new ArrayList<Integer>();
        for(Residence r: c.getResidencesList())
            residencesIds.add(r.getId());

        Page<Measure> measures = measureRepository.findByDateBetweenAndResidenceIdIn(from, to, residencesIds, pageable);

        Page<MeasureDto> measureDtos = Page.empty(pageable);

        if (!measures.isEmpty())
            measureDtos = measures.map(m -> MeasureDto.from(m));

        return measureDtos;
    }




}
