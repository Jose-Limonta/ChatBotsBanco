package com.chatbot.apiBanco.controller;

import com.chatbot.apiBanco.model.client.Range;
import com.chatbot.apiBanco.model.database.repository.ClienteRepository;
import com.chatbot.apiBanco.model.database.repository.TarjetaRepository;
import com.chatbot.apiBanco.model.database.tables.TarjetaLog;
import com.chatbot.apiBanco.model.card.Cardjs;
import mx.openpay.client.Card;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import mx.openpay.client.utils.SearchParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.chatbot.apiBanco.model.error.Error;


import java.util.Calendar;
import java.util.List;

@RestController
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");
    private final Calendar dateGte = Calendar.getInstance();
    private final Calendar dateLte = Calendar.getInstance();

    @Autowired
    private TarjetaRepository tarjetaRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @RequestMapping(value = "/card",  method = RequestMethod.POST)
    @ResponseBody
    public Card creaTarjeta(@RequestBody Cardjs input) throws OpenpayServiceException, ServiceUnavailableException {

        Card response = API.cards().create(input.getCustomerId(), input.toCard());
        TarjetaLog tlog = new TarjetaLog(response);
        tlog.setCliente(clienteRepo.findByToken(input.getCustomerId()).getId());
        return response;
    }


    @RequestMapping(value = "/card/{customerId}/{id}",  method = RequestMethod.GET)
    @ResponseBody
    public Card getTarjeta(@PathVariable  String id, @PathVariable String customerId) throws OpenpayServiceException, ServiceUnavailableException {

        Card card = API.cards().get(customerId, id);
        return card;
    }

    @RequestMapping(value = "/card/{customerId}/{id}",  method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteTarjeta(@PathVariable  String id, @PathVariable String customerId) throws OpenpayServiceException, ServiceUnavailableException {

        API.cards().delete(customerId ,id);
        return true ;
    }

    @RequestMapping(value = "/cards",  method = RequestMethod.POST)
    @ResponseBody
    public List<Card>  getTarjetas(@RequestBody Range dates) throws OpenpayServiceException, ServiceUnavailableException {
        dateGte.set(dates.getInicio().getAnio(), dates.getInicio().getMes(), dates.getInicio().getDia(), dates.getInicio().getHora(), dates.getInicio().getMinuto(), dates.getInicio().getSegundo());
        dateLte.set(dates.getFin().getAnio(), dates.getFin().getMes(), dates.getFin().getDia(), dates.getFin().getHora(), dates.getFin().getMinuto(), dates.getFin().getSegundo());

        SearchParams request = new SearchParams();
        request.creationGte(dateGte.getTime());
        request.creationLte(dateLte.getTime());
        request.offset(0);

        return  API.cards().list(dates.getCustomerId(), request);
    }

    @ExceptionHandler({ OpenpayServiceException.class })
    @ResponseBody
    public Error handleException(OpenpayServiceException ex) {
        Error e = new Error();
        e.setAdditionalProperty("Source", "Fallo de Operacion TarjetaLog");
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
