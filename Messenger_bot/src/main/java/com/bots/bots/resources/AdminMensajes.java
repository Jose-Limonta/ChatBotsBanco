package com.bots.bots.resources;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.bots.bots.model.Sesiones;
import com.bots.bots.model.Usuarios;
import com.bots.bots.service.SesionesService;
import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.MessageReceivedWebhook;
import com.clivern.racter.senders.templates.ButtonTemplate;
import com.clivern.racter.senders.templates.MessageTemplate;
import com.mashape.unirest.http.exceptions.UnirestException;

public class AdminMensajes extends AccionesMensajes{
	
	private static final Log LOGGER = LogFactory.getLog(AdminMensajes.class);
	
	private String tarjeta = "";	
	private boolean verificadorInsersion = false;
	private int numeroDeTarjetas = 0;
	private Integer identificador = 0;
	private String[] datostransfer;
	private boolean realizatransfer = false;
	
	private String reply;
	private MessageReceivedWebhook message;
	private BotPlatform platform;
	
	@Autowired
	@Qualifier("servicioSesiones")
	private SesionesService servicioSesiones;
	
	private Sesiones sesion;
	
	
	public void setConfiguration(String reply, MessageReceivedWebhook message, BotPlatform platform) throws Throwable {
		this.reply = reply;
		this.message = message;
		this.platform = platform;
		sesion = new Sesiones();
		if( getSesionesExistentes(message.getUserId()) > 0 ) 
			sesion = servicioSesiones.getSesionById(message.getUserId()).get();
		else {
			sesion.setIdSesion(message.getUserId());
			sesion.setRegistro( ( short ) 0);
			sesion.setFecha(new Date());
			setAddSesionMessageAccion(sesion);
		}
		
		LOGGER.info("Ejecutando => setConfiguration('String, MessageReceivedWebhook, BotPlatform'");
	}
	
	private void setInitializrCredentials(String text) throws Throwable {
		
		
		if(text.split(" ").length == 2) {
			short accionByNonRegisterUser = 0;
			
			if(text.split(" ")[1].length() == 3) {
				accionByNonRegisterUser = 1;
				sesion.setRegistro(accionByNonRegisterUser);
				setEditSesionMessageAccion(sesion);
			}
			
		}else if(text.split(" ").length == 3 && getValidaDatosTransferencia( text ) ) {
			datostransfer = text.split(" ");
			realizatransfer = true;
		}
		
		LOGGER.info("Método: setInitializrCredentials(Stirng) => Variable de sesión: " + sesion.getRegistro() );
	}
	
	public void messagesExecute(String text, MessageTemplate messageTpl, 
			ButtonTemplate buttonMessageTpl, String action) throws Throwable {
		
		setInitializrCredentials(text);
         
		String cuenta ="0";
        if( text.length() >= 19) 
        	cuenta = text.substring(0, text.length() -3);
        
        if( cuenta.length() == 16 && verifyStringToNumber(cuenta) 
        		&& text.substring(text.length() -3, text.length()).replace(" ", "").contains("ok") ){
        	tarjeta = cuenta;
        	messageTpl.setRecipientId(message.getUserId());
        	messageTpl.setMessageText("Escribe el número de tarjeta a transferir, el monto y tu clave de acceso, separado por espacios");
            platform.getBaseSender().send(messageTpl);
            verificadorInsersion = true;
            
        }else if( text.toLowerCase().contains("hola") || 
        		text.toLowerCase().contains("acción") || 
        		text.toLowerCase().contains("accion") ){

        	messageTpl.setRecipientId(message.getUserId());
        	messageTpl.setMessageText("Hola amigo, ¿en qué puedo ayudarte?");
        	messageTpl.setQuickReply("text", "Consulta saldo", "consulta_saldo_click", "");
        	messageTpl.setQuickReply("text", "Transferencia", "transferencia_click", "");
        	messageTpl.setQuickReply("text", "Agregar tarjeta", "add_tarjeta_click", "");
        	messageTpl.setQuickReply("text", "Eliminar tarjeta", "delete_tarjeta_click", "");
            platform.getBaseSender().send(messageTpl);

        }
        
        if(sesion.getRegistro() == 1) 
        	setActionForNonRegisterUsers( messageTpl );        
        
        if( realizatransfer )
        	realizatransfer = false;
        
        if( verificadorInsersion )
			guardaDatos( messageTpl );

        LOGGER.info("\n\nAcciones totales: " + sesion.toString() + "\n\n");
        getChoiceActions( messageTpl, action );
        
	}
	
