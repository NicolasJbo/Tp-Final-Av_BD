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

    @Column(name = "numeroSerie")
    String serialNumber;

    /*@OneToOne
    @JoinColumn(name="domicilio_id")
    private Residence residence;

    @OneToOne
    @JoinColumn(name="modelo_id")
    private MeterModel meterModel;

    @OneToOne
    @JoinColumn(name="marca_id")
    private MeterBrand meterBrand;*/

}
