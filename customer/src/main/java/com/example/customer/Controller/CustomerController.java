package com.example.customer.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.cutomer.Entity.Account;
import com.example.cutomer.Entity.Transaction;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/create-account")
    public String createAccount(@RequestBody Account account) {
        String url = "http://localhost:8080/accounts/create-account";
        ResponseEntity<String> response = restTemplate.postForEntity(url, account, String.class);
        return response.getBody();
    }

    @PutMapping("/transfer")
    public void transfer(@RequestBody Transaction transaction) {
        String url = "http://localhost:8080/accounts/transfer"; 
        		restTemplate.put(url, transaction);
    }
}