    private void getChoiceActions(MessageTemplate messageTpl, String action) throws Throwable {
    	if(!action.isEmpty()) {
    		seleccionaTarjeta( messageTpl );
    		if( (sesion.getAccion() == null && !action.equals("") ) ) {
    			sesion.setAccion(action);
    			setEditSesionMessageAccion(sesion);
    		}
    	}
    	
    	LOGGER.info("\n\nAcciones disponibles despues de seleccion: " + sesion.getAccion() + "\n\n");
    	
    	getBankActions( messageTpl );
    }
    
    private void getTarjetaOFRegisterUser(Usuarios user) {
    	for(int i = 0 ; i < numeroDeTarjetas; i++) {	    		
    		String itemString = "select_banco_banamex_click_" + i;	    		
    		if(itemString.equals(reply)) {
    			tarjeta = user.getTarjetasList().get(i).getNtarjeta();
    		}
    	}
    }
    
    private void getTarjetaOFNoRegisterUser(MessageTemplate messageTpl) throws Throwable  {
    	if( reply.equals("select_banco_banamex_click") ){
    		messageTpl.setRecipientId(message.getUserId());
    		messageTpl.setMessageText("Dame tu número de cuenta Banamex y tu clave de acceso separado por espacio");
            sesion.setRegistro( (short ) 1);
            setEditSesionMessageAccion(sesion);
            
            platform.getBaseSender().send(messageTpl);	            

        }else if( reply.equals("select_banco_bancomer_click") ){
        	messageTpl.setRecipientId(message.getUserId());
        	messageTpl.setMessageText("Dame tu número de cuenta Bancomer");
            sesion.setRegistro( (short ) 1);
            setEditSesionMessageAccion(sesion);
            
            platform.getBaseSender().send(messageTpl);

        }	
    }
    
    private void getBankActions(MessageTemplate messageTpl) throws Throwable {    	
    	Usuarios user = getUsuarioFromRegister( message.getUserId() );		
		if(user != null && user.getTarjetasList() != null) {
			getTarjetaOFRegisterUser(user);	    	
		}else {
			getTarjetaOFNoRegisterUser(messageTpl);	    	
		}
    	
    }
    
    private void saveTarjeta(MessageTemplate messageTpl) throws UnirestException {
    	messageTpl.setRecipientId(message.getUserId());
    	messageTpl.setMessageText("¡Quieres guardar esta tarjeta? " + tarjeta);
    	messageTpl.setQuickReply("text", "Si", "guarda_tarjeta_approved_click", "");
    	messageTpl.setQuickReply("text", "No", "guarda_tarjeta_denied_click", "");
        platform.getBaseSender().send(messageTpl);
        verificadorInsersion = true;
    }
    
