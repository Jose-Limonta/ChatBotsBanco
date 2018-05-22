package com.bots.bots.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Transacciones;
import com.bots.bots.model.Usuarios;
import com.bots.bots.service.TarjetasService;
import com.bots.bots.service.TransaccionesService;
import com.bots.bots.service.UsuariosService;

@RestController
@RequestMapping(path="/tempSource")
public class TemporalController {
	
	// Inicia Tarjetas
	
	@Autowired
	@Qualifier("servicioTarjetas")
	private TarjetasService tarjetasService;
	
	@GetMapping(produces="application/json", path="/tarjetas")
	private List<Tarjetas> getTarjetas() {
		return tarjetasService.listAllTarjetas();
	}
	
	@GetMapping(produces="application/json", path="/tarjetas/{id}")
	private Tarjetas getTarjeta(@PathVariable("id") String id) {
		return tarjetasService.getTarjetaById(id)
				.isPresent() ? tarjetasService
						.getTarjetaById(id)
						.get() : new Tarjetas();
	}
	
	@PostMapping(produces="application/json", path="/tarjetas" )
	public Tarjetas setTarjeta(@RequestBody Tarjetas tarjeta) {
		return tarjetasService.saveTarjeta(tarjeta);
	}
	
	@PutMapping(produces="application/json", path="/tarjetas" )
	public Tarjetas editTarjeta(@RequestBody Tarjetas tarjeta) {
		return tarjetasService.saveTarjeta(tarjeta);
	}
	
	// Terminan Tarjetas
	// Inicia Transacciones
	@Autowired
	@Qualifier("servicioTransacciones")
	private TransaccionesService transaccionesService;
	
	@GetMapping(produces="application/json", path="/transacciones")
	private List<Transacciones> getTransacciones() {
		return transaccionesService.listAllTransacciones();
	}
	
	@GetMapping(produces="application/json", path="/transacciones/{id}")
	private Transacciones getTransaccion(@PathVariable("id") Integer id) {
		return transaccionesService.getTransaccionById(id)
				.isPresent() ? transaccionesService
						.getTransaccionById(id)
						.get() : new Transacciones();
	}
	
	@PostMapping(produces="application/json", path="/transacciones" )
	public Transacciones setTransaccion(@RequestBody Transacciones transaccion) {
		return transaccionesService.saveTransaccion(transaccion);
	}
	
	@PutMapping(produces="application/json", path="/transacciones" )
	public Transacciones editTransaccion(@RequestBody Transacciones transaccion) {
		return transaccionesService.saveTransaccion(transaccion);
	}
	
	// Terminan Transacciones
	// Inicia Usuarios
	@Autowired
	@Qualifier("servicioUsuarios")
	private UsuariosService usuariosServicio;
	
	@GetMapping(produces="application/json", path="/usuarios")
	private List<Usuarios> getUsuarios() {
		return usuariosServicio.listAllUsuarios();
	}
	
	@GetMapping(produces="application/json", path="/usuarios/{id}")
	private Usuarios getUsuario(@PathVariable("id") String id) {
		return usuariosServicio.getUsuarioById(id)
				.isPresent() ? usuariosServicio
						.getUsuarioById(id)
						.get() : new Usuarios();
	}
	
	@PostMapping(produces="application/json", path="/usuarios" )
	public Usuarios setUsuario(@RequestBody Usuarios usuario) {
		return usuariosServicio.saveUsuario(usuario);
	}
	
	@PutMapping(produces="application/json", path="/usuarios" )
	public Usuarios editUsuario(@RequestBody Usuarios usuario) {
		return usuariosServicio.saveUsuario(usuario);
	}
	
	// Terminan Usuarios
	

}
