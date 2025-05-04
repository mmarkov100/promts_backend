package com.promts.promts_test_server.controller;

import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.service.Auth.AuthService;
import com.promts.promts_test_server.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private static final Logger logger = Logger.getLogger(ChatController.class.getName());

    @Autowired
    private AuthService authService;
    @Autowired
    private ChatService chatService;

    @PostMapping()
    public ResponseEntity<?> createChat(@RequestHeader String authorization,
                                          @RequestBody CreateChatDTO createChatDTO) throws InterruptedException {

        logger.info("Got request for chat creating..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.createChat(uidFirebase, createChatDTO));
    }

    @PostMapping("/chatbot")
    public ResponseEntity<?> createChatWithChatBot(@RequestHeader String authorization,
                                          @RequestBody CreateChatWithChatBotDTO createChatDTO) throws InterruptedException {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.createChatWithChatBot(uidFirebase, createChatDTO));
    }

    @PostMapping("/get")
    public ResponseEntity<?> newGetUserChats (@RequestHeader String authorization) throws InterruptedException{

        logger.info("Got request for chat list..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.newGetUserChats(uidFirebase));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatByChatId(@RequestHeader String authorization,
                                             @PathVariable Long chatId) throws InterruptedException{

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.getChatByChatId(uidFirebase, chatId));
    }

    @PutMapping("/new")
    public ResponseEntity<?> newUpdateChatSettings(@RequestHeader String authorization,
                                                   @RequestBody UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException{

        logger.info("Got request for editing settings.. + context: " + updateChatSettingsDTO.toString());

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.newUpdateChatSettings(uidFirebase, updateChatSettingsDTO));
    }

    //TODO Доделать эндпоинт потом
    @DeleteMapping("/{chatid}")
    public ResponseEntity<?> deleteChat (@PathVariable Long chatid,
                                         @RequestHeader String authorization) {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return null;
    }
}
