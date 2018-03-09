package com.chatbot.apiBanco.controller;


import com.chatbot.apiBanco.model.cliente.ClienteOut;
import com.chatbot.apiBanco.model.cliente.Range;
import com.chatbot.apiBanco.model.cuenta.Cuenta;
import mx.openpay.client.BankAccount;
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
public class CuentaController {

    private static final Logger log = LoggerFactory.getLogger(CuentaController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");
    private final Calendar dateGte = Calendar.getInstance();
    private final Calendar dateLte = Calendar.getInstance();

    @RequestMapping(value = "/cuenta",  method = RequestMethod.POST)
    @ResponseBody
    public BankAccount altaCuenta(@RequestBody  Cuenta input) throws OpenpayServiceException, ServiceUnavailableException {
         Cuenta response = API.bankAccounts().create(input.toAccount(),  );
         return new BankAccount();
    }

    @RequestMapping(value = "/cuenta/{customerId}/{accountId}", method = RequestMethod.GET)
    @ResponseBody
    public BankAccount getCuenta(@PathVariable  String customerId,@PathVariable  String accountId ) throws ServiceUnavailableException, OpenpayServiceException{
        BankAccount bankAccount = API.bankAccounts().get(customerId, accountId);
        return bankAccount;
    }

    @RequestMapping(value = "/cuenta/{customerId}/{accountId}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteCuenta(@PathVariable  String customerId,@PathVariable  String accountId ) throws ServiceUnavailableException, OpenpayServiceException{
        API.bankAccounts().delete(customerId, accountId);
        return true;
    }

    @RequestMapping(value = "/cuentas",  method = RequestMethod.POST)
    @ResponseBody
    public List<BankAccount> getCuentas(@RequestBody Range dates) throws OpenpayServiceException, ServiceUnavailableException {
        dateGte.set(dates.getInicio().getAnio(), dates.getInicio().getMes(), dates.getInicio().getDia(), dates.getInicio().getHora(), dates.getInicio().getMinuto(), dates.getInicio().getSegundo());
        dateLte.set(dates.getFin().getAnio(), dates.getFin().getMes(), dates.getFin().getDia(), dates.getFin().getHora(), dates.getFin().getMinuto(), dates.getFin().getSegundo());

        SearchParams request = new SearchParams();
        request.creationGte(dateGte.getTime());
        request.creationLte(dateLte.getTime());
        request.offset(0);

        return  API.bankAccounts().list(request);
    }
    
}
