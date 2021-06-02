package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.model.dto.TariffDto;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.service.TariffService;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tariff")
public class TariffController {

    private TariffService tariffService;

    @Autowired
    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }



    @GetMapping
    public ResponseEntity<List<TariffDto>> getAll(@RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "5") Integer size,
                                                  @RequestParam(defaultValue = "id") String sortField1,
                                                  @RequestParam(defaultValue = "name") String sortField2,
                                                  @And({ @Spec(path = "name", spec = LikeIgnoreCase.class),
                                                         @Spec(path = "id", spec = Equal.class)
                                                  }) Specification<Tariff> tariffSpecification)  {
        List<Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField1));
        orders.add(new Sort.Order(Sort.Direction.ASC, sortField2));

        Page<TariffDto> tariffs = tariffService.getAll(tariffSpecification, page, size, orders);

        if(tariffs.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        else
            return ResponseEntity.status(HttpStatus.OK)
                    .header("X-Total-Elements", Long.toString(tariffs.getTotalElements()))
                    .header("X-Total-Pages", Long.toString(tariffs.getTotalPages()))
                    .header("X-Actual-Page",Integer.toString(page))
                    .header("X-First-Sort-By", sortField1)
                    .header("X-Second-Sort-By", sortField2)
                    .body(tariffs.getContent());
    }

    @GetMapping("/{idTariff}/residences")
    public ResponseEntity<List<ResidenceDto>> getResidencesByTariff(@PathVariable Integer idTariff,
                                                                    @RequestParam(defaultValue = "0") Integer page,
                                                                    @RequestParam(defaultValue = "5") Integer size,
                                                                    @RequestParam(defaultValue = "id") String sortField1,
                                                                    @RequestParam(defaultValue = "name") String sortField2) throws Exception {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.ASC, sortField1));
        orders.add(new Order(Sort.Direction.ASC, sortField2));

        Page<ResidenceDto> residences = tariffService.getResidencesByTariff(idTariff, page, size, orders);

        return ResponseEntity.status(HttpStatus.OK)
                .header("X-Total-Elements", Long.toString(residences.getTotalElements()))
                .header("X-Total-Pages", Long.toString(residences.getTotalPages()))
                .header("X-Actual-Page",Integer.toString(page))
                .header("X-First-Sort-By", sortField1)
                .header("X-Second-Sort-By", sortField2)
                .body(residences.getContent());
    }
//todo sacar
    /*
    @DeleteMapping("/{idTariff}")
    public ResponseEntity deleteTariffById(@PathVariable Integer idTariff) throws TariffNotExists{
         tariffService.deleteTariffById(idTariff);
         return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity modifyTariff(@RequestBody Tariff tariff) throws TariffNotExists {
        Tariff tar= tariffService.modifyTariff(tariff);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Class modify",tar.getClass().getSimpleName())
                .build();
    }
    @PostMapping
    public ResponseEntity addTariff(@RequestBody Tariff tariff){
        Tariff t = tariffService.add(tariff);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idClient}")
                .buildAndExpand(t.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

     */


}
