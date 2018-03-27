package com.bots.bots.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.bots.bots.resources.AdminMensajes;
import com.bots.bots.service.UsuariosService;
import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.*;

//import com.clivern.racter.senders.*;
import com.clivern.racter.senders.templates.*;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class InicioController {
	
	private static final Log LOGGER = LogFactory.getLog(InicioController.class);
		
	@RequestMapping(method = RequestMethod.GET, value = "/webhook")
    @ResponseBody
    String verifyToken(
    		@RequestParam(value="hub.mode", defaultValue="") String hub_mode, 
    		@RequestParam(value="hub.verify_token", defaultValue="") String hub_verify_token, 
    		@RequestParam(value="hub.challenge", defaultValue="") String hub_challenge, HttpServletRequest httpsession ) throws IOException {
		LOGGER.info("Ejecución: verifyToken() => GET");
		
        BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
        platform.getVerifyWebhook().setHubMode(hub_mode);
        platform.getVerifyWebhook().setHubVerifyToken(hub_verify_token);
        platform.getVerifyWebhook().setHubChallenge(hub_challenge);

        if( platform.getVerifyWebhook().challenge() )
            return ( hub_challenge != "" ) ? hub_challenge : "";

        return "Verification token mismatch";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/webhook")
    @ResponseBody
    String webHook(@RequestBody String body, HttpServletRequest httpsession) throws IOException, UnirestException, ParseException {
    	LOGGER.info("Ejecución: webHook() => POST");
    	
        BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
        platform.getBaseReceiver().set(body).parse();
        
        HashMap<String, MessageReceivedWebhook> messages = (HashMap<String, MessageReceivedWebhook>) platform
        		.getBaseReceiver()
        		.getMessages();
        
        for (MessageReceivedWebhook message : messages.values()) {

            String user_id = (message.hasUserId()) ? message.getUserId() : "";
            String page_id = (message.hasPageId()) ? message.getPageId() : "";
            String message_id = (message.hasMessageId()) ? message.getMessageId() : "";
            String message_text = (message.hasMessageText()) ? message.getMessageText() : "";
            String quick_reply_payload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
            
            HashMap<String, String> attachments = (message.hasAttachment()) ? 
            		(HashMap<String, String>) message.getAttachment() : new HashMap<String, String>();

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
            
            AdminMensajes adminmensajes =  new AdminMensajes(quick_reply_payload, message, platform, httpsession);
            adminmensajes.messagesExecute(text, message_tpl, button_message_tpl);

            return "ok";
        }
        
        return "bla";
    }    

}