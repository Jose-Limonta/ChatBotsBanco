package com.bots.bots.resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	
}
