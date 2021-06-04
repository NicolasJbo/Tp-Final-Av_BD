package com.utn.tpFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.utn.tpFinal.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.In;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Integer id;
    private String mail;
    private Boolean isClient;

    public static UserDto from(User user){
        return UserDto.builder().id(user.getId())
                .isClient(user.getIsClient())
                .mail(user.getMail())
                .build();
    }
}
