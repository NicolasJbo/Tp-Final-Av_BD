package com.utn.tpFinal.service;

import com.utn.tpFinal.model.*;
import com.utn.tpFinal.repository.EnergyMeterRepository;
import com.utn.tpFinal.repository.MeterBrandRepository;
import com.utn.tpFinal.repository.MeterModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class EnergyMeterService {


    EnergyMeterRepository energyMeterRepository;
    MeterBrandRepository meterBrandRepository;
    MeterModelRepository meterModelRepository;

    @Autowired
    public EnergyMeterService(EnergyMeterRepository energyMeterRepository, MeterBrandRepository meterBrandRepository, MeterModelRepository meterModelRepository) {
        this.energyMeterRepository = energyMeterRepository;
        this.meterBrandRepository = meterBrandRepository;
        this.meterModelRepository = meterModelRepository;
    }

    public void addEnergyMeter(EnergyMeter energyMeter) {
        energyMeterRepository.save(energyMeter);
    }


    public List<EnergyMeter> getAllEnergyMeters(String serialNumber) {
        if(isNull(serialNumber))
            return energyMeterRepository.findAll();
        else
            return energyMeterRepository.findBySerialNumber(serialNumber);
    }

    public void addMeterBrand(MeterBrand brand) {
        meterBrandRepository.save(brand);
    }

    public List<MeterBrand> getAllMeterBrands(String name) {
        if(isNull(name))
            return meterBrandRepository.findAll();
        else
            return meterBrandRepository.findByName(name);
    }

    public void addMeterModel(MeterModel model) {
        meterModelRepository.save(model);
    }

    public List<MeterModel> getAllMeterModels(String name) {
        if(isNull(name))
            return meterModelRepository.findAll();
        else
            return meterModelRepository.findByName(name);
    }

    public EnergyMeter getEnergyMeterById(Integer id) {
        return energyMeterRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public MeterBrand getMeterBrandById(Integer id) {
        return meterBrandRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));

    }

    public MeterModel getMeterModelById(Integer id) {
        return meterModelRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addEnergyMeterToBrand(EnergyMeter energyMeter, MeterBrand brand){
        brand.getEnergyMeters().add(energyMeter); //agrega el medidor a la lista de medidores de la marca
        meterBrandRepository.save(brand);
    }

    public void addEnergyMeterToModel(EnergyMeter energyMeter, MeterModel model){
        model.getEnergyMeters().add(energyMeter); //agrega el medidor a la lista de medidores de la modelos
        meterModelRepository.save(model);
    }


    public void addBrandAndModelToEnergyMeter(Integer id, Integer idBrand, Integer idModel) {

        EnergyMeter energyMeter = getEnergyMeterById(id);
        if(!isNull(idBrand)){
            MeterBrand brand = getMeterBrandById(idBrand);
            addEnergyMeterToBrand(energyMeter,brand);
            energyMeter.setBrand(brand);
        }
        if(!isNull(idModel)){
            MeterModel model = getMeterModelById(idModel);
            addEnergyMeterToModel(energyMeter,model);
            energyMeter.setModel(model);
        }
        if(!isNull(idBrand) || !isNull(idModel)) {
            energyMeterRepository.save(energyMeter);
        }

    }



    public List<EnergyMeter> getEnergyMetersByBrand(Integer idBrand) {
        MeterBrand brand = getMeterBrandById(idBrand);
        return getAllEnergyMeters(null);
    }
}
