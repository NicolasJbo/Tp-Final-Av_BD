package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.CustomGenericException;
import com.utn.tpFinal.exception.TariffNotExists;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.repository.TariffRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

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

    @Test(expected = CustomGenericException.class)
    public void testNotExists(){
        CustomGenericException t = new TariffNotExists("TEST","testNotExists");

        when(tariffRepository.findById(any(Integer.class))).thenThrow(TariffNotExists.class);

        try {
            Tariff tariff = tariffService.getTariffById(8);

        } catch (CustomGenericException tariffNotExists) {
            assertEquals("TEST", tariffNotExists.getRoute());
            tariffNotExists.printStackTrace();
        }


    }


}
