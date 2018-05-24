package com.bots.bots.resources;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.bots.bots.model.Sesiones;
import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Usuarios;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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
		Unirest.clearDefaultHeaders();
	    ObjectMapper mapper = new ObjectMapper();
	    String bodyValue = "";
		try {
			bodyValue = mapper.writeValueAsString(sesion);
		} catch (JsonProcessingException e) {
			LOGGER.error( e.getMessage() );
		}
		HttpResponse<String> response = Unirest.post(Constantes.URL_SESIONES)
				.headers(headers)
				.body(bodyValue)
				.asString();

      if (!response.getBody().isEmpty()) {
          Response classResponse = new Response(response.getBody());
          return classResponse.getMapResponseOnlyOne();
      }
      
      return new HashMap<>();
	}
	
	public Map<Object, Object> setEditSesion(Sesiones sesion) throws  UnirestException  {
		Unirest.clearDefaultHeaders();
	    ObjectMapper mapper = new ObjectMapper();
	    String bodyMapperValue = "";
		try {
			bodyMapperValue = mapper.writeValueAsString(sesion);
		} catch (JsonProcessingException e) {
			LOGGER.error( e.getMessage() );
		}
		HttpResponse<String> response = Unirest.put(Constantes.URL_SESIONES + sesion.getIdSesion())
				.headers(headers)
				.body(bodyMapperValue)
				.asString();
		
		if (response.getBody() == null) { return new HashMap<>(); }
        if (!response.getBody().isEmpty()) {
            Response classResponse = new Response(response.getBody());
            return classResponse.getMapResponseOnlyOne();
        }
        
        return new HashMap<>();
	}
	
	public Map<Object,Object> setTarjeta(Tarjetas tarjeta) throws UnirestException {
		LOGGER.info("Método en ejecución: setNuevaTarjeta(Tarjetas)");
		if( tarjeta.getNtarjeta() != null && !tarjeta.getNtarjeta().isEmpty() ) {
			HttpResponse<String> response = Unirest.post( Constantes.URL_TARJETAS )
					.headers(headers)
					.body(tarjeta)
					.asString();
			if (!response.getBody().isEmpty()) {
				Response classResponse = new Response(response.getBody());
				return classResponse.getMapResponseOnlyOne();
			}

		}
		
		return new HashMap<>();
	}
	
	public Map<Object,Object> setUsuarios(Usuarios user) throws UnirestException  {
		
		LOGGER.info("Método en ejecución: setSaveCliente(Usuarios)");
		
		if(user.getIduser() != null && !user.getIduser().isEmpty()) {
			HttpResponse<String> response = Unirest.post( Constantes.URL_USUARIOS )
					.headers(headers)
					.body( getJSONObjectOfClass(user) )
					.asString();
			if(!response.getBody().isEmpty()) {
				Response classResponse = new Response( response.getBody() );		
				return classResponse.getMapResponseOnlyOne();
			}
		}
		return new HashMap<>();
	}
		
	public boolean setNuevaTransaccion() {
		return true;
	}
	
	public Map<Object,Object> getClienteByClave(String claveIdUser) throws UnirestException  {		
		LOGGER.info("Método en ejecución: getClienteByClave(String)");
		if(!claveIdUser.isEmpty()) {
			HttpResponse<String> response = Unirest.get(Constantes.URL_USUARIOS + claveIdUser)
					.headers(headers)
					.asString();
			if(!response.getBody().isEmpty()) {
				Response classResponse = new Response( response.getBody() );		
				return classResponse.getMapResponseOnlyOne();
			}
		}
		return new HashMap<>();
	}
	
	private <T> JSONObject getJSONObjectOfClass(T clazz) {
        JSONObject object = new JSONObject();
        for (Field field : clazz.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                object.put(field.getName(), field.get(clazz));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                object.put("error", e.getMessage());
            }
        }
        return object;
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
            if (!response.getBody().isEmpty()) {
            	Response classResponse = new Response(response.getBody());
                return classResponse.getMapResponseOnlyOne();
            }
        }

        return new HashMap<>();
    }	
	
}
