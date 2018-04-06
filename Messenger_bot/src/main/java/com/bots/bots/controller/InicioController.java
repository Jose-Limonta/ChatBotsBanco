package com.bots.bots.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bots.bots.resources.AdminMensajes;
import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.templates.*;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class InicioController {
	private AdminMensajes adminmensajes =  new AdminMensajes();
	
	private static final Log LOGGER = LogFactory.getLog(InicioController.class);
		
	@RequestMapping(method = RequestMethod.GET, value = "/webhook")
    @ResponseBody
    String verifyToken(
    		@RequestParam(value="hub.mode", defaultValue="") String hub_mode, 
    		@RequestParam(value="hub.verify_token", defaultValue="") String hub_verify_token, 
    		@RequestParam(value="hub.challenge", defaultValue="") String hub_challenge ) throws IOException {
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
    String webHook(@RequestBody String body) throws IOException, UnirestException, ParseException {
    	LOGGER.info("Ejecución: webHook() => POST");
    	
        BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
        platform.getBaseReceiver().set(body).parse();
        
        HashMap<String, MessageReceivedWebhook> messages = (HashMap<String, MessageReceivedWebhook>) platform
        		.getBaseReceiver()
        		.getMessages();
        
        for (MessageReceivedWebhook message : messages.values()) {
            String quick_reply_payload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
            
            String text = message.getMessageText();
            MessageTemplate message_tpl = platform.getBaseSender().getMessageTemplate();
            ButtonTemplate button_message_tpl = platform.getBaseSender().getButtonTemplate();
            String action = "";
            
            switch(quick_reply_payload) {
            	case "consulta_saldo_click": action = "consulta"; break;
            	case "transferencia_click": action = "transferencia"; break;
            }
            
            adminmensajes.setConfiguration(quick_reply_payload, message, platform);
            adminmensajes.messagesExecute(text, message_tpl, button_message_tpl, action);

            return "ok";
        }
        
        return "bla";
    }    

}