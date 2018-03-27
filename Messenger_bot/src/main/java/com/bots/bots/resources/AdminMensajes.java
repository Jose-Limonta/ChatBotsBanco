package com.bots.bots.resources;

import java.text.ParseException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

import com.bots.bots.model.Usuarios;
import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.MessageReceivedWebhook;
import com.clivern.racter.senders.templates.ButtonTemplate;
import com.clivern.racter.senders.templates.MessageTemplate;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AdminMensajes extends AccionesMensajes{
	
	private HttpSession httpsession;
	
	private static final Log LOGGER = LogFactory.getLog(AdminMensajes.class);
	
	private boolean accion_by_non_register_user = false;
	
	private String tarjeta = "";	
	private boolean verificador_insersion = false;
	private int numero_de_tarjetas = 0;
	private int i = 0;
	private String[] datostransfer;
	private boolean realizatransfer = false;
	
	private String reply;
	private MessageReceivedWebhook message;
	private BotPlatform platform;
	
	public AdminMensajes(String reply, MessageReceivedWebhook message, BotPlatform platform, HttpServletRequest request) {
		this.reply = reply;
		this.message = message;
		this.platform = platform;
		if(!request.getSession().getAttributeNames().hasMoreElements())
			this.httpsession = request.getSession();
	}
	
	public void messagesExecute(String text, MessageTemplate message_tpl, 
			ButtonTemplate button_message_tpl) throws UnirestException, ParseException {
		Enumeration<String> sesion = httpsession.getAttributeNames();
		while(sesion.hasMoreElements()) {
			LOGGER.info("Elementos next: " + sesion.nextElement() );
		}
		
		if(httpsession.getAttribute("accion_transferencia_consulta") == null) 
			httpsession.setAttribute("accion_transferencia_consulta","");
		
		if(httpsession.getAttribute("accion_by_non_register_user") == null) 
			httpsession.setAttribute("accion_by_non_register_user", accion_by_non_register_user);
		
		if(text.split(" ").length == 2) {			
			if(text.split(" ")[1].length() == 3)
				accion_by_non_register_user = true;
			
			httpsession.setAttribute("accion_by_non_register_user", accion_by_non_register_user);
		}else if(text.split(" ").length == 3) {
			if( getValidaDatosTransferencia( text ) ) {
				datostransfer = text.split(" ");
				realizatransfer = true;
			}
		}
         
		String cuenta ="0";
        if( text.length() >= 19) 
        	cuenta = text.substring(0, text.length() -3);
        
        if( cuenta.length() == 16 && verifyStringToNumber(cuenta) 
        		&& text.substring(text.length() -3, text.length()).replace(" ", "").contains("ok") ){
        	tarjeta = cuenta;
        	message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("Escribe el número de tarjeta a transferir, el monto y tu clave de acceso, separado por espacios");
            platform.getBaseSender().send(message_tpl);
            verificador_insersion = true;
            
        }else if( text.toLowerCase().contains("hola") ){
        	
        	message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("Hola amigo, ¿en qué puedo ayudarte?");
            message_tpl.setQuickReply("text", "Consulta saldo", "consulta_saldo_click", "");
            message_tpl.setQuickReply("text", "Transferencia", "transferencia_click", "");
            message_tpl.setQuickReply("text", "Agregar tarjeta", "add_tarjeta_click", "");
            message_tpl.setQuickReply("text", "Eliminar tarjeta", "delete_tarjeta_click", "");
            platform.getBaseSender().send(message_tpl);

        }else if( text.toLowerCase().contains("acción") || text.toLowerCase().contains("accion") ){

            message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("Hola amigo, ¿en qué puedo ayudarte?");
            message_tpl.setQuickReply("text", "Consulta saldo", "consulta_saldo_click", "");
            message_tpl.setQuickReply("text", "Transferencia", "transferencia_click", "");
            message_tpl.setQuickReply("text", "Agregar tarjeta", "add_tarjeta_click", "");
            message_tpl.setQuickReply("text", "Eliminar tarjeta", "delete_tarjeta_click", "");
            platform.getBaseSender().send(message_tpl);

        }/*else if( text.toLowerCase().contains("salir") || text.toLowerCase().contains("exit") ){
        	if(httpsession != null)
        		httpsession.invalidate();
        	
        	message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("Tu sesión se invalidó correctamente, hasta la próxima :)");
            platform.getBaseSender().send(message_tpl);
        }*/
        
        if((boolean) httpsession.getAttribute("accion_by_non_register_user")) 
        	setActionForNonRegisterUsers( message_tpl );        
        
        if( realizatransfer )
        	realizatransfer = false;
        
        if( verificador_insersion )
			guardaDatos( message_tpl );

        getChoiceActions( message_tpl );
	}
	
    private void getChoiceActions(MessageTemplate message_tpl) throws UnirestException, ParseException {
    	
    	if( reply.equals("consulta_saldo_click") ){    		
    		seleccionaTarjeta( message_tpl );
    		httpsession.setAttribute("accion_transferencia_consulta", "consulta");

        }else if( reply.equals("transferencia_click") ){
        	seleccionaTarjeta( message_tpl );
        	httpsession.setAttribute("accion_transferencia_consulta", "transferencia");
        }
    	
    	getBankActions( message_tpl );
    }
    
    
    private void getBankActions(MessageTemplate message_tpl) throws UnirestException, ParseException {    	
    	Usuarios user = getUsuarioFromRegister( message.getUserId() );
		
		if(user != null && user.getTarjetasList() != null) {
			
	    	for(int i = 0 ; i < numero_de_tarjetas; i++) {	    		
	    		String itemString = "select_banco_banamex_click_"+i;	    		
	    		if(itemString.equals(reply)) {
	    			LOGGER.info("select_banco_banamex_click_" + i + " == " + reply );
	    			tarjeta = user.getTarjetasList().get(i).getNtarjeta();
	    			
	    			if(httpsession.getAttribute("accion_transferencia_consulta") == "consulta") {
	    				httpsession.setAttribute("accion_transferencia_consulta", "");
	    			}else if(httpsession.getAttribute("accion_transferencia_consulta") == "transferencia") {
	    				httpsession.setAttribute("accion_transferencia_consulta", "");
	    			}
	    			
	    		}
	    	}
		}else {
			
			if( reply.equals("select_banco_banamex_click") ){
	    		message_tpl.setRecipientId(message.getUserId());
	            message_tpl.setMessageText("Dame tu número de cuenta Banamex y tu clave de acceso separado por espacio");
	            accion_by_non_register_user = true;
	            
	            platform.getBaseSender().send(message_tpl);	            

	        }else if( reply.equals("select_banco_bancomer_click") ){
	    		message_tpl.setRecipientId(message.getUserId());
	            message_tpl.setMessageText("Dame tu número de cuenta Bancomer");
	            accion_by_non_register_user = true;
	            
	            platform.getBaseSender().send(message_tpl);

	        }			
	    	
		}
    	
    }
    
    private void saveTarjeta(MessageTemplate message_tpl) throws UnirestException {
    	message_tpl.setRecipientId(message.getUserId());
        message_tpl.setMessageText("¡Quieres guardar esta tarjeta? " + tarjeta);
        message_tpl.setQuickReply("text", "Si", "guarda_tarjeta_approved_click", "");
        message_tpl.setQuickReply("text", "No", "guarda_tarjeta_denied_click", "");
        platform.getBaseSender().send(message_tpl);
        verificador_insersion = true;
    }
    
    private void setActionForNonRegisterUsers(MessageTemplate message_tpl) throws UnirestException {
		if(httpsession.getAttribute("accion_transferencia_consulta") == "consulta") {
    		Double saldo = setRealizarConsulta();
    		
    		message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("Tu saldo es de: " + saldo);
            platform.getBaseSender().send(message_tpl);
            
            saveTarjeta( message_tpl );
            
            httpsession.setAttribute("accion_transferencia_consulta","");
		}else if(httpsession.getAttribute("accion_transferencia_consulta") == "transferencia") {
			if( setRealizaTransaccion( datostransfer ) ) {
				message_tpl.setRecipientId(message.getUserId());
	            message_tpl.setMessageText("La transferencia se realizó correctamente");
	            platform.getBaseSender().send(message_tpl);
	            
	            saveTarjeta( message_tpl );
			}
			httpsession.setAttribute("accion_transferencia_consulta","");
		}
	}

    
    /**
     * <p>Método para guardar datos de una tarjeta sea aprobada la inserción o no, 
     * en caso de que un usuario no se encuentre registrado en la base de datos, 
     * será insertado y luego se le asigna la tarjeta a guardar, el método {@code getUsuarioFromRegister(String)}
     * obtiene el usuario de la base de datos y el método {@code insertaTarjeta(String, Usuarios, String)}
     * y por último, si el usuario no existe se crea con el método {@code insertaUser(Usuario)}</p>
     * @param message_tpl {Type: MessageTemplate}
     * @see MessageTemplate
     * @throws UnirestException
     * @throws ParseException
     * */
    private void guardaDatos(MessageTemplate message_tpl) throws UnirestException, ParseException {
    	if( reply.equals("guarda_tarjeta_approved_click") ){
    		Usuarios user = getUsuarioFromRegister( message.getUserId() );
    		
    		if(user.getIduser() != "") {    			
    			boolean insercion = insertaTarjeta( message, user, tarjeta );
	    		message_tpl.setRecipientId(message.getUserId());
	            message_tpl.setMessageText( insercion ? 
	            		"Listo, tu tarjeta fue guardada para proximas transacciones o consultas." : 
	            			"Ups! no pudimos agregar tus datos :'(");
	            platform.getBaseSender().send(message_tpl);
	            verificador_insersion = false;	            
    		}else {
    			Usuarios usuario = insertaUser(getUsuario(message));
    			if(!usuario.getIduser().isEmpty()) {    				
    				boolean insercion = insertaTarjeta( message, user, tarjeta );
    	    		message_tpl.setRecipientId(message.getUserId());
    	            message_tpl.setMessageText( insercion ? 
    	            		"Listo, tu tarjeta fue guardada para proximas transacciones o consultas." : 
    	            			"Ups! no pudimos agregar tus datos :'(");
    	            platform.getBaseSender().send(message_tpl);
    	            verificador_insersion = false;
    			}
    		}

        }else if( reply.equals("guarda_tarjeta_denied_click") ){
        	
    		message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("Tu tarjeta no fue guardada");
            platform.getBaseSender().send(message_tpl);
            verificador_insersion = false;

        }
    }
    
    private void seleccionaTarjeta(MessageTemplate message_tpl) throws JSONException, UnirestException, ParseException {
    	Usuarios user = getUsuarioFromRegister( message.getUserId() );
		
		if(user != null && user.getTarjetasList() != null) {		
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
    }
}

