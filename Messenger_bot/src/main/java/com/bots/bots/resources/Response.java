package com.bots.bots.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Response {
	
	private String cadenaJSON = "";
	private JSONObject jsonObj = null;
	
	public Response(String responseBody) throws JSONException {
		this.cadenaJSON = responseBody;
		jsonObj = new JSONObject(cadenaJSON);
	}
	
	public Response() {}
	
	private JSONArray arregloindexcero;
	private ArrayList<Map<Object,Object>> map_all;
	
	public void setConfiguration(String responseBody) {
		arregloindexcero = new JSONArray(responseBody);
	}
	
	public ArrayList<Map<Object,Object>> getMapResponseManyJSON(){
		map_all = new ArrayList<Map<Object,Object>>();
		arregloindexcero.forEach( (arrayObject) ->{
			JSONObject itemObjetoJSON = (JSONObject) arrayObject;
			Iterator<String> datos = itemObjetoJSON.keys();
			setToMapArray( datos, itemObjetoJSON );
		});		
		return map_all;
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
		map_all.add(mapa);
	}
	
	public Map<Object,Object> getMapResponseOnlyOne() throws JSONException {
		Map<Object,Object> mapa = new HashMap<>();
		Iterator<String> datos = jsonObj.keys();
		return getMapa(mapa,datos);
	}
	
	private Map<Object,Object> getMapa(Map<Object,Object> mapa, Iterator<String> datos) throws JSONException{
		while ( datos.hasNext() ) {
			String key = datos.next();
			System.out.println(jsonObj.get(key).getClass().getTypeName());
			if(jsonObj.get(key).getClass().getTypeName().equals("org.json.JSONObject")) {
				mapa.put(key, (JSONObject) jsonObj.get(key) );
			}else
				mapa.put(key, jsonObj.get(key) );
		}
		return mapa;
	}
	
}
