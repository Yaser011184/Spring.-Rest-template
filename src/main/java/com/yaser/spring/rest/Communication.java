package com.yaser.spring.rest;

import com.yaser.spring.rest.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Communication {

    private final RestTemplate restTemplate;

    private final HttpHeaders headers;

    private final String URL = "http://94.198.50.185:7081/api/users";

    @Autowired
    public Communication(RestTemplate restTemplate, HttpHeaders headers, HttpHeaders headers1) {
        this.restTemplate = restTemplate;
        this.headers = headers;
        this.headers.set("Cookie", String.join(";", restTemplate.headForHeaders(URL).get("Set-Cookie")));
    }

    public String answer() {
        return addUser().getBody() + updateUser().getBody() + deleteUser().getBody();
    }

    private List<User> allUsers() {
        ResponseEntity<List<User>> responseEntity = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {
                });
        System.out.println(responseEntity.getHeaders());
        return responseEntity.getBody();
    }

    private ResponseEntity<String> addUser() {
        User user = new User(3L, "James", "Brown", (byte) 34);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.postForEntity(URL, entity, String.class);
    }

    private ResponseEntity<String> updateUser() {
        User user = new User(3L, "Thomas", "Shelby", (byte) 34);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        return restTemplate.exchange(URL, HttpMethod.PUT, entity, String.class, 3);
    }

    private ResponseEntity<String> deleteUser() {
        Map<String, Long> uriVariables = new HashMap<>() {{
            put("id", 3L);
        }};
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(URL + "/{id}", HttpMethod.DELETE, entity, String.class, uriVariables);
    }
}