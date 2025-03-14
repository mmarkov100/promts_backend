package com.promts.promts_test_server.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/app")
@CrossOrigin(origins = "*")
public class AppController {

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck(){
        return ResponseEntity.ok().body(Map.of("available", true));
    }
}
