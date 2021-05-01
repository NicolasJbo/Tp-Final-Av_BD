package com.utn.tpFinal.model;

import lombok.*;

import javax.persistence.*;

import java.util.Date;

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

    //domicilio Manyt1


}
