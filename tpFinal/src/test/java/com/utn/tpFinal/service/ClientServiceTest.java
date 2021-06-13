package com.utn.tpFinal.service;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.ResidenceNotExists;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.User;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.dto.RegisterDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;


import java.text.ParseException;
import java.util.ArrayList;
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
        User user= UTILS_TESTCONSTANTS.getUser(1);

        RegisterDto rg= UTILS_TESTCONSTANTS.getRegisterDTO();
        rg.setBirthday(UTILS_TESTCONSTANTS.getFecha(1));

        Client c= UTILS_TESTCONSTANTS.getClient(1);

        when(userService.addClient(any(RegisterDto.class))).thenReturn(user);
        when(clientRepository.save(c)).thenReturn(c);
        Client response = clientService.add(rg);
        System.out.println(response);

        assertEquals("carlos",response.getName());
        assertEquals("11111111",response.getDni());
    }

    @Test
    public  void getALL_TestOK() throws ParseException {
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
    public void getALL_TestFAIL() {
        Specification<Client> specification = mock(Specification.class);
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","name") ;
        Pageable pageable = PageRequest.of(0,10,Sort.by(orders));

        when(clientRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(Page.empty(pageable));

        Page<ClientDto>response = clientService.getAll(specification,0,10,orders);

        assertEquals(true, response.getContent().isEmpty());

    }

    @Test
    public void deleteClientById_Test() throws ClientNotExists {
        when(clientRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(clientRepository).deleteById(anyInt());

        String response = clientService.deleteClientById(4);

        assertEquals("deleted", response);
    }

    @Test
    public void deleteClientById_TestException(){
        assertThrows(ClientNotExists.class,()->clientService.deleteClientById(4));
    }

    @Test
    public void getClientUnpaidBills_Test200() throws ParseException, ClientNotExists {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","initialDate") ;
        Client c = UTILS_TESTCONSTANTS.getClient(4);

        Residence residence1 = UTILS_TESTCONSTANTS.getResidence(1);
        Residence residence2 = UTILS_TESTCONSTANTS.getResidence(2);
        List<Residence>residences = new ArrayList<>();
        residences.add(residence1);
        residences.add(residence2);
        c.setResidencesList(residences);

        List<Bill> bills = new ArrayList<>();
        bills.add(UTILS_TESTCONSTANTS.getBill(4));
        bills.add(UTILS_TESTCONSTANTS.getBill(5));

        Page<Bill> page =new PageImpl<>(bills);
        when(clientRepository.findById(anyInt())).thenReturn(Optional.of(c));
        when(billRepository.findByIsPaidFalseAndResidenceIdIn(any(List.class),any(Pageable.class))).thenReturn(page);

        Page<BillDto> response = clientService.getClientUnpaidBills(4,0,2,orders);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals(Float.valueOf(1500), response.getContent().get(0).getFinalAmount());
        assertEquals(2,response.getNumberOfElements());
    }

    @Test
    public void getClientUnpaidBills_Test204() throws ParseException, ClientNotExists {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","initialDate") ;
        Client c = UTILS_TESTCONSTANTS.getClient(4);

        Residence residence1 = UTILS_TESTCONSTANTS.getResidence(1);
        Residence residence2 = UTILS_TESTCONSTANTS.getResidence(2);
        List<Residence>residences = new ArrayList<>();
        residences.add(residence1);
        residences.add(residence2);
        c.setResidencesList(residences);

        when(clientRepository.findById(anyInt())).thenReturn(Optional.of(c));
        when(billRepository.findByIsPaidFalseAndResidenceIdIn(any(List.class),any(Pageable.class))).thenReturn(Page.empty());

        Page<BillDto> response = clientService.getClientUnpaidBills(4,0,2,orders);

        assertEquals(true,response.getContent().isEmpty());
    }

    /*@Test
    public void getTop10ConsumerByDates_Test200(){
        List<Top10Clients> top10ClientsList = new ArrayList<>();
        Top10Clients client1 = new Top10Clients();
    }*/

    @Test
    public void getClientResidences_Test200() throws ParseException, ClientNotExists {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","initialDate") ;

        Residence residence1 = UTILS_TESTCONSTANTS.getResidence(1);
        Residence residence2 = UTILS_TESTCONSTANTS.getResidence(2);
        List<Residence>residences = new ArrayList<>();
        residences.add(residence1);
        residences.add(residence2);

        Page<Residence> page =new PageImpl<>(residences);

        when(clientRepository.existsById(anyInt())).thenReturn(true);
        when(residenceService.getResidenceByClientId(anyInt(),any(Pageable.class))).thenReturn(page);

        Page<ResidenceDto> response = clientService.getClientResidences(4,0,2,orders);

        assertEquals(false,response.getContent().isEmpty());
        assertEquals("Siempre Viva", response.getContent().get(0).getStreet());
        assertEquals(2,response.getNumberOfElements());
    }

    @Test
    public void getClientResidences_Test204() throws ClientNotExists {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","initialDate") ;

        when(clientRepository.existsById(anyInt())).thenReturn(true);
        when(residenceService.getResidenceByClientId(anyInt(),any(Pageable.class))).thenReturn(Page.empty());

        Page<ResidenceDto> response = clientService.getClientResidences(4,0,2,orders);

        assertEquals(true,response.getContent().isEmpty());
    }

    @Test
    public void getClientResidences_TestException() {
        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("id","initialDate") ;

        when(clientRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(ClientNotExists.class,()->clientService.getClientResidences(4,0,2,orders));
    }

    @Test
    public void addResidenceToClient_Test200() throws ParseException, ResidenceNotExists, ClientNotExists {
        Client c = UTILS_TESTCONSTANTS.getClient(4);
        Residence r = UTILS_TESTCONSTANTS.getResidence(4);
        List<Residence> residences = new ArrayList<>();
        c.setResidencesList(residences);

        when(clientRepository.findById(anyInt())).thenReturn(Optional.of(c));
        when(residenceService.getResidenceById(anyInt())).thenReturn(r);
        doNothing().when(residenceService).addClientToResidence(any(Client.class), any(Residence.class));
        when(clientRepository.save(any(Client.class))).thenReturn(c);

        clientService.addResidenceToClient(4,4);

        assertEquals(true, c.getResidencesList().contains(r));

    }


}
