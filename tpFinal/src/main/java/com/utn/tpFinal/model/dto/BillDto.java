package com.utn.tpFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utn.tpFinal.model.Bill;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillDto {
    private Integer billCode;
    private Boolean isPaid;
    private Float totalEnergy;
    private Float finalAmount;
    private String initialDate;
    private Float initialMedition;
    private String finalDate;
    private Float finalMedition;
    private String expirates;
    private String residence;


    public static BillDto from (Bill bill){
        Residence residence = bill.getResidence();
        String address = residence.getStreet() + ' ' + residence.getNumber();

        if( (residence.getApartament() != null) && (residence.getFloor() != null) ) //le agrega el departamento si lo posee
            address = address+" - "+residence.getFloor()+residence.getApartament();

        return BillDto.builder()
            .billCode(bill.getId())
            .isPaid(bill.getIsPaid())
            .totalEnergy(bill.getTotalEnergy())
            .finalAmount(bill.getFinalAmount())
            .initialDate(bill.getInitialDate().toString().substring(0, 10))
            .initialMedition(bill.getInitialMedition())
            .finalDate(bill.getFinalDate().toString().substring(0, 10))
            .finalMedition(bill.getFinalMedition())
            .residence(address)
            .expirates(bill.getExpirationDate().toString().substring(0, 10))
            .build();

    }

    public static List<BillDto> from (List<Bill> billsList){
        List<BillDto> billDtoList = new ArrayList<BillDto>();
        for(Bill b : billsList)
            billDtoList.add(BillDto.from(b));

        return billDtoList;
    }

}
