package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class MeterModelNotExist extends CustomGenericException {
    public MeterModelNotExist(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;

    }

    @Override
    public String getMessage() {
        return "Meter model do not exist";
    }
}
