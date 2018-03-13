package com.bots.bots.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bots.bots.model.Tarjetas;

@Repository("tarjetaRepository")
public interface TarjetasRepository extends CrudRepository<Tarjetas, String>{

}
