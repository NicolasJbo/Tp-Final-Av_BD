package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.exception.ResidenceNotExists;
import com.utn.tpFinal.exception.TariffNotExists;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.dto.TariffDto;
import com.utn.tpFinal.repository.BillRepository;
import com.utn.tpFinal.repository.MeasureRepository;
import com.utn.tpFinal.repository.ResidenceRepository;
import com.utn.tpFinal.repository.TariffRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    public void getResidenceById_Test200() throws ResidenceNotExists, ParseException {
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

    @Test
    public void addResidence_Test200() throws ParseException {
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);

        when(residenceRepository.save(any(Residence.class))).thenReturn(r);

        Residence residence = residenceService.addResidence(r);

        assertEquals("Siempre Viva", residence.getStreet());
        assertEquals(Integer.valueOf(1234), residence.getNumber());
    }

    @Test
    public void getAll_Test200() throws ParseException {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","street") ;

        Specification<Residence> specification=mock(Specification.class);
        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));

        Page<Residence> residences =new PageImpl<Residence>(UTILS_TESTCONSTANTS.getResidenceList());

        when(residenceRepository.findAll(specification,pageable)).thenReturn(residences);

        Page<ResidenceDto> response = residenceService.getAll(specification,0,2,orders);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals("Siempre Viva",response.getContent().get(0).getStreet());
        assertEquals(2,response.getNumberOfElements());
    }

    @Test
    public void getAll_Test204(){
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","street") ;

        Specification<Residence> specification=mock(Specification.class);
        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));

        Page<Residence> residences =new PageImpl<>(Collections.emptyList());

        when(residenceRepository.findAll(specification,pageable)).thenReturn(residences);

        Page<ResidenceDto> response = residenceService.getAll(specification,0,2,orders);

        assertEquals(true,response.getContent().isEmpty());
    }



    @Test
    public void getResidenceByClientId_Test200() throws ParseException {

        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","street") ;
        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));
        Page<Residence> residences =new PageImpl<>(UTILS_TESTCONSTANTS.getResidenceList());

        when(residenceRepository.findByClientId(any(Integer.class), any(Pageable.class))).thenReturn(residences);

        Page<Residence>response = residenceService.getResidenceByClientId(4,pageable);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals("Siempre Viva",response.getContent().get(0).getStreet());
        assertEquals(2,response.getNumberOfElements());
    }

    @Test
    public void getResidencesByTariffId_Test200() throws ParseException {

        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","street") ;
        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));
        Page<Residence> residences =new PageImpl<>(UTILS_TESTCONSTANTS.getResidenceList());

        when(residenceRepository.findByTariffId(any(Integer.class), any(Pageable.class))).thenReturn(residences);

        Page<Residence>response = residenceService.getResidencesByTariffId(4,pageable);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals("Siempre Viva",response.getContent().get(0).getStreet());
        assertEquals(2,response.getNumberOfElements());
    }

    @Test
    public void addClientToResidence_Test200() throws ParseException {
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);
        Client c = UTILS_TESTCONSTANTS.getClient(4);

        when(residenceRepository.save(any(Residence.class))).thenReturn(r);

        residenceService.addClientToResidence(c,r);

        assertEquals(c.getId(), r.getClient().getId());
    }

   /* @Test
    public void addEnergyMeterToResidence_Test200() throws EnergyMeterNotExists {
        EnergyMeter e = UTILS_TESTCONSTANTS.getEnergyMeter(4);

        when(energyMeterService.getEnergyMeterById(any(Integer.class))).thenReturn(e);

    }*/

}
