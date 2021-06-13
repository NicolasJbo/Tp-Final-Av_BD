package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.ClientNotExists;
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
    /*
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ResidenceService residenceService;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private UserService userService;
    */
    private ClientRepository clientRepository;
    private ResidenceService residenceService;
    private BillRepository billRepository;
    private UserService userService;

    @Autowired
    public ClientService(ClientRepository clientRepository, ResidenceService residenceService, BillRepository billRepository, UserService userService) {
        this.clientRepository = clientRepository;
        this.residenceService = residenceService;
        this.billRepository = billRepository;
        this.userService = userService;
    }

    public Client getClientById(Integer id) throws ClientNotExists {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotExists(this.getClass().getSimpleName(), "getClientById"));
    }

//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public Client add(RegisterDto registerDto) {
        User user = userService.addClient(registerDto);

        Client client = Client.builder()
                    .dni(registerDto.getDni())
                    .name(registerDto.getName())
                    .lastName(registerDto.getLastName())
                    .birthday(registerDto.getBirthday())
                .user(user)
                .id(user.getId())
                .build();
        clientRepository.save(client);
        return client;
    }

    public Page<ClientDto> getAll(Specification<Client> clientSpecification, Integer page, Integer size, List<Order>orders) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Client>clients = clientRepository.findAll(clientSpecification,pageable);

        Page<ClientDto> dtoClients =Page.empty();

        if(!clients.isEmpty())
              dtoClients = clients.map(c -> ClientDto.fromWithOutResidences(c));

        return dtoClients;
    }

    public void addResidenceToClient(Integer idClient, Integer idResidence) throws ClientNotExists, ResidenceNotExists {
        Client c = getClientById(idClient); //devuelve exception
        Residence r = residenceService.getResidenceById(idResidence); //devuelve exception
        residenceService.addClientToResidence(c, r);
        c.getResidencesList().add(r);
        clientRepository.save(c);
    }

    public Page<ResidenceDto> getClientResidences(Integer idClient, Integer page, Integer size, List<Order>orders) throws  ClientNotExists {
        if(!clientRepository.existsById(idClient))
            throw new ClientNotExists(this.getClass().getSimpleName(), "getClientResidences");

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Residence> residences = residenceService.getResidenceByClientId(idClient, pageable);

        Page<ResidenceDto>residenceDtos = Page.empty(pageable);

        if(!residences.isEmpty())
            residenceDtos = residences.map(r-> ResidenceDto.from(r));

        return residenceDtos;
    }

    public String deleteClientById(Integer idClient) throws ClientNotExists {
        if(!clientRepository.existsById(idClient))
            throw new ClientNotExists(this.getClass().getSimpleName(), "deleteClientById");

        clientRepository.deleteById(idClient);
        return "deleted";
    }

    public List<Top10Clients> getTop10ConsumerByDates(Date from, Date to) {
        return clientRepository.getTop10Clients(from,to);
    }

    public Page<BillDto> getClientUnpaidBills(Integer idClient, Integer page, Integer size, List<Order> orders) throws ClientNotExists {
        Client c = getClientById(idClient);
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        List<Integer>residencesIds = new ArrayList<Integer>();
        for(Residence r: c.getResidencesList())
            residencesIds.add(r.getId());

        Page<Bill> bills = billRepository.findByIsPaidFalseAndResidenceIdIn(residencesIds,pageable);

        Page<BillDto> billsDto = Page.empty(pageable);

        if(!bills.isEmpty())
            billsDto = bills.map(b-> BillDto.from(b));

        return billsDto;
    }


}
