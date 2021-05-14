package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.ExceptionDiferentId;
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

    @GetMapping("/{nameTariff}/residences")
    public List<Residence> getResidencesByTariff(@PathVariable String nameTariff) {
        return tariffService.getResidencesByTariff(nameTariff);
    }
    @DeleteMapping("/{idTariff}")
    public PostResponse removeTariffById(@PathVariable String idTariff ){
        return tariffService.removeTariffById(idTariff);
    }
    @PutMapping("/{idTariff}")
    public ResponseEntity modifyTariff(@PathVariable String idTariff,@RequestBody Tariff tariff) throws ExceptionDiferentId {
        Tariff tar= tariffService.modifyTariff(idTariff,tariff);
        //todo Agregar bien el header
        return ResponseEntity.status(HttpStatus.OK)
                .header("Class modify",tar.getClass().getSimpleName())
                //.header("Amount",String.valueOf(tar.getAmount()))
                .build();
    }



}
