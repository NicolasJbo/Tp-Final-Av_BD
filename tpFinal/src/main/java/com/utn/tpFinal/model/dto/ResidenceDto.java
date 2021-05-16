package com.utn.tpFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.Residence;
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
public class ResidenceDto {
    private String street;
    private Integer number;
    private String floor;
    private String apartament;
    private String client;
    private EnergyMeterDto energyMeter;


    public static ResidenceDto from(Residence residence){
        return ResidenceDto.builder()
                .street(residence.getStreet())
                .number(residence.getNumber())
                .apartament(residence.getApartament())
                .floor(residence.getFloor())
                .client(residence.getClient().getLastName()+' '+residence.getClient().getName())
                .energyMeter(EnergyMeterDto.from(residence.getEnergyMeter()))
                .build();
    }

    public static List<ResidenceDto> from (List<Residence> residencesList){
        List<ResidenceDto> listDto = new ArrayList<ResidenceDto>();

        for(Residence r : residencesList)
            listDto.add(ResidenceDto.from(r));

        return listDto;
    }

    public static ResidenceDto fromWithOutClient(Residence residence){
        return ResidenceDto.builder()
                .street(residence.getStreet())
                .number(residence.getNumber())
                .apartament(residence.getApartament())
                .floor(residence.getFloor())
                .energyMeter(EnergyMeterDto.from(residence.getEnergyMeter()))
                .build();
    }

    public static List<ResidenceDto> fromWithOutClient (List<Residence> residencesList){
        List<ResidenceDto> listDto = new ArrayList<ResidenceDto>();

        for(Residence r : residencesList)
            listDto.add(ResidenceDto.fromWithOutClient(r));

        return listDto;
    }
}
