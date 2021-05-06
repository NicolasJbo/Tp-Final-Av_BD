package com.utn.tpFinal.service;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.repository.ClientRepository;
import com.utn.tpFinal.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Properties;

import static java.util.Objects.isNull;

@Service
public class ClientService {

    ClientRepository clientRepository;
    ResidenceRepository residenceRepository;
    ResidenceService residenceService;

    @Autowired
    public ClientService(ClientRepository clientRepository, ResidenceService residenceService,ResidenceRepository residenceRepository) {
        this.clientRepository = clientRepository;
        this.residenceService = residenceService;
        this.residenceRepository = residenceRepository;
    }

    public void add(Client client) {
        clientRepository.save(client);
    }

    public List<Client> getAll(String name) {
        if(isNull(name))
            return clientRepository.findAll();
        else
            return clientRepository.findByName(name);
    }

    public Client getClientById(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addResidenseToClient(Integer id, Integer idResidence) {
        Client c = getClientById(id);
        Residence r = residenceService.getResidenceById(idResidence);
        residenceService.addClientToResidence(c, r);
        c.getResidencesList().add(r);
        clientRepository.save(c);
    }

    public List<Residence> getClientResidences(Integer idClient) {
        Client c = getClientById(idClient);
        return residenceRepository.findByClient(c);

    }
}
