package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.exception.TariffNotExists;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.model.dto.TariffDto;
import com.utn.tpFinal.repository.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffService {
    @Autowired
    private TariffRepository tariffRepository;
    @Autowired
    private ResidenceService residenceService;
    

    public Tariff getTariffById(Integer id) throws TariffNotExists {
        return tariffRepository.findById(id)
                .orElseThrow(() -> new TariffNotExists(this.getClass().getSimpleName(), "getTariffById"));
    }

    //-------------------------------------------->> BRAND <<--------------------------------------------


    public Tariff add(Tariff tariff) {
        return tariffRepository.save(tariff);
    }

    public Page<TariffDto> getAll(Specification<Tariff> tariffSpecification, Integer page, Integer size, List<Order>orders) throws NoContentException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Tariff>tariffs =tariffRepository.findAll(tariffSpecification,pageable);

        if(tariffs.isEmpty()) //todo no muestra mensaje (proba buscar por id de uno q no existe)
            throw new NoContentException(this.getClass().getSimpleName(), "getAll");

        Page<TariffDto> tariffDtos = tariffs.map(t-> TariffDto.from(t));

        return tariffDtos;
    }

    public void addResidenceToTariff(Residence residence, Tariff tariff) {
        tariff.getResidencesList().add(residence);
        tariffRepository.save(tariff);
    }

    public Page<ResidenceDto> getResidencesByTariff(Integer idTariff, Integer page, Integer size, List<Order>orders) throws TariffNotExists, NoContentException {
        if(!tariffRepository.existsById(idTariff))
           throw new TariffNotExists(this.getClass().getSimpleName(), "getResidencesByTariff");

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Residence> residences = residenceService.getResidenceByTariffId(idTariff, pageable);

        if(residences.isEmpty())
            throw new NoContentException(this.getClass().getSimpleName(), "getResidencesByTariff");

        Page<ResidenceDto> residencesDtos = residences.map(r-> ResidenceDto.from(r));
        return residencesDtos;
    }

    public void deleteTariffById(Integer idTariff) throws TariffNotExists {
        if(!tariffRepository.existsById(idTariff))
            throw new TariffNotExists(this.getClass().getSimpleName(), "deleteTariffById");

        tariffRepository.deleteById(idTariff);
    }


    public Tariff modifyTariff(Tariff tariff) throws TariffNotExists { //todo arreglar esto --> param( id, DTO )
        Tariff tar= getTariffById(tariff.getId());
        tar=tariff;
        return tariffRepository.save(tar);
    }
}
