package com.chatbot.apiBanco.model.database.repository;

import com.chatbot.apiBanco.model.database.tables.Authuser;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "authuser")
public interface AuthuserRepository extends PagingAndSortingRepository<Authuser, Long> {
    Authuser findByusername(String username);
}
