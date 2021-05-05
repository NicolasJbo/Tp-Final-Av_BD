package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClientController {

    @Autowired
    ClientService clientService;

    @PostMapping
    public void addClient(@RequestBody Client client){
        clientService.add(client);
    }

    @GetMapping
    public List<Client> getAll(@RequestParam(required = false) String name){
        return clientService.getAll(name);
    }

    @GetMapping("/{idClient}/domicilios")
    public List<Residence>getClientResidences(@PathVariable Integer idClient){
        return clientService.getClientResidences(idClient);
    }

    @PutMapping("/{id}/domicilio/{idDomicilio}")
    public void addResidenseToClient(@PathVariable Integer id, @PathVariable Integer idDomicilio) {
        clientService.addResidenseToClient(id,idDomicilio);
    }

}
