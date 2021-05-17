package com.utn.tpFinal.repository;

import com.utn.tpFinal.model.Client;
import com.utn.tpFinal.model.proyection.Top10Clients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>
{
    Page<Client> findAll(Pageable pageable);
    Page<Client> findByName(String name, Pageable pageable);

    @Query(value = "CALL getTop10Clients(:from,:to)", nativeQuery = true)
   List<Top10Clients> getTop10Clients(Date from, Date to);


}
