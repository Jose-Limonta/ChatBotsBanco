package com.chatbot.apiBanco.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

public class ApiRequest {

    private static final Logger log = LoggerFactory.getLogger(ApiRequest.class);
    private ObjectMapper mapper = new ObjectMapper();
    private HttpHeaders headers;
    private RestTemplate restTemplate = new RestTemplate();
    private HttpEntity<String> entity;

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")) );
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
    //constructor
    public ApiRequest() {
        this.headers = createHeaders("sk_e4ab3db394a247c8a0eee7099e62ff5b", "");
        this.headers.setContentType(MediaType.APPLICATION_JSON);
    }

    public  <I, O> O PostRequest(String url, I input, Class<O> type ) throws JsonProcessingException {

        entity = new HttpEntity<>(mapper.writeValueAsString(input) , headers);
        //postRequest
        O response = restTemplate.postForObject(url, entity ,type);
        log.info(response.toString());

        return response;
    }

    public  <I, O> O PatchRequest(String url, I input, Class<O> type ) throws JsonProcessingException {

        entity = new HttpEntity<>(mapper.writeValueAsString(input) ,headers);
        //patch Request
        O response = restTemplate.patchForObject(url, entity ,type);
        log.info(response.toString());

        return response;
    }

    public  <I> void PutRequest(String url, I input ) throws JsonProcessingException {

        entity = new HttpEntity<>(mapper.writeValueAsString(input) ,headers);
        //put Request
        restTemplate.put(url, entity);

    }

    public <I, O> void DeleteRequest(String url, I input) throws JsonProcessingException {
        entity = new HttpEntity<>(mapper.writeValueAsString(input) ,headers);
        //delete Request
        restTemplate.delete(url, entity);
    }

    public  <O> O GetRequest(String url, Class<O> type ){
        RestTemplate restTemplate = new RestTemplate();
        //get Request
        O response = restTemplate.getForObject(url, type);
        log.info(response.toString());
        return response;
    }
}
