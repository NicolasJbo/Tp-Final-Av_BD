package com.utn.tpFinal.model.dto;

import com.utn.tpFinal.model.EnergyMeter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;


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

    public static List<EnergyMeterDto> from (List<EnergyMeter> listenergyMeter){
        List<EnergyMeterDto> listDto = new ArrayList<EnergyMeterDto>();

        for(EnergyMeter e : listenergyMeter)
            listDto.add(EnergyMeterDto.from(e));

        return listDto;
    }

}
