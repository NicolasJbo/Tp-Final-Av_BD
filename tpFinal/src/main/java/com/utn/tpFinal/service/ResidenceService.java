package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.*;
import com.utn.tpFinal.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ResidenceService {
    @Autowired
    private ResidenceRepository residenceRepository;
    @Autowired
    private EnergyMeterService  energyMeterService;
    @Autowired
    private TariffService tariffService;

   /* @Autowired
    public ResidenceService(ResidenceRepository residenceRepository, EnergyMeterService energyMeterService, TariffService tariffService) {
        this.residenceRepository = residenceRepository;
        this.energyMeterService = energyMeterService;
        this.tariffService = tariffService;
    }*/

//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public Residence addResidence(Residence residence) {
        return  residenceRepository.save(residence);
    }

    public Page<Residence> getAll( String street, Integer pageNumber,Integer pageSize,String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Residence> pagedResult;

        if(isNull(street))
          pagedResult= residenceRepository.findAll(pageable);
        else
           pagedResult= residenceRepository.findByStreet(street,pageable);

        return pagedResult;

    }

    public Residence getResidenceById(Integer id) throws ResidenceNotExists {
        return residenceRepository.findById(id)
                .orElseThrow(() -> new ResidenceNotExists(this.getClass().getSimpleName(), "getResidenceById"));
    }

    public Page<Residence> getResidenceByClientId(Integer idClient, Pageable pageable) {
        return residenceRepository.findByClientId(idClient, pageable);
    }

    public Page<Residence> getResidenceByTariffId(Integer idTariff, Pageable pageable) {
        return residenceRepository.findByTariffId(idTariff, pageable);
    }

    public void addClientToResidence(Client client, Residence residence) {
        residence.setClient(client);
        residenceRepository.save(residence);
    }

    public void addEnergyMeterToResidence(Integer idResidence, Integer idEnergyMeter) throws ResidenceNotExists, EnergyMeterNotExists, ResidenceDefined {

        EnergyMeter energyMeter = energyMeterService.getEnergyMeterById(idEnergyMeter);
        if(energyMeter.getResidence() != null) { //si el medidor ya tiene un domicilio
            throw new ResidenceDefined(this.getClass().getSimpleName(), "getClientResidences");
        }
        Residence residence = getResidenceById(idResidence);
        energyMeterService.addResidenceToMeter(residence, energyMeter);
        residence.setEnergyMeter(energyMeter);
        residenceRepository.save(residence);

        //todo hacer Excepcion -Nico

    }

    public void addTariffToResidence(Integer idResidence, Integer idTariff) throws ResidenceNotExists, TariffNotExists {
        Residence residence = getResidenceById(idResidence);
        //retrive the Tariff to add at the residence
        Tariff tariff= tariffService.getTariffById(idTariff);
        //Add residence to Tariff
        tariffService.addResidenceToTariff(residence,tariff);
        //add the meter to the residence
        residence.setTariff(tariff);
        residenceRepository.save(residence);
    }

    public void removeResidenceById(Integer idResidence) {
        residenceRepository.deleteById(idResidence);
    }

    public Residence modifyResidence(Residence residence) throws ResidenceNotExists {
        Residence res= getResidenceById(residence.getId());
        res=residence;  //TODO este metodo esta para el culo, hay q arreglarlo
        return  residenceRepository.save(res);
    }



}
