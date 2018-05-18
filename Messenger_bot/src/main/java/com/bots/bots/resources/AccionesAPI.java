package com.bots.bots.resources;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bots.bots.model.Sesiones;
import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Transacciones;
import com.bots.bots.model.Usuarios;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AccionesAPI {
	
	private static final Log LOGGER = LogFactory.getLog(AccionesAPI.class);
	
	private static final String SERVER = "http://localhost";
	private static final String PORT = ":8087";
	
	public AccionesAPI(){
		// constructor vacío para instanciarlo
	}
	
	public void getDatosCliente(String cliente) {
		// metodo aun por confirmar su funcionamiento
	}
	
	public Map<Object, Object> getSesiones(String clave) throws UnirestException {
		return getMapFromHttpResponse("http://localhost:8087/tarjetases/" + clave );
	}
	
	public Map<Object, Object> setAddSesion(Sesiones sesion) throws Exception  {
		Unirest.clearDefaultHeaders();
	    ObjectMapper mapper = new ObjectMapper();
	    String bodyValue = mapper.writeValueAsString(sesion);
		HttpResponse<String> response = Unirest.post("http://localhost:8087/sesioneses")
				  .header(Constantes.CONTENT_TYPE, Constantes.APPLICATION_JSON_VALUE)
				  .header(Constantes.CACHE_CONTROL, Constantes.CACHE_CONTROL_NO_CACHE)
				  .body(bodyValue)
				  .asString();

      if (!response.getBody().isEmpty()) {
          Response classResponse = new Response(response.getBody());
          return classResponse.getMapResponse();
      }
      
      return new HashMap<>();
	}
	
	public Map<Object, Object> setEditSesion(Sesiones sesion) throws Throwable {
		Unirest.clearDefaultHeaders();
	    ObjectMapper mapper = new ObjectMapper();
		HttpResponse<String> response = Unirest.put("http://localhost:8087/sesioneses/" + sesion.getIdSesion())
					  .header(Constantes.CONTENT_TYPE, Constantes.APPLICATION_JSON_VALUE)
					  .header(Constantes.CACHE_CONTROL, Constantes.CACHE_CONTROL_NO_CACHE)
					  .body(mapper.writeValueAsString(sesion))
					  .asString();
		if (response.getBody() == null) { return new HashMap<>(); }
        if (!response.getBody().isEmpty()) {
            Response classResponse = new Response(response.getBody());
            return classResponse.getMapResponse();
        }
        
        return new HashMap<>();
	}
	
	public Map<Object,Object> setTarjeta(Tarjetas tarjeta) throws UnirestException {
		LOGGER.info("Método en ejecución: setNuevaTarjeta(Tarjetas)");
		if(tarjeta != null && !tarjeta.getNtarjeta().isEmpty() ) {
			HttpResponse<String> response = Unirest.post("http://localhost:8087/tarjetases")
					.header(Constantes.CONTENT_TYPE, Constantes.APPLICATION_JSON_VALUE)
					.header(Constantes.CACHE_CONTROL, Constantes.CACHE_CONTROL_NO_CACHE).body(tarjeta).asString();

			LOGGER.info("Datos de tarjeta en salida: " + response.getBody());
			if (!response.getBody().isEmpty()) {
				Response classResponse = new Response(response.getBody());
				return classResponse.getMapResponse();
			}

		}
		
		return new HashMap<>();
	}
	
	/**
	 * Funcionando
	 * @throws UnirestException 
	 * */
	public Map<Object,Object> setUsuarios(Usuarios user) throws UnirestException  {
		
		LOGGER.info("Método en ejecución: setSaveCliente(Usuarios)");
		
		if(!user.getIduser().isEmpty()) {
			HttpResponse<String> response = Unirest.post(
					SERVER + PORT + "/usuarioses" )
					  .header(Constantes.CONTENT_TYPE, Constantes.APPLICATION_JSON_VALUE)
					  .header(Constantes.CACHE_CONTROL, Constantes.CACHE_CONTROL_NO_CACHE)
					  .body( getJSONObjectOfClass(user) )
					  .asString();
			LOGGER.info("Datos de usuario en salida: "+response.getBody());
			if(!response.getBody().isEmpty()) {
				Response classResponse = new Response( response.getBody() );		
				return classResponse.getMapResponse();
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
			HttpResponse<String> response = Unirest.get(SERVER + PORT + "/usuarioses/"+claveIdUser)
					  .header(Constantes.CACHE_CONTROL, Constantes.CACHE_CONTROL_NO_CACHE)
					  .asString();
			if(!response.getBody().isEmpty()) {
				LOGGER.info("Datos de clientes por clave en salida: "+response.getBody());
				Response classResponse = new Response( response.getBody() );		
				return classResponse.getMapResponse();
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
	
	
	
	public Usuarios convertMapToUsuarios(Map<Object,Object> mapa, String idUser) 
			throws Throwable {
		if(mapa.isEmpty()) 
			return new Usuarios();
		
		Usuarios user = new Usuarios();
        user.setFecha(convertStringToDate(mapa.get(Constantes.GET_TO_MAP_FECHA).toString()));
        user.setIdpagina(mapa.get("idpagina").toString());
        user.setIduser(idUser);
        JSONObject items = (JSONObject) mapa.get(Constantes.LINKS);
        JSONObject linktarjeta = (JSONObject) items.get("tarjetasList");
        JSONObject linktransaccion = (JSONObject) items.get("transaccionesList");
        ArrayList<Tarjetas> tarjetas = getTarjetasFromLinkJSONObject(linktarjeta.get("href").toString());
        ArrayList<Transacciones> transaccion = getTransaccionesFromLinkJSONObject(linktransaccion.get("href").toString());
        user.setTarjetasList(tarjetas);
        user.setTransaccionesList(transaccion);

        return user;
	}
	
	private Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = format.parse(dateString);
        return new java.sql.Date(parsed.getTime());
    }	
	
	private ArrayList<Tarjetas> getTarjetasFromLinkJSONObject(String url) throws Throwable {
        ArrayList<Tarjetas> tarjetas = new ArrayList<>();

        JSONArray arreglotarjetas = getArregloDeLlamadas(url, "tarjetases");
        for (int i = 0; i < arreglotarjetas.length(); i++) {
            JSONObject items = (JSONObject) arreglotarjetas.get(i);
            JSONObject urltarjetas = (JSONObject) items.get(Constantes.LINKS);
            String urlclave = urltarjetas.getJSONObject(Constantes.GET_TO_MAP_TARJETAS).get("href").toString();
            Tarjetas tarjeta = getTarjetaByMap(getMapFromHttpResponse(urlclave));

            tarjetas.add(tarjeta);
        }
        return tarjetas;
    }

    private ArrayList<Transacciones> getTransaccionesFromLinkJSONObject(String url) throws Throwable  {
        ArrayList<Transacciones> transacciones = new ArrayList<>();
        JSONArray arregloTarjetas = getArregloDeLlamadas(url, "transaccioneses");
        for (int i = 0; i < arregloTarjetas.length(); i++) {
            JSONObject items = (JSONObject) arregloTarjetas.get(i);
            JSONObject urltransacciones = (JSONObject) items.get(Constantes.LINKS);
            String urlclave = urltransacciones.getJSONObject("transacciones").get("href").toString();
            
            Transacciones transaccion = getTransaccionesByMap( getMapFromHttpResponse(urlclave) );
            transacciones.add(transaccion);
        }
        return transacciones;
    }
	
	private JSONArray getArregloDeLlamadas(String url, String tipoLlamada) throws UnirestException {
        JSONObject objEmbedded = (JSONObject) getMapFromHttpResponse(url).get("_embedded");
        return (JSONArray) objEmbedded.get(tipoLlamada);
    }
	
	public Tarjetas getTarjetaByNTarjeta(String ntarjeta) throws Exception { // cambiar a false
        Map<Object, Object> mapadetarjeta = getMapFromHttpResponse("http://localhost:8087/tarjetases/" + ntarjeta);
        Tarjetas tarjeta = new Tarjetas();

        tarjeta.setFecha(convertStringToDate(mapadetarjeta.get(Constantes.GET_TO_MAP_FECHA).toString()));
        tarjeta.setNbanco(mapadetarjeta.get("nbanco").toString());
        tarjeta.setTtarjeta(mapadetarjeta.get("ttarjeta").toString());

        JSONObject urltarjetas = (JSONObject) mapadetarjeta.get(Constantes.LINKS);
        String urlclave = urltarjetas.getJSONObject(Constantes.GET_TO_MAP_TARJETAS).get("href").toString();

        tarjeta.setNtarjeta(urlclave.split("/")[urlclave.split("/").length - 1]);

        return tarjeta;
    }

    private Transacciones getTransaccionesByMap(Map<Object, Object> mapadetransacciones) throws Throwable {
        Transacciones transaccion = new Transacciones();

        transaccion.setClavetransaccion(mapadetransacciones.get("clavetransaccion").toString());
        transaccion.setFecha(convertStringToDate(mapadetransacciones.get(Constantes.GET_TO_MAP_FECHA).toString()));

        JSONObject urltransacciones = (JSONObject) mapadetransacciones.get(Constantes.LINKS);
        String urlclave = urltransacciones.getJSONObject("transacciones").get("href").toString();
        int tamaniosplit = urlclave.split("/").length;
        String id = urlclave.split("/")[tamaniosplit - 1];
        transaccion.setIdtransaccion(Integer.parseInt(id));

        Map<Object, Object> listtarjeta = getMapFromHttpResponse(urltransacciones
        		.getJSONObject("ndtarjeta").get("href").toString());
        Map<Object, Object> listusuario = getMapFromHttpResponse(urltransacciones
        		.getJSONObject("iduser").get("href").toString());
        
        transaccion.setNdtarjeta(getTarjetaByMap(listtarjeta));
        transaccion.setIduser(getUsuariosByMap(listusuario));

        return transaccion;
    }

    private Usuarios getUsuariosByMap(Map<Object, Object> mapadeusuarios) throws ParseException {
        Usuarios usuario = new Usuarios();

        usuario.setFecha(convertStringToDate(mapadeusuarios.get(Constantes.GET_TO_MAP_FECHA).toString()));
        usuario.setIdpagina(mapadeusuarios.get("idpagina").toString());

        JSONObject urltarjetas = (JSONObject) mapadeusuarios.get(Constantes.LINKS);
        String urlclave = urltarjetas.getJSONObject("usuarios").get("href").toString();

        usuario.setIduser(urlclave.split("/")[urlclave.split("/").length - 1]);

        return usuario;
    }

    private Tarjetas getTarjetaByMap(Map<Object, Object> mapadetarjeta) throws ParseException {
        Tarjetas tarjeta = new Tarjetas();

        tarjeta.setFecha(convertStringToDate(mapadetarjeta.get(Constantes.GET_TO_MAP_FECHA).toString()));
        tarjeta.setNbanco(mapadetarjeta.get("nbanco").toString());
        tarjeta.setTtarjeta(mapadetarjeta.get("ttarjeta").toString());

        JSONObject urltarjetas = (JSONObject) mapadetarjeta.get(Constantes.LINKS);
        String urlclave = urltarjetas.getJSONObject(Constantes.GET_TO_MAP_TARJETAS).get("href").toString();

        tarjeta.setNtarjeta(urlclave.split("/")[urlclave.split("/").length - 1]);

        return tarjeta;
    }

	
	private Map<Object, Object> getMapFromHttpResponse(String url) throws UnirestException {
        if (!url.isEmpty()) {
            HttpResponse<String> response = Unirest.get(url).header(Constantes.CACHE_CONTROL, Constantes.CACHE_CONTROL_NO_CACHE).asString();

            if (!response.getBody().isEmpty()) {
                Response classResponse = new Response(response.getBody());
                return classResponse.getMapResponse();
            }
        }

        return new HashMap<>();
    }
	
	
}
