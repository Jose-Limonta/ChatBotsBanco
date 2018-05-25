package com.bots.bots.resources;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bots.bots.model.Sesiones;
import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Usuarios;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

public class AccionesAPI {
	
	private static final Log LOGGER = LogFactory.getLog(AccionesAPI.class);
	private Map<String,String> headers = new HashMap<>();
	
	public AccionesAPI(){
		headers.put("Content-Type", "application/json");
		headers.put("Cache-Control", "no-cache");
	}
	
	public void getDatosCliente(String cliente) {
		// metodo aun por confirmar su funcionamiento
	}
	
	public Sesiones getSesiones(String clave, Map<String, Object> headers) throws UnirestException {
		Map<Object, Object> mapa = getOnlyOneMapFromHttpResponse(Constantes.URL_SESIONES + clave );
		return convertMapToSesiones(mapa, headers);
	}
	
	public Map<Object, Object> setAddSesion(Sesiones sesion) throws UnirestException  {
		return setAPIAction( sesion, Constantes.URL_SESIONES, "post");
	}
	
	public Map<Object, Object> setEditSesion(Sesiones sesion) throws  UnirestException  {
		return setAPIAction( sesion, Constantes.URL_SESIONES, "put");
	}
	
	public Map<Object,Object> setTarjeta(Tarjetas tarjeta) throws UnirestException {
		LOGGER.info("Método en ejecución: setTarjeta(Tarjetas)");
		return tarjeta.getNtarjeta() != null 
				&& !tarjeta.getNtarjeta().isEmpty() ? 
						setAPIAction( tarjeta, Constantes.URL_TARJETAS, "post") : 
							new HashMap<>();
	}
	
	public Map<Object,Object> setUsuarios(Usuarios user) throws UnirestException  {		
		LOGGER.info("Método en ejecución: setUsuarios(Usuarios)");
		return user.getIduser() != null 
				&& !user.getIduser().isEmpty() ? 
						setAPIAction( user, Constantes.URL_USUARIOS, "post") : new HashMap<>();
	}
		
	public boolean setNuevaTransaccion() {
		return true;
	}
	
	public Map<Object,Object> getClienteByClave(String claveIdUser) throws UnirestException  {		
		LOGGER.info("Método en ejecución: getClienteByClave(String)");
		return !claveIdUser.isEmpty() ? getOnlyOneMapFromHttpResponse(Constantes.URL_USUARIOS + claveIdUser) : new HashMap<>();
	}
	
	public Usuarios convertMapToUsuarios(Map<Object,Object> mapa) {
		if(mapa.isEmpty()) return new Usuarios();
		
		Map<String, Object> header = new HashMap<>();
		header.put("fecha", new Date());
		header.put("tarjetasList", new Tarjetas());
		
		MapToClass<Usuarios> ms = new MapToClass<>(mapa, header);
		return ms.getClassOfMap(new Usuarios());
	}
	
	public Sesiones convertMapToSesiones(Map<Object,Object> mapa, Map<String, Object> headers) {
		if(mapa.isEmpty()) return new Sesiones();		
		MapToClass<Sesiones> ms = new MapToClass<>(mapa, headers);
		return ms.getClassOfMap(new Sesiones());		
	}
	
	private Map<Object, Object> getOnlyOneMapFromHttpResponse(String url) throws UnirestException {
        if (!url.isEmpty()) {
            HttpResponse<String> response = Unirest.get(url)
            		.headers(headers)
            		.asString();            
            if( response.getStatus() == 500) return new HashMap<>();
            if (!response.getBody().isEmpty() && response.getStatus() == 200) {
            	Response classResponse = new Response(response.getBody());
                return classResponse.getMapResponseOnlyOne();
            }
        }

        return new HashMap<>();
    }
	
	private Map<Object, Object> setAPIAction(Object classAction, String urlAction, String type) throws UnirestException  {
		Unirest.clearDefaultHeaders();
	    ObjectMapper mapper = new ObjectMapper();
	    String bodyValue = "";
		try {
			bodyValue = mapper.writeValueAsString(classAction);
		} catch (JsonProcessingException e) {
			LOGGER.error( e.getMessage() );
		}
		HttpRequestWithBody request = getRequest(type, urlAction);
		HttpResponse<String> response = request
				.headers( headers ).body( bodyValue ).asString();
		
		if (response.getBody() == null) return new HashMap<>();
		
		if (!response.getBody().isEmpty() && response.getStatus() == 200) {
			Response classResponse = new Response( response.getBody() );
			return classResponse.getMapResponseOnlyOne();
			}
		return new HashMap<>();
	}
	
	private HttpRequestWithBody getRequest(String type, String urlAction) {
		switch( type ) {
			case "post": return Unirest.post( urlAction );
			case "put": return Unirest.put( urlAction );
			default : return Unirest.delete( urlAction );
		}
	}
	
}