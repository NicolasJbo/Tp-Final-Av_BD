package com.utn.tpFinal.exception;

import org.springframework.http.HttpStatus;

public class ResidencesDoNotMatch extends CustomGenericException {
    public ResidencesDoNotMatch(String route, String method) {

            this.method=method;
            this.route=route;
            this.httpStatus =HttpStatus.NOT_FOUND;
            }



        @Override
        public String getMessage() {
            return "Residence are Different.";
        }
}

