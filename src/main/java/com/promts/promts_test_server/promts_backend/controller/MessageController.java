package com.promts.promts_test_server.promts_backend.controller;

import com.promts.promts_test_server.promts_backend.dto.Message.inbound.DeleteMessagesDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.NewMessageDTO;
import com.promts.promts_test_server.promts_backend.dto.Message.inbound.RegenerateMessageDTO;
import com.promts.promts_test_server.promts_backend.service.Auth.AuthService;
import com.promts.promts_test_server.promts_backend.service.Message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private static final Logger logger = Logger.getLogger(MessageController.class.getName());

    @Autowired
    private AuthService authService;
    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<?> generateNewMessage(@RequestHeader String authorization,
                                                @RequestBody NewMessageDTO newMessageDTO) throws InterruptedException {

        logger.info("Got request to generate new message..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(messageService.generateMessage(uidFirebase, newMessageDTO));
    }

    //TODO Доделать позже
    @PostMapping("/regenerate")
    public ResponseEntity<?> regenerateMessage(@RequestHeader String authorization,
                                                @RequestBody RegenerateMessageDTO regenerateMessageDTO){

        logger.info("Got request to regenerate message..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return null;
    }

    @PostMapping("/{chatId}")
    public ResponseEntity<?> newGetMessagesByChatId(@RequestHeader String authorization,
                                                    @PathVariable Long chatId) throws InterruptedException {

        logger.info("Got request to get chat history..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(messageService.getMessagesByChatId(uidFirebase, chatId));
    }

    //TODO Сделать позже
    @DeleteMapping
    public ResponseEntity<?> deleteMessagesByMessageId(@RequestHeader String authorization,
                                                       @RequestBody DeleteMessagesDTO deleteMessagesDTO) {

        logger.info("Got request for deleting chat history..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return null;
    }

}
