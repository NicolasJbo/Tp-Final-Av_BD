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


    private EnergyMeterRepository energyMeterRepository;
    private MeterBrandRepository meterBrandRepository;
    private MeterModelRepository meterModelRepository;

    @Autowired
    public EnergyMeterService(EnergyMeterRepository energyMeterRepository, MeterBrandRepository meterBrandRepository, MeterModelRepository meterModelRepository) {
        this.energyMeterRepository = energyMeterRepository;
        this.meterBrandRepository = meterBrandRepository;
        this.meterModelRepository = meterModelRepository;
    }

//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public void addEnergyMeter(EnergyMeter energyMeter) {
        energyMeterRepository.save(energyMeter);
    }

    public EnergyMeter getEnergyMeterById(Integer id) {
        return energyMeterRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void deleteEnergyMeterById(Integer idEnergyMeter) {
        energyMeterRepository.delete( getEnergyMeterById(idEnergyMeter) );
    }

    public List<EnergyMeter> getAllEnergyMeters(String serialNumber) {
        if(isNull(serialNumber))
            return energyMeterRepository.findAll();
        else
            return energyMeterRepository.findBySerialNumber(serialNumber);
    }

    public List<EnergyMeter> getEnergyMetersByBrand(String nameBrand) {
        MeterBrand brand = getMeterBrandByName(nameBrand);
        return energyMeterRepository.findByBrand(brand);
    }

    public List<EnergyMeter> getEnergyMetersByModel(String nameModel) {
        MeterModel model= getMeterModelByName(nameModel);
        return energyMeterRepository.findByModel(model);
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

    public  void addResidenceToMeter(Residence residence,EnergyMeter energyMeter){
        energyMeter.setResidence(residence);
        energyMeterRepository.save(energyMeter);
    }
//------------------------------------------>> RESIDENCE <<------------------------------------------

    public Residence getResidenceByEnergyMeterId(Integer idEnergyMeter) {
        EnergyMeter energyMeter =getEnergyMeterById(idEnergyMeter);
        return energyMeter.getResidence();
    }
//-------------------------------------------->> BRAND <<--------------------------------------------

    public void addMeterBrand(MeterBrand brand) {
        meterBrandRepository.save(brand);
    }

    public void deleteMeterBrandByName(String nameBrand) {
        meterBrandRepository.delete( getMeterBrandByName(nameBrand) );
    }

    public List<MeterBrand> getAllMeterBrands(){
        return meterBrandRepository.findAll();
    }

    public MeterBrand getMeterBrandByName(String name) {
        return meterBrandRepository.findById(name)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addEnergyMeterToBrand(EnergyMeter energyMeter, MeterBrand brand){
        brand.getEnergyMeters().add(energyMeter); //agrega el medidor a la lista de medidores de la marca
        meterBrandRepository.save(brand);
    }

//-------------------------------------------->> MODEL <<--------------------------------------------

    public void addMeterModel(MeterModel model) {
        meterModelRepository.save(model);
    }

    public void deleteMeterModelByName(String nameModel) {
        meterModelRepository.delete( getMeterModelByName(nameModel) );
    }

    public List<MeterModel> getAllMeterModels() {
        return meterModelRepository.findAll();
    }

    public MeterModel getMeterModelByName(String name) {
        return meterModelRepository.findById(name)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addEnergyMeterToModel(EnergyMeter energyMeter, MeterModel model){
        model.getEnergyMeters().add(energyMeter); //agrega el medidor a la lista de medidores de la modelos
        meterModelRepository.save(model);
    }








}
