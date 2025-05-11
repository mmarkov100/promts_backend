package com.promts.promts_test_server.promts_backend.controller;

import com.promts.promts_test_server.promts_backend.dto.ChatBot.inbound.CreateChatBotDTO;
import com.promts.promts_test_server.promts_backend.service.Auth.AuthService;
import com.promts.promts_test_server.promts_backend.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/chatbots")
public class ChatBotController {

    private static final Logger logger = Logger.getLogger(ChatBotController.class.getName());

    @Autowired
    private ChatBotService chatBotService;
    @Autowired
    private AuthService authService;

    // Эндпоинт для получения чат-ботов по определенному запросу
    @PostMapping("/get")
    public ResponseEntity<?> getChatBots(@RequestHeader String authorization,
                                         @RequestParam(name = "search", required = false, defaultValue = "") String search,
                                         @RequestParam(name = "limit", required = false, defaultValue = "") int limit,
                                         @RequestParam(name = "offset", required = false, defaultValue = "") int offset) {

        logger.info("Got request for chatbots..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        // Отправляем логику в сервис
        return ResponseEntity.ok().body(chatBotService.getChatBots(search, limit, offset, uidFirebase));
    }

    // Эндпоинт получения информации о конкретном чат-боте
    @PostMapping("/{chat_bot_id}")
    public ResponseEntity<?> getChatBot(@PathVariable Long chat_bot_id,
                                        @RequestHeader String authorization) {

        logger.info("Got request for 1 chatbot..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatBotService.getChatBot(chat_bot_id, uidFirebase));
    }

    // Эндпоинт для создания чат-бота
    @PostMapping
    public ResponseEntity<?> createChatBot(@RequestHeader String authorization,
                                           @RequestBody CreateChatBotDTO createChatBotDTO) {

        logger.info("Got request for creating chatbot..");

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatBotService.createChatBot(createChatBotDTO, uidFirebase));
    }
}
