package com.chatbot.apiBanco.controller;


import com.chatbot.apiBanco.model.client.Range;
import com.chatbot.apiBanco.model.account.Accounts;
import com.chatbot.apiBanco.model.database.repository.ClienteRepository;
import com.chatbot.apiBanco.model.database.repository.CuentaRepository;
import com.chatbot.apiBanco.model.database.tables.CuentaLog;

import mx.openpay.client.BankAccount;
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
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    private static final OpenpayAPI API = new OpenpayAPI("https://sandbox-api.openpay.mx", "sk_e4ab3db394a247c8a0eee7099e62ff5b", "moiatycvyhadtev60q8x");
    private final Calendar dateGte = Calendar.getInstance();
    private final Calendar dateLte = Calendar.getInstance();
 
    @Autowired
    private CuentaRepository cuentaRepo;

    @Autowired
    private ClienteRepository clienteRepo;

    @RequestMapping(value = "/v1/account",  method = RequestMethod.POST)
    @ResponseBody
    public BankAccount altaCuenta(@RequestBody Accounts input) throws OpenpayServiceException, ServiceUnavailableException {
        BankAccount response = API.bankAccounts().create( input.getCustomerId() ,input.toAccount());
        CuentaLog c_log = new CuentaLog(response);
        c_log.setCliente( clienteRepo.findByToken(input.getCustomerId()).getId() );
        cuentaRepo.save(c_log);
        return response;
    }

    @RequestMapping(value = "/v1/account/{customerId}/{accountId}", method = RequestMethod.GET)
    @ResponseBody
    public BankAccount getCuenta(@PathVariable  String customerId,@PathVariable  String accountId ) throws ServiceUnavailableException, OpenpayServiceException{
        BankAccount bankAccount = API.bankAccounts().get(customerId, accountId);
        return bankAccount;
    }

    @RequestMapping(value = "/v1/account/{customerId}/{accountId}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteCuenta(@PathVariable  String customerId,@PathVariable  String accountId ) throws ServiceUnavailableException, OpenpayServiceException{
        API.bankAccounts().delete(customerId, accountId);
        return true;
    }

    @RequestMapping(value = "/v1/accounts",  method = RequestMethod.POST)
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
    
    @ExceptionHandler({ OpenpayServiceException.class })
    @ResponseBody
    public Error handleException(OpenpayServiceException ex) {
        //
        Error e = new Error();
        e.setAdditionalProperty("Source", "Fallo de Operacion Accounts");
        e.setErrorCode(ex.getErrorCode());
        e.setHttpCode(ex.getHttpCode());
        e.setDescription(ex.getDescription());  
        return e;
    }

    @ExceptionHandler({ ServiceUnavailableException.class })
    @ResponseBody
    public Error handleServiceException(ServiceUnavailableException ex) {
        //
        Error e = new Error();
        e.setAdditionalProperty("Source", "Servicio no disponible");
        e.setAdditionalProperty("Cause", ex.getCause());
        e.setDescription(ex.getMessage() );  
        return e;
    }

}
