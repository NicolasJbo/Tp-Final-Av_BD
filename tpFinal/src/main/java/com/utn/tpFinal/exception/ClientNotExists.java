package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class ClientNotExists extends CustomGenericException  {

    public ClientNotExists(String route, String method) {
        this.method=method;
        this.route=route;
        this.httpStatus = HttpStatus.NOT_FOUND;

    }

    @Override
    public String getMessage() {
        return "Client not exists";
    }

}
