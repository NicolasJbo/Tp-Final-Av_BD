package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.CustomGenericException;
import com.utn.tpFinal.exception.TariffNotExists;
import com.utn.tpFinal.exception.TariffsDoNotMatch;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.dto.TariffDto;
import com.utn.tpFinal.repository.TariffRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TariffServiceTest {

    TariffRepository tariffRepository;
    ResidenceService residenceService;
    TariffService tariffService;

    @Before
    public void setUp(){
        tariffRepository = mock(TariffRepository.class);
        residenceService = mock(ResidenceService.class);
        tariffService = new TariffService(tariffRepository, residenceService);
    }

    @Test
    public void addTariff_Test200(){
        Tariff t = UTILS_TESTCONSTANTS.getTariff(4);
        when(tariffRepository.save(any(Tariff.class))).thenReturn(t);

        Tariff tariff = tariffService.add(t);

        assertEquals("TariffExample", tariff.getName());
        assertEquals(Float.valueOf(50), tariff.getAmount());

    }

    @Test
    public void getTariffById_Test200() throws TariffNotExists {

        Tariff t = UTILS_TESTCONSTANTS.getTariff(8);

        when(tariffRepository.findById(any(Integer.class))).thenReturn(Optional.of(t));

        Tariff tariff = tariffService.getTariffById(8);

        assertEquals("TariffExample", tariff.getName());
        assertEquals(Float.valueOf(50), tariff.getAmount());

    }

    @Test
    public void getTariffById_TestException(){
        when(tariffRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        assertThrows(TariffNotExists.class,()->tariffService.getTariffById(8));
    }

    /*@Test(expected = CustomGenericException.class)
      public void testNotExists(){
        CustomGenericException t = new TariffNotExists("TEST","testNotExists");

        when(tariffRepository.findById(any(Integer.class))).thenThrow(TariffNotExists.class);

        try {
            Tariff tariff = tariffService.getTariffById(8);

        } catch (CustomGenericException tariffNotExists) {
            assertEquals("TEST", tariffNotExists.getRoute());
            tariffNotExists.printStackTrace();
        }
    }*/
    @Test
    public void getAll_Test200(){
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;

        Specification<Tariff>specification=mock(Specification.class);
        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));

        Page<Tariff> pageT =new PageImpl<Tariff>(UTILS_TESTCONSTANTS.getTariff_List());
        when(tariffRepository.findAll(specification,pageable)).thenReturn(pageT);

        Page<TariffDto> response = tariffService.getAll(specification,0,2,orders);
        assertEquals(false,response.getContent().isEmpty());
        assertEquals("TariffExample",response.getContent().get(0).getTariff());
        assertEquals(2,response.getNumberOfElements());
    }
    @Test
    public void getAll_Test204(){
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;

        Specification<Tariff>specification=mock(Specification.class);
        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));

        Page<Tariff> pageT =new PageImpl<Tariff>(Collections.emptyList());
        when(tariffRepository.findAll(specification,pageable)).thenReturn(pageT);

        Page<TariffDto> response = tariffService.getAll(specification,0,2,orders);

        assertEquals(true,response.getContent().isEmpty());
    }
    /*//todo ???
    @Test
    public void addResidenceToTariff_Test(){
        Tariff tariff = UTILS_TESTCONSTANTS.getTariff(1);

        Residence residence= UTILS_TESTCONSTANTS.getResidence(1);
       // tariff.getResidencesList().add(residence);
        when(tariffRepository.save(any(Tariff.class))).thenReturn(tariff);

       tariffService.addResidenceToTariff(residence,tariff);
    }
     */
    @Test
    public  void getResidencesByTariff_Test200() throws ParseException {
        try {
            List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;

        Specification<Tariff>specification=mock(Specification.class);
        Pageable pageable = PageRequest.of(0,2,Sort.by(orders));

        List<Residence> list = new ArrayList<>();
        list.add(UTILS_TESTCONSTANTS.getResidence(1));
        list.add(UTILS_TESTCONSTANTS.getResidence(2));

        Page<Residence> pageR =new PageImpl<Residence>(list);
        when(tariffRepository.existsById(1)).thenReturn(true);
        when(residenceService.getResidencesByTariffId(1,pageable)).thenReturn(pageR);

        Page<ResidenceDto> response = null;

            response = tariffService.getResidencesByTariff(1,0,2,orders);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals("Siempre Viva",response.getContent().get(0).getStreet());
        assertEquals(2,response.getNumberOfElements());
    } catch (TariffNotExists tariffNotExists) {
            Assert.fail("Falle");
    }

    }
    @Test
    public  void getResidencesByTariff_TestFAIL() throws ParseException {

            List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;


            Pageable pageable = PageRequest.of(0,2,Sort.by(orders));

            List<Residence> list = new ArrayList<>();
            list.add(UTILS_TESTCONSTANTS.getResidence(1));
            list.add(UTILS_TESTCONSTANTS.getResidence(2));

            Page<Residence> pageR =new PageImpl<Residence>(list);
            when(tariffRepository.existsById(1)).thenReturn(false);
            when(residenceService.getResidencesByTariffId(1,pageable)).thenReturn(pageR);

            assertThrows(TariffNotExists.class,()->tariffService.getResidencesByTariff(1,0,2,orders));

    }
    @Test
    public void deleteTariffById_TestFAIL(){
        assertThrows(TariffNotExists.class,()->tariffService.deleteTariffById(1));
    }
    @Test
    public void deleteTariffById_Test200() throws TariffNotExists {
        Integer idTarif=1;
        when(tariffRepository.existsById(1)).thenReturn(true);
        tariffService.deleteTariffById(idTarif);
    }
    //todo este no anda
    @Test
    public  void modifyTariff_Test200() throws TariffsDoNotMatch, TariffNotExists {

            Integer mod=1;
        Tariff t = UTILS_TESTCONSTANTS.getTariff(1);
        Integer idT=1;
        when(tariffRepository.findById(idT)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getTariff(1)));
        doNothing().when(tariffRepository.save(any(Tariff.class)));

        tariffService.modifyTariff(mod,t);


    }
    @Test
    public void modifyTariff_TestFAIL(){
        when(tariffRepository.findById(2)).thenReturn(Optional.of(UTILS_TESTCONSTANTS.getTariff(2)));
        assertThrows(TariffsDoNotMatch.class,()->tariffService.modifyTariff(2,UTILS_TESTCONSTANTS.getTariff(1)));
    }




}
