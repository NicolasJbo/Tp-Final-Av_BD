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
import org.springframework.security.access.prepost.PreAuthorize;
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

    private ResidenceService residenceService;
    private BillService billService;

    @Autowired
    public ResidenceController(ResidenceService residenceService, BillService billService) {
        this.residenceService = residenceService;
        this.billService = billService;
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<ResidenceDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "5") Integer size,
                                                     @RequestParam(defaultValue = "id") String sortField1,
                                                     @RequestParam(defaultValue = "street") String sortField2,
                                                     @And({  @Spec(path = "id", spec = Equal.class),
                                                          @Spec(path = "street", spec = LikeIgnoreCase.class),
                                                          @Spec(path = "number", spec = LikeIgnoreCase.class)
                                                  }) Specification<Residence> residenceSpecification) throws Exception {

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));
        Page<ResidenceDto> dtoResidence = residenceService.getAll(residenceSpecification, page, size, orders);
        if(dtoResidence.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else {
            return ResponseEntity.status(HttpStatus.OK)
                    .header("X-Total-Elements", Long.toString(dtoResidence.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(dtoResidence.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(dtoResidence.getContent());
        }
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @PutMapping("/{idResidence}/energyMeter/{idEnergyMeter}")
    public ResponseEntity addEnergyMeterToResidence(@PathVariable Integer idResidence,@PathVariable Integer idEnergyMeter ) throws Exception {
        residenceService.addEnergyMeterToResidence(idResidence,idEnergyMeter);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @PutMapping("/{idResidence}/tariff/{idTariff}")
    public ResponseEntity addTariffToResidence(@PathVariable Integer idResidence,@PathVariable Integer idTariff ) throws Exception{
        residenceService.addTariffToResidence(idResidence,idTariff);
        return ResponseEntity.ok().build();
    }


}
