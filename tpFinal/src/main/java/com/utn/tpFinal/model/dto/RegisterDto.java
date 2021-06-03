package com.utn.tpFinal.model.dto;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {


    //client
    private String dni;
    private String name;
    private String lastName;
    private Date birthday;
    //user
    private String mail;
    private String password;










}
