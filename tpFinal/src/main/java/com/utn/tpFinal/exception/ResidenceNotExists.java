package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class ResidenceNotExists extends CustomGenericException {
    public ResidenceNotExists(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;

    }

    @Override
    public String getMessage() {
        return "Residence not exists.";
    }

}
