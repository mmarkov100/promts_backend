package com.promts.promts_test_server.controller;

import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.service.Auth.AuthService;
import com.promts.promts_test_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    //TODO Обратно поменять на гет запрос, а то нгрок хуета какая-то
    @PostMapping
    public ResponseEntity<?> getUser(@RequestHeader String authorization) throws InterruptedException {

        logger.info("Got request for user..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(userService.newGetUser(uidFirebase));
    }

    @PutMapping("/settings")
    public ResponseEntity<?> updateUser(@RequestHeader String authorization,
                                        @RequestBody UpdateUserRequestDTO requestDTO) throws InterruptedException {

        logger.info("Got request for edit user settings.. + GPT: " + requestDTO.getStandardModelUriId());

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        // Отправляем логику в сервис
        return ResponseEntity.ok().body(userService.newUpdateUser(uidFirebase, requestDTO));
    }
}
