package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class NoContentException extends CustomGenericException {
    public NoContentException(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NO_CONTENT;

    }

    @Override
    public String getMessage() {
        return "The list is EMPTY";
    }
}
