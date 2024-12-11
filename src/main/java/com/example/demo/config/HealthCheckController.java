package com.example.demo.config;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class HealthCheckController {


    @PreAuthorize("permitAll()")
    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
