package com.bots.bots;

import java.util.Date;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Usuarios;
import com.bots.bots.resources.AccionesMensajes;
import com.bots.bots.resources.Resources;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AccionesMensajesTest extends Assert{
	
	private AccionesMensajes accionesMensaje = new AccionesMensajes();
	private Usuarios objUsuario;
	
	@Before
	public void setUsuario() throws UnirestException {
		Map<Object, Object> mapuser = accionesMensaje.setUsuarios(
				new Usuarios("43875348978934", new Date(), "3489573489400"));
		this.objUsuario = accionesMensaje.convertMapToUsuarios(mapuser);
	}
	
	@Test
	public void insertaTarjetaTest() throws UnirestException  {
		boolean insercionTarjet = accionesMensaje.insertaTarjeta( this.objUsuario, "57439847792387" );
		assertTrue(insercionTarjet);		
	}
	
	@Test
	public void insertaUserTest() throws UnirestException  {
		Usuarios newUsuario = accionesMensaje.insertaUser( new Usuarios(
				"595734985734874",
				new Date(),
				"75873646889934") );
		
		assertNotNull(newUsuario);
	}
	
	@Test
	public void getValidaDatosTransferenciaTest(){
		boolean checkout = accionesMensaje
				.getValidaDatosTransferencia("1234567890123456 123 123456789654321 8543.34");
		assertTrue(checkout);
	}
	
	@Test
	public void verifyStringToNumberTest() {
		boolean verifyTrue =  Resources.verifyStringToNumber("58954609894");
		boolean verifyFalse =  Resources.verifyStringToNumber("58954gdf.9894");
		assertTrue(verifyTrue);
		assertFalse(verifyFalse);
	}
	
	@Test
	public void verifyStringToDecimalTest() {
		boolean verifyTrue = Resources.verifyStringToDecimal("58739487.54");
		boolean verifyFalse = Resources.verifyStringToDecimal("58739dfg17.54");
		assertTrue(verifyTrue);
		assertFalse(verifyFalse);
	}
	
	@Test
	public void getTarjetaTest() throws UnirestException  {
		Tarjetas vgettarjeta = accionesMensaje.getTarjeta("5487897594975", new Usuarios(
				"595734985734874",
				new Date(),
				"75873646889934"));
		assertNotNull( vgettarjeta );
	}
	
	@Test
	public void getTipoTarjetaTest() {
		String typeTarjetaFalse = accionesMensaje.getTipoTarjeta("93534859348837");
		String typeTarjetaTrue = accionesMensaje.getTipoTarjeta("75463456878734");
		boolean verifyTypeFalse = typeTarjetaFalse.contains("Tarjeta Inválida");
		boolean verifyTypeTrue = typeTarjetaTrue.contains("Industria");
		assertTrue(verifyTypeFalse);
		assertTrue(verifyTypeTrue);
	}
	
}
