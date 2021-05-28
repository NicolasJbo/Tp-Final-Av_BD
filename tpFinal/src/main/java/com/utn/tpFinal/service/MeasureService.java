package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.model.Tariff;
import com.utn.tpFinal.model.dto.ResidenceDto;
import com.utn.tpFinal.repository.MeasureRepository;
import com.utn.tpFinal.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

import static java.util.Objects.isNull;

@Service
public class MeasureService {
    @Autowired
    MeasureRepository measureRepository;
    @Autowired
    EnergyMeterService  energyMeterService;
    @Autowired
    ResidenceService residenceService;


    public void add(Measure measure, String serialNumber, String password) throws EnergyMeterNotExists, IncorrectPasswordException, ResidenceNotDefined {
        EnergyMeter energyMeter= energyMeterService.getEnergyMeterBySerialNumber(serialNumber);
        Residence residence=residenceService.getResidenceByEnergyMeterId(energyMeter.getId());

        if(!energyMeter.getPassWord().equalsIgnoreCase(password))
            throw new IncorrectPasswordException(this.getClass().getSimpleName(), "add");
        if(isNull(residence))
            throw new ResidenceNotDefined(this.getClass().getSimpleName(), "add");

        measure.setResidence(residence);

        Float tariffAmount = residence.getTariff().getAmount(); //traigo la tarifa
        System.out.println("RESIDENCE TARIFF -> "+tariffAmount);
        measure.setPrice(  measure.getKw()* tariffAmount); // hago que el precio de la medicion es de tarifa * total


        System.out.println("MEASURE UPDATED -> "+measure.toString());
        measureRepository.save(measure);


    }
}
