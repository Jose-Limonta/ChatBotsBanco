package com.chatbot.apiBanco.model.transaction;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import mx.openpay.client.core.requests.transactions.CreateTransferParams;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Transactionjs {
    private BigDecimal amount;
    private String description;
    private String order;
    private String toCustomer;

    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal attribute) {
        this.amount = attribute;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String attribute) {
        this.order = attribute;
    }

    public String getToCustomer(){
        return this.toCustomer;
    }

    public void setToCustomer(String customer){
        this.toCustomer = customer;
    }

    public CreateTransferParams toTransfer(){
        CreateTransferParams transfer = new CreateTransferParams();
        transfer.toCustomerId(this.toCustomer);
        transfer.amount(this.amount);
        transfer.description(this.description);
        transfer.orderId(this.order);

        return transfer;
    }

}