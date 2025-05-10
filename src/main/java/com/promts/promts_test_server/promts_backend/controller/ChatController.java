package com.promts.promts_test_server.promts_backend.controller;

import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.promts_backend.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.promts_backend.dto.Shared.outbound.SuccessMessageDTO;
import com.promts.promts_test_server.promts_backend.service.Auth.AuthService;
import com.promts.promts_test_server.promts_backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<ChatModelDTO> createChat(@RequestHeader String authorization,
                                          @RequestBody CreateChatDTO createChatDTO) throws InterruptedException {

        logger.info("Got request for chat creating..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.createChat(uidFirebase, createChatDTO));
    }

    //TODO Сделать позже, когда реализуются чат-боты
    @PostMapping("/chatbot")
    public ResponseEntity<ChatModelDTO> createChatWithChatBot(@RequestHeader String authorization,
                                          @RequestBody CreateChatWithChatBotDTO createChatDTO) throws InterruptedException {

        logger.info("Got request for chat creating with chatbot..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.createChatWithChatBot(uidFirebase, createChatDTO));
    }

    @PostMapping("/get")
    public ResponseEntity<List<ChatModelDTO>> newGetUserChats (@RequestHeader String authorization) throws InterruptedException{

        logger.info("Got request for chat list..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.newGetUserChats(uidFirebase));
    }

    @PostMapping("/{chatId}")
    public ResponseEntity<ChatModelDTO> getChatByChatId(@RequestHeader String authorization,
                                             @PathVariable Long chatId) throws InterruptedException{

        logger.info("Got request for chat..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.getChatByChatId(uidFirebase, chatId));
    }

    @PutMapping("/new")
    public ResponseEntity<ChatModelDTO> newUpdateChatSettings(@RequestHeader String authorization,
                                                              @RequestBody UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException{

        logger.info("Got request for editing settings.. ");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.newUpdateChatSettings(uidFirebase, updateChatSettingsDTO));
    }

    @DeleteMapping("/{chatid}")
    public ResponseEntity<SuccessMessageDTO> deleteChat (@PathVariable Long chatid,
                                                         @RequestHeader String authorization) throws InterruptedException {

        logger.info("Request for deleting settings.. ");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatService.deleteChatByChatId(uidFirebase, chatid));
    }
}
