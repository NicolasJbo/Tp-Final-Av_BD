package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.Residence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResidenceRepository extends JpaRepository<Residence, Integer> {

    List<Residence> findByStreet(String street);

    //@Query(value = "select * from domicilios where idCliente = :idClient", nativeQuery = true)
    List<Residence> findByClient(Client client);
}
