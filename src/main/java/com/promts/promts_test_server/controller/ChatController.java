package com.promts.promts_test_server.controller;

import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.service.Auth.AuthService;
import com.promts.promts_test_server.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private AuthService authService;
    @Autowired
    private ChatService chatService;

    @PostMapping()
    public ResponseEntity<?> createChat(@RequestHeader String authorization,
                                          @RequestHeader Long id,
                                          @RequestBody CreateChatDTO createChatDTO) throws InterruptedException {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.createChat(uidFirebase, id, createChatDTO));
    }

    @PostMapping("/chatbot")
    public ResponseEntity<?> createChatWithChatBot(@RequestHeader String authorization,
                                          @RequestHeader Long id,
                                          @RequestBody CreateChatWithChatBotDTO createChatDTO) throws InterruptedException {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.createChatWithChatBot(uidFirebase, id, createChatDTO));
    }

    @GetMapping("/new")
    public ResponseEntity<?> newGetUserChats (@RequestHeader String authorization,
                                              @RequestHeader Long id) throws InterruptedException{

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.newGetUserChats(uidFirebase, id));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<?> getChatByChatId(@RequestHeader String authorization,
                                             @RequestHeader Long id,
                                             @PathVariable Long chatId) throws InterruptedException{

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.getChatByChatId(uidFirebase, id, chatId));
    }

    @PutMapping("/new")
    public ResponseEntity<?> newUpdateChatSettings(@RequestHeader String authorization,
                                                   @RequestHeader Long id,
                                                   @RequestBody UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException{

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.newUpdateChatSettings(uidFirebase, id, updateChatSettingsDTO));
    }

    //TODO Доделать эндпоинт потом
    @DeleteMapping("/{chatid}")
    public ResponseEntity<?> deleteChat (@PathVariable Long chatid,
                                         @RequestHeader String authorization,
                                         @RequestHeader Long id) {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return null;
    }
}
