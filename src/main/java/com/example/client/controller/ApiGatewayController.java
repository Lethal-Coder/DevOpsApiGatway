package com.example.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;

@RestController
@RequestMapping("/api/gateway")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApiGatewayController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${target.service.url}")
    private String targetServiceUrl;

    @GetMapping("/**")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> proxyGetRequest(HttpServletRequest request) {
        return forwardRequest(request, HttpMethod.GET, null);
    }

    @PostMapping("/**")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> proxyPostRequest(HttpServletRequest request, @RequestBody(required = false) String body) {
        return forwardRequest(request, HttpMethod.POST, body);
    }

    @PutMapping("/**")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> proxyPutRequest(HttpServletRequest request, @RequestBody(required = false) String body) {
        return forwardRequest(request, HttpMethod.PUT, body);
    }

    @DeleteMapping("/**")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> proxyDeleteRequest(HttpServletRequest request) {
        return forwardRequest(request, HttpMethod.DELETE, null);
    }

    private ResponseEntity<String> forwardRequest(HttpServletRequest request, HttpMethod method, String body) {
        // Extract the path from the request
        String path = request.getRequestURI().replace("/api/gateway", "");
        String queryString = request.getQueryString();
        
        // Construct the target URL
        String targetUrl = targetServiceUrl + path;
        if (queryString != null) {
            targetUrl += "?" + queryString;
        }
        
        // Debug logging
        System.out.println("DEBUG: Original URI: " + request.getRequestURI());
        System.out.println("DEBUG: Extracted path: " + path);
        System.out.println("DEBUG: Target service URL: " + targetServiceUrl);
        System.out.println("DEBUG: Final target URL: " + targetUrl);

        // Get current user authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        
        // Copy relevant headers from the original request
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("authorization") && 
                !headerName.equalsIgnoreCase("host") &&
                !headerName.equalsIgnoreCase("content-length")) {
                headers.put(headerName, Collections.list(request.getHeaders(headerName)));
            }
        }

        // Add user context header for the downstream service
        headers.add("X-User-Name", username);
        headers.add("X-Gateway-Forwarded", "true");

        // Set content type if body is present
        if (body != null) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                targetUrl,
                method,
                entity,
                String.class
            );

            return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(
                "{\"error\":\"Gateway Error: " + e.getMessage() + "\"}",
                HttpStatus.BAD_GATEWAY
            );
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        return ResponseEntity.ok(
            "{\"status\":\"UP\",\"gateway\":\"API Gateway\",\"user\":\"" + username + "\"}"
        );
    }
}