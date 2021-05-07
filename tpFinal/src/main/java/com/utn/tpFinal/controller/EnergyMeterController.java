package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.MeterBrand;
import com.utn.tpFinal.model.MeterModel;
import com.utn.tpFinal.service.EnergyMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/energyMeter")
public class EnergyMeterController {

    @Autowired
    EnergyMeterService energyMeterService;

    @PostMapping
    public void addEnergyMeter (@RequestBody EnergyMeter energyMeter){
        energyMeterService.addEnergyMeter(energyMeter);
    }

    @GetMapping
    public List<EnergyMeter> getAllEnergyMeters(@RequestParam(required = false) String serialNumber){
        return energyMeterService.getAllEnergyMeters(serialNumber);
    }

    @PostMapping("/brand")
    public void addMeterBrand (@RequestBody MeterBrand brand){
        energyMeterService.addMeterBrand(brand);
    }

    @GetMapping("/brands")
    public List<MeterBrand> getAllMeterBrands(){
        return energyMeterService.getAllMeterBrands();
    }
    @GetMapping("/brand/{nameBrand}/energyMeters")
    public List<EnergyMeter> getEnergyMetersByBrand(@PathVariable String nameBrand){
        return energyMeterService.getEnergyMetersByBrand(nameBrand);
    }

    @PostMapping("/model")
    public void addMeterModel (@RequestBody MeterModel model){
        energyMeterService.addMeterModel(model);
    }

    @GetMapping("/models")
    public List<MeterModel> getAllMeterModels(){
        return energyMeterService.getAllMeterModels();
    }

    @PutMapping("/{id}/brand/{nameBrand}/model/{nameModel}")
    public void addBrandAndModelToEnergyMeter(@PathVariable Integer id,
                                              @PathVariable String nameBrand,
                                              @PathVariable String nameModel ) {
        energyMeterService.addBrandAndModelToEnergyMeter(id,nameBrand,nameModel);
    }
    /*
    @PutMapping("/{id}/brand/{idBrand}/model/{idModel}")
    public void addBrandAndModelToEnergyMeter(@PathVariable Integer id,
                                              @PathVariable Integer idBrand,
                                              @PathVariable Integer idModel ) {
        energyMeterService.addBrandAndModelToEnergyMeter(id,idBrand,idModel);
    }*/




}
