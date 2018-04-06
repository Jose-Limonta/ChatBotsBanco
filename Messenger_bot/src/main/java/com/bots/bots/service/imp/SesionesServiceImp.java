package com.bots.bots.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bots.bots.model.Sesiones;
import com.bots.bots.repository.SesionesRepository;
import com.bots.bots.service.SesionesService;

@Service("servicioSesiones")
public class SesionesServiceImp implements SesionesService{
	
	@Autowired
	@Qualifier("sesionesRepository")
	private SesionesRepository sesionesRepository;
	
	@Override
	public List<Sesiones> listAllSesiones() {
		return sesionesRepository.findAll();
	}

	@Override
	public Optional<Sesiones> getSesionById(String idsesion) {
		return sesionesRepository.findById(idsesion);
	}

	@Override
	public Sesiones saveSesion(Sesiones sesion) {
		return sesionesRepository.save(sesion);
	}

	@Override
	public void deleteSesiones(String idsesion) {
		Optional<Sesiones> sesion = getSesionById(idsesion);
		sesionesRepository.delete(sesion.get());
	}

}
