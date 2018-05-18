package com.bots.bots.resources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public class Response {
	
	private String cadenaJSON = "";
	private JSONObject jsonObj = null;
	
	public Response(String responseBody) {
		this.cadenaJSON = responseBody;
		jsonObj = new JSONObject(cadenaJSON);
	}
	
	public JSONObject getJSONObject() {
		return jsonObj;
	}
	
	public Map<Object,Object> getMapResponse() {
		Map<Object,Object> mapa = new HashMap<>();
		Iterator<String> datos = jsonObj.keys();
		return getMapa(mapa,datos);
	}
	
	private Map<Object,Object> getMapa(Map<Object,Object> mapa, Iterator<String> datos){
		while ( datos.hasNext() ) {
			String key = datos.next();
			if(jsonObj.get(key).getClass().getTypeName() == "org.json.JSONObject") {
				mapa.put(key, (JSONObject) jsonObj.get(key) );
			}else
				mapa.put(key, jsonObj.get(key) );
		}
		return mapa;
	}
	
}
