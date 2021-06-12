package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.User;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.dto.RegisterDto;
import com.utn.tpFinal.model.dto.UserDto;
import com.utn.tpFinal.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;

import javax.persistence.Table;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class ClientServiceTest {
    private ClientRepository clientRepository;
    private ResidenceService residenceService;
    private BillRepository billRepository;
    private UserService userService;
    private ClientService clientService;

    @Before
    public void setUp(){
        clientRepository = mock(ClientRepository.class);
        residenceService = mock(ResidenceService.class);
        billRepository=mock(BillRepository.class);
        userService = mock(UserService.class);
        clientService = new ClientService(clientRepository, residenceService,billRepository,userService);
    }
    @Test
    public void getClientById_testOK() throws ParseException, ClientNotExists {
        Client client= UTILS_TESTCONSTANTS.getClient(1);
        when(clientRepository.findById(anyInt())).thenReturn(Optional.of(client));

        Client response = clientService.getClientById(1);

        assertEquals("Carlos",response.getName());
        assertEquals("1111111",response.getDni());
    }
    @Test
    public  void getClientById_testFAIL(){
        assertThrows(ClientNotExists.class,()->clientService.getClientById(1));
    }
    @Test
    public void add_TestOK() throws ParseException {
        User user= UTILS_TESTCONSTANTS.getUser();

        RegisterDto rg= UTILS_TESTCONSTANTS.getRegisterDTO();
        rg.setBirthday(UTILS_TESTCONSTANTS.getFecha(1));

        Client c= UTILS_TESTCONSTANTS.getClient(1);
        c.setResidencesList(Collections.emptyList());

       when(userService.addClient(any(RegisterDto.class))).thenReturn(user);
        when(clientRepository.save(c)).thenReturn(c);
        Client response = clientService.add(rg);
        System.out.println(response);

        assertEquals("carlos",response.getName());
        assertEquals("11111111",response.getDni());
    }
    @Test
    public  void getALL_TestOK() throws ParseException, NoContentException {
        Specification<Client> specification = mock(Specification.class);
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;
        Pageable pageable = PageRequest.of(0,10,Sort.by(orders));
        List<Client>list= new ArrayList<>();
        list.add(UTILS_TESTCONSTANTS.getClient(1));
        list.add(UTILS_TESTCONSTANTS.getClient(2));
        Page<Client> pageE =new PageImpl<Client>(list);

        when(clientRepository.findAll(specification,pageable)).thenReturn(pageE);
        Page<ClientDto> response = clientService.getAll(specification,0,10,orders);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals("PEREZ CARLOS",response.getContent().get(0).getClient());
        assertEquals(2,response.getNumberOfElements());
    }
    @Test
    public void getALL_TestFAIL() throws ParseException {
        Specification<Client> specification = mock(Specification.class);
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;
        Pageable pageable = PageRequest.of(0,10,Sort.by(orders));
        Page<Client> pageE =Page.empty();

        when(clientRepository.findAll(specification,pageable)).thenReturn(pageE);
        try {
            Page<ClientDto> response = clientService.getAll(specification,0,10,orders);
        } catch (NoContentException e) {
            assertEquals("msg",e.getMessage());
        }
    }
}
