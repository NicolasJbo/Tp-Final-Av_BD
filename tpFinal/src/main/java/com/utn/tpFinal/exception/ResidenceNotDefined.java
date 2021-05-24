package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class ResidenceNotDefined extends CustomGenericException{

    public ResidenceNotDefined(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;

    }

    @Override
    public String getMessage() {
        return "The residence is not defined for this object";
    }
}
