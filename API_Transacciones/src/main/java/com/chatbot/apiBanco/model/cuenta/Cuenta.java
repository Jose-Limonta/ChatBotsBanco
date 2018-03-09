package com.chatbot.apiBanco.model.cuenta;


import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import mx.openpay.client.BankAccount;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"clabe",
"alias",
"holder_name"
})
public class Cuenta {

	@JsonProperty("clabe")
	private String clabe;
	@JsonProperty("alias")
	private String alias;
	@JsonProperty("holder_name")
	private String holderName;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("clabe")
    public String getClabe() {
        return clabe;
    }

    @JsonProperty("clabe")
    public void setClabe(String clabe) {
        this.clabe = clabe;
    }

    @JsonProperty("alias")
    public String getAlias() {
        return alias;
    }

    @JsonProperty("alias")
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @JsonProperty("holder_name")
    public String getHolderName() {
        return holderName;
    }

    @JsonProperty("holder_name")
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
	}
	
	public BankAccount toAccount(){
		BankAccount account = new BankAccount();
		account.holderName(this.getHolderName());
		account.alias(this.getAlias());
		account.clabe(this.getClabe());

		return account;
	}

}