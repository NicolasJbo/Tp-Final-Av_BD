package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class TariffsDoNotMatch extends CustomGenericException {


    public TariffsDoNotMatch(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.CONFLICT;
    }



    @Override
    public String getMessage() {
        return "Tariff DO NOT Different.";
    }

}
