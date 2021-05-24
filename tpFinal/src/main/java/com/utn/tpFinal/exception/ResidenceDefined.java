package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class ResidenceDefined extends CustomGenericException {
    public ResidenceDefined(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.CONFLICT;

    }

    @Override
    public String getMessage() {
        return "The energy meter has already an defined residence.";
    }
}
