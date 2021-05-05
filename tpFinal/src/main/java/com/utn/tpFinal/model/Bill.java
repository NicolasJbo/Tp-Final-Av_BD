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

    @Column(name = "estaPagada")
    Boolean isPaid;
    @Column(name = "medicionInicial")
    String initialMedition;
    @Column(name = "fechaInicial")
    Date initialDate;
    @Column(name = "medicionFinal")
    String finalMedition;
    @Column(name = "fechaFinal")
    Date finalDate;
    @Column(name="consumoTotal")
    Float totalEnergy;
    @Column(name="totalPagar")
    Float finalAmount;
    @Column(name="fechaVencimiento")
    Date expirationDate;

    @OneToOne
    @JoinColumn(name="idDomicilio")
    private Residence residence;



}
