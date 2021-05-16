package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.proyection.Top10Clients;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

   /* @GetMapping
    public ResponseEntity<List<Client>> getAllClients(@RequestParam(required = false) String name,
                                                      @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
                                                      @RequestParam(defaultValue = "5", required = false) Integer pageSize,
                                                      @RequestParam(defaultValue = "id", required = false) String sortBy){

        Page<Client> clients = clientService.getAll(name, pageNumber, pageSize, sortBy);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(clients.getTotalElements()))
                .header("X-Total-Pages", Long.toString(clients.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(pageNumber))
                .body(clients.getContent());
    }*/

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients(@RequestParam(required = false) String name,
                                                      @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
                                                      @RequestParam(defaultValue = "5", required = false) Integer pageSize,
                                                      @RequestParam(defaultValue = "id", required = false) String sortBy){

        Page<Client> clients = clientService.getAll(name, pageNumber, pageSize, sortBy);
        Page<ClientDto> dtoClients = clients.map(c -> ClientDto.fromWithOutResidences(c));
        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(dtoClients.getTotalElements()))
                .header("X-Total-Pages", Long.toString(dtoClients.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(pageNumber))
                .body(dtoClients.getContent());
    }

    @GetMapping("/{idClient}")
    public ResponseEntity<ClientDto> getClientById (@PathVariable Integer idClient){
        Client c = clientService.getClientById(idClient);
        return ResponseEntity.ok(ClientDto.from(c));
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
    @GetMapping("/top10")
    public Top10Clients getTop10(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to){
      return clientService.getTop10(from,to);
    }






}
