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
@Table(name ="energy_meters")
public class EnergyMeter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String serialNumber;

    @ManyToOne
    @JoinColumn(name="id_model")
    private MeterModel model;

    @ManyToOne
    @JoinColumn(name="id_brand")
    private MeterBrand brand;

    @OneToOne(mappedBy = "energyMeter")
    private Residence residence;

    @Override
     public String toString(){
        return "SerialNumber : "+this.serialNumber+
                "Modelo: "+this.model+
                "Marca: "+this.brand;
    }
    //----------------------------------------->> METODOS <<-----------------------------------------

}
