package com.chatbot.apiBanco.errorhandler;

import com.chatbot.apiBanco.model.error.Error;

public class ErrorHandler {

    static public Error serror(Exception ex, String cause){
        Error e = new Error();
        e.setAdditionalProperty("Source", cause);
        e.setAdditionalProperty("Cause", ex.getCause());
        e.setDescription(ex.getMessage() );
        return e;
    }
}
