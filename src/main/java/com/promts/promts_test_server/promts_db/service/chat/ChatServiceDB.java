package com.promts.promts_test_server.promts_db.service.chat;

import com.promts.promts_test_server.promts_db.dto.chat.outbound.ChatDto;
import com.promts.promts_test_server.promts_db.entity.chat.Chat;
import com.promts.promts_test_server.promts_db.entity.chatbot.ChatBot;
import com.promts.promts_test_server.promts_db.entity.modeluri.ModelUri;
import com.promts.promts_test_server.promts_db.entity.user.AppUser;
import com.promts.promts_test_server.promts_db.repository.AppUserRepository;
import com.promts.promts_test_server.promts_db.repository.ChatBotRepository;
import com.promts.promts_test_server.promts_db.repository.ChatRepository;
import com.promts.promts_test_server.promts_db.repository.ModelUriRepository;
import com.promts.promts_test_server.shared.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatServiceDB {

    private final ChatRepository repo;
    private final AppUserRepository userRepo;
    private final ChatBotRepository botRepo;
    private final ModelUriRepository modelRepo;

    public Chat create(String uidFirebase, ChatDto dto) {
        Chat chat = new Chat();

        AppUser user = userRepo.findByUidFirebase(uidFirebase).orElseThrow();
        ModelUri modelUri = modelRepo.findById(dto.getModelUriId()).orElseThrow();
        chat.setUser(user);
        chat.setModelUri(modelUri);

        chat.setChatName(dto.getChatName());
        chat.setContext(dto.getContext());
        chat.setTemperature(dto.getTemperature());
        chat.setStarredChat(false);
        chat.setDateCreate(LocalDateTime.now());
        chat.setDateEdit(LocalDateTime.now());

        ChatBot chatBot;
        if (chat.getChatBot() != null) {
            chatBot = botRepo.findById(dto.getChatBotId()).orElseThrow();
            chat.setChatBot(chatBot);
            chat.setCanEditContext(chatBot.isCanEditContext());
            chat.setCanEditModelUri(chatBot.isCanEditModelUri());
            chat.setCanUseMemory(chatBot.isCanUseMemory());
            chat.setCanUpdateMemory(chatBot.isCanUpdateMemory());
            chat.setUseMemory(chatBot.isCanUseMemory() && dto.isUseMemory());
            chat.setUpdateMemory(chatBot.isCanUpdateMemory() && dto.isUpdateMemory());
        } else {
            chat.setCanEditContext(true);
            chat.setCanEditModelUri(true);
            chat.setCanUseMemory(true);
            chat.setCanUpdateMemory(true);
            chat.setUseMemory(dto.isUseMemory());
            chat.setUpdateMemory(dto.isUpdateMemory());
        }

        return repo.save(chat);
    }

    public List<Chat> getChats(String uidFirebase) {
        AppUser user = userRepo.findByUidFirebase(uidFirebase).orElseThrow();

        return repo.findByUserId(user.getId());
    }

    public Chat update(String uidFirebase, ChatDto dto) {
        AppUser user = userRepo.findByUidFirebase(uidFirebase).orElseThrow();
        Chat saveChat = repo.findById(dto.getChatId()).orElseThrow();
        if (!Objects.equals(user.getId(), saveChat.getUser().getId())) {
            throw new GlobalException("USER_NOT_EXPECTED", "Такого чата нет у пользователя");
        }
        ModelUri modelUri = modelRepo.findById(dto.getModelUriId()).orElseThrow();
        saveChat.setContext(dto.getContext());
        saveChat.setTemperature(dto.getTemperature());
        saveChat.setModelUri(modelUri);
        saveChat.setStarredChat(dto.isStarredChat());
        saveChat.setUseMemory(dto.isUseMemory());
        saveChat.setUpdateMemory(dto.isUpdateMemory());

        return repo.save(saveChat);
    }

    public void delete(String uidFirebase, Long chatId) {
        AppUser user = userRepo.findByUidFirebase(uidFirebase).orElseThrow();
        Chat deleteChat = repo.findById(chatId).orElseThrow();
        if (!Objects.equals(user.getId(), deleteChat.getUser().getId())) {
            throw new GlobalException("USER_NOT_EXPECTED", "Такого чата нет у пользователя");
        }

        repo.deleteById(chatId);
    }
}
