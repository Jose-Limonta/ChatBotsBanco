package com.bots.bots.resources;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bots.bots.model.Sesiones;
import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Transacciones;
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
	
	public boolean insertaTarjeta(Usuarios user, String tarjeta) throws UnirestException  {		
		LOGGER.info("Ejecucion: insertaTarjeta(Usuarios, String)");		
		Tarjetas objtarjeta = getTarjeta(tarjeta, user);
		if( objtarjeta.getNtarjeta() != null ) {	    			
			Map<Object,Object> tarjetaAgregada = setTarjeta(objtarjeta);
			if(!tarjetaAgregada.isEmpty()) 
				return true;
		}
		return false;
    }
    
	public Usuarios insertaUser(Usuarios usuario) throws UnirestException  {
		LOGGER.info("Ejecucion: insertaUser(Usuarios)");
    	Map<Object,Object> usuarioAgregado = setUsuarios(usuario);
    	return usuarioAgregado.isEmpty() ? new Usuarios() : convertMapToUsuarios(usuarioAgregado);
    }
	
	public Transacciones insertaTransaccion(Transacciones transaccion) throws UnirestException {
		Map<Object,Object> transaccionAdd = setTransaccion( transaccion );
		return transaccionAdd.isEmpty() 
				? new Transacciones() 
						: convertMapToTransacciones(transaccionAdd, getMapOfHeadersT() );
	}
	
	private Map<String,Object> getMapOfHeadersT(){
		Map<String,Object> headersT = new HashMap<>();
		int valorInicialInt = 0;
		headersT.put("idtransaccion", new Integer( valorInicialInt ) );
		headersT.put("fecha", new Date());
		headersT.put("ndtarjeta", new Tarjetas() );
		headersT.put("iduser", new Usuarios() );
		
		return headersT;
	}
	
	public boolean getValidaDatosTransferencia(String texto){
		String[] datosTransfer = texto.split(" ");
		return Resources.verifyStringToNumber( datosTransfer[0] ) 
				&& Resources.verifyStringToNumber( datosTransfer[1] ) 
				&& Resources.verifyStringToNumber( datosTransfer[2] )
				&& Resources.verifyStringToDecimal( datosTransfer[3] ) ? true : false; 
	}
    
    protected Usuarios getUsuario(MessageReceivedWebhook message){
    	LOGGER.info("Ejecucion: getUsuario(MessageReceivedWebhook)");
    	return new Usuarios( 
    			message.getUserId(),
    			Resources.getFechaOfStringToDateFromat(),
    			message.getPageId() );
    }
    
    public Tarjetas getTarjeta(String tarjeta, Usuarios iduser) throws UnirestException  {
    	LOGGER.info("Ejecucion: getTarjeta(String, Usuarios)");
    	if(iduser != null && !tarjeta.isEmpty()) {
    		Tarjetas objtarjeta = new Tarjetas();
	    	String ttarjeta  = getTipoTarjeta( tarjeta.substring(0, 1) );
	    	
		    objtarjeta.setFecha( Resources.getFechaOfStringToDateFromat() );
		    objtarjeta.setIduser( iduser );
		    objtarjeta.setNtarjeta( tarjeta );
		    objtarjeta.setNbanco( "Bancos" );
		    objtarjeta.setTtarjeta( ttarjeta );
		    	
		    return objtarjeta;    		
    	}
    	return new Tarjetas();
    }
    
    public String getTipoTarjeta(String tarjeta) {
    	LOGGER.info("Ejecucion: getTipoTarjeta(String)");
    	int posision = Integer.parseInt( tarjeta.substring(0, 1) );
    	return posision > 8 ? "Tarjeta Inválida" : tipoTarjetasAv[ posision ];
    }
    
    public Usuarios getUsuarioFromRegister(String clave) throws UnirestException  {
    	LOGGER.info("Ejecucion: getUsuarioFromRegister(String)");
    	Map<Object,Object> mapausuario = getClienteByClave(clave);
    	return mapausuario.isEmpty() ? new Usuarios() : convertMapToUsuarios( mapausuario ); 
    }
    
    protected Sesiones getSesion(String clave, Map<String, Object> headers) throws UnirestException {
    	Sesiones sesion = getSesiones(clave, headers);
    	return sesion != null && sesion.getIdSesion() != null ? sesion : new Sesiones();    	
    }
    
    public Tarjetas getTarjetaAccion(String clave, Map<String,Object> headers) throws UnirestException {
    	Tarjetas tarjeta = getTarjetaAPI(clave, headers);
    	return tarjeta != null && tarjeta.getNtarjeta() != null ? tarjeta : new Tarjetas();
    }
    
    protected Sesiones setAddSesionMessageAccion(Sesiones sesion, Map<String, Object> headers) throws UnirestException  {
    	Map<Object, Object> sessionAdd = setAddSesion(sesion);
    	return sessionAdd.isEmpty() ? new Sesiones() : convertMapToSesiones(sessionAdd, headers);
    }
    
    protected Sesiones setEditSesionMessageAccion(Sesiones sesion, Map<String, Object> headers) throws UnirestException {
    	Map<Object, Object> sesionEdit = setEditSesion(sesion);
    	return sesionEdit.isEmpty() ? new Sesiones() : convertMapToSesiones(sesionEdit, headers);
    }
}