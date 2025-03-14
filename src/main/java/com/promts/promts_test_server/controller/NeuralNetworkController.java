package com.promts.promts_test_server.controller;

import com.promts.promts_test_server.service.Auth.AuthService;
import com.promts.promts_test_server.service.NeuralNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/neuro")
@CrossOrigin(origins = "*")
public class NeuralNetworkController {

    @Autowired
    private AuthService authService;
    @Autowired
    private NeuralNetworkService neuralNetworkService;

    @GetMapping
    public ResponseEntity<?> getAllNeuros(@RequestHeader String authorization,
                                          @RequestHeader Long id){

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(neuralNetworkService.getAllNeuros(id, uidFirebase));
    }
}
