package com.utn.tpFinal.service;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.proyection.Top10Clients;
import com.utn.tpFinal.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ClientService {

    private static final String CLIENT_PATH ="client";

    private ClientRepository clientRepository;
    private ResidenceService residenceService;
    private BillService billService;

    @Autowired
    public ClientService(ClientRepository clientRepository, ResidenceService residenceService) {
        this.clientRepository = clientRepository;
        this.residenceService = residenceService;

    }

//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public Client add(Client client) {
        return clientRepository.save(client);
    }

    public Page<Client> getAll(String name, Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Client> pagedResult;

        if(isNull(name))
            pagedResult = clientRepository.findAll(pageable);
        else
            pagedResult = clientRepository.findByName(name,pageable);

        return pagedResult;
    }



    public Client getClientById(Integer id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public Boolean clientExists(Integer id) {
        return clientRepository.existsById(id);
    }

    public void addResidenceToClient(Integer idClient, Integer idResidence) {
        Client c = getClientById(idClient);
        Residence r = residenceService.getResidenceById(idResidence);
        residenceService.addClientToResidence(c, r);
        c.getResidencesList().add(r);
        clientRepository.save(c);
    }

    public List<Residence> getClientResidences(Integer idClient) {
        Client c = getClientById(idClient);
        return c.getResidencesList();
    }

    public void deleteClientById(Integer idClient) {
        clientRepository.deleteById(idClient);
    }

    public Top10Clients getTop10(Date from, Date to) {
        return clientRepository.getTop10Clients(from,to);
    }

}
