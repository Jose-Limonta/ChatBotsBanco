package com.bots.bots.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.pmw.tinylog.Logger;

import com.bots.bots.model.Tarjetas;
import com.bots.bots.model.Usuarios;
import com.bots.bots.resources.AccionesAPI;
import com.bots.bots.service.UsuariosService;
import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.*;

//import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class InicioController {
	
	private static final Log LOGGER = LogFactory.getLog(InicioController.class);
	
	@Autowired
	@Qualifier("servicioUsuarios")
	private UsuariosService servicioUsuarios;
	
	private String tarjeta = "";
	
	private int numero_de_tarjetas = 0;
	private int i = 0;
	
	@RequestMapping(method = RequestMethod.GET, value = "/webhook")
    @ResponseBody
    String verifyToken(@RequestParam(value="hub.mode", defaultValue="") String hub_mode, @RequestParam(value="hub.verify_token", defaultValue="") String hub_verify_token, @RequestParam(value="hub.challenge", defaultValue="") String hub_challenge ) throws IOException {

        BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
        platform.getVerifyWebhook().setHubMode(hub_mode);
        platform.getVerifyWebhook().setHubVerifyToken(hub_verify_token);
        platform.getVerifyWebhook().setHubChallenge(hub_challenge);

        if( platform.getVerifyWebhook().challenge() ){
            return ( hub_challenge != "" ) ? hub_challenge : "";
        }

        return "Verification token mismatch";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/webhook")
    @ResponseBody
    String webHook(@RequestBody String body) throws IOException, UnirestException, ParseException {
        BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
        platform.getBaseReceiver().set(body).parse();
        HashMap<String, MessageReceivedWebhook> messages = (HashMap<String, MessageReceivedWebhook>) platform.getBaseReceiver().getMessages();
        for (MessageReceivedWebhook message : messages.values()) {

            String user_id = (message.hasUserId()) ? message.getUserId() : "";
            String page_id = (message.hasPageId()) ? message.getPageId() : "";
            String message_id = (message.hasMessageId()) ? message.getMessageId() : "";
            String message_text = (message.hasMessageText()) ? message.getMessageText() : "";
            String quick_reply_payload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
            //Long timestamp = (message.hasTimestamp()) ? message.getTimestamp() : 0;
            HashMap<String, String> attachments = (message.hasAttachment()) ? (HashMap<String, String>) message.getAttachment() : new HashMap<String, String>();

            Logger.info("User ID#:" + user_id);
            Logger.info("Page ID#:" + page_id);
            Logger.info("Message ID#:" + message_id);
            Logger.info("Message Text#:" + message_text);
            Logger.info("Quick Reply Payload#:" + quick_reply_payload);

            for (String attachment : attachments.values()) {
                Logger.info("Attachment#:" + attachment);
            }

            String text = message.getMessageText();
            MessageTemplate message_tpl = platform.getBaseSender().getMessageTemplate();
            ButtonTemplate button_message_tpl = platform.getBaseSender().getButtonTemplate();
            //ListTemplate list_message_tpl = platform.getBaseSender().getListTemplate();
            //GenericTemplate generic_message_tpl = platform.getBaseSender().getGenericTemplate();
            //ReceiptTemplate receipt_message_tpl = platform.getBaseSender().getReceiptTemplate();

            String cuenta ="0";
            if( text.length() >= 19)
            	cuenta = text.substring(0, text.length() -3);
            
            if( cuenta.length() == 16 
            		&& verifyStringToNumber(cuenta) 
            		&& text.substring(text.length() -3, text.length()).replace(" ", "").contains("ok") ){
            	tarjeta = cuenta;
                message_tpl.setRecipientId(message.getUserId());
                message_tpl.setMessageText("Bueno, la verdad es que ya estás pobre :'(");
                message_tpl.setQuickReply("text", "Si", "guarda_tarjeta_approved_click", "");
                message_tpl.setQuickReply("text", "No", "guarda_tarjeta_denied_click", "");
                platform.getBaseSender().send(message_tpl);
                
            }else if( text.toLowerCase().contains("hola") ){
            	
            	message_tpl.setRecipientId(message.getUserId());
                message_tpl.setMessageText("Hola amigo, ¿en qué puedo ayudarte?");
                message_tpl.setQuickReply("text", "Consulta saldo", "consulta_saldo_click", "");
                message_tpl.setQuickReply("text", "Transferencia", "transferencia_click", "");
                platform.getBaseSender().send(message_tpl);

            }else if( text.toLowerCase().contains("noticia") 
            		|| text.toLowerCase().contains("noticias") ){

                message_tpl.setRecipientId(message.getUserId());
                message_tpl.setAttachment("image", "http://techslides.com/demos/samples/sample.jpg", false);
                message_tpl.setNotificationType("SILENT_PUSH");
                platform.getBaseSender().send(message_tpl);

            }else if( text.toLowerCase().contains("informacion") || text.toLowerCase().contains("información") ){

                message_tpl.setRecipientId(message.getUserId());
                message_tpl.setMessageText("Mira, puedes obtener información en el siguiente documento");
                message_tpl.setAttachment("file", "http://techslides.com/demos/samples/sample.pdf", false);
                message_tpl.setNotificationType("NO_PUSH");
                platform.getBaseSender().send(message_tpl);

            }else if( text.toLowerCase().contains("acción") || text.toLowerCase().contains("accion") ){

                message_tpl.setRecipientId(message.getUserId());
                message_tpl.setMessageText("¿Qué quieres hacer?");
                message_tpl.setQuickReply("text", "Consulta saldo", "consulta_saldo_click", "");
                message_tpl.setQuickReply("text", "Transferencia", "transferencia_click", "");
                platform.getBaseSender().send(message_tpl);

            }else if( text.equals("web_url_button") ){

                button_message_tpl.setRecipientId(message.getUserId());
                button_message_tpl.setMessageText("Click Below!");
                button_message_tpl.setButton("web_url", "Take the Hat Quiz", "https://m.me/petershats?ref=take_quiz", "");
                platform.getBaseSender().send(button_message_tpl);

            }else if( text.equals("phone_number_button") ){

                button_message_tpl.setRecipientId(message.getUserId());
                button_message_tpl.setMessageText("Click Below!");
                button_message_tpl.setButton("phone_number", "Call Representative", "", "+15105551234");
                platform.getBaseSender().send(button_message_tpl);

            }

            seleccionarTarjeta(quick_reply_payload,message_tpl,message, platform, user_id);

            return "ok";
        }
        return "bla";
    }
    
    private void seleccionarTarjeta(String replica, MessageTemplate message_tpl, 
    		MessageReceivedWebhook message, BotPlatform platform, String user_id ) throws UnirestException, ParseException {
    	
    	if( replica.equals("consulta_saldo_click") ){
    		
    		Usuarios user = getUsuarioFromRegister( message.getUserId() );
    		
    		if(user != null) {
    			
    			message_tpl.setRecipientId(message.getUserId());
	            message_tpl.setMessageText("Selecciona una tarjeta");
	            
	            i = 0;
	            numero_de_tarjetas = user.getTarjetasList().size();
	            
    			user.getTarjetasList().forEach( (tarjetas) -> {
    				message_tpl.setQuickReply("text",
    						tarjetas.getNtarjeta() ,
    						"select_banco_banamex_click_" + i,"");
    				i++;
    			});
    			
	            platform.getBaseSender().send(message_tpl);
    		}else {
	    		message_tpl.setRecipientId(message.getUserId());
	            message_tpl.setMessageText("No te tengo en registro, ¿Cuál es tu banco?");
	            message_tpl.setQuickReply("text", "Banamex", "select_banco_banamex_click", "");
	            message_tpl.setQuickReply("text", "Bancomer", "select_banco_bancomer_click", "");
	            platform.getBaseSender().send(message_tpl);
            }

        }else if( replica.equals("transferencia_click") ){

            message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("¿A qué cuenta transfiero?\nEjemplo: 1234567890123456 ok");
            platform.getBaseSender().send(message_tpl);

        }
    	
    	getAccionesBanco(replica, message_tpl, message, platform);
    }
    
    
    private void getAccionesBanco(String replica, MessageTemplate message_tpl, 
    		MessageReceivedWebhook message, BotPlatform platform ) throws UnirestException, ParseException {
    	
    	//numero_de_tarjetas
    	Usuarios user = getUsuarioFromRegister( message.getUserId() );
		
		if(!user.getIduser().isEmpty()) {
			
	    	for(int i = 0 ; i < numero_de_tarjetas; i++) {
	    		
	    		String itemString = "select_banco_banamex_click_"+i;	    		
	    		if(itemString.equals(replica)) {
	    			LOGGER.info("select_banco_banamex_click_" + i + " == " + replica );
	    			tarjeta = user.getTarjetasList().get(i).getNtarjeta();
	    			
	    			message_tpl.setRecipientId(message.getUserId());
	                message_tpl.setMessageText("¡Quieres hacer una transferencia de esta tarjeta? " + user.getTarjetasList().get(i).getNtarjeta());
	                message_tpl.setQuickReply("text", "Si", "guarda_tarjeta_approved_click", "");
	                message_tpl.setQuickReply("text", "No", "guarda_tarjeta_denied_click", "");
	                platform.getBaseSender().send(message_tpl);
	    		}
	    	}
		}else {
	    	if( replica.equals("select_banco_banamex_click") ){
	    		
	    		message_tpl.setRecipientId(message.getUserId());
	            message_tpl.setMessageText("Dame tu número de cuenta Banamex");
	            platform.getBaseSender().send(message_tpl);
	
	        }else if( replica.equals("select_banco_bancomer_click") ){
	    		
	    		message_tpl.setRecipientId(message.getUserId());
	            message_tpl.setMessageText("Dame tu número de cuenta Bancomer");
	            platform.getBaseSender().send(message_tpl);
	
	        }
		}
    	
		guardaDatos(replica, message_tpl,message, platform );
    	
    }
    
    private void guardaDatos(String replica, MessageTemplate message_tpl, 
    		MessageReceivedWebhook message, BotPlatform platform ) throws UnirestException, ParseException {
    	
    	if( replica.equals("guarda_tarjeta_approved_click") ){
    		Usuarios user = getUsuarioFromRegister( message.getUserId() );
    		
    		if(user.getIduser() != "") {
    			
    			boolean insercion = insertaTarjeta( message, user );
	    		message_tpl.setRecipientId(message.getUserId());
	            message_tpl.setMessageText( insercion ? 
	            		"Listo, tu tarjeta fue guardada para proximas transacciones o consultas." : 
	            			"Ups! no pudimos agregar tus datos :'(");
	            platform.getBaseSender().send(message_tpl);
    		}else {
    			Usuarios usuario = insertaUser(getUsuario(message));
    			if(!usuario.getIduser().isEmpty()) {
    				
    				boolean insercion = insertaTarjeta( message, user );
    	    		message_tpl.setRecipientId(message.getUserId());
    	            message_tpl.setMessageText( insercion ? 
    	            		"Listo, tu tarjeta fue guardada para proximas transacciones o consultas." : 
    	            			"Ups! no pudimos agregar tus datos :'(");
    	            platform.getBaseSender().send(message_tpl);
    			}
    		}

        }else if( replica.equals("guarda_tarjeta_denied_click") ){
        	
    		message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("Tu tarjeta no fue guardada");
            platform.getBaseSender().send(message_tpl);

        }
    }
    
    private boolean insertaTarjeta(MessageReceivedWebhook message, Usuarios user) throws ParseException, UnirestException {
    	AccionesAPI accion = new  AccionesAPI();
		Tarjetas objtarjeta = getTarjeta(message);
		
    	boolean inserto = false;
		
		if( !user.getIduser().isEmpty() ) {	    			
			Map<Object,Object> tarjeta_agregada = accion.setTarjeta(objtarjeta);
			if(tarjeta_agregada.get("fecha") != "") 
				inserto = true;	    			
		}
		return inserto;
    }
    
    private Usuarios insertaUser(Usuarios usuario) throws JSONException, UnirestException, ParseException {
    	AccionesAPI accion = new  AccionesAPI();
    	Map<Object,Object> usuario_agregado = accion.setUsuarios(usuario);
    	Usuarios user = accion.convertMapToUsuarios(usuario_agregado, usuario.getIduser());
    	return user;
    }
    
    private boolean verifyStringToNumber(String cuenta) {
		boolean verificador = true;
		for(char caracter : cuenta.toCharArray())	
			if(!Character.isDigit(caracter)) 
				verificador = false;			
		
		return verificador;
    }
    
    private Usuarios getUsuario(MessageReceivedWebhook message) throws ParseException{    	
    	Usuarios user = new Usuarios();
		user.setFecha( getFechaOfStringToDateFromat() );
		user.setIduser(message.getUserId());
		user.setIdpagina(message.getPageId());
		
		return user;
    }
    
    private Tarjetas getTarjeta(MessageReceivedWebhook message) throws ParseException, UnirestException {
    	Usuarios iduser = getUsuarioFromRegister(message.getUserId());
    	Tarjetas objtarjeta = new Tarjetas();
    	if(iduser != null ) {
    		if(!tarjeta.isEmpty()) {
	    		String ttarjeta  = getTipoTarjeta( tarjeta.substring(0, 1) );
	    		
		    	objtarjeta.setFecha( getFechaOfStringToDateFromat() );
		    	objtarjeta.setIduser(iduser);
		    	objtarjeta.setNtarjeta(tarjeta);
		    	objtarjeta.setNbanco("Bancos");
		    	objtarjeta.setTtarjeta(ttarjeta);
		    	
		    	return objtarjeta;
    		}
    	}
    	return objtarjeta;
    }
    
    private String getTipoTarjeta(String tarjeta) {
    	String ttarjeta  = "";
		switch(tarjeta.substring(0, 1)) {
			case "1": ttarjeta = "Aereolinea";
			case "2": ttarjeta = "Aereolinea";
			case "3": ttarjeta = "American Express";
			case "4": ttarjeta = "Visa";
			case "5": ttarjeta = "Master Card";
			case "6": ttarjeta = "Discovery Card";
			case "7": ttarjeta = "Industria del petróleo";
			case "8": ttarjeta = "Telecomunicaciones";
			default: ttarjeta = "No reconocida";
		}
		return ttarjeta;
    }
    
    private Usuarios getUsuarioFromRegister(String clave) throws UnirestException, JSONException, ParseException {
    	AccionesAPI accion = new  AccionesAPI();
    	Map<Object,Object> mapausuario = accion.getClienteByClave(clave);
    	Usuarios usuario = accion.convertMapToUsuarios(mapausuario, clave);
    	return usuario;
    }
    
    private Date getFechaOfStringToDateFromat() throws ParseException{
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);    	
        Date parsed = format.parse( format.format( new Date() ) );
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        return sql;
    }

}