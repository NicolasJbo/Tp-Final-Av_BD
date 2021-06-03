package com.utn.tpFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utn.tpFinal.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String id;
    private String mail;
    private Boolean isClient;

    public static UserDto from(User user){
        return UserDto.builder().id(String.valueOf(user.getId()))
                .isClient(user.getIsClient())
                .mail(user.getMail())
                .build();
    }
}
