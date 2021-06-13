package com.utn.tpFinal.controller;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.model.dto.EnergyMeterDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.dto.TariffDto;
import com.utn.tpFinal.service.TariffService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
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
public class TariffControllerTest {

    TariffService tariffService; //dependencia externa

    TariffController tariffController;

    @Before
    public void setUp(){
        tariffService = mock(TariffService.class);

        tariffController = new TariffController(tariffService);
    }

    @Test
    public void getAllTariffs_Test200(){
        //give
        Integer page = 0;
        Integer size = 10;
        Specification<Tariff> specification = mock(Specification.class);

        List<TariffDto> tariffs = UTILS_TESTCONSTANTS.getTariffDTO_List();
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
    @Test
    public void getAllTariffs_Test204(){
        //give
        Integer page = 0;
        Integer size = 10;
        Specification<Tariff> specification = mock(Specification.class);

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "id"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "name"));

        Page<TariffDto> emptyPage = Page.empty();

        when(tariffService.getAll(specification,page,size,orders)).thenReturn(emptyPage);
        //then
        ResponseEntity<List<TariffDto>>response = tariffController.getAll(page,size,"id","name",specification);
        //assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    @Test
    public  void getResidencesByTariff_Test200() throws Exception {
        //give
        Integer page = 0;
        Integer size = 10;
        Integer idTariff=1;


        EnergyMeterDto energyMeterDto = EnergyMeterDto.builder().brandName("brand1").serialNumber("001").modelName("model1").passWord("1234").build();
        EnergyMeterDto energyMeterDto2 = EnergyMeterDto.builder().brandName("brand1").serialNumber("002").modelName("model1").passWord("1234").build();

        ResidenceDto residenceDto =ResidenceDto.builder().street("calle1").number(123).client("nicolas").energyMeter(energyMeterDto).id(1).build();
        ResidenceDto residenceDto2 =ResidenceDto.builder().street("calle2").number(222).client("lautaro").energyMeter(energyMeterDto2).id(2).build();


        List<ResidenceDto> residences = new ArrayList<ResidenceDto>();
        residences.add(residenceDto);
        residences.add(residenceDto2);

        //Tariff tariff= Tariff.builder().id(1).name("tarifa1").amount(100F).residencesList(residences).build();


        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "id"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "name"));

        Page<ResidenceDto> mockedPage = mock(Page.class);

        when(mockedPage.getContent()).thenReturn(residences);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(residences.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(tariffService.getResidencesByTariff(idTariff,page,size,orders)).thenReturn(mockedPage);
        //then

        ResponseEntity<List<ResidenceDto>>response = tariffController.getResidencesByTariff(idTariff,page,size,"id","name");

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("calle1", response.getBody().get(0).getStreet());

    }
    @Test
    public  void getResidencesByTariff_Test204() throws Exception {
        //give
        Integer page = 0;
        Integer size = 10;
        Integer idTariff=1;

        //Tariff tariff= Tariff.builder().id(1).name("tarifa1").amount(100F).residencesList(residences).build();

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "id"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "name"));

        Page<ResidenceDto> emptyPage = Page.empty();

        when(tariffService.getResidencesByTariff(idTariff,page,size,orders)).thenReturn(emptyPage);
        //then

        ResponseEntity<List<ResidenceDto>>response = tariffController.getResidencesByTariff(idTariff,page,size,"id","name");

        //assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());


    }


}
