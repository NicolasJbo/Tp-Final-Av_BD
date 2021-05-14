package com.utn.tpFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDto {
    private String fullName;
    private String dni;
    private Date birthday;
    private List<Residence> residencesList;

    public static ClientDto from (Client client){
        return ClientDto.builder()
                .fullName(client.getLastName()+' '+client.getName())
                .dni(client.getDni())
                .birthday(client.getBirthday())
                .residencesList(client.getResidencesList())
                .build();
    }

}
