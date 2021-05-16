package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.ExceptionDiferentId;
import com.utn.tpFinal.model.*;
import com.utn.tpFinal.repository.ResidenceRepository;
import com.utn.tpFinal.util.EntityURLBuilder;
import com.utn.tpFinal.util.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public void addTariffToResidence(Integer idResidence, Integer idTariff) {
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

    public Residence modifyResidence(Residence residence)  {
        Residence res= getResidenceById(residence.getId());
        res=residence;
        return  residenceRepository.save(res);
    }
}
