package com.bots.bots.service;

import java.util.List;
import java.util.Optional;

import com.bots.bots.model.Transacciones;

public interface TransaccionesService {
	public abstract List<Transacciones> listAllTransacciones();
	public abstract Optional<Transacciones> getTransaccionById(Integer id);
	public abstract Transacciones saveTransaccion(Transacciones transaccion);
	public abstract void deleteTransaccione(Integer id);
}
