package com.utn.tpFinal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="facturas")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "esta_pagada")
    Boolean isPaid;
    @Column(name = "medicion_inicial")
    String initialMedition;
    @Column(name = "fecha_inicial")
    Date initialDate;
    @Column(name = "medicion_final")
    String finalMedition;
    @Column(name = "fecha_final")
    Date finalDate;
    @Column(name="consumo_total")
    Float totalEnergy;
    @Column(name="total_pagar")
    Float finalAmount;
    @Column(name="fecha_vencimiento")
    Date expirationDate;

    @OneToOne
    @JoinColumn(name="domicilio_id")
    private Residence residence;



}
