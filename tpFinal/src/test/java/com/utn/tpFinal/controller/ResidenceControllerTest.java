package com.utn.tpFinal.controller;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.service.BillService;
import com.utn.tpFinal.service.ResidenceService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ResidenceControllerTest {

    private ResidenceService residenceService;
    private BillService billService;
    private ResidenceController residenceController;
    private final Integer PAGE = 0;
    private final Integer SIZE = 10;

    @Before
    public void setUp(){
        residenceService = mock(ResidenceService.class);
        billService =mock(BillService.class);
        residenceController = new ResidenceController(residenceService,billService);
    }
    @Test
    public void getAllResidence_Test200() throws Exception {
        //give

        Specification<Residence> specification = mock(Specification.class);

        List<ResidenceDto> residenceDtoList = UTILS_TESTCONSTANTS.getResidendesDTO_List();

        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","street");

        Page<ResidenceDto> mockedPage = mock(Page.class);

        when(mockedPage.getContent()).thenReturn(residenceDtoList);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(residenceDtoList.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(residenceService.getAll(specification,PAGE,SIZE,orders)).thenReturn(mockedPage);

        //then
        ResponseEntity<List<ResidenceDto>> response = residenceController.getAll(PAGE,SIZE,"id","street",specification);

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("calle2", response.getBody().get(1).getStreet());
    }
    @Test
    public void getAllResidence_Test204() throws Exception {
        //give

        Specification<Residence> specification = mock(Specification.class);

        //List<ResidenceDto> residenceDtoList = UTILS_TESTCONSTANTS.getResidendesDTO_List();

        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","street");

        Page<ResidenceDto> mockedPage = Page.empty();

        when(residenceService.getAll(specification,PAGE,SIZE,orders)).thenReturn(mockedPage);

        //then
        ResponseEntity<List<ResidenceDto>> response = residenceController.getAll(PAGE,SIZE,"id","street",specification);

        //assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void addEnergyMeterToResidence_Test() throws Exception {
        Integer idResidence = 1;
        Integer idEnergyMeter = 4;
        doNothing().when(residenceService).addEnergyMeterToResidence(idResidence,idEnergyMeter);

        ResponseEntity response = residenceController.addEnergyMeterToResidence(idResidence,idEnergyMeter);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void addTariffToResidence() throws Exception {
        Integer idResidence = 1;
        Integer idTariff = 2;
        doNothing().when(residenceService).addTariffToResidence(idResidence,idTariff);

        ResponseEntity response = residenceController.addTariffToResidence(idResidence,idTariff);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
