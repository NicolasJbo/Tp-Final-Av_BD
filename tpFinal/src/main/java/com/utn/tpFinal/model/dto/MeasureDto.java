package com.utn.tpFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utn.tpFinal.model.Measure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


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


    //todo se podria cortar el string y armar la fecha por dime mes año minuto y segundo


    public static MeasureDto from (Measure measure){
        //DateFormat hourdateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return MeasureDto.builder()
                .date(measure.getDate().toString())
                .kw(measure.getKw())
                .price(measure.getPrice())
                .build();
    }


}
