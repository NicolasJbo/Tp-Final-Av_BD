package com.utn.tpFinal.exception;

import lombok.Builder;

@Builder
public class IncorrectDatesException extends Exception{
    private String route;
    private  String method;

    public IncorrectDatesException(String route,String method) {
        this.route = route;
        this.method=method;
    }

    @Override
    public String getMessage() {
        return "The entered dates are invalid.";
    }

    public String getMethod() {
        return method;
    }

    public String getRoute() {
        return this.route;
    }
}
