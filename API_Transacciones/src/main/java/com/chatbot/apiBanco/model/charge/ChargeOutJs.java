package com.chatbot.apiBanco.model.charge;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"amount",
"authorization",
"method",
"operation_type",
"transaction_type",
"card",
"status",
"refund",
"currency",
"creation_date",
"operation_date",
"description",
"error_message",
"order_id",
"customer_id"
})
public class ChargeOutJs {

	@JsonProperty("id")
	private String id;
	@JsonProperty("amount")
	private Double amount;
	@JsonProperty("authorization")
	private String authorization;
	@JsonProperty("method")
	private String method;
	@JsonProperty("operation_type")
	private String operationType;
	@JsonProperty("transaction_type")
	private String transactionType;
	@JsonProperty("card")
	private String card;
	@JsonProperty("status")
	private String status;
	@JsonProperty("refund")
	private boolean refund;
	@JsonProperty("currency")
	private String currency;
	@JsonProperty("creation_date")
	private String creationDate;
	@JsonProperty("operation_date")
	private String operationDate;
	@JsonProperty("description")
	private String description;
	@JsonProperty("error_message")
	private Object errorMessage;
	@JsonProperty("order_id")
	private String orderId;
	@JsonProperty("customer_id")
	private String customerId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("amount")
	public Double getAmount() {
		return amount;
	}

	@JsonProperty("amount")
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@JsonProperty("authorization")
	public String getAuthorization() {
		return authorization;
	}

	@JsonProperty("authorization")
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	@JsonProperty("method")
	public String getMethod() {
		return method;
	}

	@JsonProperty("method")
	public void setMethod(String method) {
		this.method = method;
	}

	@JsonProperty("operation_type")
	public String getOperationType() {
		return operationType;
	}

	@JsonProperty("operation_type")
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	@JsonProperty("transaction_type")
	public String getTransactionType() {
		return transactionType;
	}

	@JsonProperty("transaction_type")
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@JsonProperty("card")
	public String getCard() {
		return card;
	}

	@JsonProperty("card")
	public void setCard(String card) {
		this.card = card;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("refund")
	public boolean getRefund() {
		return refund;
	}

	@JsonProperty("refund")
	public void setRefund(boolean refund) {
		this.refund = refund;
	}

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	@JsonProperty("currency")
	public void setCurrency(String currency) {
	this.currency = currency;
	}

	@JsonProperty("creation_date")
	public String getCreationDate() {
		return creationDate;
	}

	@JsonProperty("creation_date")
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	@JsonProperty("operation_date")
	public String getOperationDate() {
		return operationDate;
	}

	@JsonProperty("operation_date")
	public void setOperationDate(String operationDate) {
		this.operationDate = operationDate;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("error_message")
	public Object getErrorMessage() {
		return errorMessage;
	}

	@JsonProperty("error_message")
	public void setErrorMessage(Object errorMessage) {
		this.errorMessage = errorMessage;
	}

	@JsonProperty("order_id")
	public String getOrderId() {
		return orderId;
	}

	@JsonProperty("order_id")
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@JsonProperty("customer_id")
	public String getCustomerId() {
		return customerId;
	}

	@JsonProperty("customer_id")
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}