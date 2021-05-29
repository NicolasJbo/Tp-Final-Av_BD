package com.utn.tpFinal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utn.tpFinal.model.dto.RegisterDto;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name ="clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    @NotEmpty(message = "The client MUST have dni.")
    private String dni;

    @NotEmpty(message = "Name MUST be completed.")
    private String name;

    @NotEmpty(message ="LastName MUST be completed.")
    private String lastName;

    @NotNull(message = "Birthday MUST be completed.")
    @Past(message = "INVALID Date.")
    private Date birthday;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_user")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL) //si eliminas un cliente, se eliminan sus domicilios
    private List<Residence> residencesList;



    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                '}';
    }
//----------------------------------------->> METODOS <<-----------------------------------------

    public static Client from(RegisterDto registerDto){

        return Client.builder()
                .dni(registerDto.getDni())
                .birthday(registerDto.getBirthday())
                .name(registerDto.getName())
                .lastName(registerDto.getLastName())
                .build();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) && Objects.equals(dni, client.dni) && Objects.equals(name, client.name) && Objects.equals(lastName, client.lastName) && Objects.equals(birthday, client.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dni, name, lastName, birthday);
    }

}
