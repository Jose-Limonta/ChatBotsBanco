package com.bots.bots.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bots.bots.model.Sesiones;
import com.bots.bots.service.SesionesService;

@RestController
@RequestMapping(path="/clnsession")
public class SessionController {
	
	@Autowired
	@Qualifier("servicioSesiones")
	private SesionesService servicioSesiones;
	
	@GetMapping(produces="application/json")
	private List<Sesiones> getSesions() {
		return servicioSesiones.listAllSesiones();
	}
	
	@GetMapping(produces="application/json", path="/{id}")
	private Sesiones getSession(@PathVariable("id") String id) {
		return servicioSesiones
				.getSesionById(id)
				.isPresent() ? servicioSesiones
						.getSesionById(id)
						.get() : new Sesiones();
	}
	
	@PostMapping(produces="application/json" )	
	public ResponseEntity<Sesiones> setAuthor(@RequestBody Sesiones sesion) {
		Sesiones sesionNueva = servicioSesiones.saveSesion(sesion);
		return new ResponseEntity<>(sesionNueva, HttpStatus.OK);
	}
	
	@PutMapping(produces="application/json" )	
	public ResponseEntity<Sesiones> setEditAuthor(@RequestBody Sesiones sesion) {
		Sesiones sesionNueva = servicioSesiones.saveSesion(sesion);
		return new ResponseEntity<>(sesionNueva, HttpStatus.OK);
	}

}