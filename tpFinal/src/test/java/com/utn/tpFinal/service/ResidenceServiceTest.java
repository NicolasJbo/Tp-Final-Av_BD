package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.*;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.MeasureDto;
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
import java.util.ArrayList;
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

   @Test
    public void addEnergyMeterToResidence_Test200() throws ParseException, EnergyMeterNotExists, ResidenceNotExists, ResidenceDefined {
        EnergyMeter e = UTILS_TESTCONSTANTS.getEnergyMeter(4);
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);

        when(energyMeterService.getEnergyMeterById(any(Integer.class))).thenReturn(e);
        when(residenceRepository.findById(any(Integer.class))).thenReturn(Optional.of(r));
        doNothing().when(energyMeterService).addResidenceToMeter(any(Residence.class),any(EnergyMeter.class));
        when(residenceRepository.save(any(Residence.class))).thenReturn(r);

        residenceService.addEnergyMeterToResidence(4, 4);

        assertEquals(e.getId(), r.getEnergyMeter().getId());
    }

    @Test
    public void addEnergyMeterToResidence_TestException() throws ParseException, EnergyMeterNotExists, ResidenceNotExists, ResidenceDefined {
        EnergyMeter e = UTILS_TESTCONSTANTS.getEnergyMeter(4);
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);
        e.setResidence(r);

        when(energyMeterService.getEnergyMeterById(any(Integer.class))).thenReturn(e);

        assertThrows(ResidenceDefined.class,()->residenceService.addEnergyMeterToResidence(4, 4));
    }

    @Test
    public void addTariffToResidence_Test200() throws Exception {
        Tariff t = UTILS_TESTCONSTANTS.getTariff(4);
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);


        when(residenceRepository.findById(any(Integer.class))).thenReturn(Optional.of(r));
        when(tariffService.getTariffById(any(Integer.class))).thenReturn(t);
        doNothing().when(tariffService).addResidenceToTariff(any(Residence.class),any(Tariff.class));
        when(residenceRepository.save(any(Residence.class))).thenReturn(r);

        residenceService.addTariffToResidence(4, 4);

        assertEquals(t.getId(), r.getTariff().getId());
    }

    @Test
    public void removeResidenceById_TestException(){
        when(residenceRepository.existsById(any(Integer.class))).thenReturn(false);
        assertThrows(ResidenceNotExists.class,()->residenceService.removeResidenceById(4));
    }



   @Test
    public void removeResidenceById_Test200() throws ResidenceNotExists {
        when(residenceRepository.existsById(any(Integer.class))).thenReturn(true);
        doNothing().when(residenceRepository).deleteById(any(Integer.class));

        String response = residenceService.removeResidenceById(4);

        assertEquals("deleted", response);
   }


    @Test
    public void modifyResidence_TestException() throws ParseException{
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);
        Residence residence = UTILS_TESTCONSTANTS.getResidence(9);

        when(residenceRepository.findById(any(Integer.class))).thenReturn(Optional.of(r));

        assertThrows(ResidencesDoNotMatch.class,()-> residenceService.modifyResidence(4,residence));
    }

    @Test
    public void modifyResidence_Test200() throws ParseException, ResidencesDoNotMatch, ResidenceNotExists {
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);

        when(residenceRepository.findById(any(Integer.class))).thenReturn(Optional.of(r));
        when(residenceRepository.save(any(Residence.class))).thenReturn(r);

        residenceService.modifyResidence(8, r);

        assertEquals(Integer.valueOf(4), r.getId());
    }

    @Test
    public void getResidenceByEnergyMeterId_Test200() throws ParseException {
        Residence residence = UTILS_TESTCONSTANTS.getResidence(4);

        when(residenceRepository.findByEnergyMeterId(any(Integer.class))).thenReturn(residence);

        Residence response = residenceService.getResidenceByEnergyMeterId(4);

        assertEquals(Integer.valueOf(4),response.getEnergyMeter().getId());
        assertEquals("Siempre Viva",response.getStreet());
    }

    @Test
    public void getResidenceMeasuresByDates_TestOK() throws ParseException {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","street") ;
        Pageable pageable = PageRequest.of(1,10,Sort.by(orders));

        Measure m2 =UTILS_TESTCONSTANTS.getMeasure(2);

        List<Measure> list = new ArrayList<>();
        list.add(UTILS_TESTCONSTANTS.getMeasure(4));
        list.add(m2);
        Page<Measure> pageM =new PageImpl<Measure>(list);
        when(measureRepository.findByResidenceIdAndDateBetween(1,UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2),pageable)).thenReturn(pageM);

        Page<MeasureDto> response= residenceService.getResidenceMeasuresByDates(1,UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2),1,10,orders);
        assertEquals("100.0",String.valueOf(response.getContent().get(0).getPrice()));
        assertEquals(2   ,response.getNumberOfElements());
    }

    @Test
    public void getResidenceMeasuresByDates_TestFail() throws ParseException {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","street") ;
        Pageable pageable = PageRequest.of(1,10,Sort.by(orders));

        when(measureRepository.findByResidenceIdAndDateBetween(1,UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2),pageable)).thenReturn(Page.empty());

        Page<MeasureDto>response = residenceService.getResidenceMeasuresByDates(1, UTILS_TESTCONSTANTS.getFecha(1), UTILS_TESTCONSTANTS.getFecha(2), 1,10,orders);
        assertEquals(true, response.getContent().isEmpty());
    }

    @Test
    public void getResidenceUnpaidBills_Test200() throws ParseException, ResidenceNotExists {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","initialDate") ;

        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));

        List<Bill> bills = new ArrayList<>();
        bills.add(UTILS_TESTCONSTANTS.getBill(4));
        bills.add(UTILS_TESTCONSTANTS.getBill(5));

        Page<Bill> page =new PageImpl<>(bills);
        when(residenceRepository.existsById(anyInt())).thenReturn(true);
        when(billRepository.findByIsPaidFalseAndResidenceId(any(Integer.class),any(Pageable.class))).thenReturn(page);

        Page<BillDto> response = residenceService.getResidenceUnpaidBills(4,0,2,orders);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals(Float.valueOf(1500), response.getContent().get(0).getFinalAmount());
        assertEquals(2,response.getNumberOfElements());
    }

    @Test
    public void getResidenceUnpaidBills_TestResidenceNotExistsException() {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","initialDate") ;

        when(residenceRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ResidenceNotExists.class,()->residenceService.getResidenceUnpaidBills(4,0,2,orders));
    }

    @Test
    public void getResidenceUnpaidBills_TestNoContent() throws ResidenceNotExists {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","initialDate") ;

        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));

        when(residenceRepository.existsById(anyInt())).thenReturn(true);
        when(billRepository.findByIsPaidFalseAndResidenceId(any(Integer.class),any(Pageable.class))).thenReturn(Page.empty(pageable));

        Page<BillDto> response = residenceService.getResidenceUnpaidBills(4,0,2,orders);

        assertEquals(true,response.getContent().isEmpty());

    }

}
