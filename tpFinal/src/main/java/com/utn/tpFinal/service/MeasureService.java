package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.*;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.model.Residence;
import com.utn.tpFinal.repository.MeasureRepository;
import com.utn.tpFinal.repository.ResidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class MeasureService {
    @Autowired
    MeasureRepository measureRepository;
    @Autowired
    EnergyMeterService  energyMeterService;
    @Autowired
    ResidenceRepository residenceRepository;


    public void add(Measure measure, String serialNumber, String password) throws EnergyMeterNotExists, IncorrectPasswordException, ResidenceNotDefined {
        EnergyMeter energyMeter= energyMeterService.getEnergyMeterBySerialNumber(serialNumber);
        Residence residence=residenceRepository.findByEnergyMeterId(energyMeter.getId());
        if(!energyMeter.getPassWord().equalsIgnoreCase(password))
            throw new IncorrectPasswordException(this.getClass().getSimpleName(), "add");
        if(isNull(residence))
            throw new ResidenceNotDefined(this.getClass().getSimpleName(), "add");

        measure.setResidence(residence);
        measureRepository.save(measure);


    }
}
