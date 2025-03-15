package com.promts.promts_test_server.service;

import com.promts.promts_test_server.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.dto.Chat.outbound.ChatListShortDTO;
import com.promts.promts_test_server.dto.Chat.outbound.UpdatedChatSettingsDTO;
import com.promts.promts_test_server.repository.Chat.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public ChatModelDTO createChat(String uidFirebase, Long id, CreateChatDTO createChatDTO) throws InterruptedException {

        return chatRepository.createChat(uidFirebase, id, createChatDTO);
    }

    public ChatModelDTO createChatWithChatBot(String uidFirebase, Long id, CreateChatWithChatBotDTO createChatWithChatBotDTO) throws InterruptedException {

        return chatRepository.createChatWithChatBot(uidFirebase, id, createChatWithChatBotDTO);
    }

    public ChatListShortDTO getUserChats(String uidFirebase, Long id) throws InterruptedException {

        return chatRepository.getUserChats(uidFirebase, id);
    }

    public List<ChatModelDTO> newGetUserChats(String uidFirebase, Long id) throws InterruptedException{

        return chatRepository.newGetUserChats(uidFirebase, id);
    }

    public ChatModelDTO getChatByChatId(String uidFirebase, Long id, Long chatId) throws InterruptedException {

        return chatRepository.getChatByChatId(uidFirebase, id, chatId);
    }

    public UpdatedChatSettingsDTO updateChatSettings(String uidFirebase, Long id, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException {

        return chatRepository.updateChatSettings(uidFirebase, id, updateChatSettingsDTO);
    }

    public ChatModelDTO newUpdateChatSettings(String uidFirebase, Long id, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException {

        return chatRepository.newUpdateChatSettings(uidFirebase, id, updateChatSettingsDTO);
    }
}
