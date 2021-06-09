package com.utn.tpFinal.controller;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.ResidenceNotExists;
import com.utn.tpFinal.exception.TariffNotExists;
import com.utn.tpFinal.exception.TariffsDoNotMatch;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.service.BillService;
import com.utn.tpFinal.service.ClientService;
import com.utn.tpFinal.service.ResidenceService;
import com.utn.tpFinal.service.TariffService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URISyntaxException;

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
    public void addResidence_Test200() throws URISyntaxException {
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

        doNothing().when(residenceService).removeResidenceById(residenceId);

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

}
