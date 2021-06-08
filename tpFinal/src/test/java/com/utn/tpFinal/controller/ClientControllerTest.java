package com.utn.tpFinal.controller;

import com.utn.tpFinal.UTILS_TESTCONSTANTS;
import com.utn.tpFinal.controller.ClientController;
import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.dto.*;
import com.utn.tpFinal.model.proyection.Consumption;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
    private  final Integer IDCLIENT =1;
    private final Integer PAGE = 0;
    private final Integer SIZE = 10;

    ClientService   clientService;   // externa
    BillService billService;        // externa
    ClientController clientController;


    @Before
    public void setUp(){
        clientService = mock(ClientService.class);
        billService =mock(BillService.class);
        clientController = new ClientController(clientService,billService);
    }

    @Test
    public void getAllClients_Test200() throws Exception {
        //give

        Specification<Client> specification = mock(Specification.class);

        ClientDto client1 = UTILS_TESTCONSTANTS.getClientDTO(1);
        ClientDto client2 = UTILS_TESTCONSTANTS.getClientDTO(2);

        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(client1);
        clientDtos.add(client2);

        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders(SORTFIELD1,SORTFIELD2);

        Page<ClientDto> mockedPage = mock(Page.class);

        when(mockedPage.getContent()).thenReturn(clientDtos);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(clientDtos.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(clientService.getAll(specification,PAGE,SIZE,orders)).thenReturn(mockedPage);

        //then
        ResponseEntity<List<ClientDto>> response = clientController.getAll(PAGE,SIZE,SORTFIELD1,SORTFIELD2,specification);

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("Mati", response.getBody().get(1).getClient());

    }

    @Test
    public void getAllClients_Test204() throws Exception {
        //give

        Specification<Client> specification = mock(Specification.class);

        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders(SORTFIELD1,SORTFIELD2);


        Page<ClientDto> emptyPage = Page.empty();

        when(clientService.getAll(specification,PAGE,SIZE,orders)).thenReturn(emptyPage);
        //then
        ResponseEntity<List<ClientDto>>response = clientController.getAll(PAGE,SIZE,SORTFIELD1,SORTFIELD2,specification);
        //assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public  void getById_Test200() throws ClientNotExists {
        try {
            Authentication authenticator =mock(Authentication.class);

            List<Residence> red =new ArrayList<>();

            List list = UTILS_TESTCONSTANTS.getGrandAuthorityClient();


            Date birtday = UTILS_TESTCONSTANTS.getFecha(1);
            Client client= UTILS_TESTCONSTANTS.getClient(IDCLIENT);
            client.setResidencesList(red);
            UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);

            when(authenticator.getAuthorities()).thenReturn(list);
            when(authenticator.getPrincipal()).thenReturn(userDto);
            when(clientService.getClientById(IDCLIENT)).thenReturn(client);

            //then
            ResponseEntity<ClientDto> response = clientController.getById(authenticator,IDCLIENT);

            //assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("PEREZ CARLOS",response.getBody().getClient());
        } catch (ParseException e) {
            Assert.fail("No se cargo bien la fecha");
        }

    }

    @Test
    public  void getById_Test403() throws ClientNotExists {

        Authentication authenticator =mock(Authentication.class);

        List list = UTILS_TESTCONSTANTS.getGrandAuthorityInvalid();

        UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);

        when(authenticator.getAuthorities()).thenReturn(list);
        when(authenticator.getPrincipal()).thenReturn(userDto);

        //then
        ResponseEntity<ClientDto> response = clientController.getById(authenticator,IDCLIENT);

        //assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


    @Test
    public void addClient_Test200(){

        try {
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

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

    @Test
    public void getClientResidences_Test200() throws Exception {
        //give

        Authentication authenticator =mock(Authentication.class);
        List list =UTILS_TESTCONSTANTS.getGrandAuthorityClient();


        Page<ResidenceDto> mockedPage = mock(Page.class);

        List<ResidenceDto> residences =UTILS_TESTCONSTANTS.getResidendesDTO_List();

        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("street","number") ;


        UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);

        when(authenticator.getAuthorities()).thenReturn(list);
        when(authenticator.getPrincipal()).thenReturn(userDto);

        when(mockedPage.getContent()).thenReturn(residences);
        when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(residences.size()));
        when(mockedPage.getTotalPages()).thenReturn(1);
        when(clientService.getClientResidences(IDCLIENT,PAGE,SIZE,orders)).thenReturn(mockedPage);
        //then

        ResponseEntity<List<ResidenceDto>>response = clientController.getClientResidences(authenticator,IDCLIENT,PAGE,SIZE,"street","number");

        //assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
        assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals("calle1", response.getBody().get(0).getStreet());

    }

    @Test
    public  void getClientResidences_Test403() throws Exception {
        //give


        List<Sort.Order> orders =UTILS_TESTCONSTANTS.getOrders("street","number");


        Authentication authenticator =mock(Authentication.class);
        List list = UTILS_TESTCONSTANTS.getGrandAuthorityInvalid();

        UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);

        Page<ResidenceDto> emptyPage = Page.empty();

        when(authenticator.getAuthorities()).thenReturn(list);
        when(authenticator.getPrincipal()).thenReturn(userDto);

        //then
        ResponseEntity<List<ResidenceDto>>response = clientController.getClientResidences(authenticator,IDCLIENT,PAGE,SIZE,"street","number");

        //assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

    }


    @Test
    public void addResidenceToClient_Test200() throws Exception {

        //Authenticator
        Authentication authenticator =mock(Authentication.class);
        List grand =UTILS_TESTCONSTANTS.getGrandAuthorityClient();
        UserDto userDto=UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
        when(authenticator.getAuthorities()).thenReturn(grand);
        when(authenticator.getPrincipal()).thenReturn(userDto);
        //endAuthenticator

        ResponseEntity response= clientController.addResidenceToClient(authenticator,IDCLIENT,IDCLIENT);
        //assert
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());

    }
    @Test
    public void addResidenceToClient_Test403() throws Exception {

        //Authenticator
        Authentication authenticator =mock(Authentication.class);
        List grand = UTILS_TESTCONSTANTS.getGrandAuthorityInvalid();
        UserDto userDto=UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
        when(authenticator.getAuthorities()).thenReturn(grand);
        when(authenticator.getPrincipal()).thenReturn(userDto);
        //endAuthenticator

        ResponseEntity response= clientController.addResidenceToClient(authenticator,IDCLIENT,IDCLIENT);
        //assert
        assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());

    }
    @Test
    public void  deleteClientById_Test200() throws Exception {


        ResponseEntity response = clientController.deleteClientById(IDCLIENT);
        //assert
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    public void getClientBillsByDates_Test200(){
        try {

            List<BillDto> billList = UTILS_TESTCONSTANTS.getBillsDTO_List();
            UserDto userDto= UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
            List<Sort.Order> orders = UTILS_TESTCONSTANTS.getOrders("id", "initialDate");

            Authentication authenticator = mock(Authentication.class);
            List list = UTILS_TESTCONSTANTS.getGrandAuthorityClient();
            when(authenticator.getAuthorities()).thenReturn(list);
            when(authenticator.getPrincipal()).thenReturn(userDto);

            Page<BillDto> mockedPage = mock(Page.class);

            when(mockedPage.getContent()).thenReturn(billList);
            when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(billList.size()));
            when(mockedPage.getTotalPages()).thenReturn(1);
            when(billService.getClientBillsByDates(IDCLIENT, UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2),PAGE,SIZE,orders )).thenReturn(mockedPage);
            //then
            ResponseEntity<List<BillDto>> response = clientController.getClientBillsByDates(authenticator,IDCLIENT,PAGE,SIZE,"id","initialDate",UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2));
            //assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
            assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
            assertEquals("casa1",response.getBody().get(0).getResidence());

        }catch (ParseException | IncorrectDatesException | NoContentException |  ClientNotExists e){
            Assert.fail("No se cargo bien la fecha");
        } catch (Exception e) {
            Assert.fail("no se cargo algo");
        }
    }
    @Test
    public void getClientBillsByDates_Test403() throws Exception {

        //Authenticator
        Authentication authenticator =mock(Authentication.class);
        List grand = UTILS_TESTCONSTANTS.getGrandAuthorityInvalid();
        UserDto userDto=UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
        when(authenticator.getAuthorities()).thenReturn(grand);
        when(authenticator.getPrincipal()).thenReturn(userDto);
        //endAuthenticator

        ResponseEntity<List<BillDto>> response = clientController.getClientBillsByDates(authenticator,IDCLIENT,PAGE,SIZE,"id","initialDate",UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2));
        //assert
        assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
    }
    @Test
    public void getClientUnpaidBills_Test200(){
        try {

            List<BillDto> billList = UTILS_TESTCONSTANTS.getBillsDTO_List();
            UserDto userDto= UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
            List<Sort.Order> orders = UTILS_TESTCONSTANTS.getOrders("id", "expirationDate");
            //Authenticator
            Authentication authenticator = mock(Authentication.class);
            List list = UTILS_TESTCONSTANTS.getGrandAuthorityClient();
            when(authenticator.getAuthorities()).thenReturn(list);
            when(authenticator.getPrincipal()).thenReturn(userDto);
            //endAuthenticator
            Page<BillDto> mockedPage = mock(Page.class);

            when(mockedPage.getContent()).thenReturn(billList);
            when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(billList.size()));
            when(mockedPage.getTotalPages()).thenReturn(1);
            when(clientService.getClientUnpaidBills(IDCLIENT,PAGE,SIZE,orders)).thenReturn(mockedPage);
            //then
            ResponseEntity<List<BillDto>> response = clientController.getClientUnpaidBills(authenticator,IDCLIENT,PAGE,SIZE,"id","expirationDate");
            //assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
            assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
            assertEquals("casa1",response.getBody().get(0).getResidence());

        }catch ( NoContentException |  ClientNotExists e){
            Assert.fail("No se cargo bien la fecha");
        } catch (Exception e) {
            Assert.fail("no se cargo algo");

        }
    }
    @Test
    public void getClientUnpaidBills_Test403() throws Exception {
        //Authenticator
        Authentication authenticator =mock(Authentication.class);
        List grand = UTILS_TESTCONSTANTS.getGrandAuthorityInvalid();
        UserDto userDto=UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
        when(authenticator.getAuthorities()).thenReturn(grand);
        when(authenticator.getPrincipal()).thenReturn(userDto);
        //endAuthenticator

        ResponseEntity<List<BillDto>> response = clientController.getClientUnpaidBills(authenticator,IDCLIENT,PAGE,SIZE,"id","expirationDate");
        //assert
        assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
    }
    @Test
    public void getClientTotalEnergyByAndAmountDates_Test200(){
        try {
            Consumption consumption = mock(Consumption.class);
            //Authenticator
            Authentication authenticator = mock(Authentication.class);
            List grand = UTILS_TESTCONSTANTS.getGrandAuthorityClient();
            UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
            when(authenticator.getAuthorities()).thenReturn(grand);
            when(authenticator.getPrincipal()).thenReturn(userDto);
            //endAuthenticator
            when(consumption.getClient()).thenReturn("carlos");
            when(billService.getClientTotalEnergyAndAmountByDates(IDCLIENT, UTILS_TESTCONSTANTS.getFecha(1), UTILS_TESTCONSTANTS.getFecha(2))).thenReturn(consumption);
            //then
            ResponseEntity<Consumption> response =clientController.getClientTotalEnergyByAndAmountDates(authenticator,IDCLIENT, UTILS_TESTCONSTANTS.getFecha(1), UTILS_TESTCONSTANTS.getFecha(2));
            //assert
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertEquals("carlos",response.getBody().getClient());
        }catch (ParseException e){
            Assert.fail("No se cargo bien la fecha");
        } catch (IncorrectDatesException e) {
            Assert.fail("Fechas invalidas");
        } catch (NoConsumptionsFoundException e) {
            Assert.fail("no hay consumtion");
        }
    }
    @Test
    public void getClientTotalEnergyByAndAmountDates_Test403(){
        try {

            Consumption consumption = mock(Consumption.class);
            //Authenticator
            Authentication authenticator = mock(Authentication.class);
            List grand = UTILS_TESTCONSTANTS.getGrandAuthorityInvalid();
            UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
            when(authenticator.getAuthorities()).thenReturn(grand);
            when(authenticator.getPrincipal()).thenReturn(userDto);
            //endAuthenticator
            //then
            ResponseEntity<Consumption> response =clientController.getClientTotalEnergyByAndAmountDates(authenticator,IDCLIENT, UTILS_TESTCONSTANTS.getFecha(1), UTILS_TESTCONSTANTS.getFecha(2));
            //assert
            assertEquals(HttpStatus.FORBIDDEN,response.getStatusCode());
        }catch (ParseException e){
            Assert.fail("No se cargo bien la fecha");
        } catch (IncorrectDatesException e) {
            Assert.fail("Fechas invalidas");
        } catch (NoConsumptionsFoundException e) {
            Assert.fail("no hay consumtion");
        }
    }
    @Test
    public void getClientMeasuresByDates_Test200(){
        try {

            List<Sort.Order> orders = UTILS_TESTCONSTANTS.getOrders("id", "date");
            List<MeasureDto> measureDtoList=UTILS_TESTCONSTANTS.getMeasureDTO_List();
            //Authenticator
            Authentication authenticator = mock(Authentication.class);
            List grand = UTILS_TESTCONSTANTS.getGrandAuthorityClient();
            UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
            when(authenticator.getAuthorities()).thenReturn(grand);
            when(authenticator.getPrincipal()).thenReturn(userDto);
            //endAuthenticator
            Page<MeasureDto> mockedPage = mock(Page.class);
            when(mockedPage.getContent()).thenReturn(measureDtoList);
            when(mockedPage.getTotalElements()).thenReturn(Long.valueOf(measureDtoList.size()));
            when(mockedPage.getTotalPages()).thenReturn(1);
            when(billService.getClientMeasuresByDates(IDCLIENT,UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2),PAGE,SIZE,orders)).thenReturn(mockedPage);
            //then
            ResponseEntity<List<MeasureDto>> response = clientController.getClientMeasuresByDates(authenticator,IDCLIENT,PAGE,SIZE,"id","date",UTILS_TESTCONSTANTS.getFecha(1),UTILS_TESTCONSTANTS.getFecha(2));
            //assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, Integer.parseInt(response.getHeaders().get("X-Total-Elements").get(0)));
            assertEquals(1, Integer.parseInt(response.getHeaders().get("X-Total-Pages").get(0)));
            assertEquals("1111",response.getBody().get(0).getPassword());



        }catch (ParseException e){
            Assert.fail("No se cargo bien la fecha");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getClientMeasuresByDates_Test403() throws Exception {
        try {

            Consumption consumption = mock(Consumption.class);
            //Authenticator
            Authentication authenticator = mock(Authentication.class);
            List grand = UTILS_TESTCONSTANTS.getGrandAuthorityInvalid();
            UserDto userDto = UTILS_TESTCONSTANTS.getUserDto(IDCLIENT);
            when(authenticator.getAuthorities()).thenReturn(grand);
            when(authenticator.getPrincipal()).thenReturn(userDto);
            //endAuthenticator
            //then
            ResponseEntity<List<MeasureDto>> response = clientController.getClientMeasuresByDates(authenticator, IDCLIENT, PAGE, SIZE, "id", "date", UTILS_TESTCONSTANTS.getFecha(1), UTILS_TESTCONSTANTS.getFecha(2));
            //assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        } catch (ParseException e) {
            Assert.fail("No se cargo bien la fecha");
        }

    }

}
