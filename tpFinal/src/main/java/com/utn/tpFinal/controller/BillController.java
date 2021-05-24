package com.utn.tpFinal.controller;

import com.utn.tpFinal.exception.IncorrectDatesException;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.proyection.Consumption;
import com.utn.tpFinal.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    BillService billService;



}
