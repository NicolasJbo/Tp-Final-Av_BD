package com.utn.tpFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utn.tpFinal.model.Measure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeasureDto {


    String serialNumber;
    float kw;
    String date;
    String password;
    float price;
    //static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    //final LocalDate date = LocalDate.parse("29-Mar-2019", formatter);

    static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   // LocalDateTime dateTime = LocalDateTime.parse(str, formatter2);

    //todo se podria cortar el string y armar la fecha por dime mes año minuto y segundo

    /*public static Measure from(MeasureDto measureDto){
        //LocalDate date = LocalDate.parse(measureDto.getDate(), formatter);
        LocalDateTime dateTime = LocalDateTime.parse(measureDto.getDate(), formatter2);

        return Measure.builder()
                .total(measureDto.getValue())
                .date(dateTime)
                .build();
    }*/

    public static MeasureDto from (Measure measure){
        return MeasureDto.builder()
                .date(measure.getDate().toString())
                .kw(measure.getKw())
                .price(measure.getPrice())
                .build();
    }

}
