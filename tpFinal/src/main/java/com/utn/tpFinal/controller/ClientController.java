package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.PostResponse;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public PostResponse addClient(@RequestBody Client client){

       return clientService.add(client);
    }

    @GetMapping
    public List<Client> getAll(@RequestParam(required = false) String name){
        return clientService.getAll(name);
    }
    @GetMapping("/{idClient}")
    public Client getClientById (@PathVariable Integer idClient){
        return clientService.getClientById(idClient);
    }

    @GetMapping("/{idClient}/residences")
    public List<Residence>getClientResidences(@PathVariable Integer idClient){
        return clientService.getClientResidences(idClient);
    }

    @PutMapping("/{id}/residence/{idResidence}")
    public void addResidenseToClient(@PathVariable Integer id, @PathVariable Integer idResidence) {
        clientService.addResidenseToClient(id,idResidence);
    }
    @DeleteMapping("/remove/{idClient}")
    public PostResponse deleteClientById(@PathVariable Integer idClient){
       return clientService.deleteClientById(idClient);
    }




}
