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
    /*
    private static final Logger log = LoggerFactory.getLogger(TarjetaController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");
    private final Calendar dateGte = Calendar.getInstance();
    private final Calendar dateLte = Calendar.getInstance();

    @RequestMapping(value = "/tarjeta",  method = RequestMethod.POST)
    @ResponseBody
    public Card creaTarjeta(@RequestBody Tarjeta input) throws OpenpayServiceException, ServiceUnavailableException {

        /*
            Card request = new Card();
            request.holderName("Juan Perez Ramirez");
            request.cardNumber("4111111111111111");
            request.cvv2("110");
            request.expirationMonth(12);
            request.expirationYear(20);
            request.deviceSessionId("kR1MiQhz2otdIuUlQkbEyitIqVMiI16f");
            Address address = new Address();
            address.city("Queretaro");
            address.countryCode("10");
            address.state("Queretaro");
            address.postalCode("79125");
            address.line1("Av. Pie de la cuesta #12");
            address.line2("Desarrollo San Pablo");
            address.line3("Qro. Qro.");
            request.address(address);

        request = API.cards().create("a9pvykxz4g5rg0fplze0", request);
        return new ;
    }

    @RequestMapping(value = "/tarjeta",  method = RequestMethod.PATCH)
    @ResponseBody
    public  actualizaTarjeta(@RequestBody Tarjeta input) throws OpenpayServiceException, ServiceUnavailableException {
         response = input.();
        response.setId(input.getId());
        response = ;
        return new ClienteOut(response);
    }

    @RequestMapping(value = "/tarjeta/{id}",  method = RequestMethod.GET)
    @ResponseBody
    public  getTarjeta(@PathVariable  String id) throws OpenpayServiceException, ServiceUnavailableException {
        return  ;
    }

    @RequestMapping(value = "/tarjeta/{id}",  method = RequestMethod.DELETE)
    @ResponseBody
    public  deletetarjeta(@PathVariable  String id) throws OpenpayServiceException, ServiceUnavailableException {

        return new ();
    }

    @RequestMapping(value = "/tarjeta",  method = RequestMethod.POST)
    @ResponseBody
    public List<Card>  getTarjetas(@RequestBody Range dates) throws OpenpayServiceException, ServiceUnavailableException {
        dateGte.set(dates.getInicio().getAnio(), dates.getInicio().getMes(), dates.getInicio().getDia(), dates.getInicio().getHora(), dates.getInicio().getMinuto(), dates.getInicio().getSegundo());
        dateLte.set(dates.getFin().getAnio(), dates.getFin().getMes(), dates.getFin().getDia(), dates.getFin().getHora(), dates.getFin().getMinuto(), dates.getFin().getSegundo());

        SearchParams request = new SearchParams();
        request.creationGte(dateGte.getTime());
        request.creationLte(dateLte.getTime());
        request.offset(0);

        return  API.cards().list("a9pvykxz4g5rg0fplze0", request);
    }
    */

}
