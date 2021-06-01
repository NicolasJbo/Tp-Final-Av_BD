package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.ClientNotExists;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.exception.ResidenceNotExists;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.dto.BillDto;
import com.utn.tpFinal.model.dto.ClientDto;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.proyection.MeasuresById;
import com.utn.tpFinal.service.BillService;
import com.utn.tpFinal.service.ResidenceService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.Authenticator;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/residence")
public class ResidenceController {

    @Autowired
    private ResidenceService residenceService;
    @Autowired
    BillService billService;
    //Todo borrar este endpoint si funciona el de backoffice
    /*
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
     */


    @GetMapping
    public ResponseEntity<List<ResidenceDto>> getAll(Authentication authenticator, @RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "5") Integer size,
                                                     @RequestParam(defaultValue = "id") String sortField1,
                                                     @RequestParam(defaultValue = "street") String sortField2,
                                                     @And({  @Spec(path = "id", spec = Equal.class),
                                                          @Spec(path = "street", spec = LikeIgnoreCase.class),
                                                          @Spec(path = "number", spec = LikeIgnoreCase.class)
                                                  }) Specification<Residence> residenceSpecification) throws Exception {
        System.out.println(authenticator.getPrincipal());

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<ResidenceDto> dtoResidence = residenceService.getAll(residenceSpecification, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(dtoResidence.getTotalElements()))
                .header("X-Total-Pages", Long.toString(dtoResidence.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(dtoResidence.getContent());
    }

    @PutMapping("/{idResidence}/energyMeter/{idEnergyMeter}")
    public void addEnergyMeterToResidence(@PathVariable Integer idResidence,@PathVariable Integer idEnergyMeter ) throws Exception {
        residenceService.addEnergyMeterToResidence(idResidence,idEnergyMeter);
    }

    @PutMapping("/{idResidence}/tariff/{idTariff}")
    public void addTariffToResidence(@PathVariable Integer idResidence,@PathVariable Integer idTariff ) throws Exception{
        residenceService.addTariffToResidence(idResidence,idTariff);
    }
    //Todo borrar este endpoint si funciona el de backoffice
    /*
    @PutMapping("/{idResidence}")
    public ResponseEntity modifyResidence(@PathVariable Integer idResidence, @RequestBody Residence residence) throws Exception {
        Residence res= residenceService.modifyResidence(idResidence,residence);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Class Modify",res.getClass().getSimpleName())
                .build();
    }
    */

    //Todo borrar este endpoint si funciona el de backoffice
   /* @DeleteMapping("/{idResidence}")
    public ResponseEntity removeResidenceById(@PathVariable Integer idResidence) throws ResidenceNotExists {
        residenceService.removeResidenceById(idResidence);
        return ResponseEntity.ok().build();
    }
    */

    //todo sacar
    /*
    @GetMapping("/{idResidence}/measures")
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

    @GetMapping("/{idResidence}/bills/unpaid")
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

     */


}
