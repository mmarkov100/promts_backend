package com.promts.promts_test_server.promts_backend.controller;

import com.promts.promts_test_server.promts_backend.service.Auth.AuthService;
import com.promts.promts_test_server.promts_backend.service.NeuralNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/neuro")
public class NeuralNetworkController {

    private static final Logger logger = Logger.getLogger(NeuralNetworkController.class.getName());

    @Autowired
    private AuthService authService;
    @Autowired
    private NeuralNetworkService neuralNetworkService;

    //TODO Обратно поменять на гет запрос, а то нгрок хуета какая-то
    @PostMapping
    public ResponseEntity<?> getAllNeuros(@RequestHeader String authorization){

        logger.info("Got request for list of neuros..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(neuralNetworkService.getAllNeuros(uidFirebase));
    }
}
