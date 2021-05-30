package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.IncorrectPasswordException;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.dto.MeasureDto;
import com.utn.tpFinal.model.dto.MeasureSenderDto;
import com.utn.tpFinal.service.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/measurements")
public class MeasureController {

    @Autowired
    MeasureService measureService;

    @PostMapping
    public void  addMeasure(@RequestBody MeasureSenderDto dto) throws Exception {
        System.out.println("INCOMING DTO -> "+dto.toString());

        Measure measure= Measure.from(dto);
        System.out.println("MEASURE CREATED -> "+measure.toString());

        measureService.add(measure,dto.getSerialNumber(),dto.getPassword());

    }

}
