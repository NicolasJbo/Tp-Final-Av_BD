package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.exception.ResidenceNotExists;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.User;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.dto.RegisterDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.proyection.Top10Clients;
import com.utn.tpFinal.repository.BillRepository;
import com.utn.tpFinal.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ResidenceService residenceService;
    @Autowired
    private BillRepository billRepository;


    public Client getClientById(Integer id) throws ClientNotExists {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotExists(this.getClass().getSimpleName(), "getClientById"));
    }

//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public Client add(Client client) {
        return clientRepository.save(client);
    }

    public Page<ClientDto> getAll(Specification<Client> clientSpecification, Integer page, Integer size, List<Order>orders) throws NoContentException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Client>clients = clientRepository.findAll(clientSpecification,pageable);

        if(clients.isEmpty())
            throw new NoContentException(this.getClass().getSimpleName(), "getAll");

        Page<ClientDto> dtoClients = clients.map(c -> ClientDto.fromWithOutResidences(c));

        return dtoClients;
    }

    public void addResidenceToClient(Integer idClient, Integer idResidence) throws ClientNotExists, ResidenceNotExists {
        Client c = getClientById(idClient); //devuelve exception
        Residence r = residenceService.getResidenceById(idResidence); //devuelve exception
        residenceService.addClientToResidence(c, r);
        c.getResidencesList().add(r);
        clientRepository.save(c);
    }

    public Page<ResidenceDto> getClientResidences(Integer idClient, Integer page, Integer size, List<Order>orders) throws NoContentException, ClientNotExists {
        if(!clientRepository.existsById(idClient))
            throw new ClientNotExists(this.getClass().getSimpleName(), "getClientResidences");

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Residence> residences = residenceService.getResidenceByClientId(idClient, pageable);

        if(residences.isEmpty()) //todo no muestra el contenido de la exception
            throw new NoContentException(this.getClass().getSimpleName(), "getClientResidences");

        Page<ResidenceDto> residencesDto = residences.map(r-> ResidenceDto.from(r));

        return residencesDto;
    }

    public void deleteClientById(Integer idClient) throws ClientNotExists {
        if(!clientRepository.existsById(idClient))
            throw new ClientNotExists(this.getClass().getSimpleName(), "deleteClientById");

        clientRepository.deleteById(idClient);
    }

    public List<Top10Clients> getTop10ConsumerByDates(Date from, Date to) throws NoContentException {
        List<Top10Clients> clientsList = clientRepository.getTop10Clients(from,to);
        if(clientsList.isEmpty()) //todo no muestra el contenido de la exception
            throw new NoContentException(this.getClass().getSimpleName(), "getTop10ConsumerByDates");
        return clientsList;
    }


    public Page<BillDto> getClientUnpaidBills(Integer idClient, Integer page, Integer size, List<Order> orders) throws ClientNotExists, NoContentException {
        if(!clientRepository.existsById(idClient))
            throw new ClientNotExists(this.getClass().getSimpleName(), "getClientUnpaidBills");

        Client c = getClientById(idClient);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        List<Integer>residencesIds = new ArrayList<Integer>();
        for(Residence r: c.getResidencesList())
            residencesIds.add(r.getId());

        Page<Bill> bills = billRepository.findByIsPaidFalseAndResidenceIdIn(residencesIds,pageable);

        if(bills.isEmpty())
            throw new NoContentException(this.getClass().getSimpleName(), "getClientUnpaidBills");

        Page<BillDto> billsDto = bills.map(b-> BillDto.from(b));

        return billsDto;
    }


}
