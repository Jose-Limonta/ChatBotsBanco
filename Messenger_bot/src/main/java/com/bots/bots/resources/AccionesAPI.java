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
import org.json.JSONException;
import org.json.JSONObject;

import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Transacciones;
import com.bots.bots.model.Usuarios;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AccionesAPI {
	
	private static final Log LOGGER = LogFactory.getLog(AccionesAPI.class);
	
	private final String SERVER = "http://localhost";
	private final String PORT = ":8087";
	
	public AccionesAPI(){
		
	}
	
	public void getDatosCliente(String cliente) throws UnirestException {
	}
	
	public Map<Object,Object> setTarjeta(Tarjetas tarjeta) throws UnirestException,NullPointerException{
		LOGGER.info("Método en ejecución: setNuevaTarjeta(Tarjetas)");
		if(tarjeta != null) {
			if(!tarjeta.getNtarjeta().isEmpty()) {
				HttpResponse<String> response = Unirest.post("http://localhost:8087/tarjetases")
					  .header("Content-Type", "application/json")
					  .header("Cache-Control", "no-cache").body(tarjeta)
					  .asString();
				
				LOGGER.info("Datos de salida: "+response.getBody());
				if(!response.getBody().isEmpty()) {
					Response class_response = new Response( response.getBody() );		
					return class_response.getMapResponse();
				}
			}
		}
		
		return new HashMap<Object,Object>();
	}
	
	/**
	 * Funcionando
	 * */
	public Map<Object,Object> setUsuarios(Usuarios user) throws UnirestException, JSONException {
		
		LOGGER.info("Método en ejecución: setSaveCliente(Usuarios)");
		
		if(!user.getIduser().isEmpty()) {
			HttpResponse<String> response = Unirest.post(
					SERVER + PORT + "/usuarioses" )
					  .header("Content-Type", "application/json")
					  .header("Cache-Control", "no-cache")
					  .body( getJSONObjectOfClass(user) )
					  .asString();
			LOGGER.info("Datos de salida: "+response.getBody());
			if(!response.getBody().isEmpty()) {
				Response class_response = new Response( response.getBody() );		
				return class_response.getMapResponse();
			}
		}
		return new HashMap<Object,Object>();
	}
		
	public boolean setNuevaTransaccion(Tarjetas tarjeta, Usuarios user) {
		return true;
	}
	
	public Map<Object,Object> getClienteByClave(String clave_iduser) throws UnirestException {
		
		LOGGER.info("Método en ejecución: getClienteByClave(String)");
		
		if(!clave_iduser.isEmpty()) {
			HttpResponse<String> response = Unirest.get(SERVER + PORT + "/usuarioses/"+clave_iduser)
					  .header("Cache-Control", "no-cache")
					  .asString();
			if(!response.getBody().isEmpty()) {
				LOGGER.info("Datos de salida: "+response.getBody());
				Response class_response = new Response( response.getBody() );		
				return class_response.getMapResponse();
			}
		}
		return new HashMap<Object,Object>();
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
	
	
	
	public Usuarios convertMapToUsuarios(Map<Object,Object> mapa, String id_user) 
			throws ParseException, JSONException, UnirestException {
		if(mapa.isEmpty()) 
			return new Usuarios();
		
		Usuarios user = new Usuarios();
        user.setFecha(convertStringToDate(mapa.get("fecha").toString()));
        user.setIdpagina(mapa.get("idpagina").toString());
        user.setIduser(id_user);
        JSONObject items = (JSONObject) mapa.get("_links");
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
        java.sql.Date sql = new java.sql.Date(parsed.getTime());

        return sql;
    }	
	
	private ArrayList<Tarjetas> getTarjetasFromLinkJSONObject(String url) throws UnirestException, ParseException {
        ArrayList<Tarjetas> tarjetas = new ArrayList<>();

        JSONArray arreglotarjetas = getArregloDeLlamadas(url, "tarjetases");
        for (int i = 0; i < arreglotarjetas.length(); i++) {
            JSONObject items = (JSONObject) arreglotarjetas.get(i);
            JSONObject urltarjetas = (JSONObject) items.get("_links");
            String urlclave = urltarjetas.getJSONObject("tarjetas").get("href").toString();
            Tarjetas tarjeta = getTarjetaByMap(getMapFromHttpResponse(urlclave));

            tarjetas.add(tarjeta);
        }
        return tarjetas;
    }

    private ArrayList<Transacciones> getTransaccionesFromLinkJSONObject(String url) throws UnirestException, ParseException {
        ArrayList<Transacciones> transacciones = new ArrayList<>();
        JSONArray arreglo_tarjetas = getArregloDeLlamadas(url, "transaccioneses");
        for (int i = 0; i < arreglo_tarjetas.length(); i++) {
            JSONObject items = (JSONObject) arreglo_tarjetas.get(i);
            JSONObject urltransacciones = (JSONObject) items.get("_links");
            String urlclave = urltransacciones.getJSONObject("transacciones").get("href").toString();
            
            Transacciones transaccion = getTransaccionesByMap( getMapFromHttpResponse(urlclave) );
            transacciones.add(transaccion);
        }
        return transacciones;
    }
	
	private JSONArray getArregloDeLlamadas(String url, String tipo_llamada) throws UnirestException {
        JSONObject obj_embedded = (JSONObject) getMapFromHttpResponse(url).get("_embedded");
        JSONArray arreglo_salida = (JSONArray) obj_embedded.get(tipo_llamada);
        return arreglo_salida;
    }
	
	public Tarjetas getTarjetaByNTarjeta(String ntarjeta) throws UnirestException, ParseException { // cambiar a false
        Map<Object, Object> mapadetarjeta = getMapFromHttpResponse("http://localhost:8087/tarjetases/" + ntarjeta);
        Tarjetas tarjeta = new Tarjetas();

        tarjeta.setFecha(convertStringToDate(mapadetarjeta.get("fecha").toString()));
        tarjeta.setNbanco(mapadetarjeta.get("nbanco").toString());
        tarjeta.setTtarjeta(mapadetarjeta.get("ttarjeta").toString());

        JSONObject urltarjetas = (JSONObject) mapadetarjeta.get("_links");
        String urlclave = urltarjetas.getJSONObject("tarjetas").get("href").toString();

        tarjeta.setNtarjeta(urlclave.split("/")[urlclave.split("/").length - 1]);

        return tarjeta;
    }

    private Transacciones getTransaccionesByMap(Map<Object, Object> mapadetransacciones) 
    		throws ParseException, UnirestException {
        Transacciones transaccion = new Transacciones();

        transaccion.setClavetransaccion(mapadetransacciones.get("clavetransaccion").toString());
        transaccion.setFecha(convertStringToDate(mapadetransacciones.get("fecha").toString()));

        JSONObject urltransacciones = (JSONObject) mapadetransacciones.get("_links");
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

        usuario.setFecha(convertStringToDate(mapadeusuarios.get("fecha").toString()));
        usuario.setIdpagina(mapadeusuarios.get("idpagina").toString());

        JSONObject urltarjetas = (JSONObject) mapadeusuarios.get("_links");
        String urlclave = urltarjetas.getJSONObject("usuarios").get("href").toString();

        usuario.setIduser(urlclave.split("/")[urlclave.split("/").length - 1]);

        return usuario;
    }

    private Tarjetas getTarjetaByMap(Map<Object, Object> mapadetarjeta) throws ParseException {
        Tarjetas tarjeta = new Tarjetas();

        tarjeta.setFecha(convertStringToDate(mapadetarjeta.get("fecha").toString()));
        tarjeta.setNbanco(mapadetarjeta.get("nbanco").toString());
        tarjeta.setTtarjeta(mapadetarjeta.get("ttarjeta").toString());

        JSONObject urltarjetas = (JSONObject) mapadetarjeta.get("_links");
        String urlclave = urltarjetas.getJSONObject("tarjetas").get("href").toString();

        tarjeta.setNtarjeta(urlclave.split("/")[urlclave.split("/").length - 1]);

        return tarjeta;
    }

	
	private Map<Object, Object> getMapFromHttpResponse(String url) throws UnirestException {
        if (!url.isEmpty()) {
            HttpResponse<String> response = Unirest.get(url).header("Cache-Control", "no-cache").asString();

            if (!response.getBody().isEmpty()) {
                Response class_response = new Response(response.getBody());
                return class_response.getMapResponse();
            }
        }

        return new HashMap<Object, Object>();
    }
	
	
}
