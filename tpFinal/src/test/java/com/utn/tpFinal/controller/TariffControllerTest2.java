package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.model.dto.TariffDto;
import com.utn.tpFinal.service.TariffService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


//@SpringBootTest(classes = TariffController.class)  //especificamos sobre que clase va a trabajar asi se ejecuta cuando corro los test
public class TariffControllerTest2 {

    TariffService tariffService; //dependencia externa

    TariffController tariffController;

    @Before
    public void setUp(){
        tariffService = mock(TariffService.class);

        tariffController = new TariffController(tariffService);
    }

    @Test
    public void getAllTariffsTest(){
        //give
        Integer page = 0;
        Integer size = 10;
        Specification<Tariff> specification = mock(Specification.class);

        List<TariffDto> tariffs = new ArrayList<>();
        tariffs.add(TariffDto.builder().tariff("A1").amount(Float.valueOf(10)).build());
        tariffs.add(TariffDto.builder().tariff("A2").amount(Float.valueOf(20)).build());

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "id"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "name"));

        Page<TariffDto> mockedPage = mock(Page.class);

        when(mockedPage.getContent()).thenReturn(tariffs);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(tariffs.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(tariffService.getAll(specification,page,size,orders)).thenReturn(mockedPage);

        //then
        ResponseEntity<List<TariffDto>>response = tariffController.getAll(page,size,"id","name",specification);

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("A1", response.getBody().get(0).getTariff());

    }



}
