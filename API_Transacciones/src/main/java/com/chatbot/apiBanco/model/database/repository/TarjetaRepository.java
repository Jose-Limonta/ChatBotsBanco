package com.chatbot.apiBanco.model.database.repository;

import com.chatbot.apiBanco.model.database.tables.TarjetaLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "TarjetaLog", path="tarjeta")
public interface TarjetaRepository extends PagingAndSortingRepository<TarjetaLog, Long> {

}