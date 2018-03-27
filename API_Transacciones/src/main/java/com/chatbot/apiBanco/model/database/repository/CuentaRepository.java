package com.chatbot.apiBanco.model.database.repository;

import com.chatbot.apiBanco.model.database.tables.CuentaLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "cuentaLog", path="cuenta")
public interface CuentaRepository extends PagingAndSortingRepository<CuentaLog, Long> {

}