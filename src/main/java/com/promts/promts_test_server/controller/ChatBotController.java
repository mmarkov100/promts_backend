package com.promts.promts_test_server.controller;

import com.promts.promts_test_server.dto.ChatBot.inbound.CreateChatBotDTO;
import com.promts.promts_test_server.service.Auth.AuthService;
import com.promts.promts_test_server.service.Auth.MockAuthService;
import com.promts.promts_test_server.service.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatbots")
@CrossOrigin(origins = "*")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;
    @Autowired
    private AuthService authService;

    // Эндпоинт для получения чат-ботов по определенному запросу
    @GetMapping()
    public ResponseEntity<?> getChatBots(@RequestHeader String authorization,
                                         @RequestHeader Long id,
                                         @RequestParam(name = "search", required = false, defaultValue = "") String search,
                                         @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
                                         @RequestParam(name = "offset", required = false, defaultValue = "0") int offset) throws InterruptedException {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        // Отправляем логику в сервис
        return ResponseEntity.ok().body(chatBotService.getChatBots(search, limit, offset, id, uidFirebase));
    }

    // Эндпоинт получения информации о конкретном чат-боте
    @GetMapping("/{chat_bot_id}")
    public ResponseEntity<?> getChatBot(@PathVariable Long chat_bot_id,
                                        @RequestHeader String authorization,
                                        @RequestHeader Long id) throws InterruptedException {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatBotService.getChatBot(chat_bot_id, id, uidFirebase));
    }

    // Эндпоинт для создания чат-бота
    @PostMapping
    public ResponseEntity<?> createChatBot(@RequestHeader String authorization,
                                           @RequestHeader Long id,
                                           @RequestBody CreateChatBotDTO createChatBotDTO) throws InterruptedException {

        // Сначала делаем проверку токена и получаем uid пользователя
        String uidFirebase = authService.getUidFirebaseAndValidate(authorization);

        return ResponseEntity.ok().body(chatBotService.createChatBot(createChatBotDTO, id, uidFirebase));
    }
}
