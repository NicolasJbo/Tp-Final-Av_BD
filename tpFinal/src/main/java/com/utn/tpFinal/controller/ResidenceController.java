package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.service.ResidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/domicilio")
public class ResidenceController {

    @Autowired
    ResidenceService residenceService;

    @PostMapping
    public void addResidence(@RequestBody Residence residence){
        residenceService.addResidence(residence);
    }

    @GetMapping
    public List<Residence> getAll(@RequestParam(required = false) String street) {
        return residenceService.getAll(street);
    }
}
