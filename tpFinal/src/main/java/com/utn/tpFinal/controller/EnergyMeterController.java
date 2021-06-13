package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
    //todo TEST
    @PostMapping
    public ResponseEntity addEnergyMeter (@RequestBody EnergyMeter energyMeter,@RequestParam Integer idModel,@RequestParam Integer idBrand) throws MeterModelNotExist, MeterBrandNotExist {
        EnergyMeter e = energyMeterService.add(energyMeter,idModel,idBrand);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/")
                .query("id={idEnergyMeter}")
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
                                                        }) Specification<EnergyMeter> meterSpecification)  {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<EnergyMeterDto> meters = energyMeterService.getAll(meterSpecification, page, size, orders);
        if(meters.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
             return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(meters.getTotalElements()))
                .header("X-Total-Pages", Long.toString(meters.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(meters.getContent());
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
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
    //todo estos metodos CREO QUE ESTAN DEMAS!


    @GetMapping("/brands")
    public ResponseEntity<List<MeterBrand>> getAllMeterBrands(@RequestParam(defaultValue = "0") Integer page,
                                                              @RequestParam(defaultValue = "5") Integer size,
                                                              @RequestParam(defaultValue = "id") String sortField1,
                                                              @RequestParam(defaultValue = "name") String sortField2,
                                                              @And({  @Spec(path = "id", spec = Equal.class),
                                                                      @Spec(path = "name", spec = LikeIgnoreCase.class),
                                                              }) Specification<MeterBrand> meterBrandSpecification) throws Exception {

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));


        Page<MeterBrand> meterBrands = energyMeterService.getAllMeterBrands(meterBrandSpecification, page, size, orders);
        if(meterBrands.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
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
                                                                     @Spec(path = "name", spec = LikeIgnoreCase.class),
                                                               }) Specification<MeterModel>meterModelSpecification) throws Exception {

        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<MeterModel> meterModels = energyMeterService.getAllMeterModels(meterModelSpecification, page, size, orders);
        if (meterModels.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.OK)
                    .header("X-Total-Elements", Long.toString(meterModels.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(meterModels.getTotalPages()))
                    .header("X-Actual-Page", Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(meterModels.getContent());
    }


}
