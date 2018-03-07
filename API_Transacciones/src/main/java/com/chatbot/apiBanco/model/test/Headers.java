
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
    "host",
    "content-length",
    "accept",
    "accept-encoding",
    "cache-control",
    "content-type",
    "cookie",
    "postman-token",
    "user-agent",
    "x-forwarded-port",
    "x-forwarded-proto"
})
public class Headers {

    @JsonProperty("host")
    private String host;
    @JsonProperty("content-length")
    private String contentLength;
    @JsonProperty("accept")
    private String accept;
    @JsonProperty("accept-encoding")
    private String acceptEncoding;
    @JsonProperty("cache-control")
    private String cacheControl;
    @JsonProperty("content-type")
    private String contentType;
    @JsonProperty("cookie")
    private String cookie;
    @JsonProperty("postman-token")
    private String postmanToken;
    @JsonProperty("user-agent")
    private String userAgent;
    @JsonProperty("x-forwarded-port")
    private String xForwardedPort;
    @JsonProperty("x-forwarded-proto")
    private String xForwardedProto;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("host")
    public String getHost() {
        return host;
    }

    @JsonProperty("host")
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty("content-length")
    public String getContentLength() {
        return contentLength;
    }

    @JsonProperty("content-length")
    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    @JsonProperty("accept")
    public String getAccept() {
        return accept;
    }

    @JsonProperty("accept")
    public void setAccept(String accept) {
        this.accept = accept;
    }

    @JsonProperty("accept-encoding")
    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    @JsonProperty("accept-encoding")
    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    @JsonProperty("cache-control")
    public String getCacheControl() {
        return cacheControl;
    }

    @JsonProperty("cache-control")
    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    @JsonProperty("content-type")
    public String getContentType() {
        return contentType;
    }

    @JsonProperty("content-type")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty("cookie")
    public String getCookie() {
        return cookie;
    }

    @JsonProperty("cookie")
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @JsonProperty("postman-token")
    public String getPostmanToken() {
        return postmanToken;
    }

    @JsonProperty("postman-token")
    public void setPostmanToken(String postmanToken) {
        this.postmanToken = postmanToken;
    }

    @JsonProperty("user-agent")
    public String getUserAgent() {
        return userAgent;
    }

    @JsonProperty("user-agent")
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @JsonProperty("x-forwarded-port")
    public String getXForwardedPort() {
        return xForwardedPort;
    }

    @JsonProperty("x-forwarded-port")
    public void setXForwardedPort(String xForwardedPort) {
        this.xForwardedPort = xForwardedPort;
    }

    @JsonProperty("x-forwarded-proto")
    public String getXForwardedProto() {
        return xForwardedProto;
    }

    @JsonProperty("x-forwarded-proto")
    public void setXForwardedProto(String xForwardedProto) {
        this.xForwardedProto = xForwardedProto;
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
