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
@Table(name ="mediciones")
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "fecha_consumo")
    Date date;

    @Column(name = "consumo")
    Float total;

    /*@OneToOne
    @JoinColumn(name="domicilio_id")
    private Residence residence;*/

    //Factura 1tMany NULL NO FACTURADO


}
