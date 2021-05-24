package com.utn.tpFinal.model.dto;

import com.utn.tpFinal.model.EnergyMeter;
import com.utn.tpFinal.model.MeterBrand;
import com.utn.tpFinal.model.MeterModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.MappingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnergyMeterDto {
    private String serialNumber;
    private String brandName;
    private String modelName;


    public static EnergyMeterDto from (EnergyMeter energyMeter){
           return EnergyMeterDto.builder()
                .serialNumber(energyMeter.getSerialNumber())
                .brandName(energyMeter.getBrand().getName())
                .modelName(energyMeter.getModel().getName())
                .build();
    }

    public static List<EnergyMeterDto> from (List<EnergyMeter> listEnergyMeter){
        List<EnergyMeterDto> listDto = new ArrayList<EnergyMeterDto>();

        for(EnergyMeter e : listEnergyMeter)
            listDto.add(EnergyMeterDto.from(e));

        return listDto;
    }


}
