package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.repository.BillRepository;
import com.utn.tpFinal.repository.ClientRepository;
import com.utn.tpFinal.repository.MeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BillServiceTest {

    private BillRepository billRepository;
    private ClientService clientService;
    private MeasureRepository measureRepository;
    private BillService billService;

    @Before
    public void setUp(){
        billRepository = mock(BillRepository.class);
        clientService = mock(ClientService.class);
        measureRepository = mock(MeasureRepository.class);
        billService = new BillService(billRepository,clientService,measureRepository);
    }

    @Test
    public void getClientBillsByDates_TestException() throws ParseException {
        List<Sort.Order> orders = UTILS_TESTCONSTANTS.getOrders("id","expirationDate") ;

        Date from = UTILS_TESTCONSTANTS.getFecha(2);
        Date to = UTILS_TESTCONSTANTS.getFecha(1);

        assertThrows(IncorrectDatesException.class,()-> billService.getClientBillsByDates(4, from, to,0,2, orders ));
    }

    @Test
    public void getClientBillsByDates_Test200() throws ParseException, IncorrectDatesException, ClientNotExists {
        List<Sort.Order> orders = UTILS_TESTCONSTANTS.getOrders("id","expirationDate") ;
        Client c = UTILS_TESTCONSTANTS.getClient(4);
        Date to = UTILS_TESTCONSTANTS.getFecha(2);
        Date from = UTILS_TESTCONSTANTS.getFecha(1);

        Residence residence1 = UTILS_TESTCONSTANTS.getResidence(1);
        Residence residence2 = UTILS_TESTCONSTANTS.getResidence(2);
        List<Residence>residences = new ArrayList<>();
        residences.add(residence1);
        residences.add(residence2);
        c.setResidencesList(residences);

        List<Bill>list= new ArrayList<>();
        list.add(UTILS_TESTCONSTANTS.getBill(4));
        list.add(UTILS_TESTCONSTANTS.getBill(6));

        Page<Bill> page =new PageImpl<>(list);

        when(clientService.getClientById(anyInt())).thenReturn(c);
        when(billRepository.findByInitialDateBetweenAndResidenceIdIn(any(Date.class), any(Date.class), any(List.class), any(Pageable.class))).thenReturn(page);

        Page<BillDto>response = billService.getClientBillsByDates(4,from, to, 0,2,orders);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals(Float.valueOf(1500), response.getContent().get(0).getFinalAmount());
        assertEquals(2,response.getNumberOfElements());
    }

    @Test
    public void getClientBillsByDates_Test204() throws ParseException, IncorrectDatesException, ClientNotExists {
        List<Sort.Order> orders = UTILS_TESTCONSTANTS.getOrders("id","expirationDate") ;
        Client c = UTILS_TESTCONSTANTS.getClient(4);
        Date to = UTILS_TESTCONSTANTS.getFecha(2);
        Date from = UTILS_TESTCONSTANTS.getFecha(1);

        Residence residence1 = UTILS_TESTCONSTANTS.getResidence(1);
        Residence residence2 = UTILS_TESTCONSTANTS.getResidence(2);
        List<Residence>residences = new ArrayList<>();
        residences.add(residence1);
        residences.add(residence2);
        c.setResidencesList(residences);

        when(clientService.getClientById(anyInt())).thenReturn(c);
        when(billRepository.findByInitialDateBetweenAndResidenceIdIn(any(Date.class), any(Date.class), any(List.class), any(Pageable.class))).thenReturn(Page.empty());

        Page<BillDto>response = billService.getClientBillsByDates(4,from, to, 0,2,orders);

        assertEquals(true,response.getContent().isEmpty());

    }

    @Test
    public void getClientMeasuresByDates_TestException() throws ParseException {
        List<Sort.Order> orders = UTILS_TESTCONSTANTS.getOrders("id","expirationDate") ;

        Date from = UTILS_TESTCONSTANTS.getFecha(2);
        Date to = UTILS_TESTCONSTANTS.getFecha(1);

        assertThrows(IncorrectDatesException.class,()-> billService.getClientMeasuresByDates(4, from, to,0,2, orders ));
    }

    @Test
    public void getClientMeasuresByDates_Test200() throws Exception {
        List<Sort.Order> orders = UTILS_TESTCONSTANTS.getOrders("id","expirationDate") ;
        Client c = UTILS_TESTCONSTANTS.getClient(4);
        Date to = UTILS_TESTCONSTANTS.getFecha(2);
        Date from = UTILS_TESTCONSTANTS.getFecha(1);

        Residence residence1 = UTILS_TESTCONSTANTS.getResidence(1);
        Residence residence2 = UTILS_TESTCONSTANTS.getResidence(2);
        List<Residence>residences = new ArrayList<>();
        residences.add(residence1);
        residences.add(residence2);
        c.setResidencesList(residences);

        List<Measure>list= new ArrayList<>();
        list.add(UTILS_TESTCONSTANTS.getMeasure(4));
        list.add(UTILS_TESTCONSTANTS.getMeasure(6));

        Page<Measure> page =new PageImpl<>(list);

        when(clientService.getClientById(anyInt())).thenReturn(c);
        when(measureRepository.findByDateBetweenAndResidenceIdIn(any(Date.class), any(Date.class), any(List.class), any(Pageable.class))).thenReturn(page);

        Page<MeasureDto>response = billService.getClientMeasuresByDates(4,from, to, 0,2,orders);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals(Float.valueOf(100),Float.valueOf(response.getContent().get(0).getKw()));
        assertEquals(2,response.getNumberOfElements());
    }

    @Test
    public void getClientMeasuresByDates_Test204() throws Exception {
        List<Sort.Order> orders = UTILS_TESTCONSTANTS.getOrders("id","expirationDate") ;
        Client c = UTILS_TESTCONSTANTS.getClient(4);
        Date to = UTILS_TESTCONSTANTS.getFecha(2);
        Date from = UTILS_TESTCONSTANTS.getFecha(1);

        Residence residence1 = UTILS_TESTCONSTANTS.getResidence(1);
        Residence residence2 = UTILS_TESTCONSTANTS.getResidence(2);
        List<Residence>residences = new ArrayList<>();
        residences.add(residence1);
        residences.add(residence2);
        c.setResidencesList(residences);

        when(clientService.getClientById(anyInt())).thenReturn(c);
        when(measureRepository.findByDateBetweenAndResidenceIdIn(any(Date.class), any(Date.class), any(List.class), any(Pageable.class))).thenReturn(Page.empty());

        Page<MeasureDto>response = billService.getClientMeasuresByDates(4,from, to, 0,2,orders);

        assertEquals(true,response.getContent().isEmpty());

    }




}
