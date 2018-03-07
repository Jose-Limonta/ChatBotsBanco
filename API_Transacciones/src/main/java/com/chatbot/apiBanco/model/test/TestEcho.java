
package com.chatbot.apiBanco.model.test;

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
    "args",
    "data",
    "files",
    "form",
    "headers",
    "json",
    "url"
})
public class TestEcho {

    @JsonProperty("args")
    private Args args;
    @JsonProperty("data")
    private Data data;
    @JsonProperty("files")
    private Files files;
    @JsonProperty("form")
    private Form form;
    @JsonProperty("headers")
    private Headers headers;
    @JsonProperty("json")
    private Json json;
    @JsonProperty("url")
    private String url;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("args")
    public Args getArgs() {
        return args;
    }

    @JsonProperty("args")
    public void setArgs(Args args) {
        this.args = args;
    }

    @JsonProperty("data")
    public Data getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Data data) {
        this.data = data;
    }

    @JsonProperty("files")
    public Files getFiles() {
        return files;
    }

    @JsonProperty("files")
    public void setFiles(Files files) {
        this.files = files;
    }

    @JsonProperty("form")
    public Form getForm() {
        return form;
    }

    @JsonProperty("form")
    public void setForm(Form form) {
        this.form = form;
    }

    @JsonProperty("headers")
    public Headers getHeaders() {
        return headers;
    }

    @JsonProperty("headers")
    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    @JsonProperty("json")
    public Json getJson() {
        return json;
    }

    @JsonProperty("json")
    public void setJson(Json json) {
        this.json = json;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
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
