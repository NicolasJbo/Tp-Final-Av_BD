package com.utn.tpFinal.model.dto;

import com.utn.tpFinal.model.Measure;

import java.text.SimpleDateFormat;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasureSenderDto {
    String serialNumber;
    float kw;
    String date;
    String password;

}
