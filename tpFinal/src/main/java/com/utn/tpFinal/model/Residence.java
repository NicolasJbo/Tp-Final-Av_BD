package com.utn.tpFinal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="domicilios")
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

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;
/*
    @OneToOne
    @JoinColumn(name="medidor_id")
    private EnergyMeter energyMeter;



    @OneToOne
    @JoinColumn(name="tarifa_id")
    private Tariff tariff;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="facturas_id")
    private List<Bill> bill;

*/

}
