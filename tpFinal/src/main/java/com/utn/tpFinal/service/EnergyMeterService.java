package com.utn.tpFinal.service;

import com.utn.tpFinal.model.*;
import com.utn.tpFinal.repository.EnergyMeterRepository;
import com.utn.tpFinal.repository.MeterBrandRepository;
import com.utn.tpFinal.repository.MeterModelRepository;
import com.utn.tpFinal.util.EntityURLBuilder;
import com.utn.tpFinal.util.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.criteria.CriteriaBuilder;
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

//-------------------------------------------->> M E T O D O S <<--------------------------------------------

    public PostResponse addEnergyMeter(EnergyMeter energyMeter) {
        energyMeterRepository.save(energyMeter);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildUrl(ENERGYMETER_PATH,energyMeter.getId()))
                .build();
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
            return  energyMeterRepository.findAll();
        else
            return energyMeterRepository.findBySerialNumber(serialNumber);
    }

    public Page<EnergyMeter> getAllEnergyMeters(String serialNumber, Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<EnergyMeter> pagedResult;
        if(isNull(serialNumber))
            pagedResult =  energyMeterRepository.findAll(pageable);
        else
            pagedResult =  energyMeterRepository.findBySerialNumber(serialNumber,pageable);
        return pagedResult;
    }

    public List<EnergyMeter> getEnergyMetersByBrand(Integer idBrand) {
        MeterBrand brand = getMeterBrandById(idBrand);
        return energyMeterRepository.findByBrand(brand);
    }

    public List<EnergyMeter> getEnergyMetersByModel(Integer idModel) {
        MeterModel model= getMeterModelById(idModel);
        return energyMeterRepository.findByModel(model);
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

    public PostResponse addMeterBrand(MeterBrand brand) {
        meterBrandRepository.save(brand);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildUrl(ENERGYMETER_PATH,brand.getName()))
                .build();
    }

    public void deleteMeterBrandById(Integer idBrand) {
        meterBrandRepository.delete( getMeterBrandById(idBrand) );
    }

    public List<MeterBrand> getAllMeterBrands(){
        return meterBrandRepository.findAll();
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

    public PostResponse addMeterModel(MeterModel model) {
        meterModelRepository.save(model);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildUrl(ENERGYMETER_PATH,model.getName()))
                .build();
    }

    public PostResponse deleteMeterModelById(Integer idModel) {
        meterModelRepository.delete( getMeterModelById(idModel) );
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .url(EntityURLBuilder.buildUrl(ENERGYMETER_PATH))
                .build();
    }

    public List<MeterModel> getAllMeterModels() {
        return meterModelRepository.findAll();
    }

    public MeterModel getMeterModelById(Integer id) {
        return meterModelRepository.findById(id)
            .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public void addEnergyMeterToModel(EnergyMeter energyMeter, MeterModel model){
        model.getEnergyMeters().add(energyMeter); //agrega el medidor a la lista de medidores de la modelos
        meterModelRepository.save(model);
    }


    public PostResponse removeEnergyMeterById(Integer idEnergyMeter) {
        energyMeterRepository.deleteById(idEnergyMeter);
        return PostResponse.builder()
                .status(HttpStatus.OK)
                .url(EntityURLBuilder.buildUrl(ENERGYMETER_PATH))
                .build();
    }
}
