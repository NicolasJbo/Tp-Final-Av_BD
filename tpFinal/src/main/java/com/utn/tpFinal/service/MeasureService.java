package com.utn.tpFinal.service;

import com.utn.tpFinal.exception.EnergyMeterNotExists;
import com.utn.tpFinal.exception.IncorrectPasswordException;
import com.utn.tpFinal.exception.NoContentException;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Measure;
import com.utn.tpFinal.repository.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasureService {
    @Autowired
    MeasureRepository measureRepository;
    @Autowired
    EnergyMeterService  energyMeterService;


    public void add(Measure measure, String serialNumber, String password) throws EnergyMeterNotExists, IncorrectPasswordException {
        EnergyMeter energyMeter= energyMeterService.getEnergyMeterBySerialNumber(serialNumber);
        if (energyMeter.getPassWord().equalsIgnoreCase(password))
            throw new IncorrectPasswordException(this.getClass().getSimpleName(), "add");

        measureRepository.save(measure);


    }
}
