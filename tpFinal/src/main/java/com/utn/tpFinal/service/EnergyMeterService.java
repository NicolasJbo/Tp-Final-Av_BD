package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.*;
import com.utn.tpFinal.model.dto.EnergyMeterDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.repository.EnergyMeterRepository;
import com.utn.tpFinal.repository.MeterBrandRepository;
import com.utn.tpFinal.repository.MeterModelRepository;
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
public class EnergyMeterService {


    private static final String ENERGYMETER_PATH = "energyMeter";
    private EnergyMeterRepository energyMeterRepository;
    private MeterBrandRepository meterBrandRepository;
    private MeterModelRepository meterModelRepository;

    @Autowired
    public EnergyMeterService(EnergyMeterRepository energyMeterRepository, MeterBrandRepository meterBrandRepository, MeterModelRepository meterModelRepository) {
        this.energyMeterRepository = energyMeterRepository;
        this.meterBrandRepository = meterBrandRepository;
        this.meterModelRepository = meterModelRepository;
    }

    public EnergyMeter getEnergyMeterById(Integer id) throws EnergyMeterNotExists {
        return energyMeterRepository.findById(id)
                .orElseThrow(() -> new EnergyMeterNotExists(this.getClass().getSimpleName(), "getEnergyMeterById"));
    }
    public EnergyMeter getEnergyMeterBySerialNumber(String serial) throws EnergyMeterNotExists {
        return energyMeterRepository.findBySerialNumber(serial)
                .orElseThrow(() -> new EnergyMeterNotExists(this.getClass().getSimpleName(), "getEnergyMeterBySerialNumber"));
    }

//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public EnergyMeter add(EnergyMeter energyMeter,Integer idmodel,Integer idBrand) {

        if(!isNull(idBrand)){
            MeterBrand brand = getMeterBrandById(idBrand);
            addEnergyMeterToBrand(energyMeter,brand);
            energyMeter.setBrand(brand);
        }
        if(!isNull(idmodel)){
            MeterModel model = getMeterModelById(idmodel);
            addEnergyMeterToModel(energyMeter,model);
            energyMeter.setModel(model);
        }

        return   energyMeterRepository.save(energyMeter);
    }

    public Page<EnergyMeterDto> getAll(Specification<EnergyMeter> meterSpecification, Integer page, Integer size, List<Order>orders) throws NoContentException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<EnergyMeter>meters =energyMeterRepository.findAll(meterSpecification,pageable);

        if(meters.isEmpty()) //todo no muestra mensaje (proba buscar por id de uno q no existe)
            throw new NoContentException(this.getClass().getSimpleName(), "getAll");

        Page<EnergyMeterDto> dtoMeters = meters.map(m-> EnergyMeterDto.from(m));

        return  dtoMeters;
    }

    public void addResidenceToMeter(Residence residence,EnergyMeter energyMeter){
        energyMeter.setResidence(residence);
        energyMeterRepository.save(energyMeter);
    }
//------------------------------------------>> RESIDENCE <<------------------------------------------

    public ResidenceDto getResidenceByEnergyMeterId(Integer idEnergyMeter) throws EnergyMeterNotExists, ResidenceNotDefined {
        EnergyMeter energyMeter =getEnergyMeterById(idEnergyMeter);
        Residence residence = energyMeter.getResidence();

        if(isNull(residence))
            throw new ResidenceNotDefined(this.getClass().getSimpleName(), "getResidenceByEnergyMeterId");

        return ResidenceDto.from(residence);
    }
//-------------------------------------------->> BRAND <<--------------------------------------------

    public Page<MeterBrand> getAllMeterBrands(Specification<MeterBrand> meterBrandSpecification, Integer page, Integer size, List<Sort.Order>orders) throws NoContentException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<MeterBrand>meterBrands = meterBrandRepository.findAll(meterBrandSpecification, pageable);

        if(meterBrands.isEmpty())
            throw new NoContentException(this.getClass().getSimpleName(), "getAllMeterBrands");

        return meterBrands;
    }

    public MeterBrand getMeterBrandById(Integer id) {
        return meterBrandRepository.findById(id)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addEnergyMeterToBrand(EnergyMeter energyMeter, MeterBrand brand){
        brand.getEnergyMeters().add(energyMeter); //agrega el medidor a la lista de medidores de la marca
        meterBrandRepository.save(brand);
    }

//-------------------------------------------->> MODEL <<--------------------------------------------

    public Page<MeterModel> getAllMeterModels(Specification<MeterModel> meterModelSpecification, Integer page, Integer size, List<Sort.Order>orders) throws NoContentException {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<MeterModel>meterModels = meterModelRepository.findAll(meterModelSpecification, pageable);

        if(meterModels.isEmpty())
            throw new NoContentException(this.getClass().getSimpleName(), "getAllMeterModels");

        return meterModels;
    }


    public MeterModel getMeterModelById(Integer id) {
        return meterModelRepository.findById(id)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addEnergyMeterToModel(EnergyMeter energyMeter, MeterModel model){
        model.getEnergyMeters().add(energyMeter); //agrega el medidor a la lista de medidores de la modelos
        meterModelRepository.save(model);
    }


    public void deleteEnergyMeterById(Integer idEnergyMeter) throws EnergyMeterNotExists {
        if(!energyMeterRepository.existsById(idEnergyMeter))
            throw new EnergyMeterNotExists(this.getClass().getSimpleName(), "deleteEnergyMeterById");

        energyMeterRepository.deleteById(idEnergyMeter);
    }



}
