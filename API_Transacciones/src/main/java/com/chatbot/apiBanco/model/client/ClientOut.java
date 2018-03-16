package com.chatbot.apiBanco.model.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mx.openpay.client.Customer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientOut {
    private String id;
    private String creation_date;
    private String status;

    public ClientOut(Customer customer){
        this.id = customer.getId();
        this.creation_date = customer.getCreationDate().toString();
        this.status = customer.getStatus();
    }

    public ClientOut(){
        this.id = "";
        this.creation_date = "";
        this.status = "Success";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String success) {
        this.status = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }
}
