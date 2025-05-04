package com.promts.promts_test_server.repository.ChatBot;

import com.promts.promts_test_server.dto.ChatBot.inbound.CreateChatBotDTO;
import com.promts.promts_test_server.dto.ChatBot.outbound.ChatBotResponseDTO;

import java.util.List;

public interface ChatBotRepository {
    List<ChatBotResponseDTO> getAllChatBotsBySpec(String uidFirebase, String search, int offset, int limit) throws InterruptedException;
    ChatBotResponseDTO getChatBotById(String uidFirebase, Long chatBotId) throws InterruptedException;
    ChatBotResponseDTO createChatBotById(String uidFirebase, CreateChatBotDTO createChatBotDTO) throws InterruptedException;
}
