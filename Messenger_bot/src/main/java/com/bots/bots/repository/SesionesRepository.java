package com.bots.bots.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bots.bots.model.Sesiones;

@Repository("sesionesRepository")
public interface SesionesRepository extends JpaRepository<Sesiones, String> {

}
