package com.utn.tpFinal.controller;

import com.utn.tpFinal.controller.ClientController;
import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.dto.UserDto;
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
            List<ResidenceDto> residences= Collections.EMPTY_LIST;

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


}
