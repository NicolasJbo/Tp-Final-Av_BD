package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.exception.ResidenceNotDefined;
import com.utn.tpFinal.model.*;
import com.utn.tpFinal.model.dto.EnergyMeterDto;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.service.EnergyMeterService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/energyMeter")
public class EnergyMeterController {

    private EnergyMeterService energyMeterService;

    @Autowired
    public EnergyMeterController(EnergyMeterService energyMeterService) {
        this.energyMeterService = energyMeterService;
    }

    //--------------------------- ENERGYMETER --------------------------------------------

    @PostMapping
    public ResponseEntity addEnergyMeter (@RequestBody EnergyMeter energyMeter){
        EnergyMeter e = energyMeterService.add(energyMeter);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idClient}")
                .buildAndExpand(e.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<EnergyMeterDto>> getAll( @RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "5") Integer size,
                                                        @RequestParam(defaultValue = "id") String sortField1,
                                                        @RequestParam(defaultValue = "serialNumber") String sortField2,
                                                        @And({
                                                                @Spec(path = "serialNumber", spec = LikeIgnoreCase.class),
                                                                @Spec(path = "id", spec = Equal.class)
                                                        }) Specification<EnergyMeter> meterSpecification) throws NoContentException {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<EnergyMeterDto> meters = energyMeterService.getAll(meterSpecification, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(meters.getTotalElements()))
                .header("X-Total-Pages", Long.toString(meters.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(meters.getContent());
    }

    @PutMapping("/{idEnergyMeter}")
    public ResponseEntity addBrandAndModelToEnergyMeter(@PathVariable Integer idEnergyMeter,
                                                                @RequestParam Integer idBrand,
                                                                @RequestParam Integer idModel ) throws EnergyMeterNotExists {
        energyMeterService.addBrandAndModelToEnergyMeter(idEnergyMeter,idBrand,idModel);
        return ResponseEntity.accepted().build();
    }
    @DeleteMapping("/{idEnergyMeter}")
    public ResponseEntity deleteEnergyMeterById(@PathVariable Integer idEnergyMeter ) throws EnergyMeterNotExists {
        energyMeterService.deleteEnergyMeterById(idEnergyMeter);
        return ResponseEntity.ok().build();
    }

//--------------------------- RESIDENCE --------------------------------------------

    @GetMapping("/{idEnergyMeter}/residence")
    public ResponseEntity<ResidenceDto> getResidenceByEnergyMeterId(@PathVariable Integer idEnergyMeter) throws EnergyMeterNotExists, ResidenceNotDefined {
        ResidenceDto residence = energyMeterService.getResidenceByEnergyMeterId(idEnergyMeter);
        return ResponseEntity.ok(residence);
    }
//--------------------------- BRAND --------------------------------------------

    @GetMapping("/brands")
    public ResponseEntity<List<MeterBrand>> getAllMeterBrands(@RequestParam(defaultValue = "0") Integer page,
                                                              @RequestParam(defaultValue = "5") Integer size,
                                                              @RequestParam(defaultValue = "id") String sortField1,
                                                              @RequestParam(defaultValue = "name") String sortField2,
                                                              @And({  @Spec(path = "id", spec = Equal.class),
                                                                      @Spec(path = "name", spec = Equal.class),
                                                              }) Specification<MeterBrand> meterBrandSpecification) throws Exception {

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<MeterBrand> meterBrands = energyMeterService.getAllMeterBrands(meterBrandSpecification, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(meterBrands.getTotalElements()))
                .header("X-Total-Pages", Long.toString(meterBrands.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(meterBrands.getContent());
    }

//--------------------------- MODEL --------------------------------------------
    @GetMapping("/models")
    public ResponseEntity<List<MeterModel>> getAllMeterModels(@RequestParam(defaultValue = "0") Integer page,
                                                              @RequestParam(defaultValue = "5") Integer size,
                                                              @RequestParam(defaultValue = "id") String sortField1,
                                                              @RequestParam(defaultValue = "name") String sortField2,
                                                              @And({ @Spec(path = "id", spec = Equal.class),
                                                                     @Spec(path = "name", spec = Equal.class),
                                                               }) Specification<MeterModel>meterModelSpecification) throws Exception {

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<MeterModel> meterModels = energyMeterService.getAllMeterModels(meterModelSpecification, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(meterModels.getTotalElements()))
                .header("X-Total-Pages", Long.toString(meterModels.getTotalPages()))
                .header("X-Actual-Page", Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(meterModels.getContent());
    }


}
