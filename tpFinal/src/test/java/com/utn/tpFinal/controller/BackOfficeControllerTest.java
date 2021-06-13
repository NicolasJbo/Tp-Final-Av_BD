package com.utn.tpFinal.controller;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.proyection.Top10Clients;
import com.utn.tpFinal.service.BillService;
import com.utn.tpFinal.service.ClientService;
import com.utn.tpFinal.service.ResidenceService;
import com.utn.tpFinal.service.TariffService;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BackOfficeControllerTest {

    ResidenceService residenceService;
    ClientService clientService;
    TariffService tariffService;
    BackOfficeController backOfficeController;

    @Before
    public void setUp(){
        clientService = mock(ClientService.class);
        residenceService =mock(ResidenceService.class);
        tariffService=mock(TariffService.class);
        backOfficeController = new BackOfficeController(residenceService,clientService,tariffService);
    }

    @Test
    public void addTariff_Test200() throws URISyntaxException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Tariff t = UTILS_TESTCONSTANTS.getTariff(1);

        when(tariffService.add(t)).thenReturn(t);

        ResponseEntity response = backOfficeController.addTariff(t);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void deleteTariffById_Test200() throws TariffNotExists {
        Integer tariffId=4;

        doNothing().when(tariffService).deleteTariffById(tariffId);

        ResponseEntity response = backOfficeController.deleteTariffById(tariffId);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void modifyTariff_Test202() throws Exception {

        Integer tariffId=4;
        Tariff t = UTILS_TESTCONSTANTS.getTariff(tariffId);

        doNothing().when(tariffService).modifyTariff(tariffId,t);

        ResponseEntity response = backOfficeController.modifyTariff(tariffId, t);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
    }

    @Test
    public void addResidence_Test200() throws URISyntaxException, ParseException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Residence r = UTILS_TESTCONSTANTS.getResidence(1);

        when(residenceService.addResidence(r)).thenReturn(r);

        ResponseEntity response = backOfficeController.addResidence(r);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void deleteResidenceById_Test200() throws ResidenceNotExists {
        Integer residenceId=7;

        when(residenceService.removeResidenceById(residenceId)).thenReturn("deleted");

        ResponseEntity response = backOfficeController.deleteResidenceById(residenceId);

        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void modifyResidence_Test202() throws Exception {

        Integer residenceId=7;

        Residence r = UTILS_TESTCONSTANTS.getResidence(residenceId);

        doNothing().when(residenceService).modifyResidence(residenceId, r);

        ResponseEntity response = backOfficeController.modifyResidence(residenceId, r);

        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
    }

    @Test
    public void getResidenceUnpaidBills_Test200() throws ResidenceNotExists, ClientNotExists, NoContentException {

        Page<BillDto> mockedPage = mock(Page.class);
        List<BillDto> bills =UTILS_TESTCONSTANTS.getBillsDTO_List();
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","expirationDate") ;

        when(mockedPage.getContent()).thenReturn(bills);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(bills.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(residenceService.getResidenceUnpaidBills(4,1,5,orders)).thenReturn(mockedPage);

        //then
        ResponseEntity<List<BillDto>>response = backOfficeController.getResidenceUnpaidBills(4,1,5,"id","expirationDate");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("casa1", response.getBody().get(0).getResidence());

    }

    @Test
    public void getClientUnpaidBills() throws Exception {
        Page<BillDto> mockedPage = mock(Page.class);
        List<BillDto> bills =UTILS_TESTCONSTANTS.getBillsDTO_List();
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","expirationDate") ;

        when(mockedPage.getContent()).thenReturn(bills);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(bills.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(clientService.getClientUnpaidBills(4,1,5,orders)).thenReturn(mockedPage);

        //then
        ResponseEntity<List<BillDto>>response = backOfficeController.getClientUnpaidBills(4,1,5,"id","expirationDate");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("casa1", response.getBody().get(0).getResidence());

    }

   /* @Test
    public void getTop10ConsumerByDates_Test200(){



    }*/


    @Test
    public void getResidenceMeasuresByDates_Test200() throws Exception {

        Page<MeasureDto> mockedPage = mock(Page.class);
        List<MeasureDto> measures =UTILS_TESTCONSTANTS.getMeasureDTO_List();
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","date") ;

        when(mockedPage.getContent()).thenReturn(measures);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(measures.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(residenceService.getResidenceMeasuresByDates(4,UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2),1,5,orders)).thenReturn(mockedPage);

        //then
        ResponseEntity<List<MeasureDto>>response = backOfficeController.getResidenceMeasuresByDates(4,1,5,"id","date",
                                                                                            UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("serial1", response.getBody().get(0).getSerialNumber());

    }
    @Test
    public void getTop10ConsumerByDates(){

        try {
            List<Top10Clients> top10Clients=mock(List.class);
            when(clientService.getTop10ConsumerByDates(UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2))).thenReturn(top10Clients);
            ResponseEntity<List<Top10Clients>>response = backOfficeController.getTop10ConsumerByDates(UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2));

            assertEquals(HttpStatus.OK, response.getStatusCode());

        } catch (NoContentException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            Assert.fail("No se cargo bien la fecha");

        }

    }



}
