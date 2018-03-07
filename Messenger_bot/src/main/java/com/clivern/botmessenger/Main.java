package com.clivern.botmessenger;

import static spark.Spark.*;
import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws IOException
    {
    	get("/webhook", (request, response) -> {
            BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
            platform.getVerifyWebhook().setHubMode(( request.queryParams("hub.mode") != null ) ? request.queryParams("hub.mode") : "");
            platform.getVerifyWebhook().setHubVerifyToken(( request.queryParams("hub.verify_token") != null ) ? request.queryParams("hub.verify_token") : "");
            platform.getVerifyWebhook().setHubChallenge(( request.queryParams("hub.challenge") != null ) ? request.queryParams("hub.challenge") : "");

            if( platform.getVerifyWebhook().challenge() ){
                platform.finish();
                response.status(200);
                return ( request.queryParams("hub.challenge") != null ) ? request.queryParams("hub.challenge") : "";
            }

            platform.finish();
            response.status(403);
            return "Falló la verificación";
        });

        post("/webhook", (request, response) -> {
            String body = request.body();
            BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
            platform.getBaseReceiver().set(body).parse();
            HashMap<String, MessageReceivedWebhook> messages = (HashMap<String, MessageReceivedWebhook>) platform.getBaseReceiver().getMessages();
            for (MessageReceivedWebhook message : messages.values()) {

                String user_id = (message.hasUserId()) ? message.getUserId() : "";
                String page_id = (message.hasPageId()) ? message.getPageId() : "";
                String message_id = (message.hasMessageId()) ? message.getMessageId() : "";
                String message_text = (message.hasMessageText()) ? message.getMessageText() : "";
                String quick_reply_payload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
                Long timestamp = (message.hasTimestamp()) ? message.getTimestamp() : 0;
                HashMap<String, String> attachments = (message.hasAttachment()) ? (HashMap<String, String>) message.getAttachment() : new HashMap<String, String>();

                // valores traidos la enviar mensaje (no es necesario)
                platform.getLogger().info("User ID#:" + user_id);
                platform.getLogger().info("Page ID#:" + page_id);
                platform.getLogger().info("Message ID#:" + message_id);
                platform.getLogger().info("Message Text#:" + message_text);
                platform.getLogger().info("Quick Reply Payload#:" + quick_reply_payload);

                for (String attachment : attachments.values()) {
                    platform.getLogger().info("Attachment#:" + attachment);
                }

                String text = message.getMessageText();
                MessageTemplate message_tpl = platform.getBaseSender().getMessageTemplate();
                ButtonTemplate button_message_tpl = platform.getBaseSender().getButtonTemplate();
                ListTemplate list_message_tpl = platform.getBaseSender().getListTemplate();
                GenericTemplate generic_message_tpl = platform.getBaseSender().getGenericTemplate();
                ReceiptTemplate receipt_message_tpl = platform.getBaseSender().getReceiptTemplate();

                
                String cuenta = text.substring(0, text.length() -3);
                
                if( cuenta.length() == 16 
                		&& verifyStringToNumber(cuenta) 
                		&& text.substring(text.length() -3, text.length()).replace(" ", "").contains("ok") ){
                	
                	message_tpl.setRecipientId(message.getUserId());
                    message_tpl.setMessageText("Tienes mucho saldo jejeje");
                    message_tpl.setMessageText("Bueno, la verdad es que ya estás pobre :v");
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

                accionesReplica(quick_reply_payload,message_tpl,message, platform);

                return "ok";
            }
            return "bla";
        });
        
    }
    
    private static void accionesReplica(String replica, MessageTemplate message_tpl, 
    		MessageReceivedWebhook message, BotPlatform platform ) throws UnirestException {
    	
    	if( replica.equals("consulta_saldo_click") ){
    		
    		message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("No te tengo en registro, ¿Cuál es tu banco?");
            message_tpl.setQuickReply("text", "Banamex", "select_banco_banamex_click", "");
            message_tpl.setQuickReply("text", "Bancomer", "select_banco_bancomer_click", "");
            platform.getBaseSender().send(message_tpl);

        }else if( replica.equals("transferencia_click") ){

            message_tpl.setRecipientId(message.getUserId());
            message_tpl.setMessageText("¿A qué cuenta transfiero?\nEjemplo: 1234567890123456 ok");
            platform.getBaseSender().send(message_tpl);

        }
    	
    	getNombreBanco(replica, message_tpl, message, platform);
    }
    
    private static void getNombreBanco(String replica, MessageTemplate message_tpl, 
    		MessageReceivedWebhook message, BotPlatform platform ) throws UnirestException {
    	
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
    
    private static boolean verifyStringToNumber(String cuenta) {
		boolean verificador = true;
		for(char caracter : cuenta.toCharArray())	
			if(!Character.isDigit(caracter)) 
				verificador = false;			
		
		return verificador;
    }
    
}