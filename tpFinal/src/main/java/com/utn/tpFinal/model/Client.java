package com.utn.tpFinal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    @Column(name = "fechaNacimiento")
    Date birthday;
    @Column
    String mail;
    @Column(name = "contraseña")
    String password;

    //@OneToMany(fetch = FetchType.EAGER) //EAGER-> trae la lista ya modelada (con objetos) cuando traes el cliente

    @OneToMany(mappedBy = "client")
    private List<Residence> residencesList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(dni, client.dni) && Objects.equals(name, client.name) && Objects.equals(lastName, client.lastName) && Objects.equals(birthday, client.birthday) && Objects.equals(mail, client.mail) && Objects.equals(password, client.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dni, name, lastName, birthday, mail, password);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
