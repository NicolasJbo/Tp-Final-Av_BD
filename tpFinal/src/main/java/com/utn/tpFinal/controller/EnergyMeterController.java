package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.*;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.dto.EnergyMeterDto;
import com.utn.tpFinal.service.EnergyMeterService;
import com.utn.tpFinal.util.PostResponse;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    public ResponseEntity<List<EnergyMeterDto>> getAll(
            @And({
                    @Spec(path = "serialNumber", spec = Equal.class),
                    @Spec(path = "id", spec = Equal.class)
            }) Specification<EnergyMeter> meterSpecification, Pageable pageable){

        Page<EnergyMeterDto> dtoEnergyMeter = energyMeterService.getAll(meterSpecification, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(dtoEnergyMeter.getTotalElements()))
                .header("X-Total-Pages", Long.toString(dtoEnergyMeter.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(pageable.getPageNumber()))
                .body(dtoEnergyMeter.getContent());
    }

    @PutMapping("/{idEnergyMeter}")
    public void addBrandAndModelToEnergyMeter(@PathVariable Integer idEnergyMeter,
                                              @RequestParam Integer idBrand,
                                              @RequestParam Integer idModel ) {
        energyMeterService.addBrandAndModelToEnergyMeter(idEnergyMeter,idBrand,idModel);
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

    @GetMapping("/brands")
    public List<MeterBrand> getAllMeterBrands(){
        return energyMeterService.getAllMeterBrands();
    }

//--------------------------- MODEL --------------------------------------------
    @GetMapping("/models")
    public List<MeterModel> getAllMeterModels(){
        return energyMeterService.getAllMeterModels();
    }








}
