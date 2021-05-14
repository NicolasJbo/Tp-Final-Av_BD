package com.utn.tpFinal.controller;

import com.utn.tpFinal.util.PostResponse;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.service.ResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/residence")
public class ResidenceController {

    @Autowired
    private ResidenceService residenceService;

    @PostMapping
    public ResponseEntity addResidence(@RequestBody Residence residence){
        Residence res= residenceService.addResidence(residence);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idResidence}")
                .buildAndExpand(res.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity <List<Residence>> getAll(@RequestParam(required = false) String street,
                                                   @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
                                                   @RequestParam(defaultValue = "5", required = false) Integer pageSize,
                                                   @RequestParam(defaultValue = "id", required = false) String sortBy) {

       Page<Residence> res = residenceService.getAll(street,pageNumber,pageSize,sortBy);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(res.getTotalElements()))
                .header("X-Total-Pages", Long.toString(res.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(pageNumber))
                .header("X-Sort-Method", sortBy)
                .body(res.getContent());

    }

    @PutMapping("/{idResidence}/energyMeter/{idEnergyMeter}")
    public void addEnergyMeterToResidence(@PathVariable Integer idResidence,@PathVariable Integer idEnergyMeter ){
        residenceService.addEnergyMeterToResidence(idResidence,idEnergyMeter);
    }

    @PutMapping("/{idResidence}/tariff/{idTariff}")
    public void addTariffToResidence(@PathVariable Integer idResidence,@PathVariable String idTariff ){
        residenceService.addTariffToResidence(idResidence,idTariff);
    }
    @DeleteMapping("/{idResidence}")
    public PostResponse removeResidenceById(@PathVariable Integer idResidence){
        return residenceService.removeResidenceById(idResidence);
    }

}
