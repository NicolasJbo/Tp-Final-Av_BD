package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>
{
    Page<Client> findAll(Pageable pageable);
    Page<Client> findByName(String name, Pageable pageable);



}
