package com.chatbot.apiBanco.controller;


import com.chatbot.apiBanco.model.client.Client;
import com.chatbot.apiBanco.model.client.ClientOut;
import com.chatbot.apiBanco.model.client.Range;
import com.chatbot.apiBanco.model.database.repository.ClienteRepository;
import com.chatbot.apiBanco.model.database.tables.ClienteLog;

import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.chatbot.apiBanco.model.error.Error;;

import java.util.Calendar;
import java.util.List;

@RestController
public class ClientController {

    private static final Logger log = LoggerFactory.getLogger(ClientController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");
    private final Calendar dateGte = Calendar.getInstance();
    private final Calendar dateLte = Calendar.getInstance();

    @Autowired
	private ClienteRepository crepo;


    @RequestMapping(value = "/v1/client",  method = RequestMethod.POST)
    @ResponseBody
    public ClientOut altaCliente(@RequestBody Client input) throws OpenpayServiceException, ServiceUnavailableException {
        Customer response = API.customers().create(input.toCustomer());
        ClienteLog clog = new ClienteLog(input);
        clog.setToken(response.getId());
        crepo.save(clog);
        return new ClientOut(response);
    }

    @RequestMapping(value = "/v1/client",  method = RequestMethod.PATCH)
    @ResponseBody
    public ClientOut actualizaCliente(@RequestBody Client input) throws OpenpayServiceException, ServiceUnavailableException {
        ClienteLog clog = crepo.findByToken(input.getId());
        clog.setEmail(input.getEmail());
        clog.setNombre(input.getName() + " " + input.getLastName());
        clog.setIdBanco(input.getExternalId());
        crepo.save(clog);

        Customer response = input.toCustomer();
        response.setId(input.getId());
        response = API.customers().update(response);
        return new ClientOut(response);
    }

    @RequestMapping(value = "/v1/client/{id}",  method = RequestMethod.GET)
    @ResponseBody
    public Customer getCliente(@PathVariable  String id) throws OpenpayServiceException, ServiceUnavailableException {
        return  API.customers().get(id);
    }

    @RequestMapping(value = "/v1/client/{id}",  method = RequestMethod.DELETE)
    @ResponseBody
    public ClientOut deleteCliente(@PathVariable  String id) throws OpenpayServiceException, ServiceUnavailableException {
        ClienteLog clog = crepo.findByEmail(API.customers().get(id).getEmail()).get(0);
        crepo.delete(clog);
        API.customers().delete(id);
        return new ClientOut();
    }

    @RequestMapping(value = "/v1/clients",  method = RequestMethod.POST)
    @ResponseBody
    public List<Customer> getClientes(@RequestBody Range dates) throws OpenpayServiceException, ServiceUnavailableException {
        dateGte.set(dates.getInicio().getAnio(), dates.getInicio().getMes(), dates.getInicio().getDia(), dates.getInicio().getHora(), dates.getInicio().getMinuto(), dates.getInicio().getSegundo());
        dateLte.set(dates.getFin().getAnio(), dates.getFin().getMes(), dates.getFin().getDia(), dates.getFin().getHora(), dates.getFin().getMinuto(), dates.getFin().getSegundo());

        SearchParams request = new SearchParams();
        request.creationGte(dateGte.getTime());
        request.creationLte(dateLte.getTime());
        request.offset(0);

        return  API.customers().list(request);
    }

    @ExceptionHandler({ OpenpayServiceException.class })
    @ResponseBody
    public Error handleException(OpenpayServiceException ex) {
        Error e = new Error();
        e.setAdditionalProperty("Source", "Fallo de Operacion Client");
        e.setErrorCode(ex.getErrorCode());
        e.setHttpCode(ex.getHttpCode());
        e.setDescription(ex.getDescription());  
        return e;
    }

    @ExceptionHandler({ ServiceUnavailableException.class })
    @ResponseBody
    public Error handleServiceException(ServiceUnavailableException ex) {
        Error e = new Error();
        e.setAdditionalProperty("Source", "Servicio no disponible");
        e.setAdditionalProperty("Cause", ex.getCause());
        e.setDescription(ex.getMessage() );  
        return e;
    }
}
