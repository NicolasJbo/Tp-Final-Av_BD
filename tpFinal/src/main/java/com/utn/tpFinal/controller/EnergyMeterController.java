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
    public List<MeterBrand> getAllMeterBrands(@RequestParam(required = false) String name){
        return energyMeterService.getAllMeterBrands(name);
    }

    @PostMapping("/model")
    public void addMeterModel (@RequestBody MeterModel model){
        energyMeterService.addMeterModel(model);
    }

    @GetMapping("/models")
    public List<MeterModel> getAllMeterModels(@RequestParam(required = false) String name){
        return energyMeterService.getAllMeterModels(name);
    }

    @PutMapping("/{id}/brand/{?idBrand}/model/{?idModel}")
    public void addBrandAndModelToEnergyMeter(@PathVariable Integer id,
                                              @PathVariable(required = false) Integer idBrand,
                                              @PathVariable(required = false) Integer idModel ) {
        energyMeterService.addBrandAndModelToEnergyMeter(id,idBrand,idModel);
    }



}
