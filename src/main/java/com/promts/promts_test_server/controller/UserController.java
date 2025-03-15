package com.promts.promts_test_server.controller;

import com.promts.promts_test_server.dto.Auth.inboind.EmailAndPasswordDTO;
import com.promts.promts_test_server.dto.User.inbound.UpdateUserRequestDTO;
import com.promts.promts_test_server.service.Auth.AuthService;
import com.promts.promts_test_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    // Эндпоинт регистрации нового пользователя. ПОКА ЧТО ВЫВЕДЕН ИЗ СТРОЯ, ЕМУ БУДЕТ ЗАМЕНА
    //@PostMapping("/registration")
//    public ResponseEntity<?> newUser(@RequestHeader String authorization,
//                                     @RequestBody RegistrationDTO registrationDTO) throws RuntimeException {
//
//        // Сначала делаем проверку токена и получаем uid пользователя
//        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);
//
//        // Отправляем логику в сервис
//        return ResponseEntity.ok().body(userService.createUser(registrationDTO.getEmail(), uidFirebase));
//    }

    // Эндпоинт входа пользователя и получения информации о нем, его чатах и существующих нейросетях. ПОКА ЧТО ВЫВЕДЕН ИЗ СТРОЯ, ЕМУ БУДЕТ ЗАМЕНА
    //@GetMapping("/login")
//    public ResponseEntity<?> getUser(@RequestHeader String authorization) throws RuntimeException {
//
//        logger.info(authorization);
//
//        // Сначала делаем проверку токена и получаем uid пользователя
//        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);
//
//        // Отправляем логику в сервис
//        return ResponseEntity.ok().body(userService.getUser(uidFirebase));
//    }

    // Проверка актуальности токена. Нужен, чтобы при заходе на сайт если токен актуальный сразу переключалось на главный экран
    @GetMapping("/tokencheck")
    public ResponseEntity<?> tokenCheck(@RequestHeader String authorization) throws RuntimeException{

        logger.info("Checking JWTToken..");

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

    @GetMapping
    public ResponseEntity<?> newGetUser(@RequestHeader String authorization) throws InterruptedException {

        logger.info("Got request for user..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(userService.newGetUser(uidFirebase));
    }

    // Эндпоинт обновления настроек пользователя. ПОКА ЧТО ВЫВЕДЕН ИЗ СТРОЯ, ЕМУ БУДЕТ ЗАМЕНА
//    @PutMapping()
//    public ResponseEntity<?> updateUser(@RequestHeader String authorization,
//                                        @RequestHeader Long id,
//                                        @RequestBody UpdateUserRequestDTO requestDTO) throws RuntimeException {
//
//        // Сначала делаем проверку токена и получаем uid пользователя
//        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);
//
//        // Отправляем логику в сервис
//        return ResponseEntity.ok().body(userService.updateUser(uidFirebase, id, requestDTO));
//    }

    @PutMapping("/settings")
    public ResponseEntity<?> newUpdateUser(@RequestHeader String authorization,
                                        @RequestHeader Long id,
                                        @RequestBody UpdateUserRequestDTO requestDTO) throws InterruptedException {

        logger.info("Got request for edit user settings..");
        
        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        // Отправляем логику в сервис
        return ResponseEntity.ok().body(userService.newUpdateUser(uidFirebase, id, requestDTO));
    }
}
