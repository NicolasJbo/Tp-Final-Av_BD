package com.utn.tpFinal.controller;

import com.utn.tpFinal.util.PostResponse;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tariff")
public class TariffController {

    @Autowired
    private TariffService tariffService;

    @PostMapping
    public PostResponse addTariff(@RequestBody Tariff tariff){

       return tariffService.addTariff(tariff);
    }

    @GetMapping
    public List<Tariff> getAll(){
        return tariffService.getAll();
    }

    @GetMapping("/{idTariff}/residences")
    public List<Residence> getResidencesByTariff(@PathVariable Integer idTariff) {
        return tariffService.getResidencesByTariff(idTariff);
    }
    @DeleteMapping("/{idTariff}")
    public PostResponse removeTariffById(@PathVariable Integer idTariff ){
        return tariffService.removeTariffById(idTariff);
    }
    @PutMapping("")
    public ResponseEntity modifyTariff(@RequestBody Tariff tariff)  {
        Tariff tar= tariffService.modifyTariff(tariff);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Class modify",tar.getClass().getSimpleName())
                .build();
    }



}
