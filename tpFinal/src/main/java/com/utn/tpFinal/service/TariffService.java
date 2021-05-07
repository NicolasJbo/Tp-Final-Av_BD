package com.utn.tpFinal.service;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TariffService {
    @Autowired
    private TariffRepository tariffRepository;

//-------------------------------------------->> BRAND <<--------------------------------------------


    public void addTariff(Tariff tariff) {
        tariffRepository.save(tariff);
    }

    public Tariff getTariffById(String name) {
        return tariffRepository.findById(name)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public List<Tariff> getAll(){
            return tariffRepository.findAll();
    }

    public void addResidenceToTariff(Residence residence, Tariff tariff) {
        tariff.getResidencesList().add(residence);
        tariffRepository.save(tariff);
    }

    public List<Residence> getResidencesByTariff(String nameTariff) {
        Tariff tariff = getTariffById(nameTariff);
        return tariff.getResidencesList();
    }
}
