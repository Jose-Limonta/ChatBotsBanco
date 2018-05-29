package com.bots.bots.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bots.bots.resources.AdminMensajes;
import com.clivern.racter.BotPlatform;
import com.clivern.racter.receivers.webhook.*;

import com.clivern.racter.senders.templates.*;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class InicioController {
	@Autowired
	@Qualifier("adminMensajes")
	private AdminMensajes adminmensajes;
		
	@RequestMapping(method = RequestMethod.GET, value = "/webhook")
    @ResponseBody
    String verifyToken(
    		@RequestParam(value="hub.mode", defaultValue="") String hubMode, 
    		@RequestParam(value="hub.verify_token", defaultValue="") String hubVerifyToken, 
    		@RequestParam(value="hub.challenge", defaultValue="") String hubChallenge ) throws IOException {
		
        BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
        platform.getVerifyWebhook().setHubMode(hubMode);
        platform.getVerifyWebhook().setHubVerifyToken(hubVerifyToken);
        platform.getVerifyWebhook().setHubChallenge(hubChallenge);

        if( platform.getVerifyWebhook().challenge() )
            return ( hubChallenge != "" ) ? hubChallenge : "";

        return "Verification token mismatch";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/webhook")
    @ResponseBody
    String webHook(@RequestBody String body) throws IOException, UnirestException  {
    	
        BotPlatform platform = new BotPlatform("src/main/java/resources/config.properties");
        platform.getBaseReceiver().set(body).parse();
        
        HashMap<String, MessageReceivedWebhook> messages = (HashMap<String, MessageReceivedWebhook>) platform
        		.getBaseReceiver()
        		.getMessages();
        
        for (MessageReceivedWebhook message : messages.values()) {
            String quickReplyPayload = (message.hasQuickReplyPayload()) ? message.getQuickReplyPayload() : "";
            
            String text = message.getMessageText();
            MessageTemplate messageTpl = platform.getBaseSender().getMessageTemplate();
            ButtonTemplate buttonMessageTpl = platform.getBaseSender().getButtonTemplate();
            String action = "";
            
            switch(quickReplyPayload) {
            	case "consulta_saldo_click": action = "consulta"; break;
            	case "transferencia_click": action = "transferencia"; break;
            	default: action = "";
            }
            
            adminmensajes.setConfiguration(quickReplyPayload, message, platform, messageTpl, buttonMessageTpl);
            adminmensajes.messagesExecute(text, action);

        }
        
        return "bla";
    }    

}