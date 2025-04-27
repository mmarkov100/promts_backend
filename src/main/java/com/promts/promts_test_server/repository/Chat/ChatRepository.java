package com.promts.promts_test_server.repository.Chat;

import com.promts.promts_test_server.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;

import java.util.List;

public interface ChatRepository {
    ChatModelDTO createChat(String uidFirebase, CreateChatDTO createChatDTO) throws InterruptedException;
    ChatModelDTO createChatWithChatBot(String uidFirebase, CreateChatWithChatBotDTO createChatWithChatBotDTO) throws InterruptedException;
    List<ChatModelDTO> newGetUserChats(String uidFirebase)  throws InterruptedException;
    ChatModelDTO getChatByChatId (String uidFirebase, Long chatId) throws InterruptedException;
    ChatModelDTO newUpdateChatSettings(String uidFirebase, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException;
}
