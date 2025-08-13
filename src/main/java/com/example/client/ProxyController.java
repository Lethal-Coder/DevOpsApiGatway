package com.example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProxyController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${TARGET_URL:http://my-spring-app:8080/}")
    private String targetUrl;

    @GetMapping("/proxy")
    public String proxy() {
       return  restTemplate.getForObject(targetUrl, String.class);
    }
}