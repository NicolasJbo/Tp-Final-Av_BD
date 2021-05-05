package com.utn.tpFinal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="idCliente")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Residence residence = (Residence) o;
        return Objects.equals(id, residence.id) && Objects.equals(street, residence.street) && Objects.equals(number, residence.number) && Objects.equals(floor, residence.floor) && Objects.equals(apartament, residence.apartament);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, number, floor, apartament);
    }
}
