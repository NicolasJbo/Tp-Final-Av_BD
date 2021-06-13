package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.exception.NoConsumptionsFoundException;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.dto.*;
import com.utn.tpFinal.model.proyection.Consumption;
import com.utn.tpFinal.service.BillService;
import com.utn.tpFinal.service.ClientService;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.Sort.Order;


import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private ClientService clientService;
    private BillService billService;

    @Autowired
    public ClientController(ClientService clientService, BillService billService) {
        this.clientService = clientService;
        this.billService = billService;
    }

    public Boolean isEmployeeOrIsClientAndIdMatch(Authentication authenticator, Integer id) {

        String role = authenticator.getAuthorities().stream().findFirst().get().getAuthority();//.toString().replaceAll("[^A-Za-z]","");
        UserDto userDto = (UserDto) authenticator.getPrincipal();
        return (role.equalsIgnoreCase("EMPLOYEE") ||
                (role.equalsIgnoreCase("CLIENT") && userDto.getId() == id));

    }

    //--------------------------- CLIENT --------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getById(Authentication authenticator, @PathVariable Integer id) throws ClientNotExists {
        if (isEmployeeOrIsClientAndIdMatch(authenticator, id)) {
            Client c = clientService.getClientById(id);
            ClientDto clientDto = ClientDto.from(c);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(clientDto);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    @PostMapping
    public ResponseEntity addClient(@RequestBody RegisterDto registerDto) {

        Client c = clientService.add(registerDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idClient}")
                .buildAndExpand(c.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<ClientDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "5") Integer size,
                                                  @RequestParam(defaultValue = "lastName") String sortField1,
                                                  @RequestParam(defaultValue = "dni") String sortField2,
                                                  @And({
                                                          @Spec(path = "lastName", spec = LikeIgnoreCase.class),
                                                          @Spec(path = "dni", spec = LikeIgnoreCase.class),
                                                          @Spec(path = "birthday", spec = LikeIgnoreCase.class)
                                                  }) Specification<Client> clientSpecification) throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));

        Page<ClientDto> dtoClients = clientService.getAll(clientSpecification, page, size, orders);
        if (dtoClients.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.OK)
                    .header("X-Total-Elements", Long.toString(dtoClients.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(dtoClients.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(dtoClients.getContent());
    }

    @GetMapping("/{idClient}/residences")
    public ResponseEntity<List<ResidenceDto>> getClientResidences(Authentication authenticator, @PathVariable Integer idClient,
                                                                  @RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "5") Integer size,
                                                                  @RequestParam(defaultValue = "street") String sortField1,
                                                                  @RequestParam(defaultValue = "number") String sortField2) throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));
        if (isEmployeeOrIsClientAndIdMatch(authenticator, idClient)) {
            Page<ResidenceDto> residences = clientService.getClientResidences(idClient, page, size, orders);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("X-Total-Elements", Long.toString(residences.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(residences.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(residences.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    @PutMapping("/{idClient}/residence/{idResidence}")
    public ResponseEntity addResidenceToClient(Authentication authenticator, @PathVariable Integer idClient, @PathVariable Integer idResidence) throws Exception {
        if (isEmployeeOrIsClientAndIdMatch(authenticator, idClient)) {
            clientService.addResidenceToClient(idClient, idResidence);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @DeleteMapping("{idClient}")
    public ResponseEntity deleteClientById(@PathVariable Integer idClient) throws Exception {
        clientService.deleteClientById(idClient);
        return ResponseEntity.ok().build();
    }

//--------------------------- BILLS --------------------------------------------

    //  [PROG - PUNTO 2] USUARIOS -> Consulta de facturas con rango de fechas
    @GetMapping("/{idClient}/bills") //TODO TEST
    public ResponseEntity<List<BillDto>> getClientBillsByDates(Authentication authenticator, @PathVariable Integer idClient,
                                                               @RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "5") Integer size,
                                                               @RequestParam(defaultValue = "id") String sortField1,
                                                               @RequestParam(defaultValue = "initialDate") String sortField2,
                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws Exception {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));

        if (isEmployeeOrIsClientAndIdMatch(authenticator, idClient)) {
            Page<BillDto> bills = billService.getClientBillsByDates(idClient, from, to, page, size, orders);

            return ResponseEntity.status(HttpStatus.OK)
                    .header("X-Total-Elements", Long.toString(bills.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(bills.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(bills.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }

    }

    //  [PROG - PUNTO 3] USUARIOS -> Consulta de facturas impagas EN BACKOFFICE CONTROLLER
    @GetMapping("{idClient}/bills/unpaid") //TODO TEST
    public ResponseEntity<List<BillDto>> getClientUnpaidBills(Authentication authenticator, @PathVariable Integer idClient,
                                                              @RequestParam(defaultValue = "0") Integer page,
                                                              @RequestParam(defaultValue = "5") Integer size,
                                                              @RequestParam(defaultValue = "id") String sortField1,
                                                              @RequestParam(defaultValue = "expirationDate") String sortField2) throws NoContentException, ClientNotExists {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        if (isEmployeeOrIsClientAndIdMatch(authenticator, idClient)) {
            Page<BillDto> bills = clientService.getClientUnpaidBills(idClient, page, size, orders);

            return ResponseEntity.status(HttpStatus.OK)
                    .header("X-Total-Elements", Long.toString(bills.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(bills.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(bills.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    //  [PROG - PUNTO 4] USUARIOS -> Consulta de consumo por rango de fechas
    @GetMapping("/{idClient}/consumption") //TODO TEST
    public ResponseEntity<Consumption> getClientTotalEnergyByAndAmountDates(Authentication authenticator, @PathVariable Integer idClient,
                                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws IncorrectDatesException, NoConsumptionsFoundException {
        if (isEmployeeOrIsClientAndIdMatch(authenticator, idClient)) {
            Consumption consumption = billService.getClientTotalEnergyAndAmountByDates(idClient, from, to);
            return ResponseEntity.status(HttpStatus.OK).body(consumption);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }

    //  [PROG - PUNTO 5] USUARIOS -> Consulta de mediciones por rango de fechas
    @GetMapping("/{idClient}/measures") //TODO TEST
    public ResponseEntity<List<MeasureDto>> getClientMeasuresByDates(Authentication authenticator, @PathVariable Integer idClient,
                                                                     @RequestParam(defaultValue = "0") Integer page,
                                                                     @RequestParam(defaultValue = "5") Integer size,
                                                                     @RequestParam(defaultValue = "id") String sortField1,
                                                                     @RequestParam(defaultValue = "date") String sortField2,
                                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));
        if (isEmployeeOrIsClientAndIdMatch(authenticator, idClient)) {
            Page<MeasureDto> measures = billService.getClientMeasuresByDates(idClient, from, to, page, size, orders);

            return ResponseEntity.status(HttpStatus.OK)
                    .header("X-Total-Elements", Long.toString(measures.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(measures.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(measures.getContent());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }
    }


}
