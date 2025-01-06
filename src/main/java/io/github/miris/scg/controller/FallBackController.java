package io.github.miris.scg.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackController {
    
    @GetMapping("/fallback")
    public ResponseEntity<Object> fallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is down. Please try again later.");
    }

    @GetMapping
    public String home() {
        return "Gateway is up and running.";
    }
}
