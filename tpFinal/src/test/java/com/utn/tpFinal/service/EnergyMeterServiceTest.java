package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.*;
import com.utn.tpFinal.model.dto.EnergyMeterDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.dto.TariffDto;
import com.utn.tpFinal.repository.EnergyMeterRepository;
import com.utn.tpFinal.repository.MeterBrandRepository;
import com.utn.tpFinal.repository.MeterModelRepository;
import com.utn.tpFinal.repository.TariffRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public void getEnergyMeterById_TestOK() throws EnergyMeterNotExists, ParseException {
        when(energyMeterRepository.findById(1)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter(1)));
        EnergyMeter response= energyMeterService.getEnergyMeterById(1);
        assertEquals("001",response.getSerialNumber() );
    }

    @Test
    public void getEnergyMeterById_TestFAIL()  throws ParseException {
        when(energyMeterRepository.findById(1)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter(1)));
        assertThrows(EnergyMeterNotExists.class,()->energyMeterService.getEnergyMeterById(8));
    }

    @Test
    public void getEnergyMeterBySerialNumber_TestOK() throws EnergyMeterNotExists , ParseException{
        when(energyMeterRepository.findBySerialNumber(anyString())).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter(1)));
        EnergyMeter response= energyMeterService.getEnergyMeterBySerialNumber(anyString());
        assertEquals("001",response.getSerialNumber() );
    }

    @Test
    public void getEnergyMeterBySerialNumber_TestFAIL() throws ParseException {
        when(energyMeterRepository.findBySerialNumber("hola")).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getEnergyMeter(1)));
        assertThrows(EnergyMeterNotExists.class,()->energyMeterService.getEnergyMeterBySerialNumber(anyString()));
    }

    @Test
    public void add_Test200() throws ParseException, MeterModelNotExist, MeterBrandNotExist {
        EnergyMeter meter= UTILS_TESTCONSTANTS.getEnergyMeter(1);
        Integer idModel=1;
        Integer idBrand=1;

        Residence r =UTILS_TESTCONSTANTS.getResidence(1);
        EnergyMeter e= UTILS_TESTCONSTANTS.getEnergyMeter(1);

        e.setResidence(r);
        List<EnergyMeter>list=new ArrayList<>();
        list.add(e);

        when(energyMeterRepository.save(any(EnergyMeter.class))).thenReturn(meter);
        when(meterBrandRepository.findById(idBrand)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getMeterBrand_List().get(0)));
        when(meterModelRepository.findById(idModel)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getMeterModel_List().get(0)));
        //------------------------------------------------------------------------
        when(meterBrandRepository.save(UTILS_TESTCONSTANTS.getMeterBrand_List().get(0))).thenReturn(MeterBrand.builder().build()) ;
        //-----------------------------------------------------------
        when(meterModelRepository.save(UTILS_TESTCONSTANTS.getMeterModel_List().get(0))).thenReturn(MeterModel.builder().build()) ;
        //-----------------------------------------------------------
        EnergyMeter response= energyMeterService.add(meter,1,1);
        assertEquals("001",response.getSerialNumber());

    }
    @Test
    public void getAll_Test200() throws ParseException {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;

        Specification<EnergyMeter> specification=mock(Specification.class);
        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));
        List<EnergyMeter> listE = new ArrayList<>();
        listE.add(UTILS_TESTCONSTANTS.getEnergyMeter(1));
        listE.add(UTILS_TESTCONSTANTS.getEnergyMeter(2));
        Page<EnergyMeter> pageE =new PageImpl<EnergyMeter>(listE);
        when(energyMeterRepository.findAll(specification,pageable)).thenReturn(pageE);

        Page<EnergyMeterDto> response = energyMeterService.getAll(specification,0,2,orders);
        assertEquals(false,response.getContent().isEmpty());
        assertEquals("001",response.getContent().get(0).getSerialNumber());
        assertEquals(2,response.getNumberOfElements());
    }

    @Test
    public void getAll_Test204() throws ParseException {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;
        Specification<EnergyMeter> specification=mock(Specification.class);
        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));


        when(energyMeterRepository.findAll(specification,pageable)).thenReturn(Page.empty(pageable));

        Page<EnergyMeterDto> response = energyMeterService.getAll(specification,0,2,orders);

        assertEquals(true,response.getContent().isEmpty());

    }

    @Test
    public void addClientToResidence_Test200() throws ParseException {
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);
        EnergyMeter e = UTILS_TESTCONSTANTS.getEnergyMeter(4);

        when(energyMeterRepository.save(any(EnergyMeter.class))).thenReturn(e);

        energyMeterService.addResidenceToMeter(r,e);

        assertEquals(e.getId(), r.getId());
    }
    @Test
    public void getResidenceByEnergyMeterId_Test() throws ParseException, ResidenceNotDefined, EnergyMeterNotExists {

        EnergyMeter e=UTILS_TESTCONSTANTS.getEnergyMeter(1);
        Residence residence= UTILS_TESTCONSTANTS.getResidence(1);
        e.setResidence(residence);
        when(energyMeterRepository.findById(1)).thenReturn(Optional.of(e));
        ResidenceDto response= energyMeterService.getResidenceByEnergyMeterId(1);
        assertEquals("Siempre Viva",response.getStreet());

    }
    @Test
    public void getResidenceByEnergyMeterId_TestFAIL() throws ParseException, ResidenceNotDefined, EnergyMeterNotExists {
        EnergyMeter e=UTILS_TESTCONSTANTS.getEnergyMeter(1);
        Residence residence= UTILS_TESTCONSTANTS.getResidence(1);
        when(energyMeterRepository.findById(1)).thenReturn(Optional.of(e));
        assertThrows(ResidenceNotDefined.class,()->energyMeterService.getResidenceByEnergyMeterId(1));
    }
    @Test
    public void getAllMeterBrands_TestOK(){
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;
        Specification<MeterBrand> specification=mock(Specification.class);
        Pageable pageable= PageRequest.of(10,10,Sort.by(orders));

        Page<MeterBrand> pageE =new PageImpl<MeterBrand>(UTILS_TESTCONSTANTS.getMeterBrand_List());
        when(meterBrandRepository.findAll(specification,pageable)).thenReturn(pageE);

        Page<MeterBrand>response =energyMeterService.getAllMeterBrands(specification,10,10,orders);

        assertEquals("brand1",response.getContent().get(0).getName());
        assertEquals(2,response.getTotalElements());

    }
    @Test
    public void getAllMeterBrands_TestFAIL(){
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;
        Specification<MeterBrand> specification=mock(Specification.class);
        Pageable pageable= PageRequest.of(10,10,Sort.by(orders));

        Page<MeterBrand> pageE =new PageImpl<MeterBrand>(UTILS_TESTCONSTANTS.getMeterBrand_List());
        when(meterBrandRepository.findAll(specification,pageable)).thenReturn(Page.empty());

        Page<MeterBrand>response =energyMeterService.getAllMeterBrands(specification,10,10,orders);

        assertEquals(true,response.isEmpty());
    }
    @Test
    public void getAllMeterModel_TestOK()  {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;
        Specification<MeterModel> specification=mock(Specification.class);
        Pageable pageable= PageRequest.of(10,1,Sort.by(orders));

        List<MeterModel>list=new ArrayList<>();
        list.add(MeterModel.builder().id(1).name("model1").energyMeters(Collections.emptyList()).build());
        list.add(MeterModel.builder().id(2).name("model2").energyMeters(Collections.emptyList()).build());

        Page<MeterModel> pageM =new PageImpl<>(list);
        when(meterModelRepository.findAll(specification,pageable)).thenReturn(pageM);

        Page<MeterModel>response =energyMeterService.getAllMeterModels(specification,10,1,orders);

        assertEquals("model1",response.getContent().get(0).getName());
        assertEquals(2,response.getTotalElements());
    }
    @Test
    public void getAllMeterModel_TestFAIL(){
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;
        Specification<MeterModel> specification=mock(Specification.class);
        Pageable pageable= PageRequest.of(10,10,Sort.by(orders));

        Page<MeterModel> pageE =new PageImpl<MeterModel>(UTILS_TESTCONSTANTS.getMeterModel_List());
        when(meterModelRepository.findAll(specification,pageable)).thenReturn(Page.empty());

        Page<MeterModel>response =energyMeterService.getAllMeterModels(specification,10,10,orders);

        assertEquals(true,response.isEmpty());
    }
    @Test
    public  void deleteEnergyMeterById_TestOk() throws EnergyMeterNotExists {
        when(energyMeterRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(energyMeterRepository).deleteById(anyInt());

        String response = energyMeterService.deleteEnergyMeterById(4);

        assertEquals("deleted", response);

    }
    @Test
    public  void deleteEnergyMeterById_TestFAIL(){
        assertThrows(EnergyMeterNotExists.class,()->energyMeterService.deleteEnergyMeterById(anyInt()));
    }

    @Test
    public void getMeterBrandById_TestException(){
        assertThrows(MeterBrandNotExist.class,()->energyMeterService.getMeterBrandById(anyInt()));

    }

    @Test
    public void getMeterModelById_TestException(){
        assertThrows(MeterModelNotExist.class,()->energyMeterService.getMeterModelById(anyInt()));

    }

}
