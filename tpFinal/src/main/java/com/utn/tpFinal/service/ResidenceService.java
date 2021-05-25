package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.*;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
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



//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public Residence addResidence(Residence residence) {
        return  residenceRepository.save(residence);
    }

    public Page<ResidenceDto> getAll(Specification<Residence> residenceSpecification,Integer page,Integer size,List<Order> orders) throws NoContentException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Residence> residences = residenceRepository.findAll(residenceSpecification,pageable);

      if(residences.isEmpty())//TODO muestra mal
          throw new NoContentException(this.getClass().getSimpleName(), "getAll");

        Page<ResidenceDto> dtoResidence = residences.map(r -> ResidenceDto.from(r));
        return dtoResidence;
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
        res=residence;  //TODO este metodo esta para el culo, hay q arreglarlo --> parama(id , DTO)
        return  residenceRepository.save(res);
    }



}