    private void setActionForNonRegisterUsers(MessageTemplate messageTpl) throws UnirestException {
    	LOGGER.info("setActionForNonRegisterUsers(MessageTemplate) " 
    			+ "if("+sesion.getAccion()+" == 'consulta') " );
		if( sesion.getAccion() != null ) {
			if(sesion.getAccion().equals( "consulta" )) {
	    		Double saldo = setRealizarConsulta();
	    		
	    		messageTpl.setRecipientId(message.getUserId());
	    		messageTpl.setMessageText("Tu saldo es de: " + saldo);
	            platform.getBaseSender().send(messageTpl);
				
	            saveTarjeta( messageTpl );
			} else if(sesion.getAccion().equals( "transferencia" ) && setRealizaTransaccion( datostransfer ) ) {
				messageTpl.setRecipientId(message.getUserId());
				messageTpl.setMessageText("La transferencia se realizó correctamente");
		        platform.getBaseSender().send(messageTpl);
		            
		        saveTarjeta( messageTpl );
			}
    	}
	}

    
    /**
     * <p>Método para guardar datos de una tarjeta sea aprobada la inserción o no, 
     * en caso de que un usuario no se encuentre registrado en la base de datos, 
     * será insertado y luego se le asigna la tarjeta a guardar, el método {@code getUsuarioFromRegister(String)}
     * obtiene el usuario de la base de datos y el método {@code insertaTarjeta(String, Usuarios, String)}
     * y por último, si el usuario no existe se crea con el método {@code insertaUser(Usuario)}</p>
     * @param messageTpl {Type: MessageTemplate}
     * @see MessageTemplate
     * @throws UnirestException
     * @throws ParseException
     * */
    private void guardaDatos(MessageTemplate messageTpl) throws Throwable {
    	if( reply.equals("guarda_tarjeta_approved_click") ){
    		Usuarios user = getUsuarioFromRegister( message.getUserId() );
    		
    		if(!user.getIduser().equals("") ) {    			
    			boolean insercion = insertaTarjeta( message, user, tarjeta );
	    		messageTpl.setRecipientId(message.getUserId());
	            messageTpl.setMessageText( insercion ? 
	            		"Listo, tu tarjeta fue guardada para proximas transacciones o consultas." : 
	            			"Ups! no pudimos agregar tus datos :'(");
	            platform.getBaseSender().send(messageTpl);
	            verificadorInsersion = false;	            
    		}else {
    			Usuarios usuario = insertaUser(getUsuario(message));
    			if(!usuario.getIduser().isEmpty()) {    				
    				boolean insercion = insertaTarjeta( message, user, tarjeta );
    	    		messageTpl.setRecipientId(message.getUserId());
    	            messageTpl.setMessageText( insercion ? 
    	            		"Listo, tu tarjeta fue guardada para proximas transacciones o consultas." : 
    	            			"Ups! no pudimos agregar tus datos :'(");
    	            platform.getBaseSender().send(messageTpl);
    	            verificadorInsersion = false;
    			}
    		}

        }else if( reply.equals("guarda_tarjeta_denied_click") ){        	
    		messageTpl.setRecipientId(message.getUserId());
            messageTpl.setMessageText("Tu tarjeta no fue guardada");
            platform.getBaseSender().send(messageTpl);
            verificadorInsersion = false;
        }
    }
    
    private void seleccionaTarjeta(MessageTemplate messageTpl) throws Throwable {
    	Usuarios user = getUsuarioFromRegister( message.getUserId() );
		
		if(user != null && user.getTarjetasList() != null) {		
			messageTpl.setRecipientId(message.getUserId());
            messageTpl.setMessageText("Selecciona una tarjeta");
            
            numeroDeTarjetas = user.getTarjetasList().size();
            
			user.getTarjetasList().forEach( tarjetas -> {
				messageTpl.setQuickReply("text",
						tarjetas.getNtarjeta() ,
						"select_banco_banamex_click_" + identificador ,"");
				identificador++;
			});
			
            platform.getBaseSender().send(messageTpl);
		}else {
    		messageTpl.setRecipientId(message.getUserId());
            messageTpl.setMessageText("No te tengo en registro, ¿Cuál es tu banco?");
            messageTpl.setQuickReply("text", "Banamex", "select_banco_banamex_click", "");
            messageTpl.setQuickReply("text", "Bancomer", "select_banco_bancomer_click", "");
            platform.getBaseSender().send(messageTpl);
        }
    }
}

