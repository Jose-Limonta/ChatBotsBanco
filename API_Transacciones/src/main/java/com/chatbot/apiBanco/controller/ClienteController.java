package com.chatbot.apiBanco.controller;


import com.chatbot.apiBanco.model.Cliente.ClienteOut;
import com.chatbot.apiBanco.model.cliente.Cliente;
import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");

    @RequestMapping(value = "/cliente/crea",  method = RequestMethod.POST)
    @ResponseBody
    public ClienteOut alta(@RequestBody  Cliente input) throws OpenpayServiceException, ServiceUnavailableException {
         Customer response = API.customers().create(input.toCustomer());
         return new ClienteOut(response);
    }

    @RequestMapping(value = "/cliente/actualiza",  method = RequestMethod.POST)
    @ResponseBody
    public ClienteOut actualiza(@RequestBody  Cliente input) throws OpenpayServiceException, ServiceUnavailableException {
        Customer response = API.customers().update(input.toCustomer());
        return new ClienteOut(response);
    }
}
