package com.bots.bots.service;

import java.util.List;
import java.util.Optional;

import com.bots.bots.model.Sesiones;

public interface SesionesService {
	public abstract List<Sesiones> listAllSesiones();
	public abstract Optional<Sesiones> getSesionById(String idsesion);
	public abstract Sesiones saveSesion(Sesiones sesion);
	public abstract void deleteSesiones(String idsesion);
}
