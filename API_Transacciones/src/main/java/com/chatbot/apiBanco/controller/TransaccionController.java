package com.chatbot.apiBanco.controller;


import com.chatbot.apiBanco.model.error.Error;
import com.chatbot.apiBanco.model.transaccion.Transaccion;
import mx.openpay.client.Transfer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.chatbot.apiBanco.model.error.Error;;


import java.util.Calendar;


@RestController
public class TransaccionController {

    private static final Logger log = LoggerFactory.getLogger(TransaccionController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");
    private final Calendar dateGte = Calendar.getInstance();
    private final Calendar dateLte = Calendar.getInstance();

    @RequestMapping(value = "/transaccion",  method = RequestMethod.POST)
    @ResponseBody
    public Transfer  transferencia(@RequestBody Transaccion input) throws OpenpayServiceException, ServiceUnavailableException {

        Transfer transfer = API.transfers().create(input.getCustomerId(), input.toTransfer());
       
        return transfer;
    }

   
    @RequestMapping(value = "/transaccion/{customerId}/{transactionId}", method = RequestMethod.GET)
    @ResponseBody
    public Transfer getTransfer(@PathVariable String customerId, @PathVariable String transactionId) throws OpenpayServiceException, ServiceUnavailableException{
        Transfer transfer = API.transfers().get(customerId, transactionId);
        return transfer;

    }

    @ExceptionHandler({ OpenpayServiceException.class })
    @ResponseBody
    public Error handleException(OpenpayServiceException ex) {
        Error e = new Error();
        e.setAdditionalProperty("Source", "Fallo de Operacion transaccion");
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
