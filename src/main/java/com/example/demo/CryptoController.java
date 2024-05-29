package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoController {
    @Autowired
    private KmsEncryptionService encryptionService;

    @GetMapping("v1/encrypt")
    public String encryptData(@RequestHeader String token) {
        return encryptionService.encrypt(token);
    }

    @GetMapping("v1/decrypt")
    public String decrypt(@RequestHeader String token) {
        return encryptionService.decrypt(token);
    }
}
