package com.utn.tpFinal.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="medidores")
public class EnergyMeter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "numero_serie")
    String serialNumber;


    //domicilio 1t1
    //MOdelo 1t1
    //marca 1t1
}
