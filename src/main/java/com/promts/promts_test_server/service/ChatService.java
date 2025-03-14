package com.promts.promts_test_server.service;

import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.dto.Chat.outbound.ChatListShortDTO;
import com.promts.promts_test_server.dto.Chat.outbound.CreatedChatDTO;
import com.promts.promts_test_server.dto.Chat.outbound.UpdatedChatSettingsDTO;
import com.promts.promts_test_server.repository.Chat.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public CreatedChatDTO createChat(String uidFirebase, Long id, CreateChatDTO createChatDTO) throws InterruptedException {

        return chatRepository.createChat(uidFirebase, id, createChatDTO);
    }

    public CreatedChatDTO createChatWithChatBot(String uidFirebase, Long id, CreateChatWithChatBotDTO createChatWithChatBotDTO) throws InterruptedException {

        return chatRepository.createChatWithChatBot(uidFirebase, id, createChatWithChatBotDTO);
    }

    public ChatListShortDTO getUserChats(String uidFirebase, Long id) throws InterruptedException {

        return chatRepository.getUserChats(uidFirebase, id);
    }

    public UpdatedChatSettingsDTO updateChatSettings(String uidFirebase, Long id, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException {

        return chatRepository.updateChatSettings(uidFirebase, id, updateChatSettingsDTO);
    }
}
