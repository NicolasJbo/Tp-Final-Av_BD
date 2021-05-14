package com.utn.tpFinal.exception;

public class ExceptionDiferentId extends Exception {
    private String route;

    public ExceptionDiferentId(String route) {
        this.route = route;
    }

    @Override
    public String getMessage() {
        return "The Id Is Different.";
    }

    public String getRoute() {
        return this.route;
    }
}
