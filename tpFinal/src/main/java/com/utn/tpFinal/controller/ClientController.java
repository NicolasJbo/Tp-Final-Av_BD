package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.proyection.Top10Clients;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.service.ClientService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.net.URI;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity addClient(@RequestBody Client client){
       Client c = clientService.add(client);

       URI location = ServletUriComponentsBuilder
                     .fromCurrentRequest()
                     .path("/{idClient}")
                     .buildAndExpand(c.getId())
                     .toUri();
       return ResponseEntity.created(location).build();
    }

    @GetMapping("")
    public ResponseEntity<List<ClientDto>> getAll(
            @And({
                    @Spec(path = "name", spec = Equal.class),
                    @Spec(path = "lastName", spec = Equal.class),
                    @Spec(path="dni", spec = Equal.class),
                    @Spec(path = "birthday", spec = Equal.class)
            }) Specification<Client> pesonaSpecification, Pageable pageable){

        Page<ClientDto> dtoClients = clientService.getAll(pesonaSpecification, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(dtoClients.getTotalElements()))
                .header("X-Total-Pages", Long.toString(dtoClients.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(pageable.getPageNumber()))
                .body(dtoClients.getContent());
    }

    @GetMapping("/{idClient}/residences")
    public ResponseEntity<List<Residence>>getClientResidences(@PathVariable Integer idClient){
        List<Residence> residences = clientService.getClientResidences(idClient);
        if(residences.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok(residences);
    }

    @PutMapping("/{idClient}/residence/{idResidence}")
    public ResponseEntity addResidenceToClient(@PathVariable Integer idClient, @PathVariable Integer idResidence) {
        clientService.addResidenceToClient(idClient,idResidence);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("{idClient}")
    public ResponseEntity deleteClientById(@PathVariable Integer idClient){
        clientService.deleteClientById(idClient);
       return ResponseEntity.ok().build();
    }
    @GetMapping("/topConsumers")
    public ResponseEntity<List<Top10Clients>>getTop10ConsumerByDates(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to){
      List<Top10Clients> rta = clientService.getTop10ConsumerByDates(from,to);

        return ResponseEntity.status(HttpStatus.OK).body(rta);
    }






}
