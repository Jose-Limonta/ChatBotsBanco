package com.bots.bots.resources;

//import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public class Response {
	
	private String cadena_json = "";
	private JSONObject jsonObj = null;
	
	public Response(String response_body) {
		this.cadena_json = response_body;
		jsonObj = new JSONObject(cadena_json);
	}
	
	public JSONObject getJSONObject() {
		return jsonObj;
	}
	
	public Map<Object,Object> getMapResponse() {
		Map<Object,Object> mapa = new HashMap<Object,Object>();
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
	
	/*public <T> T convertToClass(T klass) throws IllegalArgumentException, IllegalAccessException {
		Map<Object,Object> mapa = getMapResponse();
		for (Field field : klass.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if(mapa.containsKey(field.getName())) {
				field.set(klass.getClass(), mapa.get( field.getName() ) );
			}
		}
		return klass;	
	}*/
	
}
