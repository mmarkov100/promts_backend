package com.promts.promts_test_server.controller;

import com.promts.promts_test_server.dto.Auth.inboind.EmailAndPasswordDTO;
import com.promts.promts_test_server.service.Auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Autowired
    private AuthService authService;

    // Проверка актуальности токена. Нужен, чтобы при заходе на сайт если токен актуальный сразу переключалось на главный экран
    //TODO Обратно поменять на гет запрос, а то нгрок хуета какая-то
    @PostMapping("/tokencheck")
    public ResponseEntity<?> tokenCheck(@RequestHeader String authorization) throws RuntimeException{

        logger.info("Checking JWTToken.." + authorization);

        return ResponseEntity.ok().body(authService.actualiseToken(authorization));
    }

    @PostMapping("/reg")
    public ResponseEntity<?> regUser(@RequestBody EmailAndPasswordDTO emailAndPasswordDTO) throws RuntimeException{

        logger.info("Registration new user..");

        return ResponseEntity.ok().body(authService.registerNewUser(emailAndPasswordDTO.getEmail(), emailAndPasswordDTO.getPassword()));
    }

    @PostMapping("/log")
    public ResponseEntity<?> logUser(@RequestBody EmailAndPasswordDTO emailAndPasswordDTO) throws RuntimeException{

        logger.info("Logging user..");

        return ResponseEntity.ok().body(authService.loginUserGetJWTToken(emailAndPasswordDTO.getEmail(), emailAndPasswordDTO.getPassword()));
    }
}
