package com.utn.tpFinal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="tariffs")
public class Tariff {



    @Id
    @Size(min = 1, max = 2 ,message = "The name MUST be at least 2 character.")
    private String name;

    private Float amount;

    @OneToMany(mappedBy = "tariff")
    @JsonIgnore
    List<Residence> residencesList;

}
