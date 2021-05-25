package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class IncorrectPasswordException extends CustomGenericException {
    public IncorrectPasswordException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.UNAUTHORIZED;

    }

    @Override
    public String getMessage() {
        return "Incorrect PASSWORD";
    }
}
