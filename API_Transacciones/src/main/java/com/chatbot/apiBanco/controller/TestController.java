package com.chatbot.apiBanco.controller;


import com.chatbot.apiBanco.model.Test;
import com.chatbot.apiBanco.model.test.TestEcho;
import com.chatbot.apiBanco.services.ApiRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    private ApiRequest API = new ApiRequest();

    @RequestMapping(value = "/test",  method = RequestMethod.POST)
    @ResponseBody
    public TestEcho alta(@RequestBody Test input) throws JsonProcessingException {
        //return Api.sendRequest("http://gturnquist-quoters.cfapps.io/api/random",input, Quote.class);
        return API.PostRequest("https://postman-echo.com/post", input, TestEcho.class);
    }


}
