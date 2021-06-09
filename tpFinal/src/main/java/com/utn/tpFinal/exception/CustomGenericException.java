package com.utn.tpFinal.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
public class CustomGenericException extends Exception {
     String route;
     String method;
     HttpStatus httpStatus;
}
