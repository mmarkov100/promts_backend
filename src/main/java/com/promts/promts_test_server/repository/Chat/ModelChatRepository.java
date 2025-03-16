package com.promts.promts_test_server.repository.Chat;

import com.promts.promts_test_server.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.dto.Chat.outbound.ChatListShortDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("main")
public class ModelChatRepository implements ChatRepository{
    @Override
    public ChatModelDTO createChat(String uidFirebase, Long id, CreateChatDTO createChatDTO) {
        return null;
    }

    @Override
    public ChatModelDTO createChatWithChatBot(String uidFirebase, Long id, CreateChatWithChatBotDTO createChatWithChatBotDTO) {
        return null;
    }

    @Override
    public ChatListShortDTO getUserChats(String uidFirebase, Long id) throws InterruptedException {
        return null;
    }

    @Override
    public List<ChatModelDTO> newGetUserChats(String uidFirebase, Long id) throws InterruptedException {
        return List.of();
    }

    @Override
    public ChatModelDTO getChatByChatId(String uidFirebase, Long id, Long chatId) throws InterruptedException {
        return null;
    }

    @Override
    public ChatModelDTO newUpdateChatSettings(String uidFirebase, Long id, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException {
        return null;
    }
}
