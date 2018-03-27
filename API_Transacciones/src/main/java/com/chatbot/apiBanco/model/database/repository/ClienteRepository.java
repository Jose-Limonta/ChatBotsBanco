package com.chatbot.apiBanco.model.database.repository;


import com.chatbot.apiBanco.model.database.tables.ClienteLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "clienteLog", path="cliente")
public interface ClienteRepository extends PagingAndSortingRepository<ClienteLog, Long>{
    List<ClienteLog> findByEmail(String email);
    List<ClienteLog> findByNombre(String nombre);
    ClienteLog findByToken(String token);
}
