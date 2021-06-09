package com.utn.tpFinal.controller;


import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.IncorrectPasswordException;
import com.utn.tpFinal.exception.ResidenceNotDefined;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.dto.MeasureSenderDto;
import com.utn.tpFinal.service.EnergyMeterService;
import com.utn.tpFinal.service.MeasureService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class MeasureControllerTest {

    MeasureService measureService;
    MeasureController measureController;

    @Before
    public void setUp(){
        measureService = mock(MeasureService.class);
        measureController = new MeasureController(measureService);
    }

    /*@Test
    public void addMeasure_Test200 () throws Exception {

        Measure m = UTILS_TESTCONSTANTS.getMeasure();
        MeasureSenderDto measureSenderDto = UTILS_TESTCONSTANTS.getMeasureSenderDto();

        doNothing().when(measureService).add(m,measureSenderDto.getSerialNumber(), measureSenderDto.getPassword());

        ResponseEntity response = measureController.addMeasure(measureSenderDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }*/




}
