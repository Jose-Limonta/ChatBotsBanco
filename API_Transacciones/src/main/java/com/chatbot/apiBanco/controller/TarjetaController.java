package com.chatbot.apiBanco.controller;

import com.chatbot.apiBanco.model.cliente.ClienteOut;
import com.chatbot.apiBanco.model.cliente.Range;
import com.chatbot.apiBanco.model.tarjeta.Tarjeta;
import mx.openpay.client.Card;
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
public class TarjetaController {

    private static final Logger log = LoggerFactory.getLogger(TarjetaController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");
    private final Calendar dateGte = Calendar.getInstance();
    private final Calendar dateLte = Calendar.getInstance();

    @RequestMapping(value = "/tarjeta",  method = RequestMethod.POST)
    @ResponseBody
    public Card creaTarjeta(@RequestBody Tarjeta input) throws OpenpayServiceException, ServiceUnavailableException {

        Card response = API.cards().create(input.getCustomerId(), input.toCard());
       
        return response;
    }


    @RequestMapping(value = "/tarjeta/{customerId}/{id}",  method = RequestMethod.GET)
    @ResponseBody
    public Card getTarjeta(@PathVariable  String id, @PathVariable String customerId) throws OpenpayServiceException, ServiceUnavailableException {
        Card card = API.cards().get(customerId, id);
        return card;
    }

    @RequestMapping(value = "/tarjeta/{customerId}/{id}",  method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteTarjeta(@PathVariable  String id, @PathVariable String customerId) {
        try {
            API.cards().delete(customerId ,id);
        }catch (OpenpayServiceException e){
            //TODO
            return false;
        }catch (ServiceUnavailableException e ){
            //TODO
            return false;
        }
        return true ;
    }

    @RequestMapping(value = "/tarjetas",  method = RequestMethod.POST)
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


}
