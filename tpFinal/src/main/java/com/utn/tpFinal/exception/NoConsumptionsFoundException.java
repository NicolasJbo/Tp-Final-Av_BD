package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class NoConsumptionsFoundException extends CustomGenericException {

    public NoConsumptionsFoundException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NO_CONTENT;

    }

    @Override
    public String getMessage() {
        return "No consumptions found between the entered dates";
    }
}
