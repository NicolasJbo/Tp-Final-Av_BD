package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.IncorrectPasswordException;
import com.utn.tpFinal.exception.ResidenceNotDefined;
import com.utn.tpFinal.exception.ResidenceNotExists;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.repository.MeasureRepository;
import com.utn.tpFinal.repository.ResidenceRepository;
import com.utn.tpFinal.repository.TariffRepository;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class MeasureServiceTest {

    private ResidenceService residenceService;
    private EnergyMeterService  energyMeterService;
    private MeasureRepository measureRepository;
    private MeasureService measureService;

    @Before
    public void setUp(){
        measureRepository = mock(MeasureRepository.class);
        energyMeterService = mock(EnergyMeterService.class);
        residenceService = mock(ResidenceService.class);
        measureService = new MeasureService(measureRepository, energyMeterService, residenceService);
        /*measureService.setResidenceService(residenceService);
        measureService.setEnergyMeterService(energyMeterService);
        measureService.setMeasureRepository(measureRepository);*/
    }

    @Test
    public void add_Test200() throws Exception {
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);
        EnergyMeter e = r.getEnergyMeter();
        e.setResidence(r);
        Measure m = UTILS_TESTCONSTANTS.getMeasure(20);
        m.setResidence(UTILS_TESTCONSTANTS.getResidence(8));

        when(energyMeterService.getEnergyMeterBySerialNumber(any(String.class))).thenReturn(e);
        when(residenceService.getResidenceByEnergyMeterId(any(Integer.class))).thenReturn(r);
        when(measureRepository.save(any(Measure.class))).thenReturn(m);

        measureService.add(m,"001","1234");

        assertEquals(Float.valueOf(5000),m.getPrice());
        assertEquals(r, m.getResidence());

    }

    @Test
    public void add_TestIncorrectPasswordException() throws Exception {
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);
        EnergyMeter e = EnergyMeter.builder().id(8).passWord("no-mas-tests-porfavor").serialNumber("123").build();
        Measure m = UTILS_TESTCONSTANTS.getMeasure(20);
        m.setResidence(UTILS_TESTCONSTANTS.getResidence(8));

        when(energyMeterService.getEnergyMeterBySerialNumber(any(String.class))).thenReturn(e);
        when(residenceService.getResidenceByEnergyMeterId(any(Integer.class))).thenReturn(r);

        assertThrows(IncorrectPasswordException.class,()->measureService.add(m,"001","1234"));

    }

    @Test
    public void add_TestResidenceNotDefinedException() throws Exception {
        EnergyMeter e = UTILS_TESTCONSTANTS.getEnergyMeter(4);
        Measure m = UTILS_TESTCONSTANTS.getMeasure(20);
        m.setResidence(UTILS_TESTCONSTANTS.getResidence(8));

        when(energyMeterService.getEnergyMeterBySerialNumber(any(String.class))).thenReturn(e);
        when(residenceService.getResidenceByEnergyMeterId(any(Integer.class))).thenReturn(null);

        assertThrows(ResidenceNotDefined.class,()->measureService.add(m,"001","1234"));

    }




}
