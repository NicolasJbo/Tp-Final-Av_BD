package com.utn.tpFinal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="clientes")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String dni;
    @Column(name = "nombre")
    String name;
    @Column(name = "apellido")
    String lastName;
    @Column(name = "fecha_nacimiento")
    Date birthday;
    @Column
    String mail;
    @Column(name = "contraseña")
    String password;

    //@OneToMany(fetch = FetchType.EAGER) //EAGER-> trae la lista ya modelada (con objetos) cuando traes el cliente

    @OneToMany(mappedBy = "client")
    private List<Residence> residencesList;


}
