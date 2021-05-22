package com.utn.tpFinal.exception;


import org.springframework.http.HttpStatus;



public class IncorrectDatesException extends CustomGenericException {

    public IncorrectDatesException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.BAD_REQUEST;

    }

    @Override
    public String getMessage() {
        return "The entered dates are invalid.";
    }

}
