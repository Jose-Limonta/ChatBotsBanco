package com.bots.bots.resources;

import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Resources {
	
	private static final Log LOGGER = LogFactory.getLog(Resources.class);
	
	private Resources() {}
	
	public static Date convertStringToDate(String dateString, String parseFormat) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat( parseFormat );
        Date parsed = format.parse( dateString );
        return new java.sql.Date( parsed.getTime() );
    }
	
	public static Date getFechaOfStringToDateFromat(){    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);    	
        Date parsed = new Date();
		try {
			parsed = format.parse( format.format( new Date() ) );
		} catch (ParseException e) {
			LOGGER.error( e.getMessage() );
		}
        return new java.sql.Date(parsed.getTime());
    }
	
	public static Short stringToShort(String value) {
    	return Short.valueOf(value);
    }
    
	public static String integerToString(Integer value) {
    	return String.valueOf(value);
    }
	
	public static boolean verifyStringToDecimal(Object content) {
		return content instanceof Double || content instanceof Long ? true :  false;
	}
    
	public static boolean verifyStringToNumber(String cuenta) {
    	LOGGER.info("Ejecucion: verifyStringToNumber(String)");
    	
		boolean verificador = true;
		for(char caracter : cuenta.toCharArray())	
			if(!Character.isDigit(caracter)) 
				verificador = false;
		
		return verificador;
    }
	
	public static String getEncrypt(String numtarjeta, String iduser) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        key = new SecretKeySpec( iduser.getBytes(),  0, 16, "AES");
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
        aes.init(Cipher.ENCRYPT_MODE, key);
        byte[] encriptado = aes.doFinal( numtarjeta.getBytes() );
        StringBuilder buffer = new StringBuilder();
        for (byte b : encriptado) {
        	buffer.append(Integer.toHexString(0xFF & b));
        }
        return buffer.toString();
	}
	
	public static String getDEncrypt(byte[] encriptado, String idUser) throws Exception{
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        key = new SecretKeySpec( idUser.getBytes(),  0, 16, "AES");
        Cipher aes = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
		aes.init(Cipher.DECRYPT_MODE, key);
		byte[] desencriptado = aes.doFinal(encriptado);
		return new String(desencriptado);
	}
	
}
