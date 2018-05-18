package com.bots.bots.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bots.bots.model.Sesiones;
import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Usuarios;
import com.clivern.racter.receivers.webhook.MessageReceivedWebhook;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AccionesMensajes extends AccionesAPI{
	
	private static final Double CONSULTA = 3009.54;
	private String[] tipoTarjetasAv = {"","Aereolinea", "Aereolinea","American Express",
    		"Visa", "Master Card","Discovery Card", "Industria del petróleo","Telecomunicaciones"};	
	private static final Log LOGGER = LogFactory.getLog(AccionesMensajes.class);
	
	protected boolean setRealizaTransaccion( String[] datostransfer) {
		return datostransfer.length > 0 ? true : false;
	}
	
	protected Double setRealizarConsulta() {
		return CONSULTA;
	}
	
	protected boolean insertaTarjeta(MessageReceivedWebhook message, Usuarios user, String tarjeta) throws Throwable {		
		LOGGER.info("Ejecucion: insertaTarjeta(MessageReceivedWebhook, Usuarios, String)");
		
		Tarjetas objtarjeta = getTarjeta(message, tarjeta);
		
		if( !user.getIduser().isEmpty() ) {	    			
			Map<Object,Object> tarjetaAgregada = setTarjeta(objtarjeta);
			if(!tarjetaAgregada.get("fecha").equals("")) 
				return true;	    			
		}
		return false;
    }
    
	protected Usuarios insertaUser(Usuarios usuario) throws Throwable  {
		LOGGER.info("Ejecucion: insertaUser(Usuarios)");
		
    	Map<Object,Object> usuarioAgregado = setUsuarios(usuario);
    	if(usuarioAgregado.isEmpty())
    		return new Usuarios();
    	
    	return convertMapToUsuarios(usuarioAgregado, usuario.getIduser());
    }
	
	protected boolean getValidaDatosTransferencia(String texto){
		String[] datosTransfer = texto.split(" ");
		return verifyStringToNumber( datosTransfer[0] ) && verifyStringToNumber( datosTransfer[2] ) ? true : false; 
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
    
    protected Tarjetas getTarjeta(MessageReceivedWebhook message, String tarjeta) throws Throwable {
    	LOGGER.info("Ejecucion: getTarjeta(MessageReceivedWebhook)");
    	
    	Usuarios iduser = getUsuarioFromRegister(message.getUserId());
    	Tarjetas objtarjeta = new Tarjetas();
    	if(iduser != null && !tarjeta.isEmpty()) {
	    	String ttarjeta  = getTipoTarjeta( tarjeta.substring(0, 1) );
	    	
		    objtarjeta.setFecha( getFechaOfStringToDateFromat() );
		    objtarjeta.setIduser(iduser);
		    objtarjeta.setNtarjeta(tarjeta);
		    objtarjeta.setNbanco("Bancos");
		    objtarjeta.setTtarjeta(ttarjeta);
		    	
		    return objtarjeta;    		
    	}
    	return objtarjeta;
    }
    
    protected String getTipoTarjeta(String tarjeta) {
    	LOGGER.info("Ejecucion: getTipoTarjeta(String)");
    	int posision = Integer.parseInt( tarjeta.substring(0, 1) );
    	return tipoTarjetasAv[ posision ];
    }
    
    protected Usuarios getUsuarioFromRegister(String clave)  throws Throwable {
    	LOGGER.info("Ejecucion: getUsuarioFromRegister(String)");
    	Map<Object,Object> mapausuario = getClienteByClave(clave);
    	if(mapausuario.isEmpty())
    		return new Usuarios();
    	return convertMapToUsuarios(mapausuario, clave);
    }
    
    protected Date getFechaOfStringToDateFromat() throws ParseException{
    	LOGGER.info("Ejecucion: getFechaOfStringToDateFromat()");
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);    	
        Date parsed = format.parse( format.format( new Date() ) );
        return new java.sql.Date(parsed.getTime());
    }
    
    protected int getSesionesExistentes(String clave) throws UnirestException{
    	return getSesiones( clave ).size();
    }
    
    protected boolean setAddSesionMessageAccion(Sesiones sesion) throws Throwable {
    	Map<Object, Object> sesionRetorno = setAddSesion(sesion);
    	return sesionRetorno.containsKey("message") ? false : true;    	
    }
    
    protected boolean setEditSesionMessageAccion(Sesiones sesion) throws Throwable {
    	Map<Object, Object> sesionRetorno = setEditSesion(sesion);
    	return sesionRetorno.containsKey("message") ? false : true;    	
    }
}

