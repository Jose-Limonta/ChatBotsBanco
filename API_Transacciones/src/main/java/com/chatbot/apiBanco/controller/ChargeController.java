package com.chatbot.apiBanco.controller;

import com.chatbot.apiBanco.errorhandler.ErrorHandler;
import com.chatbot.apiBanco.model.charge.Chargejs;
import com.chatbot.apiBanco.model.charge.Confirmjs;
import com.chatbot.apiBanco.model.database.repository.TransactionRepository;
import mx.openpay.client.Card;
import mx.openpay.client.Charge;
import mx.openpay.client.Customer;
import mx.openpay.client.core.OpenpayAPI;
import mx.openpay.client.core.requests.transactions.ConfirmCaptureParams;
import mx.openpay.client.core.requests.transactions.CreateCardChargeParams;
import mx.openpay.client.core.requests.transactions.RefundParams;
import mx.openpay.client.exceptions.OpenpayServiceException;
import mx.openpay.client.exceptions.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.chatbot.apiBanco.model.error.Error;

import java.util.Calendar;

@RestController
public class ChargeController {
    private static final Logger log = LoggerFactory.getLogger(ChargeController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");
    private final Calendar dateGte = Calendar.getInstance();
    private final Calendar dateLte = Calendar.getInstance();


    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping(value = "/v1/charge",  method = RequestMethod.POST)
    @ResponseBody
    public Charge addCharge(@RequestBody Chargejs input) throws OpenpayServiceException, ServiceUnavailableException {

        CreateCardChargeParams request = new CreateCardChargeParams();
        Customer customer = API.customers().get(input.getCustomerId());
        Card card = API.cards().get(input.getCardId());

        request.cardId(card.getId());
        request.amount(input.getAmount());
        request.currency(input.getCurrency());
        request.description(input.getDescription());
        request.orderId(input.getOrderId());
        request.deviceSessionId(card.getDeviceSessionId());

        request.sendEmail(input.isConfirm());
        request.confirm(input.isSendEmail());

        request.customer(customer);


        return API.charges().create(request);

    }


    @RequestMapping(value = "/v1/confirm",  method = RequestMethod.POST)
    @ResponseBody
    public Charge confirmCharge(@RequestBody Confirmjs input) throws OpenpayServiceException, ServiceUnavailableException {

        ConfirmCaptureParams request = new ConfirmCaptureParams();
        request.chargeId(input.getChargeId());
        request.amount(input.getAmount());

        return API.charges().confirmCapture( input.getCustomerId(),request);

    }

    @RequestMapping(value = "/v1/refund",  method = RequestMethod.POST)
    @ResponseBody
    public Charge refundCharge(@RequestBody Confirmjs input) throws OpenpayServiceException, ServiceUnavailableException {

        RefundParams request = new RefundParams();
        request.chargeId(input.getChargeId());
        request.description("Devolucion de Cargo : " + input.getDescription());
        request.amount(input.getAmount());
        return API.charges().refund( input.getCustomerId(),request);

    }

    @RequestMapping(value = "/v1/charge/{cudtomerId}/{transactionId}", method = RequestMethod.GET)
    @ResponseBody
    public Charge findCharge(@PathVariable String customerId, @PathVariable String transactionId) throws OpenpayServiceException, ServiceUnavailableException{
        return API.charges().get(customerId, transactionId);
    }

    @ExceptionHandler({ OpenpayServiceException.class })
    @ResponseBody
    public Error handleException(OpenpayServiceException ex) {
        return ErrorHandler.serror(ex, "Falla de Servicio de OpenPay");
    }

    @ExceptionHandler({ ServiceUnavailableException.class })
    @ResponseBody
    public Error handleServiceException(ServiceUnavailableException ex) {
        return ErrorHandler.serror(ex, "Servicio no Disponible");
    }


}
