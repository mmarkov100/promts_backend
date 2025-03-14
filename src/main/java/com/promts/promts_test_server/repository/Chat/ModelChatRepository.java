package com.promts.promts_test_server.repository.Chat;

import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.dto.Chat.outbound.ChatListShortDTO;
import com.promts.promts_test_server.dto.Chat.outbound.CreatedChatDTO;
import com.promts.promts_test_server.dto.Chat.outbound.UpdatedChatSettingsDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("main")
public class ModelChatRepository implements ChatRepository{
    @Override
    public CreatedChatDTO createChat(String uidFirebase, Long id, CreateChatDTO createChatDTO) {
        return null;
    }

    @Override
    public CreatedChatDTO createChatWithChatBot(String uidFirebase, Long id, CreateChatWithChatBotDTO createChatWithChatBotDTO) {
        return null;
    }

    @Override
    public ChatListShortDTO getUserChats(String uidFirebase, Long id) throws InterruptedException {
        return null;
    }

    @Override
    public UpdatedChatSettingsDTO updateChatSettings(String uidFirebase, Long id, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException {
        return null;
    }
}
