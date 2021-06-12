package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.TariffNotExists;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.MeterBrand;
import com.utn.tpFinal.model.MeterModel;
import com.utn.tpFinal.repository.EnergyMeterRepository;
import com.utn.tpFinal.repository.MeterBrandRepository;
import com.utn.tpFinal.repository.MeterModelRepository;
import com.utn.tpFinal.repository.TariffRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

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
        when(energyMeterRepository.findById(1)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter(1)));
        EnergyMeter response= energyMeterService.getEnergyMeterById(1);
        assertEquals("001",response.getSerialNumber() );
    }
    @Test
    public void getEnergyMeterById_TestFAIL()  {
        when(energyMeterRepository.findById(1)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter(1)));
        assertThrows(EnergyMeterNotExists.class,()->energyMeterService.getEnergyMeterById(8));
    }

    @Test
    public void getEnergyMeterBySerialNumber_TestOK() throws EnergyMeterNotExists {
        when(energyMeterRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter(1)));
        EnergyMeter response= energyMeterService.getEnergyMeterBySerialNumber(anyString());
        assertEquals("001",response.getSerialNumber() );
    }
    @Test
    public void getEnergyMeterBySerialNumber_TestFAIL()  {
        when(energyMeterRepository.findBySerialNumber("hola")).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter(1)));
        assertThrows(EnergyMeterNotExists.class,()->energyMeterService.getEnergyMeterBySerialNumber(anyString()));
    }
    
   /* @Test
    public void add_Test200(){
        EnergyMeter meter= UTILS_TESTCONSTANTS.getEnergyMeter(1);
        Integer idModel=1;
        Integer idBrand=1;

        when(energyMeterRepository.save(any(EnergyMeter.class))).thenReturn(meter);
        when(meterBrandRepository.findById(idBrand)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getMeterBrand_List().get(0)));
        when(meterModelRepository.findById(idModel)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getMeterModel_List().get(0)));
        doNothing().when(meterBrandRepository.save(any(MeterBrand.class))) ;
        //doNothing().when(meterModelRepository.save(any(MeterModel.class)));

        EnergyMeter response= energyMeterService.add(meter,idModel,idBrand);

    }
    @Test
    public void addEnergyMeterToBrand(){
        doNothing().when(meterBrandRepository.save(any(MeterBrand.class))) ;
        energyMeterService.addEnergyMeterToBrand(UTILS_TESTCONSTANTS.getEnergyMeter(1),UTILS_TESTCONSTANTS.getMeterBrand_List().get(0));

    }*/

}
