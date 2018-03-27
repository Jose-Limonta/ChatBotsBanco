package com.chatbot.apiBanco.model.database.repository;

import com.chatbot.apiBanco.model.database.tables.TransactionLOGS;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "TransactionLOGS", path = "transaccion")
public interface TransactionRepository extends PagingAndSortingRepository<TransactionLOGS, Long> {
}
