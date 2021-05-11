package com.utn.tpFinal.service;

import com.utn.tpFinal.model.*;
import com.utn.tpFinal.repository.ResidenceRepository;
import com.utn.tpFinal.util.EntityURLBuilder;
import com.utn.tpFinal.util.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ResidenceService {

    private static final String RESIDENCE_PATH = "residence";
    private ResidenceRepository residenceRepository;
    private EnergyMeterService  energyMeterService;
    private TariffService tariffService;

    @Autowired
    public ResidenceService(ResidenceRepository residenceRepository, EnergyMeterService energyMeterService, TariffService tariffService) {
        this.residenceRepository = residenceRepository;
        this.energyMeterService = energyMeterService;
        this.tariffService = tariffService;
    }

//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public PostResponse addResidence(Residence residence) {
        residenceRepository.save(residence);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildUrl(RESIDENCE_PATH,residence.getId()))
                .build();
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

    public void addEnergyMeterToResidence(Integer idResidence, Integer idEnergyMeter) {
        Residence residence = getResidenceById(idResidence);

        //retrive the energymeter to add at the residence
        EnergyMeter energyMeter = energyMeterService.getEnergyMeterById(idEnergyMeter);
        //Add residence to energymeter
        energyMeterService.addResidenceToMeter(residence,energyMeter);
        //add the meter to the residence
        residence.setEnergyMeter(energyMeter);
        residenceRepository.save(residence);
    }

    public void addTariffToResidence(Integer idResidence, String idTariff) {
        Residence residence = getResidenceById(idResidence);
        //retrive the Tariff to add at the residence
        Tariff tariff= tariffService.getTariffById(idTariff);
        //Add residence to Tariff
        tariffService.addResidenceToTariff(residence,tariff);
        //add the meter to the residence
        residence.setTariff(tariff);
        residenceRepository.save(residence);

    }

    public PostResponse removeResidenceById(Integer idResidence) {
        residenceRepository.deleteById(idResidence);
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .url(EntityURLBuilder.buildUrl(RESIDENCE_PATH))
                .build();
    }
}
