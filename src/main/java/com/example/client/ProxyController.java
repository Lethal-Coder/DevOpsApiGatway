//Demo_Project_Devops/my-spring-app-client/src/main/java/com/example/client/ProxyController.java
package com.example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProxyController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/proxy")
    public String proxy() {
        return restTemplate.getForObject("http://localhost:8080/", String.class);
    }
}