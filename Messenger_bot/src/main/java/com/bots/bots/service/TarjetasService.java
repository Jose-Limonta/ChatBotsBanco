package com.bots.bots.service;

import java.util.List;
import java.util.Optional;

import com.bots.bots.model.Tarjetas;

public interface TarjetasService {
	public abstract List<Tarjetas> listAllTarjetas();
	public abstract Optional<Tarjetas> getTarjetaById(String idtarjeta);
	public abstract Tarjetas saveTarjeta(Tarjetas tarjeta);
	public abstract void deleteTarjetas(String idtarjeta);
}
