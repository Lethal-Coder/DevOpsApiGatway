package com.example.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
        System.out.println("===========================================");
        System.out.println("API Gateway with Authentication Started!");
        System.out.println("Port: 8082");
        System.out.println("===========================================");
        System.out.println("Available Endpoints:");
        System.out.println("POST /api/auth/signin - Login");
        System.out.println("POST /api/auth/signup - Register");
        System.out.println("GET  /api/gateway/health - Health check");
        System.out.println("ALL  /api/gateway/** - Proxy to backend");
        System.out.println("===========================================");
        System.out.println("Default Users:");
        System.out.println("admin/admin123 (ADMIN, USER roles)");
        System.out.println("user/user123 (USER role)");
        System.out.println("moderator/mod123 (MODERATOR, USER roles)");
        System.out.println("===========================================");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
