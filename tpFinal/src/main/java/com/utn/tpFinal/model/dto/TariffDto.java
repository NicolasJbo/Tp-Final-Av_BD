package com.utn.tpFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utn.tpFinal.model.Tariff;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TariffDto {
    private String tariff;
    private Float amount;


    public static TariffDto from (Tariff tariff){

        return TariffDto.builder()
                .tariff(tariff.getName())
                .amount(tariff.getAmount())
                .build();

    }

    public static List<TariffDto> from (List<Tariff> tariffs){
        List<TariffDto> tariffDtos = new ArrayList<TariffDto>();
        for(Tariff t : tariffs)
            tariffDtos.add(TariffDto.from(t));

        return tariffDtos;
    }
}
