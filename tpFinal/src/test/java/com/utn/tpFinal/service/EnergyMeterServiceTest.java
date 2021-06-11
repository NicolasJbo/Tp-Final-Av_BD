package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.TariffNotExists;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.repository.EnergyMeterRepository;
import com.utn.tpFinal.repository.MeterBrandRepository;
import com.utn.tpFinal.repository.MeterModelRepository;
import com.utn.tpFinal.repository.TariffRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnergyMeterServiceTest {

    EnergyMeterService energyMeterService;
    EnergyMeterRepository energyMeterRepository;
    MeterBrandRepository meterBrandRepository;
    MeterModelRepository meterModelRepository;

    @Before
    public void setUp(){
        energyMeterRepository = mock(EnergyMeterRepository.class);
        meterBrandRepository = mock(MeterBrandRepository.class);
        meterModelRepository=mock(MeterModelRepository.class);
        energyMeterService = new EnergyMeterService(energyMeterRepository, meterBrandRepository,meterModelRepository);
    }
    @Test
    public void getEnergyMeterById_TestOK() throws EnergyMeterNotExists {
        when(energyMeterRepository.findById(1)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter()));
        EnergyMeter response= energyMeterService.getEnergyMeterById(1);
        assertEquals("001",response.getSerialNumber() );
    }
    @Test
    public void getEnergyMeterById_TestFAIL()  {
        when(energyMeterRepository.findById(1)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter()));
        assertThrows(EnergyMeterNotExists.class,()->energyMeterService.getEnergyMeterById(8));

    }

}
