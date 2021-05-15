package com.utn.tpFinal.controller;

import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Consumption;
import com.utn.tpFinal.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    BillService billService;

//  [PROG - PUNTO 2] Consulta de facturas con rango de fechas
    @GetMapping
    List<Bill> getClientBillsByDates(@RequestParam Integer idClient,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to){

        return billService.getClientBillsByDates(idClient,from, to);
        /*return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(bills.getTotalElements()))
                .header("X-Total-Pages", Long.toString(bills.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(pageNumber))
                .body(bills.getContent());*/
    }

    @GetMapping("/unpaid/{idClient}")
    public List<Bill>getClientUnpaidBills(@PathVariable Integer idClient){
        return billService.getClientUnpaidBills(idClient);
    }

    @GetMapping("/unpaid/{idClient}")
    public Consumption getClientTotalEnergyByDates(@RequestParam Integer idClient,
                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to){
        return billService.getClientTotalEnergyByDates(idClient, from, to);
    }


}
