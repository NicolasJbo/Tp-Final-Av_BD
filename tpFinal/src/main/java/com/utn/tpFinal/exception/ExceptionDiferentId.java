package com.utn.tpFinal.exception;

import lombok.Builder;

@Builder
public class ExceptionDiferentId extends Exception {

    private String route;
    private  String method;

    public ExceptionDiferentId(String route,String method) {
        this.route = route;
        this.method=method;
    }

    @Override
    public String getMessage() {
        return "The Id are Different.";
    }

    public String getMethod() {
        return method;
    }

    public String getRoute() {
        return this.route;
    }
}
