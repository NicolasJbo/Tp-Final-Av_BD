package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class EnergyMeterNotExists extends CustomGenericException {
    public EnergyMeterNotExists(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;

    }

    @Override
    public String getMessage() {
        return "EnergyMeter not exists";
    }

}
