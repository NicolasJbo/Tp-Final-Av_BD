package com.utn.tpFinal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.tpFinal.model.dto.RegisterDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(unique = true)
    @Email(message = "INVALID type of Email.")
    private String mail;

    @Size(min = 3,message = "Password characters MIN= 3 .")
    private String password;

    @Column(columnDefinition = "boolean default true")
    private Boolean isClient;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Client client;



}
