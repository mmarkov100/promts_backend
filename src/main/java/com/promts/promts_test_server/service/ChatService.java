package com.promts.promts_test_server.service;

import com.promts.promts_test_server.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.repository.Chat.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public ChatModelDTO createChat(String uidFirebase, CreateChatDTO createChatDTO) throws InterruptedException {

        return chatRepository.createChat(uidFirebase, createChatDTO);
    }

    public ChatModelDTO createChatWithChatBot(String uidFirebase, CreateChatWithChatBotDTO createChatWithChatBotDTO) throws InterruptedException {

        return chatRepository.createChatWithChatBot(uidFirebase, createChatWithChatBotDTO);
    }

    public List<ChatModelDTO> newGetUserChats(String uidFirebase) throws InterruptedException{

        return chatRepository.newGetUserChats(uidFirebase);
    }

    public ChatModelDTO getChatByChatId(String uidFirebase, Long chatId) throws InterruptedException {

        return chatRepository.getChatByChatId(uidFirebase, chatId);
    }

    public ChatModelDTO newUpdateChatSettings(String uidFirebase, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException {

        return chatRepository.newUpdateChatSettings(uidFirebase, updateChatSettingsDTO);
    }
}
