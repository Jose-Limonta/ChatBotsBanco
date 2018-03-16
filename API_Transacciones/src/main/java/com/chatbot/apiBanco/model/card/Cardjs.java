package com.chatbot.apiBanco.model.card;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import mx.openpay.client.Address;
import mx.openpay.client.Card;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "card_number",
    "holder_name",
    "expiration_year",
    "expiration_month",
    "cvv2",
    "device_session_id"
})
public class Cardjs {

    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("card_number")
    private String cardNumber;
    @JsonProperty("holder_name")
    private String holderName;
    @JsonProperty("expiration_year")
    private String expirationYear;
    @JsonProperty("expiration_month")
    private String expirationMonth;
    @JsonProperty("cvv2")
    private String cvv2;
    @JsonProperty("device_session_id")
    private String deviceSessionId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String id) {
        this.customerId = id;
    }

    @JsonProperty("address")
    private com.chatbot.apiBanco.model.client.Address address;

    @JsonProperty("card_number")
    public String getCardNumber() {
        return cardNumber;
    }

    @JsonProperty("card_number")
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @JsonProperty("holder_name")
    public String getHolderName() {
        return holderName;
    }

    @JsonProperty("holder_name")
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    @JsonProperty("expiration_year")
    public String getExpirationYear() {
        return expirationYear;
    }

    @JsonProperty("expiration_year")
    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    @JsonProperty("expiration_month")
    public String getExpirationMonth() {
        return expirationMonth;
    }

    @JsonProperty("expiration_month")
    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    @JsonProperty("cvv2")
    public String getCvv2() {
        return cvv2;
    }

    @JsonProperty("cvv2")
    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    @JsonProperty("device_session_id")
    public String getDeviceSessionId() {
        return deviceSessionId;
    }

    @JsonProperty("device_session_id")
    public void setDeviceSessionId(String deviceSessionId) {
        this.deviceSessionId = deviceSessionId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public com.chatbot.apiBanco.model.client.Address getAddress() {
        return address;
    }

    public void setAddress(com.chatbot.apiBanco.model.client.Address address) {
        this.address = address;
    }

    public Card toCard(){

        Card card = new Card();
        card.holderName(this.holderName);
        card.cardNumber(this.cardNumber);
        card.cvv2(this.cvv2);
        card.setExpirationMonth(this.expirationMonth);
        card.setExpirationYear(this.expirationYear);
        card.setDeviceSessionId(this.deviceSessionId);

        Address address = new Address();
            address.setCity(this.getAddress().getCity());
            address.setCountryCode(this.getAddress().getCountryCode());
            address.setState(this.getAddress().getState());
            address.setPostalCode(this.getAddress().getPostalCode());
            address.setLine1(this.getAddress().getLine1());
            address.setLine2(this.getAddress().getLine2());
            address.setLine3(this.getAddress().getLine3());
        card.setAddress(address);

        return card;
    }

    public String toString(){
        return this.toCard().toString();
    }

}
