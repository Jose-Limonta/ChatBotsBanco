package com.bots.bots.service;

import java.util.List;
import java.util.Optional;

import com.bots.bots.model.Usuarios;

public interface UsuariosService {
	public abstract List<Usuarios> listAllUsuarios();
	public abstract Optional<Usuarios> getUsuarioById(String iduser);
	public abstract Usuarios saveUsuario(Usuarios usuarios);
	public abstract void deleteUsuario(String iduser);
}
