package com.utn.tpFinal.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="measures")
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Date MUST be completed.")
    @Past(message = "INVALID Date.")
    private Date date;

    @NotNull(message = "Total MUST be completed.")
    @Positive(message = "Total Must be positive number")
    private Float total;

    @OneToOne
    @JoinColumn(name="id_residence")
    private Residence residence;

    //Factura 1tMany NULL NO FACTURADO


}
