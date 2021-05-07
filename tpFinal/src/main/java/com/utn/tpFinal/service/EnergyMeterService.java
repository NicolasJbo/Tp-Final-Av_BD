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

//--------------------------- BRAND --------------------------------------------
    public MeterBrand getMeterBrandByName(String name) {
        return meterBrandRepository.findById(name)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }
    public void addEnergyMeterToBrand(EnergyMeter energyMeter, MeterBrand brand){
        brand.getEnergyMeters().add(energyMeter); //agrega el medidor a la lista de medidores de la marca
        meterBrandRepository.save(brand);
    }

    public List<EnergyMeter> getEnergyMetersByBrand(String nameBrand) {
        MeterBrand brand = getMeterBrandByName(nameBrand);
        return energyMeterRepository.findByBrand(brand);
    }
//--------------------------- MODEL --------------------------------------------

    public MeterModel getMeterModelByName(String name) {
        return meterModelRepository.findById(name)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addEnergyMeterToModel(EnergyMeter energyMeter, MeterModel model){
        model.getEnergyMeters().add(energyMeter); //agrega el medidor a la lista de medidores de la modelos
        meterModelRepository.save(model);
    }

    public List<EnergyMeter> getEnergyMetersByModel(String nameModel) {
        MeterModel model= getMeterModelByName(nameModel);
        return energyMeterRepository.findByModel(model);

    }
//--------------------------- ENERGYMETER --------------------------------------------
    public List<EnergyMeter> getAllEnergyMeters(String serialNumber) {
        if(isNull(serialNumber))
            return energyMeterRepository.findAll();
        else
            return energyMeterRepository.findBySerialNumber(serialNumber);
    }

    public void addMeterBrand(MeterBrand brand) {
        meterBrandRepository.save(brand);
    }

    public List<MeterBrand> getAllMeterBrands(){
        return meterBrandRepository.findAll();

    }

    public void addMeterModel(MeterModel model) {
        meterModelRepository.save(model);
    }

    public List<MeterModel> getAllMeterModels() {
        return meterModelRepository.findAll();
    }

    public EnergyMeter getEnergyMeterById(Integer id) {
        return energyMeterRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addBrandAndModelToEnergyMeter(Integer id, String nameBrand, String nameModel) {

        EnergyMeter energyMeter = getEnergyMeterById(id);
        if(!isNull(nameBrand)){
            MeterBrand brand = getMeterBrandByName(nameBrand);
            addEnergyMeterToBrand(energyMeter,brand);
            energyMeter.setBrand(brand);
        }
        if(!isNull(nameModel)){
            MeterModel model = getMeterModelByName(nameModel);
            addEnergyMeterToModel(energyMeter,model);
            energyMeter.setModel(model);
        }
        if(!isNull(nameBrand) || !isNull(nameModel)) {
            energyMeterRepository.save(energyMeter);
        }

    }
//--------------------------- RESIDENCE --------------------------------------------

    public  void addResidenceToMeter(Residence residence,EnergyMeter energyMeter){
        energyMeter.setResidence(residence);
        energyMeterRepository.save(energyMeter);
    }
    public Residence getResidenceByEnergyMeterId(Integer idEnergyMeter) {
        EnergyMeter energyMeter =getEnergyMeterById(idEnergyMeter);
        return energyMeter.getResidence();
    }


}
