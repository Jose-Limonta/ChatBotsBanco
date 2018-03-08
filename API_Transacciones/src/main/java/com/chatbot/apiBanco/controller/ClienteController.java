package com.chatbot.apiBanco.controller;


import com.chatbot.apiBanco.model.Cliente.ClienteOut;
import com.chatbot.apiBanco.model.Cliente.Cliente;
import com.chatbot.apiBanco.model.Cliente.Range;
import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;

@RestController
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");
    private final Calendar dateGte = Calendar.getInstance();
    private final Calendar dateLte = Calendar.getInstance();

    @RequestMapping(value = "/cliente",  method = RequestMethod.POST)
    @ResponseBody
    public ClienteOut altaCliente(@RequestBody  Cliente input) throws OpenpayServiceException, ServiceUnavailableException {
         Customer response = API.customers().create(input.toCustomer());
         return new ClienteOut(response);
    }

    @RequestMapping(value = "/cliente",  method = RequestMethod.PATCH)
    @ResponseBody
    public ClienteOut actualizaCliente(@RequestBody  Cliente input) throws OpenpayServiceException, ServiceUnavailableException {
        Customer response = input.toCustomer();
        response.setId(input.getId());
        response = API.customers().update(response);
        return new ClienteOut(response);
    }

    @RequestMapping(value = "/cliente/{id}",  method = RequestMethod.GET)
    @ResponseBody
    public Customer getCliente(@PathVariable  String id) throws OpenpayServiceException, ServiceUnavailableException {
        return  API.customers().get(id);
    }

    @RequestMapping(value = "/cliente/{id}",  method = RequestMethod.DELETE)
    @ResponseBody
    public ClienteOut deleteCliente(@PathVariable  String id) throws OpenpayServiceException, ServiceUnavailableException {
        API.customers().delete(id);
        return new ClienteOut();
    }

    @RequestMapping(value = "/clientes",  method = RequestMethod.POST)
    @ResponseBody
    public List<Customer> getClientes(@RequestBody Range dates) throws OpenpayServiceException, ServiceUnavailableException {
        dateGte.set(dates.getInicio().getAnio(), dates.getInicio().getMes(), dates.getInicio().getDia(), dates.getInicio().getHora(), dates.getInicio().getMinuto(), dates.getInicio().getSegundo());
        dateLte.set(dates.getFin().getAnio(), dates.getFin().getMes(), dates.getFin().getDia(), dates.getFin().getHora(), dates.getFin().getMinuto(), dates.getFin().getSegundo());

        SearchParams request = new SearchParams();
        request.creationGte(dateGte.getTime());
        request.creationLte(dateLte.getTime());
        request.offset(0);
        request.limit(100);

        return  API.customers().list(request);
    }
}
