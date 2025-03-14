package com.promts.promts_test_server.repository.Chat;

import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.dto.Chat.outbound.ChatListShortDTO;
import com.promts.promts_test_server.dto.Chat.outbound.CreatedChatDTO;
import com.promts.promts_test_server.dto.Chat.outbound.UpdatedChatSettingsDTO;

public interface ChatRepository {
    CreatedChatDTO createChat(String uidFirebase, Long id, CreateChatDTO createChatDTO) throws InterruptedException;
    CreatedChatDTO createChatWithChatBot(String uidFirebase, Long id, CreateChatWithChatBotDTO createChatWithChatBotDTO) throws InterruptedException;
    ChatListShortDTO getUserChats(String uidFirebase, Long id) throws InterruptedException;
    UpdatedChatSettingsDTO updateChatSettings(String uidFirebase, Long id, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException;
}
