package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.ResidenceNotExists;
import com.utn.tpFinal.exception.TariffNotExists;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.repository.BillRepository;
import com.utn.tpFinal.repository.MeasureRepository;
import com.utn.tpFinal.repository.ResidenceRepository;
import com.utn.tpFinal.repository.TariffRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResidenceServiceTest {

    private ResidenceRepository residenceRepository;
    private EnergyMeterService  energyMeterService;
    private TariffService tariffService;
    private MeasureRepository measureRepository;
    private BillRepository billRepository;
    private ResidenceService residenceService;

   /* @Before
    public void setUp(){
        residenceRepository = mock(ResidenceRepository.class);
        energyMeterService = mock(EnergyMeterService.class);
        residenceRepository = mock(ResidenceRepository.class);
        tariffService = mock(TariffService.class);
        measureRepository = mock(MeasureRepository.class);
        billRepository = mock(BillRepository.class);
        residenceService = new ResidenceService();
    }*/

    @Before
    public void setUp(){
        residenceRepository = mock(ResidenceRepository.class);
        energyMeterService = mock(EnergyMeterService.class);
        residenceRepository = mock(ResidenceRepository.class);
        tariffService = mock(TariffService.class);
        measureRepository = mock(MeasureRepository.class);
        billRepository = mock(BillRepository.class);
        residenceService = new ResidenceService(residenceRepository,energyMeterService,tariffService,measureRepository,billRepository);
    }


    @Test
    public void addResidence_Test200(){
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);

        when(residenceRepository.save(any(Residence.class))).thenReturn(r);

        Residence residence = residenceService.addResidence(r);

        assertEquals("Siempre Viva", residence.getStreet());
        assertEquals(Integer.valueOf(1234), residence.getNumber());
    }

    @Test
    public void getResidenceById_Test200() throws ResidenceNotExists {
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);

        when(residenceRepository.findById(any(Integer.class))).thenReturn(Optional.of(r));

        Residence residence = residenceService.getResidenceById(4);

        assertEquals("Siempre Viva", residence.getStreet());
        assertEquals(Integer.valueOf(1234), residence.getNumber());
    }

    @Test
    public void getResidenceById_TestException(){
        when(residenceRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(ResidenceNotExists.class,()->residenceService.getResidenceById(8));
    }
    
    //todo get all test = al de tariff service test
    
    @Test
    public void getResidenceByClientId(){
        List<Residence> residences = UTILS_TESTCONSTANTS.getResidendesList();
    }

}
