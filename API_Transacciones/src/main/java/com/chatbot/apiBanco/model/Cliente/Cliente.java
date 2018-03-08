
package com.chatbot.apiBanco.model.Cliente;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import mx.openpay.client.Customer;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "last_name",
    "email",
    "phone_number",
    "external_id",
    "status",
    "balance",
    "address"
})
public class Cliente {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("external_id")
    private String externalId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("balance")
    private Double balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("address")
    private com.chatbot.apiBanco.model.Cliente.Address address;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("last_name")
    public String getLastName() {
        return lastName;
    }

    @JsonProperty("last_name")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty("phone_number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty("external_id")
    public String getExternalId() {
        return externalId;
    }

    @JsonProperty("external_id")
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("balance")
    public Double getBalance() {
        return balance;
    }

    @JsonProperty("balance")
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @JsonProperty("address")
    public com.chatbot.apiBanco.model.Cliente.Address getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(com.chatbot.apiBanco.model.Cliente.Address address) {
        this.address = address;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Customer toCustomer() {
        Customer request = new Customer();
        request.externalId(this.externalId);
        request.name(this.name);
        request.lastName(this.lastName);
        request.email(this.email);
        request.phoneNumber(this.phoneNumber);
        request.requiresAccount(false);
            mx.openpay.client.Address address = new mx.openpay.client.Address();
            address.city(this.address.getCity());
            address.countryCode(this.address.getCountryCode());
            address.state(this.address.getState());
            address.postalCode(this.address.getPostalCode());
            address.line1(this.address.getLine1());
            address.line2(this.address.getLine2());
            address.line3(this.address.getLine3());
        request.address(address);
        return request;
    }
}
