package com.utn.tpFinal.service;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ResidenceService {

    @Autowired
    ResidenceRepository residenceRepository;

    public void addResidence(Residence residence) {
        residenceRepository.save(residence);
    }

    public List<Residence> getAll(String street) {
        if(isNull(street))
            return residenceRepository.findAll();
        else
            return residenceRepository.findByStreet(street);
    }

    public Residence getResidenceById(Integer id) {
        return residenceRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addClientToResidence(Client client, Residence residence) {
        residence.setClient(client);
        residenceRepository.save(residence);
    }
}
