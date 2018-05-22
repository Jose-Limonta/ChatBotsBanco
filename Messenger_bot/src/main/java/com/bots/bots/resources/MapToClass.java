package com.bots.bots.resources;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MapToClass <T>{
	
	private static final Log LOGGER = LogFactory.getLog(MapToClass.class);
	
	private Map<Object,Object> mapa = new HashMap<>();
	
	public MapToClass(Map<Object,Object> mapa) {
		this.mapa= mapa;
	}
	
	public T getClassOfMap(T classe){
		Class<?> goClass = classe.getClass();
		this.mapa.forEach( ( k, v )->{
			try {
				LOGGER.info(v.getClass().getSimpleName());
				Field attributo = goClass.getDeclaredField( (String) k );				
				attributo.setAccessible(true);
				attributo.set(classe, v);
				
			}catch(Exception ex) {
				ex.getMessage();
			}
		});
		return classe;
	}
	
	/*
	 * 
				if( k.getClass().getTypeName().equals("org.json.JSONObject")) {
					LOGGER.info("Es un arreglo " + k + " => " + v);
				}
	 * */
}
