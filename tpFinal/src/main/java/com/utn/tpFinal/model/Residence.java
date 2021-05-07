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
@Table(name ="residences")
public class Residence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String street;
    private Integer number;
    private String floor;
    private String apartament;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="id_client")
    private Client client;

    @OneToMany(mappedBy = "residence")
    private List<Bill> bill;

    @OneToOne
    @JoinColumn(name="id_energyMeter")
    private EnergyMeter energyMeter;

    @ManyToOne
    @JoinColumn(name = "id_tariff")
    private Tariff tariff;




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
