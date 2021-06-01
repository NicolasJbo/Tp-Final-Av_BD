package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.exception.NoConsumptionsFoundException;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.proyection.Consumption;
import com.utn.tpFinal.model.proyection.MeasureProyection;
import com.utn.tpFinal.model.proyection.Top10Clients;
import com.utn.tpFinal.service.BillService;
import com.utn.tpFinal.service.ClientService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//--------------------------- CLIENT --------------------------------------------

    @PostMapping
    public ResponseEntity addClient(@RequestBody Client client){
        //todo hacer RegisterDTO
       Client c = clientService.add(client);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/")
                .query("id={idClient}")//todo arreglar esto
                .buildAndExpand(c.getId())
                .toUri();
       return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAll( @RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "5") Integer size,
                                                   @RequestParam(defaultValue = "id") String sortField1,
                                                   @RequestParam(defaultValue = "name") String sortField2,
                                                   @And({  @Spec(path = "id", spec = Equal.class),
                                                           @Spec(path = "name", spec = LikeIgnoreCase.class),
                                                           @Spec(path = "lastName", spec = LikeIgnoreCase.class),
                                                           @Spec(path="dni", spec = LikeIgnoreCase.class),
                                                           @Spec(path = "birthday", spec = LikeIgnoreCase.class)
                                                   }) Specification<Client> clientSpecification) throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));

        Page<ClientDto> dtoClients = clientService.getAll(clientSpecification, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(dtoClients.getTotalElements()))
                .header("X-Total-Pages", Long.toString(dtoClients.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(dtoClients.getContent());
    }

    @GetMapping("/{idClient}/residences")
    public ResponseEntity<List<ResidenceDto>>getClientResidences(@PathVariable Integer idClient,
                                                                 @RequestParam(defaultValue = "0") Integer page,
                                                                 @RequestParam(defaultValue = "5") Integer size,
                                                                 @RequestParam(defaultValue = "street") String sortField1,
                                                                 @RequestParam(defaultValue = "number") String sortField2) throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));

        Page<ResidenceDto> residences = clientService.getClientResidences(idClient, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(residences.getTotalElements()))
                .header("X-Total-Pages", Long.toString(residences.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(residences.getContent());
    }

    @PutMapping("/{idClient}/residence/{idResidence}")
    public ResponseEntity addResidenceToClient(@PathVariable Integer idClient, @PathVariable Integer idResidence) throws Exception {
        clientService.addResidenceToClient(idClient,idResidence);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("{idClient}")
    public ResponseEntity deleteClientById(@PathVariable Integer idClient) throws Exception {
        clientService.deleteClientById(idClient);
        return ResponseEntity.ok().build();
    }

//--------------------------- BILLS --------------------------------------------

    //  [PROG - PUNTO 2] USUARIOS -> Consulta de facturas con rango de fechas
    @GetMapping("/{idClient}/bills") //TODO TEST
    public ResponseEntity<List<BillDto>> getClientBillsByDates(@PathVariable Integer idClient,
                                                               @RequestParam(defaultValue = "0") Integer page,
                                                               @RequestParam(defaultValue = "5") Integer size,
                                                               @RequestParam(defaultValue = "id") String sortField1,
                                                               @RequestParam(defaultValue = "initialDate") String sortField2,
                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws Exception {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));

        Page<BillDto> bills = billService.getClientBillsByDates(idClient,from, to, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(bills.getTotalElements()))
                .header("X-Total-Pages", Long.toString(bills.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(bills.getContent());
    }

    //  [PROG - PUNTO 3] USUARIOS -> Consulta de facturas impagas EN BACKOFFICE CONTROLLER
    @GetMapping("{idClient}/bills/unpaid") //TODO TEST
    public ResponseEntity<List<BillDto>>getClientUnpaidBills(@PathVariable Integer idClient,
                                                             @RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "5") Integer size,
                                                             @RequestParam(defaultValue = "id") String sortField1,
                                                             @RequestParam(defaultValue = "expirationDate") String sortField2) throws NoContentException, ClientNotExists {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<BillDto> bills = clientService.getClientUnpaidBills(idClient, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(bills.getTotalElements()))
                .header("X-Total-Pages", Long.toString(bills.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(bills.getContent());
    }

    //  [PROG - PUNTO 4] USUARIOS -> Consulta de consumo por rango de fechas
    @GetMapping("/{idClient}/consumption") //TODO TEST
    public ResponseEntity<Consumption> getClientTotalEnergyByAndAmountDates(@PathVariable Integer idClient,
                                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws IncorrectDatesException, NoConsumptionsFoundException {
        Consumption consumption = billService.getClientTotalEnergyAndAmountByDates(idClient, from, to);
        return ResponseEntity.status(HttpStatus.OK).body(consumption);
    }

    //  [PROG - PUNTO 5] USUARIOS -> Consulta de mediciones por rango de fechas
    @GetMapping("/{idClient}/measures") //TODO TEST
    public ResponseEntity<List<MeasureDto>> getClientMeasuresByDates(@PathVariable Integer idClient,
                                                                            @RequestParam(defaultValue = "0") Integer page,
                                                                            @RequestParam(defaultValue = "5") Integer size,
                                                                            @RequestParam(defaultValue = "id") String sortField1,
                                                                            @RequestParam(defaultValue = "date") String sortField2,
                                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));

        Page<MeasureDto> measures = billService.getClientMeasuresByDates(idClient, from, to, page, size,orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(measures.getTotalElements()))
                .header("X-Total-Pages", Long.toString(measures.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(measures.getContent());
    }

//--------------------------- BACKOFFICE --------------------------------------------
//todo sacar
    /*
    //  [PROG - PUNTO 5] BACKOFFICE -> Consulta de 10 clientes mas consumidores por fechas
    @GetMapping("/topConsumers")
    public ResponseEntity<List<Top10Clients>>getTop10ConsumerByDates(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws NoContentException {
        List<Top10Clients> rta = clientService.getTop10ConsumerByDates(from,to);

        return ResponseEntity.status(HttpStatus.OK).body(rta);
    }


    //  [PROG - PUNTO 3-4] USUARIOS - BACKOFFICE -> Consulta de facturas impagas por cliente y domicilio
    @GetMapping("/{idClient}/bills/unpaid")
    public ResponseEntity<List<BillDto>>getClientUnpaidBills(@PathVariable Integer idClient,
                                                             @RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "5") Integer size,
                                                             @RequestParam(defaultValue = "id") String sortField1,
                                                             @RequestParam(defaultValue = "expirationDate") String sortField2) throws NoContentException, ClientNotExists {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));

        Page<BillDto> bills = clientService.getClientUnpaidBills(idClient, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(bills.getTotalElements()))
                .header("X-Total-Pages", Long.toString(bills.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(bills.getContent());
    }
     */




}
