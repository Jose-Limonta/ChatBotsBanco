package com.bots.bots.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>Para usar esta clase, hay dos tipos de request, cuando se obtiene 
 * un solo valor (aunque contenga otros arreglos) y los que tienen muchos 
 * resultados, es por ello que puede solo retornar un Mapa o un arreglo 
 * de mapas, la forma en que se mandan a llamar, son diferentes.<br>
 * Para porder convertir una cadena de un solo valor (un Mapa) es la sigueinte: </p>
 * <code>{@code Response class_response = new Response(response.getBody()); }<br>
 * {@code class_response.getMapResponseOnlyOne();}<code>
 * <br>
 * <p>Para convertir una cadena con muchos valores (arreglo de mapas) es la siguente:</p>
 * <code>{@code Response classResponse = new Response(); }<br>
 * classResponse.setConfiguration( "[{"propiedad":"valor"},{"propiedad":"valor"}]" ); <br>
 * {@code List<Map<Object,Object>> arregloDatos = classResponse.getMapResponseManyJSON();}</code>
 * */
public class Response {
	
	// Variables para cadenas simples	 
	private String cadenaJSON = "";
	private JSONObject jsonObj = null;
	
	// Variables para un array de datos
	private JSONArray arregloindexcero;
	private ArrayList<Map<Object,Object>> mapAll;
	
	public Response(String responseBody) throws JSONException {
		this.cadenaJSON = responseBody;
		this.jsonObj = new JSONObject(cadenaJSON);
	}
	
	public Response() {}
	
	/**
	 * <p>Este método es utilizao para inicializar la cadena que contiene un arreglo
	 * dentro de una de sus propiedades.</p>
	 * @param String - Es la cadena la cual se van a obtener las propiedades
	 * */
	public void setConfiguration(String responseBody) {
		this.arregloindexcero = new JSONArray(responseBody);
	}
	
	/**
	 * <p>Este método obtiene la cadena en un mapa, previamente usado 
	 * en {@link setConfiguration( String ) }</p>
	 * @return {@code ArrayList<Map<Object,Object>>}
	 * @see #setConfiguration(String)
	 * */
	public List<Map<Object,Object>> getMapResponseManyJSON(){
		this.mapAll = new ArrayList<>();
		this.arregloindexcero.forEach( arrayObject ->{
			JSONObject itemObjetoJSON = (JSONObject) arrayObject;
			Iterator<String> datos = itemObjetoJSON.keys();
			setToMapArray( datos, itemObjetoJSON );
		});		
		return this.mapAll;
	}
	
	private void setToMapArray(Iterator<String> datos, JSONObject jsonObj) {
		Map<Object,Object> mapa = new HashMap<>();
		while ( datos.hasNext() ) {
			String key = datos.next();
			if( jsonObj.get(key).getClass().getTypeName().equals( "org.json.JSONObject" ) ) {
				mapa.put(key, (JSONObject) jsonObj.get(key) );
			}else
				mapa.put(key, jsonObj.get(key) );
		}
		this.mapAll.add(mapa);
	}
	
	public Map<Object,Object> getMapResponseOnlyOne() throws JSONException {
		Map<Object,Object> mapa = new HashMap<>();
		Iterator<String> datos = this.jsonObj.keys();
		return getMapa(mapa,datos);
	}
	
	private Map<Object,Object> getMapa(Map<Object,Object> mapa, Iterator<String> datos) throws JSONException{
		while ( datos.hasNext() ) {
			String key = datos.next();
			if(this.jsonObj.get(key).getClass().getTypeName().equals("org.json.JSONObject")) {
				JSONObject objAltern = (JSONObject) this.jsonObj.get(key);
				mapa.put( key, getAlternate( objAltern ) );
			}else
				mapa.put( key, this.jsonObj.get( key ) );
		}
		return mapa;
	}
	
	private Map<Object,Object> getAlternate(JSONObject objAltern){
		Iterator<String> datosItem = objAltern.keys();
		Map<Object,Object> mapa = new HashMap<>();
		while ( datosItem.hasNext() ) {
			String key = datosItem.next();
			if(objAltern.get(key).getClass().getTypeName().equals("org.json.JSONObject")) {
				JSONObject objAlternSecond = (JSONObject) objAltern.get(key);
				mapa.put( key, getAlternate( objAlternSecond ) );
			}else
				mapa.put(key, objAltern.get(key) );
		}
		return mapa;
	}
	
}
