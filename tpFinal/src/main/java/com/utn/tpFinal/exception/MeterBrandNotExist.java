package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class MeterBrandNotExist extends CustomGenericException {
    public MeterBrandNotExist(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;

    }

    @Override
    public String getMessage() {
        return "Meter Brand do not exist";
    }
}
