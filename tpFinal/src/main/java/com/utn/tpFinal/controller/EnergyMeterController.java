package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.*;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.dto.EnergyMeterDto;
import com.utn.tpFinal.service.EnergyMeterService;
import com.utn.tpFinal.util.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/energyMeter")
public class EnergyMeterController {

    @Autowired
    private EnergyMeterService energyMeterService;

//--------------------------- ENERGYMETER --------------------------------------------
    @PostMapping
    public PostResponse addEnergyMeter (@RequestBody EnergyMeter energyMeter){
       return energyMeterService.addEnergyMeter(energyMeter);
    }

    @GetMapping
    public ResponseEntity<List<EnergyMeter>>getAllEnergyMeters(@RequestParam(required = false) String serialNumber,
                                                              @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
                                                              @RequestParam(defaultValue = "5", required = false) Integer pageSize,
                                                              @RequestParam(defaultValue = "id", required = false) String sortBy){
        Page<EnergyMeter> meters = energyMeterService.getAllEnergyMeters(serialNumber, pageNumber, pageSize, sortBy);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(meters.getTotalElements()))
                .header("X-Total-Pages", Long.toString(meters.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(pageNumber))
                .body(meters.getContent());
    }

    @PutMapping("/{id}/brand/{idBrand}/model/{idModel}")
    public void addBrandAndModelToEnergyMeter(@PathVariable Integer id,
                                              @PathVariable Integer idBrand,
                                              @PathVariable Integer idModel ) {
        energyMeterService.addBrandAndModelToEnergyMeter(id,idBrand,idModel);
    }
    @DeleteMapping("/{idEnergyMeter}")
    public PostResponse removeEnergyMeterById(@PathVariable Integer idEnergyMeter ){
        return energyMeterService.removeEnergyMeterById(idEnergyMeter);
    }

//--------------------------- RESIDENCE --------------------------------------------

    @GetMapping("/{idEnergyMeter}/residence")
    public Residence getResidenceByEnergyMeterId(@PathVariable Integer idEnergyMeter){
        return energyMeterService.getResidenceByEnergyMeterId(idEnergyMeter);
    }
//--------------------------- BRAND --------------------------------------------
    @PostMapping("/brand")
    public PostResponse addMeterBrand (@RequestBody MeterBrand brand){

        return energyMeterService.addMeterBrand(brand);
    }

    @GetMapping("/brand/{idBrand}/delete")
    public void deleteMeterBrandById(@PathVariable Integer idBrand){
        energyMeterService.deleteMeterBrandById(idBrand);
    }

    @GetMapping("/brands")
    public List<MeterBrand> getAllMeterBrands(){
        return energyMeterService.getAllMeterBrands();
    }

    @GetMapping("/brand/{idBrand}/energyMeters")
    public List<EnergyMeter> getEnergyMetersByBrand(@PathVariable Integer idBrand){
        return energyMeterService.getEnergyMetersByBrand(idBrand);
    }

//--------------------------- MODEL --------------------------------------------
    @PostMapping("/model")
    public PostResponse addMeterModel (@RequestBody MeterModel model) {
        return energyMeterService.addMeterModel(model);
    }

    @GetMapping("/model/{idModel}/delete")
    public PostResponse deleteMeterModelById(@PathVariable Integer idModel){
        return energyMeterService.deleteMeterModelById(idModel);
    }

    @GetMapping("/models")
    public List<MeterModel> getAllMeterModels(){
        return energyMeterService.getAllMeterModels();
    }

    @GetMapping("/model/{idModel}/energyMeters")
    public List<EnergyMeter> getEnergyMetersByModel(@PathVariable Integer idModel){
        return energyMeterService.getEnergyMetersByModel(idModel);
    }







}
