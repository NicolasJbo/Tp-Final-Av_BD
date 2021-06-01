package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.proyection.Top10Clients;
import com.utn.tpFinal.service.ClientService;
import com.utn.tpFinal.service.ResidenceService;
import com.utn.tpFinal.service.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/backoffice")
public class BackOfficeController {

    @Autowired
    ResidenceService residenceService;
    @Autowired
    ClientService clientService;
    @Autowired
    TariffService tariffService;
//-----------------------------PUNTO 1 ------------------------------------------------
    //todo login
//-----------------------------PUNTO 2 ------------------------------------------------
    @PostMapping("/tariff")
    public ResponseEntity addTariff(@RequestBody Tariff tariff) throws URISyntaxException {
        Tariff t = tariffService.add(tariff);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/")
                .query("id={idTariff}")
                .buildAndExpand(t.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @DeleteMapping("/tariff/{idTariff}")
    public ResponseEntity deleteTariffById(@PathVariable Integer idTariff) throws TariffNotExists {
        tariffService.deleteTariffById(idTariff);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/tariff/{idTarrif}")
    public ResponseEntity modifyTariff(@PathVariable Integer idTarrif ,@RequestBody Tariff tariff) throws Exception {
        Tariff tar= tariffService.modifyTariff(idTarrif,tariff);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Class modify",tar.getClass().getSimpleName())
                .build();
    }

//-----------------------------PUNTO 3 ------------------------------------------------
    @PostMapping("/residence")
    public ResponseEntity addResidence(@RequestBody Residence residence){
        Residence res= residenceService.addResidence(residence);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/")
                .query("id={idResidence}")
                .buildAndExpand(res.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @DeleteMapping("/residence/{idResidence}")
    public ResponseEntity removeResidenceById(@PathVariable Integer idResidence) throws ResidenceNotExists {
        residenceService.removeResidenceById(idResidence);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/residence/{idResidence}")
    public ResponseEntity modifyResidence(@PathVariable Integer idResidence, @RequestBody Residence residence) throws Exception {
        Residence res= residenceService.modifyResidence(idResidence,residence);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Class Modify",res.getClass().getSimpleName())
                .build();
    }
    //-----------------------------PUNTO 4 ------------------------------------------------
//  [PROG - 4]  BACKOFFICE -> Consulta de facturas impagas por  domicilio
    @GetMapping("/residence/{idResidence}/bills/unpaid")
    public ResponseEntity<List<BillDto>>getResidenceUnpaidBills(@PathVariable Integer idResidence,
                                                                @RequestParam(defaultValue = "0") Integer page,
                                                                @RequestParam(defaultValue = "5") Integer size,
                                                                @RequestParam(defaultValue = "id") String sortField1,
                                                                @RequestParam(defaultValue = "expirationDate") String sortField2) throws NoContentException, ClientNotExists, ResidenceNotExists {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<BillDto> bills = residenceService.getResidenceUnpaidBills(idResidence, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(bills.getTotalElements()))
                .header("X-Total-Pages", Long.toString(bills.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(bills.getContent());
    }

    //  [PROG - 4]  BACKOFFICE -> Consulta de facturas impagas por  cliente
    @GetMapping("/client/{idClient}/bills/unpaid")
    public ResponseEntity<List<BillDto>>getClientUnpaidBills(@PathVariable Integer idClient,
                                                             @RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "5") Integer size,
                                                             @RequestParam(defaultValue = "id") String sortField1,
                                                             @RequestParam(defaultValue = "expirationDate") String sortField2) throws NoContentException, ClientNotExists {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<BillDto> bills = clientService.getClientUnpaidBills(idClient, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(bills.getTotalElements()))
                .header("X-Total-Pages", Long.toString(bills.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(bills.getContent());
    }
//-----------------------------PUNTO 5  ------------------------------------------------
//  [PROG - PUNTO 5] BACKOFFICE -> Consulta de 10 clientes mas consumidores por fechas
    @GetMapping("/client/topConsumers")
    public ResponseEntity<List<Top10Clients>>getTop10ConsumerByDates(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws NoContentException {
        List<Top10Clients> rta = clientService.getTop10ConsumerByDates(from,to);

        return ResponseEntity.status(HttpStatus.OK).body(rta);
    }
//-----------------------------PUNTO 6  ------------------------------------------------
//  [PROG - PUNTO 6] BACKOFFICE -> Consulta mediciones de un domicilio por rango de fechas
    @GetMapping("/residence/{idResidence}/measures")
    public ResponseEntity<List<MeasureDto>>getResidenceMeasuresByDates(@PathVariable Integer idResidence,
                                                                       @RequestParam(defaultValue = "0") Integer page,
                                                                       @RequestParam(defaultValue = "5") Integer size,
                                                                       @RequestParam(defaultValue = "id") String sortField1,
                                                                       @RequestParam(defaultValue = "date") String sortField2,
                                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) throws NoContentException, ResidenceNotExists {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<MeasureDto> measures = residenceService.getResidenceMeasuresByDates(idResidence,from,to,page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(measures.getTotalElements()))
                .header("X-Total-Pages", Long.toString(measures.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(measures.getContent());
    }

//---------------------------------------------------------------------------------------------



}
