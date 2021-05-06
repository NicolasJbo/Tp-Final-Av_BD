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
@Table(name ="bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Boolean isPaid;
    private String initialMedition;
    private Date initialDate;
    private String finalMedition;
    private Date finalDate;
    private Float totalEnergy;
    private Float finalAmount;
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name="id_residence")
    private Residence residence;

    //----------------------------------------->> METODOS <<-----------------------------------------


}
