package com.emersonluiz.DsClient.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emersonluiz.DsClient.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
