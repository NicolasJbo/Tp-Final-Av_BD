package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.proyection.Consumption;
import com.utn.tpFinal.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    BillService billService;


/*
//  [PROG - PUNTO 2] Consulta de facturas con rango de fechas
    @GetMapping
    List<Bill> getClientBillsByDates(@PathVariable Integer idClient,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws IncorrectDatesException {

        return billService.getClientBillsByDates(idClient,from, to);
        /*return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(bills.getTotalElements()))
                .header("X-Total-Pages", Long.toString(bills.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(pageNumber))
                .body(bills.getContent());
    }

    @GetMapping("/unpaids/{idClient}")
    public List<Bill>getClientUnpaidBills(@PathVariable Integer idClient){
        return billService.getClientUnpaidBills(idClient);
    }

    @GetMapping("/{idClient}/consumption/")
    public Consumption getClientTotalEnergyByAndAmountDates(@PathVariable Integer idClient,
                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                   @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws IncorrectDatesException {
        Consumption c = billService.getClientTotalEnergyAndAmountByDates(idClient, from, to);
        return c;
    }
*/

}
