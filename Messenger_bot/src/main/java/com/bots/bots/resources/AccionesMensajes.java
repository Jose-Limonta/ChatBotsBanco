package com.bots.bots.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

import com.bots.bots.model.Sesiones;
import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Usuarios;
import com.clivern.racter.receivers.webhook.MessageReceivedWebhook;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AccionesMensajes {
	
	private static final Log LOGGER = LogFactory.getLog(AccionesMensajes.class);
	
	protected boolean setRealizaTransaccion(String[] datos_de_transaccion) {
		return true;
	}
	
	protected Double setRealizarConsulta() {
		return 3009.54;
	}
	
	protected boolean insertaTarjeta(MessageReceivedWebhook message,
			Usuarios user, String tarjeta) throws ParseException, UnirestException {		
		LOGGER.info("Ejecucion: insertaTarjeta(MessageReceivedWebhook, Usuarios, String)");
		
    	AccionesAPI accion = new  AccionesAPI();
		Tarjetas objtarjeta = getTarjeta(message, tarjeta);
		
    	boolean inserto = false;
		
		if( !user.getIduser().isEmpty() ) {	    			
			Map<Object,Object> tarjeta_agregada = accion.setTarjeta(objtarjeta);
			if(tarjeta_agregada.get("fecha") != "") 
				inserto = true;	    			
		}
		return inserto;
    }
    
	protected Usuarios insertaUser(Usuarios usuario) 
			throws JSONException, UnirestException, ParseException {
		LOGGER.info("Ejecucion: insertaUser(Usuarios)");
		
    	AccionesAPI accion = new  AccionesAPI();
    	Map<Object,Object> usuario_agregado = accion.setUsuarios(usuario);
    	if(usuario_agregado.isEmpty())
    		return new Usuarios();
    	Usuarios user = accion.convertMapToUsuarios(usuario_agregado, usuario.getIduser());
    	return user;
    }
	
	protected boolean getValidaDatosTransferencia(String texto){
		String[] datos_transfer = texto.split(" ");
		if( verifyStringToNumber( datos_transfer[0] ) && verifyStringToNumber( datos_transfer[2] ) ) 
			return true;
		
		return false;
	}
    
    protected boolean verifyStringToNumber(String cuenta) {
    	LOGGER.info("Ejecucion: verifyStringToNumber(String)");
    	
		boolean verificador = true;
		for(char caracter : cuenta.toCharArray())	
			if(!Character.isDigit(caracter)) 
				verificador = false;			
		
		return verificador;
    }
    
    protected Usuarios getUsuario(MessageReceivedWebhook message) throws ParseException{
    	LOGGER.info("Ejecucion: getUsuario(MessageReceivedWebhook)");
    	
    	Usuarios user = new Usuarios();
		user.setFecha( getFechaOfStringToDateFromat() );
		user.setIduser(message.getUserId());
		user.setIdpagina(message.getPageId());
		
		return user;
    }
    
    protected Tarjetas getTarjeta(MessageReceivedWebhook message, String tarjeta) 
    		throws ParseException, UnirestException {
    	LOGGER.info("Ejecucion: getTarjeta(MessageReceivedWebhook)");
    	
    	Usuarios iduser = getUsuarioFromRegister(message.getUserId());
    	Tarjetas objtarjeta = new Tarjetas();
    	if(iduser != null ) {
    		if(!tarjeta.isEmpty()) {
	    		String ttarjeta  = getTipoTarjeta( tarjeta.substring(0, 1) );
	    		
		    	objtarjeta.setFecha( getFechaOfStringToDateFromat() );
		    	objtarjeta.setIduser(iduser);
		    	objtarjeta.setNtarjeta(tarjeta);
		    	objtarjeta.setNbanco("Bancos");
		    	objtarjeta.setTtarjeta(ttarjeta);
		    	
		    	return objtarjeta;
    		}
    	}
    	return objtarjeta;
    }
    
    protected String getTipoTarjeta(String tarjeta) {
    	LOGGER.info("Ejecucion: getTipoTarjeta(String)");
    	
    	String ttarjeta  = "";
		switch(tarjeta.substring(0, 1)) {
			case "1": ttarjeta = "Aereolinea";
			case "2": ttarjeta = "Aereolinea";
			case "3": ttarjeta = "American Express";
			case "4": ttarjeta = "Visa";
			case "5": ttarjeta = "Master Card";
			case "6": ttarjeta = "Discovery Card";
			case "7": ttarjeta = "Industria del petróleo";
			case "8": ttarjeta = "Telecomunicaciones";
			default: ttarjeta = "No reconocida";
		}
		return ttarjeta;
    }
    
    protected Usuarios getUsuarioFromRegister(String clave) 
    		throws UnirestException, JSONException, ParseException {
    	LOGGER.info("Ejecucion: getUsuarioFromRegister(String)");
    	
    	AccionesAPI accion = new  AccionesAPI();
    	Map<Object,Object> mapausuario = accion.getClienteByClave(clave);
    	if(mapausuario.isEmpty())
    		return new Usuarios();
    	Usuarios usuario = accion.convertMapToUsuarios(mapausuario, clave);
    	return usuario;
    }
    
    protected Date getFechaOfStringToDateFromat() throws ParseException{
    	LOGGER.info("Ejecucion: getFechaOfStringToDateFromat()");
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);    	
        Date parsed = format.parse( format.format( new Date() ) );
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        return sql;
    }
    
    protected int getSesionesExistentes(String clave) throws UnirestException{
    	AccionesAPI accion = new AccionesAPI();
    	return accion.getSesiones( clave ).size();
    }
    
    protected boolean setAddSesion(Sesiones sesion) throws UnirestException, JsonProcessingException {
    	AccionesAPI accion = new AccionesAPI();
    	Map<Object, Object> sesion_retorno = accion.setAddSesion(sesion);
    	if(sesion_retorno.containsKey("message")) 
    		return false;
    	else
    		return true;
    	
    }
    
    protected boolean setEditSesion(Sesiones sesion) throws UnirestException, JsonProcessingException {
    	AccionesAPI accion = new AccionesAPI();
    	Map<Object, Object> sesion_retorno = accion.setEditSesion(sesion);
    	if(sesion_retorno.containsKey("message")) 
    		return false;
    	else
    		return true;
    	
    }
}

