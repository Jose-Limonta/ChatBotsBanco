package com.bots.bots.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bots.bots.model.Usuarios;
import com.bots.bots.repository.UsuariosRepository;
import com.bots.bots.service.UsuariosService;

@Service("servicioUsuarios")
public class UsuariosServiceImp implements UsuariosService{
	
	@Autowired
	@Qualifier("usuarioRepository")
	private UsuariosRepository usuarioRepository;

	@Override
	public List<Usuarios> listAllUsuarios() {
		return (List<Usuarios>) usuarioRepository.findAll();
	}

	@Override
	public Optional<Usuarios> getUsuarioById(String iduser) {
		return usuarioRepository.findById(iduser);
	}

	@Override
	public Usuarios saveUsuario(Usuarios usuarios) {
		return usuarioRepository.save(usuarios);
	}

	@Override
	public void deleteUsuario(String iduser) {
		Optional<Usuarios> usuario = getUsuarioById(iduser);
		if( usuario.isPresent() ) {
			usuarioRepository.delete(usuario.get());
		}
	}

}
