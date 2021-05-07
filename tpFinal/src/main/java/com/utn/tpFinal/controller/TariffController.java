package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.service.ClientService;
import com.utn.tpFinal.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tariff")
public class TariffController {

    @Autowired
    TariffService tariffService;

    @PostMapping
    public void addTariff(@RequestBody Tariff tariff){
        tariffService.addTariff(tariff);
    }

    @GetMapping
    public List<Tariff> getAll(){
        return tariffService.getAll();
    }
    @GetMapping("/{nameTariff}/residences")
    public List<Residence> getResidencesByTariff(@PathVariable String nameTariff)
    {
        return tariffService.getResidencesByTariff(nameTariff);
    }



}
