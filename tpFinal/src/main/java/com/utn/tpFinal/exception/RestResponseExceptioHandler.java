package com.utn.tpFinal.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class RestResponseExceptioHandler extends ResponseEntityExceptionHandler {

    //Validoaciones
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handlerContraintViolation(ConstraintViolationException ex , WebRequest request){
        List<String> errors = new ArrayList<>();

        for (ConstraintViolation violation : ex.getConstraintViolations()){
            errors.add( "Route:  "+violation.getRootBeanClass()+"   || Message:  "+violation.getMessage()  );

        }
        ApiError apiError= new ApiError(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage() ,errors);

        return new ResponseEntity<Object>(apiError ,new HttpHeaders(),apiError.getHttpStatus());

    }
    @ExceptionHandler({ExceptionDiferentId.class})
    public ResponseEntity<Object> handlerContraintViolation(ExceptionDiferentId ex , WebRequest request){
        List<String> errors = new ArrayList<>();
        errors.add("Route: "+ ex.getRoute());
        errors.add("Method: "+ ex.getMethod());

        ApiError apiError= new ApiError(HttpStatus.BAD_REQUEST,ex.getLocalizedMessage() ,errors);

        return new ResponseEntity<Object>(apiError ,new HttpHeaders(),apiError.getHttpStatus());

    }


}
