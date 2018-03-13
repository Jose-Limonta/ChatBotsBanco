package com.bots.bots.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bots.bots.model.Transacciones;

@Repository("transaccionRepository")
public interface TransaccionesRepository extends CrudRepository<Transacciones, Integer>{

}
