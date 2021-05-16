package com.utn.tpFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {
    private String client;
    private String dni;
    private String birthday;
    private List<ResidenceDto> residencesList;


    static SimpleDateFormat objSDF = new SimpleDateFormat("dd-MMM-aaaa"); // La cadena de formato de fecha se pasa como un argumento al objeto

    public static ClientDto from (Client client){
        return ClientDto.builder()
                .client(client.getLastName().toUpperCase(Locale.ROOT)+' '+client.getName().toUpperCase())
                .dni(client.getDni())
                .birthday(client.getBirthday().toString().substring(0,10)) //saca los bit de hora-min-seg
                .residencesList(ResidenceDto.fromWithOutClient(client.getResidencesList()))
                .build();
    }

    public static ClientDto fromWithOutResidences (Client client){
        return ClientDto.builder()
                .client(client.getLastName().toUpperCase(Locale.ROOT)+' '+client.getName().toUpperCase())
                .dni(client.getDni())
                .birthday(client.getBirthday().toString().substring(0,10)) //saca los bit de hora-min-seg
                .build();
    }

}
