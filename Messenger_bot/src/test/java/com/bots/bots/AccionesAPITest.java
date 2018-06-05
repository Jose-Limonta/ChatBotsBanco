package com.bots.bots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bots.bots.model.Sesiones;
import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Usuarios;
import com.bots.bots.resources.AccionesAPI;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AccionesAPITest {
	private Map<String,String> headers = new HashMap<>();
	private Map<String, Object> objheaders = new HashMap<>();
	Map<Object, Object> mapaUsuarios;
	Map<Object, Object> mapaTarjetas;
	Map<Object, Object> mapaSesiones;
	
	private AccionesAPI accionesApi = new AccionesAPI();
	
	public AccionesAPITest(){
		this.headers.put("Content-Type", "application/json");
		this.headers.put("Cache-Control", "no-cache");
		
		short valorInicial = 0;
		this.objheaders.put("fecha", new Date());
		this.objheaders.put("registro", new Short(valorInicial));
	}
	
	@Test
	public void getSesionesTest() throws UnirestException {
		Sesiones sesion = accionesApi.getSesiones("1809264795810706", objheaders);
		assertEquals("1809264795810706", sesion.getIdSesion());
	}
	
	@Test
	public void setAddSesionTest() throws UnirestException  {
		Sesiones sesion = new Sesiones("1234567890", new Date());
		mapaSesiones =  accionesApi.setAddSesion(sesion);
		assertFalse("En caso de que no inserte sesión: ", mapaSesiones.isEmpty());
	}
	
	@Test
	public void setEditSesionTest() throws  UnirestException  {
		Sesiones sesion = new Sesiones("1234567890", new Date());
		Map<Object, Object> mapa =  accionesApi.setEditSesion(sesion);
		assertFalse("En caso de que no edite sesión: ", mapa.isEmpty());
	}
	
	@Test
	public void setTarjetaTest() throws UnirestException {
		Tarjetas tarjeta = new Tarjetas("28934789374229", new Date(), "Banamex","VISA");
		mapaTarjetas =  accionesApi.setTarjeta(tarjeta);
		assertTrue("En caso de que no inserte tarjeta: ", mapaTarjetas.isEmpty());
	}
	
	@Test
	public void setUsuariosTest() throws UnirestException  {
		Usuarios user = new Usuarios("38475834788", new Date(), "34938479394858");
		mapaUsuarios =  accionesApi.setUsuarios(user);
		assertFalse("En caso de que no inserte usuario: ", mapaUsuarios.isEmpty());
	}
	
	@Test
	public void convertMapToUsuariosNullTest() {
		Usuarios usuario = getUsuarios(new HashMap<>());
		assertNull(usuario.getIduser());
	}
	
	@Test
	public void convertMapToSesionesNullTest() {
		Sesiones sesion = getSesion(new HashMap<>());
		assertNull(sesion.getIdSesion());
	}
	
	private Sesiones getSesion(Map<Object,Object> mapa) {
		return accionesApi.convertMapToSesiones(mapa, this.objheaders);
	}
	
	private Usuarios getUsuarios(Map<Object,Object> mapa) {
		return accionesApi.convertMapToUsuarios(mapa);
	}
}
