package com.utn.tpFinal.model.proyection;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface MeasureProyection {

    Date getMeasureDate();
    Float getMeasureTotal();
    @Value("#{target.residenceStreet + ' ' + target.residenceNumber}")
    String getResidence();

}
