package com.example.masterservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/master")
public class MasterController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${slave.service.url:http://slave:8081/slave}")
    private String slaveServiceUrl;



    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        return restTemplate.getForEntity(slaveServiceUrl + "/" + id, String.class);
    }

    @PostMapping
    public ResponseEntity<?> saveData(@RequestBody Map<String, String> requestData) {
        if (requestData == null || !requestData.containsKey("data")) {
            return ResponseEntity.badRequest().body("Request body must contain 'data' field");
        }

        String data = requestData.get("data");
        if (data == null || data.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Data cannot be empty");
        }

        HttpEntity<String> request = new HttpEntity<>(data, createHeaders());
        return restTemplate.postForEntity(slaveServiceUrl, request, String.class);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateData(@PathVariable int id, @RequestBody String data) {
        if (data == null || data.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Data cannot be empty");
        }

        HttpEntity<String> request = new HttpEntity<>(data, createHeaders());
        return restTemplate.exchange(slaveServiceUrl + "/" + id, HttpMethod.PUT, request, String.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteData(@PathVariable int id) {
        restTemplate.delete(slaveServiceUrl + "/" + id);
        return ResponseEntity.ok("Data deleted successfully");
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
