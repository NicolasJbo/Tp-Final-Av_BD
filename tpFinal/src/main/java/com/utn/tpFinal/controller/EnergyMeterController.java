package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.*;
import com.utn.tpFinal.service.EnergyMeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/energyMeter")
public class EnergyMeterController {

    @Autowired
    private EnergyMeterService energyMeterService;


//--------------------------- RESIDENCE --------------------------------------------

    @GetMapping("/{idEnergyMeter}/residence")
    public Residence getResidenceByEnergyMeterId(@PathVariable Integer idEnergyMeter){
        return energyMeterService.getResidenceByEnergyMeterId(idEnergyMeter);
    }
//--------------------------- ENERGYMETER --------------------------------------------
    @PostMapping
    public void addEnergyMeter (@RequestBody EnergyMeter energyMeter){
        energyMeterService.addEnergyMeter(energyMeter);
    }

    @GetMapping("{idEnergyMeter}/delete")
    public void deleteEnergyMeterById(@PathVariable Integer idEnergyMeter){
        energyMeterService.deleteEnergyMeterById(idEnergyMeter);
    }

    @GetMapping
    public List<EnergyMeter> getAllEnergyMeters(@RequestParam(required = false) String serialNumber){
        return energyMeterService.getAllEnergyMeters(serialNumber);
    }
    @PutMapping("/{id}/brand/{nameBrand}/model/{nameModel}")
    public void addBrandAndModelToEnergyMeter(@PathVariable Integer id,
                                              @PathVariable String nameBrand,
                                              @PathVariable String nameModel ) {
        energyMeterService.addBrandAndModelToEnergyMeter(id,nameBrand,nameModel);
    }

//--------------------------- BRAND --------------------------------------------
    @PostMapping("/brand")
    public void addMeterBrand (@RequestBody MeterBrand brand){
        energyMeterService.addMeterBrand(brand);
    }

    @GetMapping("/brand/{nameBrand}/delete")
    public void deleteMeterBrandByName(@PathVariable String nameBrand){
        energyMeterService.deleteMeterBrandByName(nameBrand);
    }

    @GetMapping("/brands")
    public List<MeterBrand> getAllMeterBrands(){
        return energyMeterService.getAllMeterBrands();
    }

    @GetMapping("/brand/{nameBrand}/energyMeters")
    public List<EnergyMeter> getEnergyMetersByBrand(@PathVariable String nameBrand){
        return energyMeterService.getEnergyMetersByBrand(nameBrand);
    }

//--------------------------- MODEL --------------------------------------------
    @PostMapping("/model")
    public void addMeterModel (@RequestBody MeterModel model){
        energyMeterService.addMeterModel(model);
    }

    @GetMapping("/model/{nameModel}/delete")
    public void deleteMeterModelByName(@PathVariable String nameModel){
        energyMeterService.deleteMeterModelByName(nameModel);
    }

    @GetMapping("/models")
    public List<MeterModel> getAllMeterModels(){
        return energyMeterService.getAllMeterModels();
    }

    @GetMapping("/model/{nameModel}/energyMeters")
    public List<EnergyMeter> getEnergyMetersByModel(@PathVariable String nameModel){
        return energyMeterService.getEnergyMetersByModel(nameModel);
    }







}
