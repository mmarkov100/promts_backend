package com.promts.promts_test_server.repository.Chat;

import com.promts.promts_test_server.dto.Chat.inbound.ChatModelDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatDTO;
import com.promts.promts_test_server.dto.Chat.inbound.CreateChatWithChatBotDTO;
import com.promts.promts_test_server.dto.Chat.inbound.UpdateChatSettingsDTO;
import com.promts.promts_test_server.dto.Chat.outbound.ChatListShortDTO;

import java.util.List;

public interface ChatRepository {
    ChatModelDTO createChat(String uidFirebase, Long id, CreateChatDTO createChatDTO) throws InterruptedException;
    ChatModelDTO createChatWithChatBot(String uidFirebase, Long id, CreateChatWithChatBotDTO createChatWithChatBotDTO) throws InterruptedException;
    ChatListShortDTO getUserChats(String uidFirebase, Long id) throws InterruptedException;
    List<ChatModelDTO> newGetUserChats(String uidFirebase, Long id)  throws InterruptedException;
    ChatModelDTO getChatByChatId (String uidFirebase, Long id, Long chatId) throws InterruptedException;
    ChatModelDTO newUpdateChatSettings(String uidFirebase, Long id, UpdateChatSettingsDTO updateChatSettingsDTO) throws InterruptedException;
}
