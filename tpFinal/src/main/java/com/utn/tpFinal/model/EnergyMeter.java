package com.utn.tpFinal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

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

    @Column(unique = true)
    @NotEmpty(message = "Serial Number MUST be completed.")
    private String serialNumber;

    @ManyToOne
    @NotEmpty(message = "Model MUST be completed.")
    @JoinColumn(name="id_model")
    private MeterModel model;

    @ManyToOne
    @NotEmpty(message = "Brand MUST be completed.")
    @JoinColumn(name="id_brand")
    private MeterBrand brand;

    @OneToOne(mappedBy = "energyMeter")
    @JsonIgnore
    private Residence residence;

    @Override
     public String toString(){
        return "SerialNumber : "+this.serialNumber+
                "Modelo: "+this.model+
                "Marca: "+this.brand;
    }
    //----------------------------------------->> METODOS <<-----------------------------------------

}
