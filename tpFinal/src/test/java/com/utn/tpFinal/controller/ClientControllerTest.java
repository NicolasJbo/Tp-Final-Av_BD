package com.utn.tpFinal.controller;

import com.utn.tpFinal.controller.ClientController;
import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.dto.*;
import com.utn.tpFinal.service.BillService;
import com.utn.tpFinal.service.ClientService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientControllerTest {

    private final String SORTFIELD1 = "lastName";
    private final String SORTFIELD2 = "dni";

    ClientService   clientService;   // externa
    BillService billService;        // externa
    ClientController clientController;
    private UserDto userDto;

    @Before
    public void setUp(){
        clientService = mock(ClientService.class);
        clientController = new ClientController(clientService,billService);
    }

    @Test
    public void getAllClients_Test200() throws Exception {
        //give
        Integer page = 0;
        Integer size = 10;
        Specification<Client> specification = mock(Specification.class);

        ClientDto client1 = ClientDto.builder().client("Carlos").dni("11111111").birthday("2020-02-02").build();
        ClientDto client2 = ClientDto.builder().client("Mati").dni("22222222").birthday("2020-02-02").build();

        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(client1);
        clientDtos.add(client2);

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, SORTFIELD1));
        orders.add(new Sort.Order(Sort.Direction.ASC, SORTFIELD2));

        Page<ClientDto> mockedPage = mock(Page.class);

        when(mockedPage.getContent()).thenReturn(clientDtos);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(clientDtos.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(clientService.getAll(specification,page,size,orders)).thenReturn(mockedPage);

        //then
        ResponseEntity<List<ClientDto>> response = clientController.getAll(page,size,SORTFIELD1,SORTFIELD2,specification);

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("Mati", response.getBody().get(1).getClient());

    }

    @Test
    public void getAllClients_Test204() throws Exception {
        //give
        Integer page = 0;
        Integer size = 10;
        Specification<Client> specification = mock(Specification.class);

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, SORTFIELD1));
        orders.add(new Sort.Order(Sort.Direction.ASC, SORTFIELD2));

        Page<ClientDto> emptyPage = Page.empty();

        when(clientService.getAll(specification,page,size,orders)).thenReturn(emptyPage);
        //then
        ResponseEntity<List<ClientDto>>response = clientController.getAll(page,size,SORTFIELD1,SORTFIELD2,specification);
        //assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public  void getById_Test200() throws ClientNotExists {
        try {
            Integer idClient =1;
            Authentication authenticator =mock(Authentication.class);

            List<Residence> red =new ArrayList<>();

            List list = new ArrayList<SimpleGrantedAuthority>();
            list.add(new SimpleGrantedAuthority("CLIENT"));


            Date birtday = new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-02");
            Client client= Client.builder().id(1).name("Carlos").lastName("Perez").birthday(birtday).dni("1111111").residencesList(red).build();
            UserDto userDto = UserDto.builder().id(idClient).mail("carlos@gmail.com").build();

            when(authenticator.getAuthorities()).thenReturn(list);
            when(authenticator.getPrincipal()).thenReturn(userDto);
            when(clientService.getClientById(idClient)).thenReturn(client);

            //then
            ResponseEntity<ClientDto> response = clientController.getById(authenticator,idClient);

            //assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("PEREZ CARLOS",response.getBody().getClient());
        } catch (ParseException e) {
                Assert.fail("No se cargo bien la fecha");
        }

    }

    @Test
    public  void getById_Test403() throws ClientNotExists {

            Integer idClient =1;
            Authentication authenticator =mock(Authentication.class);

            List list = new ArrayList<SimpleGrantedAuthority>();
            list.add(new SimpleGrantedAuthority("INVALID"));

            UserDto userDto = UserDto.builder().id(idClient).mail("carlos@gmail.com").build();

            when(authenticator.getAuthorities()).thenReturn(list);
            when(authenticator.getPrincipal()).thenReturn(userDto);

            //then
            ResponseEntity<ClientDto> response = clientController.getById(authenticator,idClient);

            //assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
/*
    @Test
    public void addClient_Test200(){

        try {  //todo VER COMO HACER
            Date birtday = new SimpleDateFormat("yyyy-MM-dd").parse("2020-02-02");
            ServletUriComponentsBuilder location = mock(ServletUriComponentsBuilder.class);

            RegisterDto registerDto = RegisterDto.builder()
                    .dni("123456")
                    .name("Pepe")
                    .lastName("Popas")
                    .birthday(birtday)
                    .mail("pepe@gmail.com")
                    .password("1234")
                    .build();

            Client client= Client.builder().id(1).name("Pepe").lastName("Popas").birthday(birtday).dni("123456").build();

            when(clientService.add(registerDto)).thenReturn(client);

            ResponseEntity response = clientController.addClient(registerDto);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());

        } catch (ParseException e) {
            Assert.fail("No se cargo bien la fecha");
        }

    }
*/
    @Test
    public void getClientResidences_Test200() throws Exception {
        //give
        Integer page = 0;
        Integer size = 10;
        Integer idClient=1;

        Authentication authenticator =mock(Authentication.class);
        List list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority("CLIENT"));

        EnergyMeterDto energyMeterDto = EnergyMeterDto.builder().brandName("brand1").serialNumber("001").modelName("model1").passWord("1234").build();
        EnergyMeterDto energyMeterDto2 = EnergyMeterDto.builder().brandName("brand1").serialNumber("002").modelName("model1").passWord("1234").build();

        ResidenceDto residenceDto =ResidenceDto.builder().street("calle1").number(123).client("nicolas").energyMeter(energyMeterDto).id(1).build();
        ResidenceDto residenceDto2 =ResidenceDto.builder().street("calle2").number(222).client("lautaro").energyMeter(energyMeterDto2).id(2).build();

        Page<ResidenceDto> mockedPage = mock(Page.class);

        List<ResidenceDto> residences = new ArrayList<ResidenceDto>();
        residences.add(residenceDto);
        residences.add(residenceDto2);

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "street"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "number"));

        UserDto userDto = UserDto.builder().id(idClient).mail("carlos@gmail.com").build();

        when(authenticator.getAuthorities()).thenReturn(list);
        when(authenticator.getPrincipal()).thenReturn(userDto);

        when(mockedPage.getContent()).thenReturn(residences);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(residences.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(clientService.getClientResidences(idClient,page,size,orders)).thenReturn(mockedPage);
        //then

        ResponseEntity<List<ResidenceDto>>response = clientController.getClientResidences(authenticator,idClient,page,size,"street","number");

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("calle1", response.getBody().get(0).getStreet());

    }

    @Test
    public  void getClientResidences_Test403() throws Exception {
        //give
        Integer page = 0;
        Integer size = 10;
        Integer idClient=1;

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "street"));
        orders.add(new Sort.Order(Sort.Direction.ASC, "number"));

        Authentication authenticator =mock(Authentication.class);
        List list = new ArrayList<SimpleGrantedAuthority>();
        list.add(new SimpleGrantedAuthority("INVALID"));

        UserDto userDto = UserDto.builder().id(idClient).mail("carlos@gmail.com").build();

        Page<ResidenceDto> emptyPage = Page.empty();

        when(authenticator.getAuthorities()).thenReturn(list);
        when(authenticator.getPrincipal()).thenReturn(userDto);

        //then
        ResponseEntity<List<ResidenceDto>>response = clientController.getClientResidences(authenticator,idClient,page,size,"street","number");

        //assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }



}
