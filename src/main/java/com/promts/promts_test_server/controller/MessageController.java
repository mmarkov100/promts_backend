package com.promts.promts_test_server.controller;

import com.promts.promts_test_server.dto.Message.inbound.DeleteMessagesDTO;
import com.promts.promts_test_server.dto.Message.inbound.NewMessageDTO;
import com.promts.promts_test_server.dto.Message.inbound.RegenerateMessageDTO;
import com.promts.promts_test_server.service.Auth.AuthService;
import com.promts.promts_test_server.service.Message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private AuthService authService;
    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<?> generateNewMessage(@RequestHeader String authorization,
                                                @RequestHeader Long id,
                                                @RequestBody NewMessageDTO newMessageDTO) throws InterruptedException {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(messageService.generateMessage(uidFirebase, id, newMessageDTO));
    }

    //TODO Доделать позже
    @PostMapping("/regenerate")
    public ResponseEntity<?> regenerateMessage(@RequestHeader String authorization,
                                                @RequestHeader Long id,
                                                @RequestBody RegenerateMessageDTO regenerateMessageDTO){

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return null;
    }
    //ЭНДПОИНТ ЗАМЕНЕН
//    @GetMapping("/{chatId}")
//    public ResponseEntity<?> getMessagesByChatId(@RequestHeader String authorization,
//                                                 @RequestHeader Long id,
//                                                 @PathVariable Long chatId) throws InterruptedException {
//
//        // Сначала делаем проверку токена и получаем uid пользователя
//        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);
//
//        return ResponseEntity.ok().body(messageService.getMessagesByChatId(uidFirebase, id, chatId));
//    }

    //TODO Доделать
    @GetMapping("/{chatId}")
    public ResponseEntity<?> newGetMessagesByChatId(@RequestHeader String authorization,
                                                    @RequestHeader Long id,
                                                    @PathVariable Long chatId) throws InterruptedException {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return null;
    }

    //TODO Сделать позже
    @DeleteMapping
    public ResponseEntity<?> deleteMessagesByMessageId(@RequestHeader String authorization,
                                                       @RequestHeader Long id,
                                                       @RequestBody DeleteMessagesDTO deleteMessagesDTO) {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return null;
    }

}
