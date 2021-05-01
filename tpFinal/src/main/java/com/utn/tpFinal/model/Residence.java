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
@Table(name ="domicilio")
public class Residence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "calle")
    String street;
    @Column(name = "altura")
    Integer number;
    @Column(name = "piso")
    String floor;
    @Column(name = "departamento")
    String apartament;

    //Medidior 1t1
    //cliente 1t1
    //tarifa 1t1
    //Factura 1 t Many


}
