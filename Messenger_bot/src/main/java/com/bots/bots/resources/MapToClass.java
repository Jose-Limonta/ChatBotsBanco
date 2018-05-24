package com.bots.bots.resources;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MapToClass <T>{
	
	private static final Log LOGGER = LogFactory.getLog(MapToClass.class);
	
	private Map<Object,Object> mapa;
	private Map<String,Object> headers;
	private T classeOf;
	private Class<?> goClass;
	private Object objeto = new Object();
	
	public MapToClass(Map<Object,Object> mapa, Map<String,Object> headers) {
		this.mapa = mapa;
		this.headers = headers;
	}
	
	public T getClassOfMap(T classe){
		this.classeOf = classe;
		this.goClass = this.classeOf.getClass();
		
		this.mapa.forEach( ( k, v )->{
			try {
				//headers.put("fecha", Date.class);
				if(headers.containsKey( k )) {
					if( headers.get(k).getClass().getSimpleName().equals("Date") ) {
						Date fecha = convertStringToDate( (String) v);
						setValueToField( ( String ) k, fecha);
					}else {
						if(v.getClass().getSimpleName().equals( "JSONArray" ) ){
							Response classResponse = new Response();
							LOGGER.info("Arreglo en String: " +v);
							classResponse.setConfiguration( (String) v );
							getKey(k);
							ArrayList<Map<Object,Object>> arregloDatos = classResponse.getMapResponseManyJSON();
							arregloDatos.forEach( item ->{
								item.forEach( ( key, value ) ->{
									LOGGER.info(key+ " =>" +value);
								});
							});
							ArrayList<T> arrayClass = getClass(arregloDatos );
							arrayClass.forEach( item ->{
								LOGGER.info(item.toString());
							});
							setValueToField( ( String ) k, arrayClass);
						}
					}
				}else 
					setValueToField( ( String ) k, v);
				
			}catch(Exception ex) {
				ex.getMessage();
			}
		});
		return this.classeOf;
	}
	
	private void getKey(Object key) {
		this.headers.forEach( ( k, v ) -> {
			if(k.equals(key)) this.objeto = v;
		});
	}
	
	private void setValueToField(String key, Object value) {
		try {
			Field attributo = this.goClass.getDeclaredField( (String) key );				
			attributo.setAccessible(true);
			attributo.set(this.classeOf, value);
		}catch(Exception ex) {
			ex.getMessage();
		}
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<T> getClass(ArrayList<Map<Object,Object>> mapaNewClass) {
		ArrayList<T> listaDeClasesGenericas = new ArrayList<>();
		mapaNewClass.forEach( item->{
			try {
				T classe = (T) this.objeto.getClass().newInstance();
				Class<?> goClass = classe.getClass();
				
				item.forEach( ( k, v )->{
					try {
						Field attributo = goClass.getDeclaredField( (String) k );				
						attributo.setAccessible(true);
						attributo.set(classe, v);				
					}catch(Exception ex) {
						ex.getMessage();
					}
				});
				
				listaDeClasesGenericas.add(classe);
			}catch(Exception ex) {
				ex.getMessage();
			}
		});
		
		return listaDeClasesGenericas;
	}
	
	private Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = format.parse(dateString);
        return new java.sql.Date( parsed.getTime() );
    }	
	
}
