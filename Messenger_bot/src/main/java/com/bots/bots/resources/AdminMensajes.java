package com.bots.bots.resources;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
	private Map<String, Object> headers = new HashMap<>();
	
	private String reply;
	private MessageReceivedWebhook message;
	private BotPlatform platform;
	private MessageTemplate messageTpl;
	
	@Autowired
	@Qualifier("serviciothis.sesiones")
	private SesionesService serviciosesiones;
	
	private Sesiones sesion;
	
	
	public void setConfiguration(String reply, MessageReceivedWebhook message, 
			BotPlatform platform, MessageTemplate messageTpl, 
			ButtonTemplate buttonessageTpl) throws UnirestException {
		this.reply = reply;
		this.message = message;
		this.platform = platform;
		this.messageTpl = messageTpl;
		
		getSessionExist();		
		LOGGER.info("Ejecutando => setConfiguration(String, MessageReceivedWebhook, BotPlatform, MessageTemplate, ButtonTemplate)");
	}
	
	private void getSessionExist() throws UnirestException   {
		short valorInicial = 0;
		this.headers.put("fecha", new Date());
		this.headers.put("registro", new Short(valorInicial));
		
		this.sesion = new Sesiones();
		this.sesion = getSesion( this.message.getUserId(), this.headers );
		
		if( this.sesion.getIdSesion() == null ) {
			this.sesion.setIdSesion(this.message.getUserId());
			this.sesion.setRegistro( ( short ) 0);
			this.sesion.setFecha(new Date());
			this.sesion = setAddSesionMessageAccion(this.sesion, this.headers);
		}
		
		LOGGER.info("Ejecutando => getSessionExist() " + this.sesion.toString());
	}
	
	private void setInitializrCredentials(String text) throws UnirestException {		
		
		if(text.split(" ").length == 2) {
			short accionByNonRegisterUser = 0;
			
			if(text.split(" ")[1].length() == 3) {
				accionByNonRegisterUser = 1;
				this.sesion.setRegistro(accionByNonRegisterUser);
				this.sesion = setEditSesionMessageAccion(this.sesion, this.headers);
			}
			
		}else if(text.split(" ").length == 3 && getValidaDatosTransferencia( text ) ) {
			datostransfer = text.split(" ");
			realizatransfer = true;
		}
		
		LOGGER.info("Método: setInitializrCredentials(Stirng) => Variable de sesión: " + this.sesion.getRegistro() );
	}
	
	public void messagesExecute(String text, String action) throws UnirestException{
		
		setInitializrCredentials(text);
         
		String cuenta ="0";
        if( text.length() >= 19) 
        	cuenta = text.substring(0, text.length() -3);
        
        if( cuenta.length() == 16 && verifyStringToNumber(cuenta) 
        		&& text.substring(text.length() -3, text.length()).replace(" ", "").contains("ok") ){
        	tarjeta = cuenta;
        	this.messageTpl.setRecipientId(this.message.getUserId());
        	this.messageTpl.setMessageText("Escribe el número de tarjeta a transferir, el monto y tu clave de acceso, separado por espacios");
            this.platform.getBaseSender().send(this.messageTpl);
            verificadorInsersion = true;
            
        }else if( text.toLowerCase().contains("hola") || 
        		text.toLowerCase().contains("acción") || 
        		text.toLowerCase().contains("accion") ){

        	this.messageTpl.setRecipientId(this.message.getUserId());
        	this.messageTpl.setMessageText("Hola amigo, ¿en qué puedo ayudarte?");
        	this.messageTpl.setQuickReply("text", "Consulta saldo", "consulta_saldo_click", "");
        	this.messageTpl.setQuickReply("text", "Transferencia", "transferencia_click", "");
        	this.messageTpl.setQuickReply("text", "Agregar tarjeta", "add_tarjeta_click", "");
        	this.messageTpl.setQuickReply("text", "Eliminar tarjeta", "delete_tarjeta_click", "");
            this.platform.getBaseSender().send(this.messageTpl);

        }
        
        if(this.sesion.getRegistro() == 1) 
        	setActionForNonRegisterUsers();        
        
        if( realizatransfer )
        	realizatransfer = false;
        
        if( verificadorInsersion )
			guardaDatos();

        LOGGER.info("\n\nAcciones totales: " + this.sesion.toString() + "\n\n");
        getChoiceActions( action );
        
	}
	
    private void getChoiceActions(String action) throws UnirestException  {
    	if(!action.isEmpty()) {
    		seleccionaTarjeta();
    		if( (this.sesion.getAccion() == null && !action.equals("") ) ) {
    			this.sesion.setAccion(action);
    			this.sesion = setEditSesionMessageAccion(this.sesion, this.headers);
    		}
    	}
    	
    	LOGGER.info("\n\nAcciones disponibles despues de seleccion: " + this.sesion.getAccion() + "\n\n");
    	
    	getBankActions();
    }
    
    private void getTarjetaOFRegisterUser(Usuarios user) {
    	for(int i = 0 ; i < numeroDeTarjetas; i++) {	    		
    		String itemString = "select_banco_banamex_click_" + i;	    		
    		if(itemString.equals(reply)) {
    			tarjeta = user.getTarjetasList().get(i).getNtarjeta();
    		}
    	}
    }
    
    private void getTarjetaOFNoRegisterUser() throws UnirestException   {
    	if( reply.equals("select_banco_banamex_click") ){
    		this.messageTpl.setRecipientId(this.message.getUserId());
    		this.messageTpl.setMessageText("Dame tu número de cuenta Banamex y tu clave de acceso separado por espacio");
            this.sesion.setRegistro( (short ) 1);
            this.sesion = setEditSesionMessageAccion(this.sesion, this.headers);
            
            this.platform.getBaseSender().send(this.messageTpl);	            

        }else if( reply.equals("select_banco_bancomer_click") ){
        	this.messageTpl.setRecipientId(this.message.getUserId());
        	this.messageTpl.setMessageText("Dame tu número de cuenta Bancomer");
            this.sesion.setRegistro( (short ) 1);
            this.sesion = setEditSesionMessageAccion(this.sesion, this.headers);
            
            this.platform.getBaseSender().send(this.messageTpl);

        }	
    }
    
    private void getBankActions() throws UnirestException  {    	
    	Usuarios user = getUsuarioFromRegister( this.message.getUserId() );		
		if(user != null && user.getTarjetasList() != null) {
			getTarjetaOFRegisterUser(user);	    	
		}else {
			getTarjetaOFNoRegisterUser();	    	
		}
    	
    }
    
    private void saveTarjeta() throws UnirestException {
    	this.messageTpl.setRecipientId(this.message.getUserId());
    	this.messageTpl.setMessageText("¡Quieres guardar esta tarjeta? " + tarjeta);
    	this.messageTpl.setQuickReply("text", "Si", "guarda_tarjeta_approved_click", "");
    	this.messageTpl.setQuickReply("text", "No", "guarda_tarjeta_denied_click", "");
        this.platform.getBaseSender().send(this.messageTpl);
        verificadorInsersion = true;
    }
    
    private void setActionForNonRegisterUsers() throws UnirestException {
    	LOGGER.info("setActionForNonRegisterUsers(this.messageTemplate) " 
    			+ "if("+this.sesion.getAccion()+" == 'consulta') " );
		if( this.sesion.getAccion() != null ) {
			if(this.sesion.getAccion().equals( "consulta" )) {
	    		Double saldo = setRealizarConsulta();
	    		
	    		this.messageTpl.setRecipientId(this.message.getUserId());
	    		this.messageTpl.setMessageText("Tu saldo es de: " + saldo);
	            this.platform.getBaseSender().send(this.messageTpl);
				
	            saveTarjeta();
			} else if(this.sesion.getAccion().equals( "transferencia" ) && setRealizaTransaccion( datostransfer ) ) {
				this.messageTpl.setRecipientId(this.message.getUserId());
				this.messageTpl.setMessageText("La transferencia se realizó correctamente");
		        this.platform.getBaseSender().send(this.messageTpl);
		            
		        saveTarjeta();
			}
    	}
	}

    
    /**
     * <p>Método para guardar datos de una tarjeta sea aprobada la inserción o no, 
     * en caso de que un usuario no se encuentre registrado en la base de datos, 
     * será insertado y luego se le asigna la tarjeta a guardar, el método {@code getUsuarioFromRegister(String)}
     * obtiene el usuario de la base de datos y el método {@code insertaTarjeta(String, Usuarios, String)}
     * y por último, si el usuario no existe se crea con el método {@code insertaUser(Usuario)}</p>
     * @param this.messageTpl {Type: this.messageTemplate}
     * @throws Throwable 
     * @see this.messageTemplate
     * @throws UnirestException
     * @throws ParseException
     * */
    private void guardaDatos() throws UnirestException {
    	if( reply.equals("guarda_tarjeta_approved_click") ){
    		Usuarios user = getUsuarioFromRegister( this.message.getUserId() );
    		
    		if( user.getIduser() != null ) {    			
    			boolean insercion = insertaTarjeta( this.message, user, tarjeta );
    			setActionToZero();
	    		this.messageTpl.setRecipientId(this.message.getUserId());
	            this.messageTpl.setMessageText( insercion ? 
	            		"Listo, tu tarjeta fue guardada para proximas transacciones o consultas." : 
	            			"Ups! no pudimos agregar tus datos :'(");
	            this.platform.getBaseSender().send(this.messageTpl);
	            verificadorInsersion = false;
    		}else {
    			Usuarios usuario = insertaUser(getUsuario(this.message));
    			setActionToZero();
    			if(usuario.getIduser() != null) {    				
    				boolean insercion = insertaTarjeta( this.message, usuario, tarjeta );
    	    		this.messageTpl.setRecipientId(this.message.getUserId());
    	            this.messageTpl.setMessageText( insercion ? 
    	            		"Listo, tu tarjeta fue guardada para proximas transacciones o consultas." : 
    	            			"Ups! no pudimos agregar tus datos :'(");
    	            this.platform.getBaseSender().send(this.messageTpl);
    	            verificadorInsersion = false;
    			}
    		}

        }else if( reply.equals("guarda_tarjeta_denied_click") ){
        	setActionToZero();
        	
    		this.messageTpl.setRecipientId(this.message.getUserId());
            this.messageTpl.setMessageText("Tu tarjeta no fue guardada");
            this.platform.getBaseSender().send(this.messageTpl);
            verificadorInsersion = false;
        }
    }
    
    private void setActionToZero() throws UnirestException {
    	this.sesion.setRegistro( (short ) 0);
        this.sesion = setEditSesionMessageAccion(this.sesion, this.headers);
    }
    
    private void seleccionaTarjeta() throws UnirestException{
    	Usuarios user = getUsuarioFromRegister( this.message.getUserId() );
		
		if(user != null && user.getTarjetasList() != null) {		
			this.messageTpl.setRecipientId(this.message.getUserId());
            this.messageTpl.setMessageText("Selecciona una tarjeta");
            
            numeroDeTarjetas = user.getTarjetasList().size();
            
			user.getTarjetasList().forEach( tarjetas -> {
				this.messageTpl.setQuickReply("text",
						tarjetas.getNtarjeta() ,
						"select_banco_banamex_click_" + identificador ,"");
				identificador++;
			});
			
            this.platform.getBaseSender().send(this.messageTpl);
		}else {
    		this.messageTpl.setRecipientId(this.message.getUserId());
            this.messageTpl.setMessageText("No te tengo en registro, ¿Cuál es tu banco?");
            this.messageTpl.setQuickReply("text", "Banamex", "select_banco_banamex_click", "");
            this.messageTpl.setQuickReply("text", "Bancomer", "select_banco_bancomer_click", "");
            this.platform.getBaseSender().send(this.messageTpl);
        }
    }
}

